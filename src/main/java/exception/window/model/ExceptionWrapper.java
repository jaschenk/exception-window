package exception.window.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionWrapper {

    private String id;

    private Date timeOfException;

    private Throwable throwable;

    private String metaData;


    public ExceptionWrapper(Throwable t) {
        this.id = UUID.randomUUID().toString();
        this.timeOfException = Date.from(Instant.now());
        this.throwable = t;
    }

    public ExceptionWrapper(String id, Date date, Throwable t) {
        this.id = id;
        this.timeOfException = date;
        this.throwable = t;
    }

}
