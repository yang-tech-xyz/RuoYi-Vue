package com.ruoyi.web;

import com.ruoyi.web.filter.RequestContextFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.TimeZone;

/**
 * 启动程序
 */
@SpringBootApplication(scanBasePackages = {"com.ruoyi"})
@EnableScheduling
public class ClientAdminApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(ClientAdminApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean toolFilter() {
        FilterRegistrationBean<RequestContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestContextFilter());
        registration.addUrlPatterns("/*");
        registration.setName("myFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
