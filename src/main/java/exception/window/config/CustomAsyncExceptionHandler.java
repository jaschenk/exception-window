package exception.window.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;
import java.util.Arrays;

@Log4j2
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... params) {
        log.error("=== Async Exception Handler ===");
        log.error("Thread: {}", Thread.currentThread());
        log.error("Is Virtual Thread: {}", Thread.currentThread().isVirtual());
        log.error("Exception in method: {}", method.getName());
        log.error("Exception message: {}", throwable.getMessage());
        log.error("Method parameters: {}", Arrays.toString(params));
        log.error("Exception: ", throwable);

        // Add custom logic:
        // - Alert monitoring system
        // - Save to error database
        // - Send to message queue for retry
    }
}
