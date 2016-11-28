package hello.spring.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.spring.security.token.TokenConstants;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket jwt() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("jwt")
				.select()
				.apis(RequestHandlerSelectors.basePackage("hello.spring.security.token.jwt"))
				.paths(PathSelectors.any())
				.build()
				.pathMapping("/")
				.apiInfo(apiInfo())
				.securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(
						new ApiKey("jwtKey", TokenConstants.JWT_HEADER, "header")))
				.globalOperationParameters(Arrays.asList(new ParameterBuilder()
						.name(TokenConstants.JWT_HEADER)
						.description("token")
						.modelRef(new ModelRef("string"))
						.parameterType("header")
						.required(true)
						.build()));
	}

	@Bean
	public Docket token() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("token")
				.select()
				.apis(RequestHandlerSelectors.basePackage("hello.spring.security.token.custom"))
				.paths(PathSelectors.any())
				.build()
				.pathMapping("/")
				.apiInfo(apiInfo())
				.securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(
						new ApiKey("customKey", TokenConstants.CUSTOM_TOKEN_HEADER, "header")))
				.globalOperationParameters(Arrays.asList(new ParameterBuilder()
						.name(TokenConstants.CUSTOM_TOKEN_HEADER)
						.description("token")
						.modelRef(new ModelRef("string"))
						.parameterType("header")
						.required(true)
						.build()));
	}

	@Bean
	public Docket digest() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("digest")
				.select()
				.apis(RequestHandlerSelectors.basePackage("hello.spring.security.digest"))
				.paths(PathSelectors.any())
				.build()
				.pathMapping("/")
				.apiInfo(apiInfo());
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Test API").version("1.0").build();
	}

	@Bean
	public UiConfiguration uiConfig() {
		return new UiConfiguration(null);
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(defaultAuth())
				.build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("customKey", authorizationScopes), 
				new SecurityReference("jwtKey", authorizationScopes));
	}

}
