package exception.window;

import exception.window.aspect.ExceptionHandlingAspect;
import exception.window.service.ExceptionWindowService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.MethodName.class)
class BeanVerificationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void test01_verifyAspectBeanExists() {
        // Check if aspect bean exists
        assertTrue(applicationContext.containsBean("exceptionHandlingAspect"));

        ExceptionHandlingAspect aspect =
                applicationContext.getBean(ExceptionHandlingAspect.class);
        assertNotNull(aspect);

        System.out.println("✅ Aspect bean exists!");
    }

    @Test
    void test02_verifyServiceBeanExists() {
        assertTrue(applicationContext.containsBean("exceptionWindowServiceImpl"));

        ExceptionWindowService service = applicationContext.getBean(ExceptionWindowService.class);
        assertNotNull(service);

        // Check if it's a proxy (it should be for AOP)
        System.out.println("Service class: " + service.getClass().getName());
        System.out.println("Is proxy: " + service.getClass().getName().contains("$"));

        System.out.println("✅ Service bean exists!");
    }
}

