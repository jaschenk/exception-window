package exception.window.service;

import exception.window.model.ExceptionWrapper;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ExceptionWindowService {

    Integer getExceptionCount();

    void postException(ExceptionWrapper exceptionWrapper);

    Optional<List<ExceptionWrapper>> getExceptions();

    Optional<List<ExceptionWrapper>> getExceptionsByTime(Date from, Date to);

    Optional<ExceptionWrapper> getExceptionById(String id);

    void scheduledCleanUpHistory();

    void clearAllHistory();

    // For Testing ...
    void throwDomainException(String message);

    // For Testing ...
    void throwNPE(String message);

    // For Testing ...
    void throwRuntimeException(String message);

    // For Testing ...
    void throwException(String message) throws Exception;
}
