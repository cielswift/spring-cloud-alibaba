package com.ciel.scatquick.oauth2;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "constants")
@Getter
@Setter
public class Constants {

	private String qqAppId;

	private String qqAppSecret;

	private String qqRedirectUrl;

	private String weCatAppId;

	private String weCatAppSecret;

	private String weCatRedirectUrl;

	//自行生成set get方法

}