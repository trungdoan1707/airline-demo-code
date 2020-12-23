package com.codegym.airline.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * Config Cross origin request cho toàn bộ project.
     * là dự án demo nên ở đây config cho phép toàn bộ request được truy cập
     * tham khảo thêm tại: https://spring.io/guides/gs/rest-service-cors/
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }
}
