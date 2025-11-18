package exception.window.aspect;

import exception.window.service.ExceptionTestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to demonstrate @AfterThrowing advice execution
 * Run these tests and check the console output to see the aspect logging
 */
@SpringBootTest
class AfterThrowingExamplesTest {
    
    @Autowired
    private ExceptionTestService exceptionTestService;
    
    @Test
    void testNullPointerException() {
        System.out.println("\n========== TEST: NullPointerException ==========");
        
        assertThrows(NullPointerException.class, () -> {
            exceptionTestService.throwNullPointerException();
        });
        
        System.out.println("Check logs above - multiple aspects should have caught this exception");
        System.out.println("================================================\n");
    }
    
    @Test
    void testIllegalArgumentException() {
        System.out.println("\n========== TEST: IllegalArgumentException ==========");
        
        assertThrows(IllegalArgumentException.class, () -> {
            exceptionTestService.throwIllegalArgumentException();
        });
        
        System.out.println("Check logs above - multiple aspects should have caught this exception");
        System.out.println("====================================================\n");
    }
    
    @Test
    void testRuntimeException() {
        System.out.println("\n========== TEST: RuntimeException ==========");
        
        assertThrows(RuntimeException.class, () -> {
            exceptionTestService.throwRuntimeException();
        });
        
        System.out.println("Check logs above - multiple aspects should have caught this exception");
        System.out.println("============================================\n");
    }
    
    @Test
    void testArithmeticException() {
        System.out.println("\n========== TEST: ArithmeticException (Division by Zero) ==========");
        
        assertThrows(ArithmeticException.class, () -> {
            exceptionTestService.divide(10, 0);
        });
        
        System.out.println("Check logs above - aspects with argument binding should show a=10, b=0");
        System.out.println("==================================================================\n");
    }
    
    @Test
    void testSuccessfulMethod() {
        System.out.println("\n========== TEST: Successful Method (No Exception) ==========");
        
        String result = exceptionTestService.methodThatSucceeds();
        
        assertEquals("Success!", result);
        System.out.println("No aspect logging should appear for this test");
        System.out.println("============================================================\n");
    }
    
    @Test
    void testMultipleExceptions() {
        System.out.println("\n========== TEST: Multiple Exceptions in Sequence ==========");
        
        // Test 1: NullPointerException
        assertThrows(NullPointerException.class, () -> {
            exceptionTestService.throwNullPointerException();
        });
        
        // Test 2: IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            exceptionTestService.throwIllegalArgumentException();
        });
        
        // Test 3: ArithmeticException
        assertThrows(ArithmeticException.class, () -> {
            exceptionTestService.divide(5, 0);
        });
        
        System.out.println("Check logs above - you should see different exception handlers triggered");
        System.out.println("===========================================================\n");
    }
}