package com.ciel.scaproducer2.config.oauth2;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;
import java.util.*;

/**
 * oauth2授权服务器
 */

@Configuration
@EnableAuthorizationServer

@AllArgsConstructor
@Slf4j
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    protected DataSource dataSource;

    protected AuthenticationManager authenticationManager;

    protected AutowireCapableBeanFactory beanFactory;

    /**
     *
     * get http://127.0.0.1:25011/producer20/oauth/authorize?client_id=p10&response_type=code&redirect_uri=https://translate.google.cn&Authentication=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqd3RfdG9rZW4iLCJhdWQiOiJ1c2VyIiwiaXNzIjoic3NvX3NlcnZlciIsImJvZHkiOiJ7XCJhdXRob3JpdGVzXCI6W1wiUk9MRV9BRE1JTlwiLFwiQ0hBTkdFXCIsXCJBRERcIixcIlJFTU9WRVwiXSxcImlkXCI6NDI1NzUyOTQzNTMyMDU2NTc2LFwidXNlcm5hbWVcIjpcIkFubmFTbWl0aFwiLFwicmVhbG5hbWVcIjpcIkFubmFTbWl0aFwifSIsImV4cCI6MTU4Mzc4MDI2NCwiaWF0IjoxNTgzNzU4NjY0fQ.6uEP3chze_JAmcgBeHwZf8BZ8hcDkA4rsk89caNR4Yw
     *
     * post http://127.0.0.1:25011/producer20/oauth/token?client_id=p10&client_secret=123456&grant_type=authorization_code&code=OF27D8&redirect_uri=https://translate.google.cn
     *
     *
     * 同jdbc差不多,返回的是token
     *
     * {
     *     "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsic3ByaW5nY2xvdWQtcHJvZHVjZXIiXSwidXNlcl9uYW1lIjoieGlhcGVpeGluIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTU4MDY0NzU0MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9hZG1pbiIsInN5c19hZGQiXSwianRpIjoiZjFjY2QwNWMtNTZhOS00OWFmLTgxNTgtMDljZDhkYzlmZGRmIiwiY2xpZW50X2lkIjoieGlhcGVpeGluIn0.PMmzM6r60sQ94e6U9kjdnBwZVXIMhToOBnYI8sMZPo4",
     *     "token_type": "bearer",
     *     "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsic3ByaW5nY2xvdWQtcHJvZHVjZXIiXSwidXNlcl9uYW1lIjoieGlhcGVpeGluIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImF0aSI6ImYxY2NkMDVjLTU2YTktNDlhZi04MTU4LTA5Y2Q4ZGM5ZmRkZiIsImV4cCI6MTU4MzIzMjM0MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9hZG1pbiIsInN5c19hZGQiXSwianRpIjoiYTA1YjRjZDktMWIxMS00ZWI5LWI1MDMtMzQ1OTE1YmNhMmRiIiwiY2xpZW50X2lkIjoieGlhcGVpeGluIn0.XQxkTj6bc8XztgIPtgfra96OT_uZqnT5LYvajfsToWw",
     *     "expires_in": 7199,
     *     "scope": "read write",
     *     "jti": "f1ccd05c-56a9-49af-8158-09cd8dc9fddf"
     * }
     *
     */

    /**
     * jwt 内容增强器
     */

    @Bean
    public TokenEnhancer enhancer(){
        return new TokenEnhancer() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

                Map<String, Object> info = new HashMap<>();
                info.put("enhance", "夏培鑫202");

                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
                return accessToken;
            }
        };
    }

    /**
     * 令牌存储策略
     */
    @Bean
    public TokenStore tokenStore() {

//        RedisConnectionFactory bean = beanFactory.getBean(RedisConnectionFactory.class);
//        return new RedisTokenStore(bean);

        return new JwtTokenStore(accessTokenConverter());
    }


    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("^x?ia-pe=i#xi&n!20_2$"); //密钥
        return accessTokenConverter;
    }


    /**
     * 授权码模式存储策略
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * 授权信息保存策略
     */
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    @Bean
    public AuthorizationServerTokenServices tokenServices() {

        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(jdbcClientDetailsService()); //客户端服务详情
        tokenServices.setSupportRefreshToken(true); //支持刷新
        tokenServices.setTokenStore(tokenStore()); //令牌存储策略

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter()));

        tokenServices.setTokenEnhancer(tokenEnhancerChain);

        tokenServices.setAccessTokenValiditySeconds(7200); //有效期
        tokenServices.setRefreshTokenValiditySeconds(25920); //刷新时间

        return tokenServices;
    }

    /**
     * 检测token的策略
     * 配置 AuthorizationServer 安全认证的相关信息，创建 ClientCredentialsTokenEndpointFilter 核心过滤器
     *
     * TokenEndpoint 加载客户端信息,结合请求信息，创建 TokenRequest,将 TokenRequest 传递给 TokenGranter 颁发 token
     *  就是 OAuth2AccessToken 的实现类 DefaultOAuth2AccessToken 就是最终在控制台得到的 token 序列化之前的原始类
     *
     *      TokenGranter 的设计思路是使用 CompositeTokenGranter 管理一个 List 列表，每一种 grantType 对应一个具体的真正授权者，
     *      在 debug 过程中可以发现 CompositeTokenGranter 内部就是在循环调用五种 TokenGranter 实现类的 grant 方法，
     *      而 granter 内部则是通过 grantType 来区分是否是各自的授权类型。
     *             5种模式
     *          ResourceOwnerPasswordTokenGranter ==> password 密码模式
     *          AuthorizationCodeTokenGranter ==> authorization_code 授权码模式
     *          ClientCredentialsTokenGranter ==> client_credentials 客户端模式
     *          ImplicitTokenGranter ==> implicit 简化模式
     *          RefreshTokenGranter ==>refresh_token 刷新 token 专用
     *  通过 TokenGranter 的 AuthorizationServerTokenServices.createAccessToken() 来创建 token
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();  // 允许表单认证

        security.checkTokenAccess("permitAll()") //检验必须认证; isAuthenticated()
                .tokenKeyAccess("permitAll()");
    }

    /**
     *  配置 AuthorizationServerEndpointsConfigurer 众多相关类，包括配置身份认证器，配置认证方式，
     *  TokenStore，TokenGranter，OAuth2RequestFactory
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        //endpoints.tokenEnhancer(enhancer());

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        // 配置jwt内容增强器
        delegates.add(enhancer());
        delegates.add(accessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(delegates);
        endpoints.tokenEnhancer(tokenEnhancerChain);

        endpoints.tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
                .authenticationManager(authenticationManager);
    }

    /**
     * 客户端服务详情
     */
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 客户端保存策略
     * 配置 OAuth2 的客户端相关信息
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService());
    }

}
