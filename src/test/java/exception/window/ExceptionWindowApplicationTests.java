package exception.window;

import exception.window.aspect.ExceptionHandlingAspect;
import exception.window.exception.DomainException;
import exception.window.model.ExceptionWrapper;
import exception.window.service.ExceptionWindowService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log4j2
@Import({ ExceptionHandlingAspect.class})
@SpringBootTest
@EnableAspectJAutoProxy
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
            service.throwDomainException("foo-bar-test");
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
