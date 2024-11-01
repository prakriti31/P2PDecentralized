package com.example.p2pdecentralized.model;

import java.util.HashMap;
import java.util.Map;

public class DHT {
    private final Map<String, Integer> topicNodeMap = new HashMap<>();

    public int getNodeForTopic(String topic) {
        int hash = Math.abs(topic.hashCode()) % 8; // Hash function
        topicNodeMap.putIfAbsent(topic, hash);
        return topicNodeMap.get(topic);
    }
}
