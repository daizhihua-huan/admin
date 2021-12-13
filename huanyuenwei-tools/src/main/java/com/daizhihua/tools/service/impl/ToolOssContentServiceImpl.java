package com.daizhihua.tools.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.core.util.StringUtils;
import com.daizhihua.tools.entity.ToolOssConfig;
import com.daizhihua.tools.entity.ToolOssContent;
import com.daizhihua.tools.mapper.ToolOssContentMapper;
import com.daizhihua.tools.service.ToolOssContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-15
 */
@Slf4j
@Service
public class ToolOssContentServiceImpl extends ServiceImpl<ToolOssContentMapper, ToolOssContent> implements ToolOssContentService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Override
    public IPage<ToolOssContent> page(Pageable pageable, QueryVo queryVo) {
        Page<ToolOssContent> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<ToolOssContent> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(queryVo.getBlurry())){
            queryWrapper.like("name",queryVo.getBlurry());
        }
        if(null!=queryVo.getCreateTime()&&queryVo.getCreateTime().size()==2){
            queryWrapper.between("update_time",queryVo.getCreateTime().get(0),queryVo.getCreateTime().get(1));
        }
        return this.page(page,queryWrapper);
    }

    @Override
    public ToolOssContent upload(OSS ossClient, ToolOssConfig toolOssConfig, MultipartFile multipartFile) {
        ToolOssContent toolOssContent = getOssContentByName(multipartFile.getOriginalFilename());
        try {
            InputStream inputStream = multipartFile.getInputStream();
            String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
            PutObjectResult putObjectResult = ossClient.putObject(toolOssConfig.getBucketName(), multipartFile.getOriginalFilename(), inputStream);

            if(toolOssContent==null){
                toolOssContent = new ToolOssContent();
                toolOssContent.setPath(multipartFile.getOriginalFilename());
                toolOssContent.setEtag(putObjectResult.getETag());
                toolOssContent.setVersion(putObjectResult.getVersionId());
                toolOssContent.setName(multipartFile.getOriginalFilename());
                toolOssContent.setSize(multipartFile.getSize()+"");
                toolOssContent.setBucketName(toolOssConfig.getBucketName());
                toolOssContent.setUpdateTime(DateUtils.getDateTime());
                toolOssContent.setSuffix(suffix);
                this.save(toolOssContent);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ossClient.shutdown();
        }
        return toolOssContent;
    }

    @Override
    public ToolOssContent getOssContentByName(String name) {
        QueryWrapper<ToolOssContent > queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        List<ToolOssContent> list = this.list(queryWrapper);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void delete(OSS ossClinet, ToolOssConfig toolOssConfig, List<Long> ids) {
        /**
         * 获取删除的oss内容
         * 逐一进行删除
         * 最后删除数据库
         */
        List<ToolOssContent> toolOssContents = this.listByIds(ids);
        for (ToolOssContent toolOssContent : toolOssContents) {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClinet.deleteObject(toolOssConfig.getBucketName(), toolOssContent.getPath());
        }
        ossClinet.shutdown();
        this.removeByIds(ids);
    }

    @Override
    public void download(OSS ossClient, ToolOssConfig toolOssConfig, Long id) {
        ToolOssContent toolOssContent = this.getById(id);
        if(toolOssContent!=null){
//            FileUtil.SYS_TEM_DIR;
            File file = new File(FileUtil.SYS_TEM_DIR+toolOssContent.getPath());
            // 下载Object到本地文件，并保存到指定的本地路径中。如果指定的本地文件存在会覆盖，不存在则新建。
// 如果未指定本地路径，则下载后的文件默认保存到示例程序所属项目对应本地路径中。
            ossClient.getObject(new GetObjectRequest(toolOssConfig.getBucketName(),
                    toolOssContent.getPath()),file);
            FileUtil.downloadFile(request,response,file,true);
        }
    }

    @Override
    public void sync(OSS ossClient, ToolOssConfig toolOssConfig) {
        ObjectListing objectListing = ossClient.listObjects(toolOssConfig.getBucketName());
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        for (OSSObjectSummary sum : sums) {
            log.info("{}",sum.getBucketName());
            log.info("{}",sum.getOwner());
            log.info("{}",sum.getType());
            ToolOssContent ossContentByName = getOssContentByName(sum.getKey());
            if(ossContentByName==null){
                ossContentByName = new ToolOssContent();
                ossContentByName.setName(sum.getKey());
                ossContentByName.setSize(sum.getSize()+"");
                ossContentByName.setBucketName(sum.getBucketName());
                ossContentByName.setEtag(sum.getETag());
                ossContentByName.setPath(sum.getKey());
                ossContentByName.setSuffix(FileUtil.getExtensionName(sum.getKey()));
                this.save(ossContentByName);
            }
        }
    }
}
