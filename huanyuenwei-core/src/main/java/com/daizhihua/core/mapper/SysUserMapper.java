package com.daizhihua.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daizhihua.core.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author 代志华
 * @since 2021-05-17
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    @Select("select * from sys_user where username = #{usearName}")
    SysUser findByUsername(String usearName);

    @Select("SELECT  s.user_id as userId,s.dept_id as deptId,username,nick_name as nickName,gender,phone," +
            "email,avatar_name as avatarName,avatar_path as avatarPath,is_admin as isAdmin,s.enabled,s.create_by as createBy," +
            "s.update_by as updateBy,pwd_reset_time as pwdResetTime,s.create_time as createTime,s.update_time as updateTime,expired_time as expiredTime, \n" +
            "d.name " +
            "from sys_user s \n" +
            "join sys_dept d\n" +
            "on s.dept_id = d.dept_id")
    List<Map<String, Object>> listUser();

    @Select({
            "<script>",
            "SELECT  count(*)" +
                    "from sys_user s \n" +
                    "join sys_dept d\n" +
                    "on s.dept_id = d.dept_id",
            "<where> ",
            "<if test=\"deptsId != null and deptsId.size>0\" >",
            "d.dept_id in ",
            "<foreach collection='deptsId' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</if>",
            "<if test=\"username != '' and username!= null  \">",
            "and s.username LIKE  #{username}",
            "</if>",
            "<if test=\"enabled != '' and enabled != null\" >",
            "and s.enabled = #{enabled}",
            "</if>",
            "<if test=\"startTime != '' and startTime!= null  \">",
            "and s.create_time  <![CDATA[ >= ]]>  #{startTime}",
            "</if>",
            "<if test=\"endTime != '' and endTime!= null  \">",
            "and s.create_time  <![CDATA[ <= ]]>  #{endTime}",
            "</if>",
            "</where>",
            "</script>"
    })
    int count(@Param("deptsId") List<Long> deptsId,
              @Param("username") String username,
              @Param("enabled") String enabled,
              @Param("startTime") String startTime,
              @Param("endTime") String endTime
    );

    @Select({
            "<script>",
            "SELECT  s.user_id as userId,s.dept_id as deptId,username,nick_name as nickName,gender,phone,",
            "email,avatar_name as avatarName,avatar_path as avatarPath,is_admin as isAdmin,s.enabled,s.create_by as createBy,",
            "s.update_by as updateBy,pwd_reset_time as pwdResetTime,s.create_time as createTime,s.update_time as updateTime,expired_time as expiredTime, \n",
            "d.name ",
            "from sys_user s \n",
            "join sys_dept d\n",
            "on s.dept_id = d.dept_id",
            "<where> ",
            "<if test=\"deptsId != null and deptsId.size>0\" >",
            "d.dept_id in ",
            "<foreach collection='deptsId' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</if>",
            "</where>",
            " ORDER BY  s.user_id  limit #{page},#{size}",
            "</script>"
    })
    List<Map<String, Object>> listUserForDepids(@Param("deptsId") List<Long> deptsId, @Param("page") int page, @Param("size") int size);

    @Select({
            "<script>",
            "SELECT  s.user_id as userId,s.user_id as id,s.dept_id as deptId,username,nick_name as nickName,gender,phone,",
            "email,avatar_name as avatarName,avatar_path as avatarPath,is_admin as isAdmin,s.enabled,s.create_by as createBy,",
            "s.update_by as updateBy,pwd_reset_time as pwdResetTime,s.create_time as createTime,s.update_time as updateTime,expired_time as expiredTime, \n",
            "d.name ",
            "from sys_user s \n",
            "join sys_dept d\n",
            "on s.dept_id = d.dept_id",
            "<where> ",
                "<if test=\"deptsId != null and deptsId.size>0\" >",
                "d.dept_id in ",
                "<foreach collection='deptsId' item='id' open='(' separator=',' close=')'>",
                "#{id}",
                "</foreach>",
                "</if>",
                "<if test=\"enabled != '' and enabled != null\" >",
                "and s.enabled = #{enabled}",
                "</if>",
                "<if test=\"username != '' and username!= null  \">",
                "and s.username LIKE  #{username}",
                "</if>",
                "<if test=\"startTime != '' and startTime!= null  \">",
                "and s.create_time  <![CDATA[ >= ]]>  #{startTime}",
                "</if>",
                "<if test=\"endTime != '' and endTime!= null  \">",
                "and s.create_time  <![CDATA[ <= ]]>  #{endTime}",
                "</if>",
            "</where>",
            " ORDER BY s.user_id  limit #{page},#{size}",
            "</script>"
    })
    List<Map<String, Object>> listUserForDepidsLikeSearch(@Param("deptsId") List<Long> deptsId, @Param("page") int page, @Param("size") int size,
                                                          @Param("enabled") String enabled, @Param("username") String username,
                                                          @Param("startTime") String startTime, @Param("endTime") String endTime);

}
