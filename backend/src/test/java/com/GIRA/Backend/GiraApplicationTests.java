package com.GIRA.Backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

/**
 * Integration test for the GIRA Spring Boot application context.
 * <p>
 * Verifies that the application context loads successfully.
 * </p>
 *
 * @author Mohamed yahya jabrane
 * @since 1.0
 */
@SpringBootTest
@Import(TestConfig.class)
@TestPropertySource(properties = {
    "spring.jpa.show-sql=true",
    "spring.jpa.properties.hibernate.format_sql=true",
    "spring.jpa.properties.hibernate.use_sql_comments=true",
    "logging.level.org.hibernate.SQL=DEBUG",
    "logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE",
    "spring.mail.username=test@example.com",
    "spring.mail.password=password",
    "spring.mail.host=localhost",
    "spring.mail.port=1025",
    "app.upload.dir=target/test-uploads",
    "app.file.max-size=10485760"
})
class GiraApplicationTests {

    @Test
    void contextLoads() {
        // This test will help us see the exact SQL being generated
        // and identify where the table creation is failing
    }

}
