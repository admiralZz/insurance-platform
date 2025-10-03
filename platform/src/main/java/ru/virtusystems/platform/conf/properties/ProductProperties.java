package ru.virtusystems.platform.conf.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "app")
public record ProductProperties(Map<String, ProductConfig> products) {
    public static final String PRODUCT_CODE_DMS = "dms";
    public static final String PRODUCT_CODE_ZACHITA_DOHODA_2_0 = "zdc";

    public ProductConfig getProductConfig(String key) {
        return products.get(key);
    }

    public record ProductConfig(
            boolean enabled,
            String name,
            String description,
            TariffConfig tariff
    ) {
    }

    public record TariffConfig(
            String path,
            boolean showEmptyValuesParams
    ) {
    }
}
