package mouda.backend.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:8081",
				"https://dev.mouda.site",
				"https://appleid.apple.com",
				"https://test.mouda.site", "http://test.mouda.site",
				"https://mouda.site", "http://mouda.site",
				"https://www.mouda.site", "http://www.mouda.site"
			)
			.allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
			.allowedHeaders("Authorization", "Content-Type")
			.allowCredentials(true)
			.maxAge(3600);
	}
}
