package exception.window.service;

import org.springframework.stereotype.Service;

@Service
public class ExceptionTestService {
    
    public void throwNullPointerException() {
        throw new NullPointerException("This is a test NullPointerException");
    }
    
    public void throwIllegalArgumentException() {
        throw new IllegalArgumentException("This is a test IllegalArgumentException");
    }
    
    public void throwRuntimeException() {
        throw new RuntimeException("This is a test RuntimeException");
    }
    
    public String methodThatSucceeds() {
        return "Success!";
    }
    
    public int divide(int a, int b) {
        // This will throw ArithmeticException if b is 0
        return a / b;
    }
}