package com.daizhihua.tools.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.config.FileProperties;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.exception.BadRequestException;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.core.util.SecurityUtils;
import com.daizhihua.core.util.StringUtils;
import com.daizhihua.tools.entity.ToolLocalStorage;
import com.daizhihua.tools.mapper.ToolLocalStorageMapper;
import com.daizhihua.tools.service.ToolLocalStorageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * <p>
 * 本地存储 服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-12
 */
@Service
@Slf4j
public class ToolLocalStorageServiceImpl extends ServiceImpl<ToolLocalStorageMapper, ToolLocalStorage> implements ToolLocalStorageService {

    @Autowired
    private ToolLocalStorageMapper toolLocalStorageMapper;

    @Autowired
    private FileProperties properties;
    /**
     * 创建
     * @param name
     * @param file
     */
    @Override
    public void create(String name, MultipartFile file) {
        FileUtil.checkSize(properties.getMaxSize(), file.getSize());
        String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
        String type = FileUtil.getFileType(suffix);
        File file_data = FileUtil.upload(file, properties.getPath().getPath() + type +  File.separator);
        if(ObjectUtil.isNull(file_data)){
            throw new BadRequestException("上传失败");
        }
        name = StringUtils.isBlank(name) ? FileUtil.getFileNameNoEx(file.getOriginalFilename()) : name;
        ToolLocalStorage localStorage = new ToolLocalStorage(
                file_data.getName(),
                name,
                suffix,
                file_data.getPath(),
                type,
                FileUtil.getSize(file.getSize()));
        localStorage.setCreateBy(SecurityUtils.getCurrentUsername());
        localStorage.setUpdateBy(SecurityUtils.getCurrentUsername());
        localStorage.setCreateTime(DateUtils.getDateTime());
        localStorage.setUpdateTime(DateUtils.getDateTime());
        this.save(localStorage);
    }

    @Override
    public IPage<ToolLocalStorage> page(Pageable pageable, QueryVo queryVo) {
        IPage<ToolLocalStorage> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<ToolLocalStorage> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(queryVo.getBlurry())){
            queryWrapper.like("name",queryVo.getBlurry());
        }
        if(null!=queryVo.getCreateTime()&&queryVo.getCreateTime().size()==2){
            queryWrapper.between("create_time",queryVo.getCreateTime().get(0),queryVo.getCreateTime().get(1));
        }
        return  this.page(page,queryWrapper);
    }
}
