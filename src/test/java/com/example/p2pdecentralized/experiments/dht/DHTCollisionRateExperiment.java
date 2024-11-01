package com.example.p2pdecentralized.experiments.dht;

import com.example.p2pdecentralized.model.DHT;

import java.util.HashMap;
import java.util.Map;

public class DHTCollisionRateExperiment {

    private static final int NUMBER_OF_TOPICS = 10000;

    public static void main(String[] args) {
        DHT dht = new DHT();
        Map<Integer, Integer> nodeDistribution = new HashMap<>();

        for (int i = 0; i < NUMBER_OF_TOPICS; i++) {
            String topic = "Topic" + i;
            int node = dht.getNodeForTopic(topic);
            nodeDistribution.put(node, nodeDistribution.getOrDefault(node, 0) + 1);
        }

        int maxTopics = nodeDistribution.values().stream().max(Integer::compareTo).orElse(0);
        int minTopics = nodeDistribution.values().stream().min(Integer::compareTo).orElse(0);

        System.out.printf("Max topics on a single node: %d%n", maxTopics);
        System.out.printf("Min topics on a single node: %d%n", minTopics);
    }
}
