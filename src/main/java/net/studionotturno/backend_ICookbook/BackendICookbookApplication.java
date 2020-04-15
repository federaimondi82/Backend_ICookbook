package net.studionotturno.backend_ICookbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
 * Mette a disposizione i servizi backend; main dell'applicazione springboot
 * @author Raimondi Federico
 *
 */
@SpringBootApplication
public class BackendICookbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendICookbookApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/").allowedOrigins("http://localhost:8080");
			}
		};
	}

}
