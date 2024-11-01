package com.example.p2pdecentralized.experiments.dht;

import com.example.p2pdecentralized.model.DHT;
import java.util.HashMap;
import java.util.Map;

public class DHTDistributionExperiment {

    private static final int NUMBER_OF_TOPICS = 10000;

    public static void main(String[] args) {
        DHT dht = new DHT();

        Map<Integer, Integer> nodeDistribution = new HashMap<>();

        for (int i = 0; i < NUMBER_OF_TOPICS; i++) {
            String topic = "Topic" + i;
            int node = dht.getNodeForTopic(topic);
            nodeDistribution.put(node, nodeDistribution.getOrDefault(node, 0) + 1);
        }

        System.out.println("Topic Distribution Across Nodes:");
        for (int node : nodeDistribution.keySet()) {
            System.out.printf("Node %d: %d topics%n", node, nodeDistribution.get(node));
        }
    }
}
