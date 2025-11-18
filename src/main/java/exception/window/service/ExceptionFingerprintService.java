package exception.window.service;

public interface ExceptionFingerprintService {

    String generateFingerprint(
            Throwable throwable,
            String methodName
    );

}
