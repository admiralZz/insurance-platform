package ru.virtusystems.platform.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.virtusystems.domain.client.StandardClientService;
import ru.virtusystems.domain.port.client.ClientService;
import ru.virtusystems.domain.port.product.ProductService;
import ru.virtusystems.domain.product.StandardProductService;
import ru.virtusystems.domain.port.territory.CountryService;
import ru.virtusystems.domain.dictionary.territory.StandardCountryService;
import ru.virtusystems.platform.database.repository.adapter.InsuredRepositoryJpaAdapter;
import ru.virtusystems.platform.database.repository.adapter.ProductRepositoryJpaAdapter;

@Configuration
public class StandardServiceConfiguration {

    @Bean
    public ClientService clientService(InsuredRepositoryJpaAdapter insuredRepositoryJpaAdapter) {
        return new StandardClientService(insuredRepositoryJpaAdapter);
    }

    @Bean
    public ProductService productService(ProductRepositoryJpaAdapter productRepositoryJpaAdapter) {
        return new StandardProductService(productRepositoryJpaAdapter);
    }

    @Bean
    public CountryService countryService() {
        return new StandardCountryService();
    }

}
