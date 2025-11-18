package exception.window.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Test configuration to ensure AOP is enabled in test context
 * Import this in your test classes if AOP is not working
 */
@TestConfiguration
@EnableAspectJAutoProxy
public class TestAopConfiguration {
    // This ensures AOP is enabled in tests
}