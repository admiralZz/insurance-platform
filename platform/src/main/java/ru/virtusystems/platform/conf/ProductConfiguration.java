package ru.virtusystems.platform.conf;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.virtusystems.platform.conf.properties.ProductProperties;

@Configuration
@EnableConfigurationProperties(ProductProperties.class)
public class ProductConfiguration {
}
