package exception.window.service;

import exception.window.model.ExceptionWrapper;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ExceptionWindowService {

    void postException(ExceptionWrapper exceptionWrapper);

    Optional<List<ExceptionWrapper>> getExceptions();

    Optional<List<ExceptionWrapper>> getExceptionsByTime(Date from, Date to);

    Optional<ExceptionWrapper> getExceptionById(String id);

    void cleanUpHistory();
}
