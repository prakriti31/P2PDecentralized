package com.example.p2pdecentralized.model;

import java.util.HashMap;
import java.util.Map;

public class EventLogger {
    private final Map<String, String> eventLogs = new HashMap<>();

    public void logEvent(String event) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        eventLogs.put(timestamp, event);
    }

    public Map<String, String> getEventLogs() {
        return eventLogs;
    }
}
