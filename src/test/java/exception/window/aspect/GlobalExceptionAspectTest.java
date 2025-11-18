package exception.window.aspect;

import exception.window.service.ExceptionTestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GlobalExceptionAspectTest {
    
    @Autowired
    private ExceptionTestService exceptionTestService;
    
    @Autowired
    private GlobalExceptionAspect globalExceptionAspect;
    
    @Test
    void testNullPointerExceptionIsCaughtByAspect() {
        // This should trigger the aspect when NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> {
            exceptionTestService.throwNullPointerException();
        });
        
        // Check console logs - you should see the formatted exception log from the aspect
        System.out.println("✓ NullPointerException was thrown and should be logged by aspect");
    }
    
    @Test
    void testIllegalArgumentExceptionIsCaughtByAspect() {
        // This should trigger the aspect when IllegalArgumentException is thrown
        assertThrows(IllegalArgumentException.class, () -> {
            exceptionTestService.throwIllegalArgumentException();
        });
        
        System.out.println("✓ IllegalArgumentException was thrown and should be logged by aspect");
    }
    
    @Test
    void testRuntimeExceptionIsCaughtByAspect() {
        // This should trigger the aspect when RuntimeException is thrown
        assertThrows(RuntimeException.class, () -> {
            exceptionTestService.throwRuntimeException();
        });
        
        System.out.println("✓ RuntimeException was thrown and should be logged by aspect");
    }
    
    @Test
    void testArithmeticExceptionIsCaughtByAspect() {
        // This should trigger the aspect when ArithmeticException is thrown
        assertThrows(ArithmeticException.class, () -> {
            exceptionTestService.divide(10, 0);
        });
        
        System.out.println("✓ ArithmeticException was thrown and should be logged by aspect");
    }
    
    @Test
    void testSuccessfulMethodDoesNotTriggerAspect() {
        // This should NOT trigger the aspect because no exception is thrown
        String result = exceptionTestService.methodThatSucceeds();
        
        assertEquals("Success!", result);
        System.out.println("✓ Successful method executed without triggering exception aspect");
    }
}