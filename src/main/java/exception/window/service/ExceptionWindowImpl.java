package exception.window.service;

import exception.window.model.ExceptionWrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
public class ExceptionWindowImpl implements ExceptionWindow {


    private final ConcurrentHashMap<String, ExceptionWrapper> EXCEPTION_HISTORY =
            new ConcurrentHashMap<>();


    @Override
    public void postException(ExceptionWrapper exceptionWrapper) {

    }

    @Override
    public Optional<List<ExceptionWrapper>> getExceptions() {
        return Optional.empty();
    }

    @Override
    public Optional<List<ExceptionWrapper>> getExceptionsByTime(Date from, Date to) {
        return Optional.empty();
    }

    @Override
    public Optional<ExceptionWrapper> getExceptionById(String id) {
        return Optional.empty();
    }

    // TODO Set up Schedule to expire older exceptions ...
    @Override
    public void cleanUp() {

    }
}
