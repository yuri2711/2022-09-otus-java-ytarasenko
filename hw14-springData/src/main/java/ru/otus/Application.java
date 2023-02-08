package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Полезные ссылки:
 *     Стартовая страница - http://localhost:8080
 *     Стартовая логина   - http://localhost:8080/login
 *     Страница клиентов  - http://localhost:8080/clients
 *     REST сервис        - http://localhost:8080/api/client/3
 */

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
