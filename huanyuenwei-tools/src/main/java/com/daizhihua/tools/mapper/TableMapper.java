package com.daizhihua.tools.mapper;

import com.daizhihua.tools.entity.CodeColumnConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TableMapper {

    @Select("select table_name as tableName,create_time as createTime," +
            " engine , table_collation as coding, table_comment  as remark" +
            "  from information_schema.TABLES where TABLE_SCHEMA=(select database())")
    List<Map> listTable();

    @Select( "select  table_name as tableName, column_name, is_nullable as notNull, " +
            "data_type as columnType, column_comment as remark, column_key as keyType, extra" +
            "  from information_schema.columns " +
            " where table_name = #{tableName} and table_schema = (select database()) order by ordinal_position")
    List<CodeColumnConfig> query(@Param("tableName") String tableName);
}
