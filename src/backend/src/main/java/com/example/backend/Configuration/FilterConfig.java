package com.example.backend.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.backend.Components.TokenFilter;

@Configuration
public class FilterConfig{

    @Autowired
    TokenFilter tokenFilter;

    @Bean
    public FilterRegistrationBean<TokenFilter> filterRegistrationBean(){
        FilterRegistrationBean<TokenFilter> registrationBean=new FilterRegistrationBean<>();
        registrationBean.setFilter(tokenFilter);
        registrationBean.addUrlPatterns("/secure/*");
        registrationBean.addUrlPatterns("/user/login/check");
        registrationBean.addUrlPatterns("/user/follow/add");
        registrationBean.addUrlPatterns("/user/follow/del");
        registrationBean.addUrlPatterns("/user/bookmark/add");
        registrationBean.addUrlPatterns("/user/bookmark/del");
        registrationBean.addUrlPatterns("/user/preference/add");
        registrationBean.addUrlPatterns("/user/preference/del");
        registrationBean.addUrlPatterns("/user/avatar/set");
        registrationBean.addUrlPatterns("/recipe/upload");
        registrationBean.addUrlPatterns("/recipe/*");
        registrationBean.addUrlPatterns("/comment/add");
        registrationBean.addUrlPatterns("/comment/del");
        registrationBean.addUrlPatterns("/comment/liked/add");
        registrationBean.addUrlPatterns("/comment/liked/del");
        return registrationBean;
    }
}
