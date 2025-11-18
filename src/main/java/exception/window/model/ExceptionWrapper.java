package exception.window.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionWrapper {

    private String id;

    private String exceptionKey; // Unique fingerprint

    private String exceptionType;

    private String exceptionMessage;

    private String stackTrace;

    private Date firstOccurrence;

    private Date lastOccurrence;

    private String methodName;

    private String threadInfo;

    private String metaData;

    private Integer count = 0;


    public ExceptionWrapper(Throwable t) {
        this.id = UUID.randomUUID().toString();
        this.firstOccurrence = Date.from(Instant.now());
        this.lastOccurrence = Date.from(Instant.now());
        this.exceptionMessage = t.getMessage();
        this.exceptionType = t.getClass().getName();
        this.stackTrace = getStackTraceAsString(t);
        this.count = 1;
        this.threadInfo = Thread.currentThread().getName();
    }

    public ExceptionWrapper(String id, Date date, Throwable t) {
        this.id = id;
        this.firstOccurrence = date;
        this.lastOccurrence = date;
        this.exceptionMessage = t.getMessage();
        this.exceptionType = t.getClass().getName();
        this.stackTrace = getStackTraceAsString(t);
        this.count = 1;
        this.threadInfo = Thread.currentThread().getName();
    }

    public void incrementCount() {
        this.count++;
        this.lastOccurrence = Date.from(Instant.now());
    }

    @Override
    public String toString() {
        return "ExceptionWrapper{" +
                "id='" + id + '\'' +
                (count <= 1 ? "" : " (count=" + count + ")" ) +
                ", firstOccurrence=" + firstOccurrence +
                ", lastOccurrence=" + lastOccurrence +
                ", metaData='" + metaData + '\'' +
                ", message='" + exceptionMessage + "' " +
                '}';
    }

    public String getStackTraceAsString(Throwable throwable) {
        return ExceptionUtils.getStackTrace(throwable);
    }
}
