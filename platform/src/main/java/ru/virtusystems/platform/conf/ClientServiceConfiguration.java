package ru.virtusystems.platform.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.virtusystems.domain.client.StandardClientService;
import ru.virtusystems.domain.port.ClientService;
import ru.virtusystems.platform.database.repository.adapter.InsuredRepositoryJpaAdapter;

@Configuration
public class ClientServiceConfiguration {

    @Bean
    public ClientService clientService(InsuredRepositoryJpaAdapter insuredRepositoryJpaAdapter) {
        return new StandardClientService(insuredRepositoryJpaAdapter);
    }

}
