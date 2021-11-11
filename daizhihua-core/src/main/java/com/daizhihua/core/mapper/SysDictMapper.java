package com.daizhihua.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daizhihua.core.entity.SysDict;
import com.daizhihua.core.entity.SysDictDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author 代志华
 * @since 2021-11-03
 */
public interface SysDictMapper extends BaseMapper<SysDict> {

    @Select("SELECT d.* FROM sys_dict s JOIN  sys_dict_detail d\n" +
            "ON s.dict_id = d.dict_id WHERE s.name = #{name}")
    List<SysDictDetail> getDicDetailForName(@Param("name") String name);

}
