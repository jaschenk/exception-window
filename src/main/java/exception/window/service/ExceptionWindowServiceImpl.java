package exception.window.service;

import exception.window.model.ExceptionWrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ExceptionWindowServiceImpl implements ExceptionWindowService {

    /**
     * In-Memory Exception History containing Name, Timestamp and Exception information. This will be a wrapped History,
     * oldest will fall off based upon maximum defined. Maximum Number of Saved Exception History, defines how many
     * exceptions we should keep in our history. A Scheduled Method will clean these up and will spill off as
     * applicable.
     */
    private final ConcurrentMap<String, ExceptionWrapper> EXCEPTION_HISTORY =
            new ConcurrentHashMap<>();

    @Value("${exception.window.history.max.size:128}")
    private Integer MAX_EXCEPTION_HISTORY_SIZE;

    @Override
    public void postException(ExceptionWrapper exceptionWrapper) {
        EXCEPTION_HISTORY.put(exceptionWrapper.getId(), exceptionWrapper);
    }

    @Override
    public Optional<List<ExceptionWrapper>> getExceptions() {
        return Optional.of(new ArrayList<>(EXCEPTION_HISTORY.values()));
    }

    @Override
    public Optional<List<ExceptionWrapper>> getExceptionsByTime(Date from, Date to) {
        if (from == null || to == null) {
            return Optional.empty();
        }

        List<ExceptionWrapper> filtered = EXCEPTION_HISTORY.values().stream()
                .filter(e -> e.getTimeOfException() != null)
                .filter(e ->
                        !e.getTimeOfException().before(from) &&
                                !e.getTimeOfException().after(to)
                )
                .collect(Collectors.toList());

        return filtered.isEmpty() ? Optional.empty() : Optional.of(filtered);
    }

    @Override
    public Optional<ExceptionWrapper> getExceptionById(String id) {
        return Optional.ofNullable(EXCEPTION_HISTORY.get(id));
    }

    @Override
    @Scheduled(fixedRateString = "${exception.window.history.ttl:60000}", initialDelayString = "${exception.window.history.ttl:60000}")
    public void cleanUpHistory() {

        cleanUpHistory(EXCEPTION_HISTORY, MAX_EXCEPTION_HISTORY_SIZE);

    }

    protected static void cleanUpHistory(ConcurrentMap<String, ?> lastHistory,
                                         Integer maximumHistorySize) {

        if (lastHistory.isEmpty()) {
            log.debug("The {} History is empty, nothing to clean.", "Exception");
            return;
        }

        log.debug("Cleaning Up {} History containing: '{}' Entries ...", "Exception", lastHistory.size());
        List<String> elementsToRemove = new ArrayList<>();
        List<String> historyKeys = new ArrayList<>(generateSortedKeySet(lastHistory));
        int keyPosition = -1;
        for (String historyKey : historyKeys) {
            keyPosition++;
            String[] historyKeyParts = historyKey.split(":");
            if (historyKeyParts.length != 2) {
                elementsToRemove.add(historyKey);
                continue;
            }
            Date historyDate = getHistoryDateFromString(historyKeyParts[0]);
            if (historyDate == null || isHistoryExpired(historyDate)) {
                elementsToRemove.add(historyKey);
                continue;
            }
            if (keyPosition >= maximumHistorySize || maximumHistorySize <= 0) {
                elementsToRemove.add(historyKey);
            }
        }
        // Empty Aged entries or over Maximum ...
        for (String key : elementsToRemove) {
            lastHistory.remove(key);
        }
        log.info("Removed '{}' {} entries from History, current size after Clean up: '{}'.", elementsToRemove.size(),
                "Exception", lastHistory.size());
    }

    protected static List<String> generateSortedKeySet(ConcurrentMap<String, ?> lastHistory) {
        List<String> sortedKeys = new ArrayList<>(lastHistory.keySet());
        // Now Sort keys descending ...
        sortedKeys.sort(Collections.reverseOrder());
        return sortedKeys;
    }

    protected static boolean isHistoryExpired(Date historyDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(historyDate);
        // Add seven Days to History Date ( DATE is a synonym for DAY_OF_MONTH) ...
        c.add(Calendar.DATE, 7);
        return c.getTime().compareTo(Date.from(Instant.now())) < 0;
    }

    protected static Date getHistoryDateFromString(String aLongDate) {
        Date historyDate;
        try {
            historyDate = new Date(Long.parseLong(aLongDate));
        } catch (Exception e) {
            historyDate = null;
            log.error("Unable to Parse String Long into a Date: {}", e.getMessage());
        }
        return historyDate;
    }


}
