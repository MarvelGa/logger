package org.logger;

import org.junit.jupiter.api.Test;
import org.logger.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = Application.class)
class SpringBootContextTest {
    @Autowired
    private ApplicationContext context;

    @Test
    void checkContext() {
        assertNotNull(context.getBean(LogService.class));
    }
}
