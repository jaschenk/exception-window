package exception.window.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Log4j2
@Aspect
@Component
public class GlobalExceptionAspect {
   
    /**
     * ORIGINAL POINTCUT - Only catches exceptions in exception.window package
     */
    @Pointcut("within(exception.window..*)")
    public void originalPackageOnly() {}
    
    /**
     * MODIFIED POINTCUT - Catches exceptions in ALL application packages
     * Excludes aspect and config packages to avoid recursion
     */
    @Pointcut("execution(* exception.window..*(..))")
    public void allApplicationMethods() {}
    
    /**
     * ALTERNATIVE 2 - Catch only Spring-managed components
     */
    @Pointcut("within(@org.springframework.stereotype.Service *) || " +
              "within(@org.springframework.stereotype.Repository *) || " +
              "within(@org.springframework.stereotype.Controller *) || " +
              "within(@org.springframework.web.bind.annotation.RestController *)")
    public void allSpringComponents() {}
    
    /**
     * Exception handler using the modified pointcut
     * Change the pointcut parameter to use different matching strategies
     */
    @AfterThrowing(pointcut = "allApplicationMethods()", throwing = "exception")
    public void handleException(JoinPoint joinPoint, Throwable exception) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String exceptionType = exception.getClass().getSimpleName();
        
        log.error("╔════════════════════════════════════════════════════════════");
        log.error("║ EXCEPTION CAUGHT BY GLOBAL AOP ASPECT");
        log.error("╠════════════════════════════════════════════════════════════");
        log.error("║ Class:     {}", className);
        log.error("║ Method:    {}", methodName);
        log.error("║ Exception: {}", exceptionType);
        log.error("║ Message:   {}", exception.getMessage());
        log.error("╚════════════════════════════════════════════════════════════");
        log.error("Full stack trace:", exception);
    }
}