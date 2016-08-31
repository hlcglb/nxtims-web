package com.hyundaiuni.nxtims.security;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.hyundaiuni.nxtims.service.app.ResourceService;
import com.hyundaiuni.nxtims.service.app.UserService;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();

        filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());
        filterSecurityInterceptor.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource());
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());

        return filterSecurityInterceptor;
    }

    @Bean
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap() {
        ResourcesMapFactoryBean factoryBean = new ResourcesMapFactoryBean();

        factoryBean.setResourceService(resourceService);

        try {
            return factoryBean.getObject();
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Bean
    public CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource() {
        CustomFilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource = new CustomFilterInvocationSecurityMetadataSource(
            requestMap());

        return filterInvocationSecurityMetadataSource;
    }

    @Bean
    public AffirmativeBased affirmativeBased() {
        List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();
        accessDecisionVoters.add(roleVoter());

        AffirmativeBased affirmativeBased = new AffirmativeBased(accessDecisionVoters);
        affirmativeBased.setAllowIfAllAbstainDecisions(false);

        return affirmativeBased;
    }

    @Bean
    public RoleVoter roleVoter() {
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix("ROLE_");

        return roleVoter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() {
        try {
            return super.authenticationManagerBean();
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();

        repository.setHeaderName("X-XSRF-TOKEN");

        return repository;
    }

    @Bean
    public CustomAuthenticationSuccessHandler authenticationSuccessHandler() {
        CustomAuthenticationSuccessHandler authenticationSuccessHandler = new CustomAuthenticationSuccessHandler();

        authenticationSuccessHandler.setUserService(userService);

        return authenticationSuccessHandler;
    }

    @Bean
    public CustomAuthenticationFailureHandler authenticationFailureHandler() {
        CustomAuthenticationFailureHandler authenticationFailureHandler = new CustomAuthenticationFailureHandler();

        authenticationFailureHandler.setUserService(userService);

        return authenticationFailureHandler;
    }

    @Bean
    public CustomBasicAuthFilter basicAuthFilter() {
        CustomBasicAuthFilter customBasicAuthFilter = new CustomBasicAuthFilter(authenticationManagerBean());

        customBasicAuthFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        customBasicAuthFilter.setAuthenticationFailureHandler(authenticationFailureHandler());

        return customBasicAuthFilter;
    }

    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler() {
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();

        return accessDeniedHandler;
    }

    @Bean
    public CustomLogoutSuccessHandler logoutHandler() {
        CustomLogoutSuccessHandler logoutHandler = new CustomLogoutSuccessHandler();

        logoutHandler.setUserService(userService);

        return logoutHandler;
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().addFilterBefore(filterSecurityInterceptor(),
            FilterSecurityInterceptor.class).addFilterBefore(basicAuthFilter(),
                BasicAuthenticationFilter.class).addFilterAfter(new CsrfHeaderFilter(),
                    CsrfFilter.class).csrf().csrfTokenRepository(csrfTokenRepository());

        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        http.formLogin().loginPage("/login");

        http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutHandler()).deleteCookies("JSESSIONID");
    }
}
