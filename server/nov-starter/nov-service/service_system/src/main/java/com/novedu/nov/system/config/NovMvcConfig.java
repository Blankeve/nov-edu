package com.novedu.nov.system.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ：juam
 * @date ：2021/12/9 15:37
 * @description：
 * @modified By：
 * @version:
 */
//@Configuration
public class NovMvcConfig implements WebMvcConfigurer {


//    @Bean
//    public LoginInterceptor getSessionInterceptor() {
//        return new LoginInterceptor();
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        InterceptorRegistration registration = registry.addInterceptor(getSessionInterceptor());
//        registration.addPathPatterns("/**");                      //所有路径都被拦截
//        registration.excludePathPatterns(                         //添加不拦截路径//登录
//                "/**/*.html",            //html静态资源
//                "/**/*.js",              //js静态资源
//                "/**/*.css",             //css静态资源
//                "/**/*.woff",
//                "/**/*.ttf"
//        );
//    }

}
