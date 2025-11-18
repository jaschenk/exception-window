package exception.window;

import exception.window.model.ExceptionWrapper;
import exception.window.service.ExceptionWindowService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log4j2
@SpringBootTest
@TestMethodOrder( MethodOrderer.MethodName.class )
class ExceptionWindowApplicationTests {

    @Autowired
    ExceptionWindowService service;

	@Test
	void test01_contextLoads() {
        assertNotNull(service);
	}

    @Test
    void test02_serviceTest() {
        assertNotNull(service);

        service.postException(new ExceptionWrapper(new RuntimeException("test")));
        service.postException(new ExceptionWrapper(new RuntimeException("test")));
        service.postException(new ExceptionWrapper(new RuntimeException("test")));

        service.getExceptions().ifPresent(exceptions -> {
            assertNotNull(exceptions);
            assertEquals(3, exceptions.size());

            for (ExceptionWrapper exception : exceptions) {
                assertNotNull(exception);
                log.error("Exception: {} -- {}", exception, exception.getStackTrace());
            }
        });

    }

    @Test
    void test03_serviceTest() {
        assertNotNull(service);

        try {
            throw new RuntimeException("test-foo-bar");
        } catch(Exception e) {
            // Do Nothing
        }

        service.getExceptions().ifPresent(exceptions -> {
            assertNotNull(exceptions);

            for (ExceptionWrapper exception : exceptions) {
                assertNotNull(exception);
                log.error("Exception: {} -- {}", exception, exception.getStackTrace());
            }
        });

    }
}
