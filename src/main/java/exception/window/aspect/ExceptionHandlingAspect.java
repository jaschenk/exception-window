package exception.window.aspect;

import exception.window.config.ApplicationContextProvider;
import exception.window.model.ExceptionWrapper;
import exception.window.service.ExceptionWindowService;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Log4j2
@Aspect
@Component
public class ExceptionHandlingAspect {

    private ExceptionWindowService exceptionWindowService;

    public ExceptionHandlingAspect() {
        log.info("🟢 ExceptionHandlingAspect BEAN CREATED");
    }

    /**
     * Advice that logs exceptions thrown by any method in the application
     */
    @AfterThrowing(
            pointcut = "execution(* exception.window..*(..)) || " +
                    "within(@org.springframework.stereotype.Service *) || " +
                    "within(@org.springframework.stereotype.Repository *) || " +
                    "within(@org.springframework.stereotype.Controller *) || " +
                    "within(@org.springframework.web.bind.annotation.RestController *)",
            throwing = "exception"
    )
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        if (exceptionWindowService == null) {
            exceptionWindowService =
                    ApplicationContextProvider.getBean(ExceptionWindowService.class);
        }

        log.error("\uD83D\uDD34 ASPECT TRIGGERED Exception in {}.{}() with cause = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                exception.getCause() != null ? exception.getCause() : "NULL",
                exception);

        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(exception);
        exceptionWrapper.setMethodName(joinPoint.getSignature().getName());
        exceptionWindowService.postException(exceptionWrapper);

        // Add other custom exception handling logic here
        // For example: send alerts, log to external system, etc.
    }
}

