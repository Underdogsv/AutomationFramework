package api;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"api", "configs", "ui"})
@PropertySource("classpath:application.properties")
@Import(WireMockConfig.class)
public class ApiTestConfig {
}
