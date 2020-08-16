package com.xia.contional;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.context.annotation.Profile;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 运行环境
 */
@Profile("dev")
public class ConfigCon implements ConfigurationCondition {

    /**
     * 一个配置类被spring处理有2个阶段：配置类解析阶段、bean注册阶段（将配置类作为bean被注册到spring容器)。
     * 此时通过Condition是无法精细的控制某个阶段的，如果想控制某个阶段，比如可以让他解析，但是不能让他注册
     */

    /**
     * 条件判断的阶段，是在解析配置类的时候过滤还是在创建bean的时候过滤
     *
     * 配置类解析阶段，如果条件为false，配置类将不会被解析
            PARSE_CONFIGURATION,
     * bean注册阶段，如果为false，bean将不会被注册
            REGISTER_BEAN
     */
    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return null;
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return false;
    }

}
