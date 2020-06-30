package com.ciel.scacommons.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ciel.scaapi.util.Faster;
import com.ciel.scaentity.entity.ScaUser;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class MybatisPlusAuto implements MetaObjectHandler {

    /**
     *  注意！这里需要标记为填充字段
     *   @TableField(.. fill = fill = FieldFill.INSERT_UPDATE)
     *
     *   要想根据注解FieldFill.xxx和字段名以及字段类型来区分必须使用父类的strictInsertFill或者strictUpdateFill方法
     *    不需要根据任何来区分可以使用父类的fillStrategy方法
     */

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createDate", Faster.now(), metaObject); //mp自动填充
        this.setFieldValByName("deleted", 0, metaObject);
        this.setFieldValByName("updateDate", Faster.now(), metaObject);

        if(metaObject.getOriginalObject().getClass().equals(ScaUser.class)){
            this.setFieldValByName("version", 1, metaObject);
        }

        /**
         *   this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
         *   this.fillStrategy(metaObject, "createTime", LocalDateTime.now()); // 也可以使用(3.3.0 该方法有bug请升级到之后的版本如`3.3.1.8-SNAPSHOT`)
         *          上面选其一使用,下面的已过时(注意 strictInsertFill 有多个方法,详细查看源码)
         *    this.setFieldValByName("operator", "Jerry", metaObject);
         *    this.setInsertFieldValByName("operator", "Jerry", metaObject);
         */
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        this.setFieldValByName("updateDate", Faster.now(), metaObject);

        /**
         *
         *  this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
         *  this.fillStrategy(metaObject, "updateTime", LocalDateTime.now()); // 也可以使用(3.3.0 该方法有bug请升级到之后的版本如`3.3.1.8-SNAPSHOT`)
         *         // 上面选其一使用,下面的已过时(注意 strictUpdateFill 有多个方法,详细查看源码)
         *   //this.setFieldValByName("operator", "Tom", metaObject);
         *    //this.setUpdateFieldValByName("operator", "Tom", metaObject);
         */
    }
}
