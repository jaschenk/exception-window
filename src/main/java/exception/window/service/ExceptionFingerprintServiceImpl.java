package exception.window.service;

import org.springframework.stereotype.Service;
import java.security.MessageDigest;

@Service
public class ExceptionFingerprintServiceImpl implements ExceptionFingerprintService {

    /**
     * Creates a unique fingerprint for an exception based on:
     * - Exception type
     * - Exception message
     * - Top 3 stack trace elements (to capture the actual error location)
     * - Method name where it occurred
     */
    @Override
    public String generateFingerprint(
            Throwable throwable,
            String methodName
    ) {
        StringBuilder fingerprintBuilder = new StringBuilder();

        // Add exception type
        fingerprintBuilder.append(throwable.getClass().getName()).append("|");

        // Add exception message (normalized)
        String message = throwable.getMessage();
        if (message != null) {
            // Remove dynamic data like IDs, timestamps, etc.
            message = normalizeDynamicData(message);
            fingerprintBuilder.append(message).append("|");
        }

        // Add top 3 stack trace elements
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int elementsToInclude = Math.min(3, stackTrace.length);
        for (int i = 0; i < elementsToInclude; i++) {
            StackTraceElement element = stackTrace[i];
            fingerprintBuilder.append(element.getClassName())
                    .append(".")
                    .append(element.getMethodName())
                    .append(":")
                    .append(element.getLineNumber())
                    .append("|");
        }

        // Add method name
        fingerprintBuilder.append(methodName);

        // Generate hash for shorter key
        return generateMD5Hash(fingerprintBuilder.toString());
    }

    private String normalizeDynamicData(String message) {
        // Remove common dynamic patterns
        return message
                .replaceAll("\\d{4}-\\d{2}-\\d{2}", "DATE") // dates
                .replaceAll("\\d{2}:\\d{2}:\\d{2}", "TIME") // times
                .replaceAll("\\b\\d+\\b", "NUM") // numbers
                .replaceAll("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", "UUID"); // UUIDs
    }

    private String generateMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            return String.valueOf(input.hashCode());
        }
    }
}

