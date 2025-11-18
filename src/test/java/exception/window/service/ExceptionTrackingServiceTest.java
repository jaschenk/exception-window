package exception.window.service;

import exception.window.exception.DomainException;
import exception.window.model.ExceptionWrapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class demonstrating the CORRECT and WRONG ways to test exception handling with AOP
 */
@Log4j2
@SpringBootTest
@EnableAspectJAutoProxy
@TestMethodOrder( org.junit.jupiter.api.MethodOrderer.MethodName.class)
class ExceptionTrackingServiceTest {

    @Autowired
    private ExceptionWindowService service;
    
    @BeforeEach
    void setUp() {
        assertNotNull(service);
        service.clearAllHistory();
        System.out.println("\n" + "=".repeat(70));
    }
    
    @Test
    void test01_verifyServiceIsLoaded() {
        System.out.println("TEST 01: Verify Service is Loaded");
        System.out.println("=".repeat(70));
        
        assertNotNull(service);
        log.info("✓ Service is loaded: {}", service.getClass().getName());
        
        System.out.println("=".repeat(70) + "\n");
    }
    
    @Test
    void test02_correctWay_exceptionFromServiceMethod() {
        System.out.println("TEST 02: CORRECT WAY - Exception from Service Method");
        System.out.println("=".repeat(70));
        
        // ✅ CORRECT - Exception thrown from service method
        assertThrows(DomainException.class, () -> {
            service.throwDomainException("This is a Domain Exception...");
        });
        
        // Verify aspect caught it
        service.getExceptions().ifPresent(exceptions -> {
            assertNotNull(exceptions);
            assertEquals(1, exceptions.size(), "Should have caught 1 exception");
            
            ExceptionWrapper exception = exceptions.get(0);
            assertEquals("exception.window.exception.DomainException", exception.getExceptionType());
            assertEquals("throwDomainException", exception.getMethodName());
            
            log.error("✓ Exception caught by aspect: {}", exception);
        });
        
        System.out.println("=".repeat(70) + "\n");
    }
    
    @Test
    void test03_correctWay_exceptionFromTestHelperMethod() {
        System.out.println("TEST 03: CORRECT WAY - Exception from Test Helper Method");
        System.out.println("=".repeat(70));
        
        // ✅ CORRECT - Exception thrown from service method
        assertThrows(DomainException.class, () -> {
            service.throwDomainException("test-foo-bar");
        });
        
        // Verify aspect caught it
        service.getExceptions().ifPresent(exceptions -> {
            assertNotNull(exceptions);
            assertEquals(1, exceptions.size(), "Should have caught 1 exception");
            
            ExceptionWrapper exceptionWrapper = exceptions.getFirst();
            assertEquals("test-foo-bar", exceptionWrapper.getExceptionMessage());
            assertEquals("exception.window.exception.DomainException", exceptionWrapper.getExceptionType());
            assertEquals("throwDomainException", exceptionWrapper.getMethodName());
            
            log.error("✓ Exception caught by aspect: {}", exceptionWrapper.getStackTrace());
        });
        
        System.out.println("=".repeat(70) + "\n");
    }
    
    @Test
    void test04_wrongWay_exceptionInTestCode() {
        System.out.println("TEST 04: WRONG WAY - Exception Thrown in Test Code");
        System.out.println("=".repeat(70));
        
        // ❌ WRONG - This exception is NOT caught by aspect
        try {
            throw new DomainException("test-foo-bar");
        } catch(Exception e) {
            log.warn("Exception thrown in test code (NOT caught by aspect): {}", e.getMessage());
        }
        
        // Verify aspect did NOT catch it
        service.getExceptions().ifPresentOrElse(
            exceptions -> {
                assertEquals(0, exceptions.size(), 
                    "Should NOT have caught exception thrown in test code");
            },
            () -> log.info("✓ Correctly, no exceptions were tracked (exception was in test code)")
        );
        
        System.out.println("=".repeat(70) + "\n");
    }
    
    @Test
    void test05_multipleExceptions() {
        System.out.println("TEST 05: Multiple Exceptions from Service Methods");
        System.out.println("=".repeat(70));
        
        // Throw multiple exceptions from service methods
        assertThrows(NullPointerException.class, () -> service.throwNPE(null));
        assertThrows(NullPointerException.class, () -> service.throwNPE(null));
        assertThrows(DomainException.class, () -> service.throwDomainException("test-1"));
        
        // Verify all were caught
        service.getExceptions().ifPresent(exceptions -> {
            assertEquals(3, exceptions.size(), "Should have caught 3 exceptions");
            
            log.info("✓ All 3 exceptions were caught by aspect:");
            for (int i = 0; i < exceptions.size(); i++) {
                ExceptionWrapper exception = exceptions.get(i);
                assertNotNull(exception);
                log.error("  Exception {}: {}", i + 1, exception);
            }
        });
        
        System.out.println("=".repeat(70) + "\n");
    }
    
    @Test
    void test06_demonstrateDifference() {
        System.out.println("TEST 06: Demonstrating the Difference");
        System.out.println("=".repeat(70));
        
        // First, throw exception in test code (won't be caught)
        try {
            throw new DomainException("exception-in-test-code");
        } catch(Exception e) {
            log.warn("❌ Exception in test code: {}", e.getMessage());
        }
        
        assertEquals(0, service.getExceptionCount(), 
            "No exceptions should be tracked yet");
        
        // Now, throw exception from service method (will be caught)
        assertThrows(DomainException.class, () -> {
            service.throwDomainException("exception-from-service");
        });
        
        assertEquals(1, service.getExceptionCount(), 
            "Should have tracked 1 exception from service method");
        
        service.getExceptions().ifPresent(exceptions -> {
            ExceptionWrapper exceptionWrapper = exceptions.getFirst();
            assertEquals("exception-from-service", exceptionWrapper.getExceptionMessage());
            log.info("✓ Only the service method exception was caught: {}", exceptionWrapper.getExceptionMessage());
        });
        
        System.out.println("=".repeat(70) + "\n");
    }
    
    @Test
    void test07_verifyExceptionDetails() {
        System.out.println("TEST 07: Verify Exception Details");
        System.out.println("=".repeat(70));
        
        assertThrows(NullPointerException.class, () -> {
            service.throwNPE("");
        });
        
        service.getExceptions().ifPresent(exceptions -> {
            assertEquals(1, exceptions.size());
            
            ExceptionWrapper exception = exceptions.get(0);
            
            // Verify all details are captured
            assertNotNull(exception.getExceptionMessage());
            assertNotNull(exception.getExceptionType());
            assertNotNull(exception.getMethodName());
            assertNotNull(exception.getExceptionType());
            assertNotNull(exception.getStackTrace());
            assertNotNull(exception.getFirstOccurrence());
            
            assertEquals("java.lang.NullPointerException", exception.getExceptionType());
            assertEquals("throwNPE", exception.getMethodName());
            
            log.info("✓ All exception details captured correctly:");
            log.info("  Message: {}", exception.getExceptionMessage());
            log.info("  Type: {}", exception.getExceptionType());
            log.info("  Method: {}", exception.getMethodName());
            log.info("  Class: {}", exception.getExceptionType());
            log.info("  Timestamp: {}", exception.getFirstOccurrence());
            log.info("  Stack trace length: {}", exception.getStackTrace().length());
        });
        
        System.out.println("=".repeat(70) + "\n");
    }
}