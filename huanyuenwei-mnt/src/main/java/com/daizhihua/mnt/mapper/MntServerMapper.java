package com.daizhihua.mnt.mapper;

import com.daizhihua.mnt.entity.MntServer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 服务器管理 Mapper 接口
 * </p>
 *
 * @author 代志华
 * @since 2021-11-16
 */
public interface MntServerMapper extends BaseMapper<MntServer> {

    @Select("SELECT s.* FROM mnt_server s \n" +
            "JOIN mnt_deploy_server sd \n" +
            "ON s.server_id = sd.server_id \n" +
            "JOIN mnt_deploy m ON m.deploy_id= sd.deploy_id\n" +
            "WHERE m.deploy_id = #{deployId}")
    List<MntServer> getMntServerForDeployId(Long deployId);

}
