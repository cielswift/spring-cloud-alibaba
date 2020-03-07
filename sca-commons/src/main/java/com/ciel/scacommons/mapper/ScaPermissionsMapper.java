package com.ciel.scacommons.mapper;

import com.ciel.scaentity.entity.ScaPermissions;
import com.ciel.scaentity.entity.ScaRole;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiapeixin
 * @since 2020-02-14
 */
@Mapper
public interface ScaPermissionsMapper extends MyBaseMapper<ScaPermissions> {


    @Select("<script> "+
            "select pe.* from sca_role_permissions as  rp " +
            "left join sca_permissions as  pe " +
            "on rp.PERMISSIONS_ID = pe.id where rp.ROLE_ID in " +
            "<foreach item='item' index='index' collection='list'  open='(' separator=',' close=')'> " +
            "#{item.id} " +
            "</foreach> " +
            "</script> ")
    public List<ScaPermissions> byRoles(@Param("list") List<ScaRole> list);

}
