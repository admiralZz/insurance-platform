package ru.virtusystems.platform;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

/**
 * Основа нагрузочного теста с контейнером
 *
 * Контейнер и spring-контекст пересоздается для каждого тест-класса
 */
@SpringBootTest
@ActiveProfiles("test")
//@Transactional нельзя использовать для имитации конкуретной среды
//@WithMockUser(username = "testuser", password = "testpassword", authorities = {"ROLE_USER", "ROLE_ADMIN"})
@Testcontainers
// Создаем новый spring-контекст для каждого отдельного класса тестов
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class ConcurrentIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14.4");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }

}
