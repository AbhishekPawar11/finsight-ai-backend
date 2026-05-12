package com.example.finance_tracker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.api.client.util.Value;

@Configuration
public class WebConfig {
    
	@Value("${frontend.url:http://localhost:4200}}")
	private String frontendurl;
	
	@Bean
  public WebMvcConfigurer corsConfigurer() {
	   return new WebMvcConfigurer() {
		   @Override
		   public void addCorsMappings(CorsRegistry  registry) {
			    registry.addMapping("/**")
			            .allowedOriginPatterns(frontendurl)
			            .allowedMethods("GET","POST" ,"PUT" , "DELETE")
			            .allowedHeaders("*")
			            .allowCredentials(true);
		   }
		   
	};
  }
}
