package pbl.GNUB.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
        // ✅ 정적 리소스 설정
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/uploads/**")
                                .addResourceLocations("file:uploads/");
                registry.addResourceHandler("/javascript/**")
                                .addResourceLocations("classpath:/static/javascript/");
                registry.addResourceHandler("/image/**")
                                .addResourceLocations("classpath:/static/image/");
                registry.addResourceHandler("/mobile/**")
                                .addResourceLocations("classpath:/static/mobile/");
        }

        // ✅ React 라우팅 처리 - index.html 포워딩
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
                // React Router 경로 지원: /mobile, /mobile/abc, /mobile/abc/def 등 모두 index.html 반환
                registry.addViewController("/mobile")
                .setViewName("forward:/mobile/index.html");
                registry.addViewController("/mobile/")
                .setViewName("forward:/mobile/index.html");
                registry.addViewController("/mobile/{path:^(?!static|.*\\..*).*$}")
                .setViewName("forward:/mobile/index.html");
                registry.addViewController("/mobile/**/{path:^(?!static|.*\\..*).*$}")
                .setViewName("forward:/mobile/index.html");
        }

        // ✅ CORS 설정 추가
        @Override
        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 경로에 대해
                                .allowedOrigins("http://localhost:3000") // React 개발 서버 주소
                                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                                .allowCredentials(true); // 세션/쿠키 공유
        }

}
