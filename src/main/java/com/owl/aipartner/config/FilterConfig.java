package com.owl.aipartner.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.owl.aipartner.filter.LogApiFilter;

@Configuration
public class FilterConfig {

    // @Bean
    // public FilterRegistrationBean<LogProcessTimeFilter> logProcessTimeFilter() {
    // FilterRegistrationBean<LogProcessTimeFilter> registrationBean = new
    // FilterRegistrationBean<>();
    // LogProcessTimeFilter filter = new LogProcessTimeFilter();
    // registrationBean.setName(filter.getClass().getName());
    // registrationBean.setFilter(filter);
    // registrationBean.addUrlPatterns("/*");
    // registrationBean.setOrder(1);
    // return registrationBean;
    // }

    @Bean
    public FilterRegistrationBean<LogApiFilter> logApiFilter() {
        FilterRegistrationBean<LogApiFilter> registrationBean = new FilterRegistrationBean<>();
        LogApiFilter filter = new LogApiFilter();
        registrationBean.setName(filter.getClass().getName());
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

}
