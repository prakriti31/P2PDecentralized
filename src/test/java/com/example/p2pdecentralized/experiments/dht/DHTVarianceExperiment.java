package com.example.p2pdecentralized.experiments.dht;

import com.example.p2pdecentralized.model.DHT;

import java.util.HashMap;
import java.util.Map;

public class DHTVarianceExperiment {

    private static final int NUMBER_OF_TOPICS = 10000;

    public static void main(String[] args) {
        DHT dht = new DHT();
        Map<Integer, Integer> nodeDistribution = new HashMap<>();

        for (int i = 0; i < NUMBER_OF_TOPICS; i++) {
            String topic = "Topic" + i;
            int node = dht.getNodeForTopic(topic);
            nodeDistribution.put(node, nodeDistribution.getOrDefault(node, 0) + 1);
        }

        double mean = (double) NUMBER_OF_TOPICS / 8;
        double variance = 0;
        for (int count : nodeDistribution.values()) {
            variance += Math.pow(count - mean, 2);
        }
        variance /= 8;

        System.out.printf("Mean: %.2f topics per node%n", mean);
        System.out.printf("Variance: %.2f%n", variance);
    }
}
