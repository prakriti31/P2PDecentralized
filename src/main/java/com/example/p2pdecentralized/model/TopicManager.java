package com.example.p2pdecentralized.model;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class TopicManager {
    private final DHT dht = new DHT();
    private final EventLogger logger = new EventLogger();
    private final Map<String, String> topics = new HashMap<>();
    private final Map<String, String> messages = new HashMap<>();

    public String createTopic(String topic) {
        int nodeId = dht.getNodeForTopic(topic);
        topics.put(topic, "Node " + nodeId);
        logger.logEvent("Created topic: " + topic + " at Node " + nodeId);
        return "Topic created at Node " + nodeId;
    }

    public String subscribeToTopic(String topic) {
        logger.logEvent("Subscribed to topic: " + topic);
        return "Subscribed to " + topic;
    }

    public String publishMessage(String topic, String message) {
        messages.put(topic, message);
        logger.logEvent("Published message to topic: " + topic);
        return "Message published to " + topic;
    }

    public String pullMessages(String topic) {
        String message = messages.remove(topic);
        logger.logEvent("Pulled messages from topic: " + topic);
        return message != null ? "Message: " + message : "No messages found";
    }

    public String deleteTopic(String topic) {
        topics.remove(topic);
        messages.remove(topic);
        logger.logEvent("Deleted topic: " + topic);
        return "Topic deleted";
    }

    public String queryTopicLocation(String topic) {
        int nodeId = dht.getNodeForTopic(topic);
        return "Topic " + topic + " is hosted on Node " + nodeId;
    }

    public Map<String, String> fetchEventLogs() {
        return logger.getEventLogs();
    }
}
