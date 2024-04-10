package com.kopchak.authserver.integration.testcontainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@SpringBootTest
public class PostgresContainerBaseTest {
    @Autowired
    protected DataSource dataSource;

    @ServiceConnection
    protected static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:14.2-alpine3.15")
                    .withDatabaseName("test")
                    .withUsername("postgres")
                    .withPassword("password");

    static {
        postgresContainer.start();
    }
}
