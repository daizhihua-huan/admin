package com.daizhihua.tools.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.exception.BadRequestException;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.core.util.StringUtils;
import com.daizhihua.tools.entity.ToolQiniuConfig;
import com.daizhihua.tools.entity.ToolQiniuContent;
import com.daizhihua.tools.mapper.ToolQiniuConfigMapper;
import com.daizhihua.tools.mapper.ToolQiniuContentMapper;
import com.daizhihua.tools.service.ToolQiniuContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daizhihua.tools.util.QiNiuUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 七牛云文件存储 服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-12
 */
@Service
@Slf4j
public class ToolQiniuContentServiceImpl extends ServiceImpl<ToolQiniuContentMapper, ToolQiniuContent> implements ToolQiniuContentService {

    @Autowired
    private ToolQiniuConfigMapper toolQiniuConfigMapper;

    @Autowired
    private ToolQiniuContentMapper toolQiniuContentMapper;

    @Override
    public IPage<ToolQiniuContent> page(Pageable pageable, QueryVo queryVo) {
        IPage<ToolQiniuContent> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<ToolQiniuContent> qiniuContentQueryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(queryVo.getBlurry())){
            qiniuContentQueryWrapper.like("name",queryVo.getBlurry());
        }
        if(null!=queryVo.getCreateTime()&&queryVo.getCreateTime().size()==2){
            qiniuContentQueryWrapper.between("update_time",
                    queryVo.getCreateTime().get(0),
                    queryVo.getCreateTime().get(1));
        }
        return this.page(page,qiniuContentQueryWrapper);
    }

    /**
     * 上传七牛云文件
     * @param multipartFile
     */
    @Override
    public void upload(MultipartFile multipartFile) throws IOException {
        /**
         * 获取七牛云配置
         *
         */
        ToolQiniuConfig qiniuConfig = getToolQiniuConfig();
        Configuration cfg = new Configuration(QiNiuUtil.getRegion(qiniuConfig.getZone()));
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        String upToken = auth.uploadToken(qiniuConfig.getBucket());
        String key = multipartFile.getOriginalFilename();
        if(findByName(key) != null) {
            key = QiNiuUtil.getKey(key);
        }
        Response response = uploadManager.put(multipartFile.getBytes(), key, upToken);
        //解析上传成功的结果
        DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
        log.info("key是:{}",putRet.key);
        log.info("hash是:{}",putRet.hash);
        /**
         * 判断上传的文件是不是第一次上传
         */
        if(findByName(FileUtil.getFileNameNoEx(putRet.key))==null){
            ToolQiniuContent toolQiniuContent = new ToolQiniuContent();
            toolQiniuContent.setSuffix(FileUtil.getExtensionName(putRet.key));
            toolQiniuContent.setBucket(qiniuConfig.getBucket());
            toolQiniuContent.setType(qiniuConfig.getType());
            toolQiniuContent.setName(FileUtil.getFileNameNoEx(putRet.key));
            toolQiniuContent.setSize(FileUtil.getSize(Integer.parseInt(multipartFile.getSize()+"")));
            toolQiniuContent.setUrl(qiniuConfig.getHost()+"/"+putRet.key);
            toolQiniuContent.setUpdateTime(DateUtils.getDateTime());
            this.save(toolQiniuContent);
        }
        log.info("{}",putRet);
    }

    /**
     * 同步七牛云
     */
    @Override
    public void synchronize() {
        ToolQiniuConfig toolQiniuConfig = getToolQiniuConfig();
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiNiuUtil.getRegion(toolQiniuConfig.getZone()));
        Auth auth = Auth.create(toolQiniuConfig.getAccessKey(), toolQiniuConfig.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        //文件名前缀
        String prefix = "";
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(toolQiniuConfig.getBucket(), prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            ToolQiniuContent qiniuContent;
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                if(findByName(FileUtil.getFileNameNoEx(item.key)) == null){
                    qiniuContent = new ToolQiniuContent();
                    qiniuContent.setSize(FileUtil.getSize(Integer.parseInt(item.fsize+"")));
                    qiniuContent.setSuffix(FileUtil.getExtensionName(item.key));
                    qiniuContent.setName(FileUtil.getFileNameNoEx(item.key));
                    qiniuContent.setType(toolQiniuConfig.getType());
                    qiniuContent.setBucket(toolQiniuConfig.getBucket());
                    qiniuContent.setUrl(toolQiniuConfig.getHost()+"/"+item.key);
                    this.save(qiniuContent);
                }
            }
        }
    }

    @Override
    public void delete(List<Long> ids) {
        /**
         * 1.获取七牛云的配置
         * 2 获取删除的内容
         * 3 七牛云删除
         * 4 数据库删除
         */
        ToolQiniuConfig toolQiniuConfig = getToolQiniuConfig();
        List<ToolQiniuContent> toolQiniuContents = this.listByIds(ids);
        Configuration cfg = new Configuration(QiNiuUtil.getRegion(toolQiniuConfig.getZone()));
        Auth auth = Auth.create(toolQiniuConfig.getAccessKey(), toolQiniuConfig.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        for (ToolQiniuContent toolQiniuContent : toolQiniuContents) {
            try {
                bucketManager.delete(toolQiniuContent.getBucket(), toolQiniuContent.getName() + "." + toolQiniuContent.getSuffix());
            } catch (QiniuException e) {
                e.printStackTrace();
            }
        }
        this.removeByIds(ids);
    }

    /**
     * 根据名称查询存储对象
     * @param name
     * @return
     */
    @Override
    public ToolQiniuContent findByName(String name) {
        QueryWrapper<ToolQiniuContent> qiniuContentQueryWrapper = new QueryWrapper<>();
        qiniuContentQueryWrapper.eq("name",name);
        List<ToolQiniuContent> toolQiniuContents = toolQiniuContentMapper.selectList(qiniuContentQueryWrapper);
        if(toolQiniuContents!=null&&toolQiniuContents.size()>0){
            return toolQiniuContents.get(0);
        }
        return null;
    }


    public ToolQiniuConfig getToolQiniuConfig(){
        List<ToolQiniuConfig> toolQiniuConfigs = toolQiniuConfigMapper.selectByMap(null);
        if(toolQiniuConfigs!=null&&toolQiniuConfigs.size()>0){
            return toolQiniuConfigs.get(0);
        }
        throw new BadRequestException("请先配置七牛云存储");
    }


}
