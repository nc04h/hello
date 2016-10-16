package hello.spring.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan(value={"hello.spring.security"})
@EnableWebMvc
public class ApplicationConfig {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ApplicationConfig.class);
		app.run(args);
	}

}
