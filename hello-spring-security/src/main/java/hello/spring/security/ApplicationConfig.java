package hello.spring.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(value={"hello.spring.security"})
@EnableSwagger2
public class ApplicationConfig {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ApplicationConfig.class);
		app.run(args);
	}

}
