package exception.window.service;

import exception.window.model.ExceptionWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExceptionWindowServiceImplTest {

    private ExceptionWindowServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ExceptionWindowServiceImpl();
    }

    @Test
    void postException_and_getExceptions_shouldReturnPostedExceptions() {
        ExceptionWrapper ex1 = new ExceptionWrapper("1", new Date(), new RuntimeException("error1"));
        ExceptionWrapper ex2 = new ExceptionWrapper("2", new Date(), new RuntimeException("error2"));

        service.postException(ex1);
        service.postException(ex2);

        Optional<List<ExceptionWrapper>> resultOpt = service.getExceptions();

        assertTrue(resultOpt.isPresent());
        List<ExceptionWrapper> result = resultOpt.get();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(e -> "1".equals(e.getId())));
        assertTrue(result.stream().anyMatch(e -> "2".equals(e.getId())));
    }

    @Test
    void getExceptions_shouldReturnEmptyListWhenNoExceptions() {
        Optional<List<ExceptionWrapper>> resultOpt = service.getExceptions();

        assertTrue(resultOpt.isPresent());
        assertTrue(resultOpt.get().isEmpty());
    }

    @Test
    void getExceptionsByTime_shouldReturnOnlyExceptionsInRange() throws InterruptedException {
        Date before = new Date();
        Thread.sleep(5);

        ExceptionWrapper ex1 = new ExceptionWrapper("1", new Date(), new RuntimeException("error1"));
        service.postException(ex1);

        Thread.sleep(5);
        Date middle = new Date();
        Thread.sleep(5);

        ExceptionWrapper ex2 = new ExceptionWrapper("2", new Date(), new RuntimeException("error2"));
        service.postException(ex2);

        Thread.sleep(5);
        Date after = new Date();

        // range that contains only ex1
        Optional<List<ExceptionWrapper>> onlyFirstOpt =
                service.getExceptionsByTime(before, middle);
        assertTrue(onlyFirstOpt.isPresent());
        List<ExceptionWrapper> onlyFirst = onlyFirstOpt.get();
        assertEquals(1, onlyFirst.size());
        assertEquals("1", onlyFirst.getFirst().getId());

        // range that contains both
        Optional<List<ExceptionWrapper>> bothOpt =
                service.getExceptionsByTime(before, after);
        assertTrue(bothOpt.isPresent());
        List<ExceptionWrapper> both = bothOpt.get();
        assertEquals(2, both.size());
    }

    @Test
    void getExceptionsByTime_shouldReturnEmptyOptionalForNullBounds() {
        ExceptionWrapper ex = new ExceptionWrapper("1", new Date(), new RuntimeException("error"));
        service.postException(ex);

        assertTrue(service.getExceptionsByTime(null, new Date()).isEmpty());
        assertTrue(service.getExceptionsByTime(new Date(), null).isEmpty());
        assertTrue(service.getExceptionsByTime(null, null).isEmpty());
    }

    @Test
    void getExceptionsByTime_shouldReturnEmptyOptionalWhenNoMatches() {
        ExceptionWrapper ex = new ExceptionWrapper("1", new Date(), new RuntimeException("error"));
        service.postException(ex);

        Date from = new Date(System.currentTimeMillis() + 10_000);
        Date to = new Date(System.currentTimeMillis() + 20_000);

        Optional<List<ExceptionWrapper>> resultOpt = service.getExceptionsByTime(from, to);
        assertTrue(resultOpt.isEmpty());
    }

    @Test
    void getExceptionById_shouldReturnMatchingException() {
        ExceptionWrapper ex1 = new ExceptionWrapper("1", new Date(), new RuntimeException("error1"));
        ExceptionWrapper ex2 = new ExceptionWrapper("2", new Date(), new RuntimeException("error2"));
        service.postException(ex1);
        service.postException(ex2);

        Optional<ExceptionWrapper> result1 = service.getExceptionById("1");
        Optional<ExceptionWrapper> result2 = service.getExceptionById("2");

        assertTrue(result1.isPresent());
        assertEquals("1", result1.get().getId());

        assertTrue(result2.isPresent());
        assertEquals("2", result2.get().getId());
    }

    @Test
    void getExceptionById_shouldReturnEmptyOptionalForUnknownId() {
        ExceptionWrapper ex = new ExceptionWrapper("1", new Date(), new RuntimeException("error"));
        service.postException(ex);

        Optional<ExceptionWrapper> result = service.getExceptionById("unknown");
        assertTrue(result.isEmpty());
    }
}
