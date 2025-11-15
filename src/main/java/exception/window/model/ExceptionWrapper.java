package exception.window.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionWrapper {

    private String id;

    private Date timeOfException;

    private Throwable throwable;



}
