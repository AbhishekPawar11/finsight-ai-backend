package com.example.finance_tracker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
  
	@Bean
  public WebMvcConfigurer corsConfigurer() {
	   return new WebMvcConfigurer() {
		   @Override
		   public void addCorsMappings(CorsRegistry  registry) {
			    registry.addMapping("/**")
			            .allowedOriginPatterns("http://localhost:4200" , "http://172.20.10.2:4200")
			            .allowedMethods("GET","POST" ,"PUT" , "DELETE")
			            .allowedHeaders("*")
			            .allowCredentials(true);
		   }
		   
	};
  }
}
