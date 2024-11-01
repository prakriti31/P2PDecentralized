package com.example.p2pdecentralized.experiments.dht;

import com.example.p2pdecentralized.model.DHT;

import java.util.HashMap;
import java.util.Map;

public class DHTLoadTestingExperiment {

    private static final int[] TOPIC_SIZES = {1000, 10000, 100000};

    public static void main(String[] args) {
        DHT dht = new DHT();

        for (int size : TOPIC_SIZES) {
            Map<Integer, Integer> nodeDistribution = new HashMap<>();

            for (int i = 0; i < size; i++) {
                String topic = "Topic" + i;
                int node = dht.getNodeForTopic(topic);
                nodeDistribution.put(node, nodeDistribution.getOrDefault(node, 0) + 1);
            }

            System.out.println("Topic Distribution for " + size + " topics:");
            for (int node : nodeDistribution.keySet()) {
                System.out.printf("Node %d: %d topics%n", node, nodeDistribution.get(node));
            }

            System.out.println();
        }
    }
}
