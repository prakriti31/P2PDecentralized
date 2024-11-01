package com.example.p2pdecentralized.bonusexperiments;

import java.util.*;

public class DataDistributionExperiment {

    private static final int TOTAL_NODES = 8;
    private static final List<Node> nodes = new ArrayList<>();
    private static final Map<String, String> dataStore = new HashMap<>();

    public static void main(String[] args) {
        initializeNodes();
        performRandomDistribution();
        performRoundRobinDistribution();
        performConsistentHashing();
    }

    private static void initializeNodes() {
        for (int i = 0; i < TOTAL_NODES; i++) {
            nodes.add(new Node("Node_" + (i + 1)));
        }
        System.out.println("Initialized nodes: " + nodes);
    }

    private static void performRandomDistribution() {
        System.out.println("\nPerforming Random Data Distribution...");
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            String dataKey = "data_" + i;
            String nodeId = "Node_" + (random.nextInt(TOTAL_NODES) + 1);
            dataStore.put(dataKey, nodeId);
            System.out.println("Stored " + dataKey + " in " + nodeId);
        }
    }

    private static void performRoundRobinDistribution() {
        System.out.println("\nPerforming Round Robin Data Distribution...");
        int nodeIndex = 0;

        for (int i = 0; i < 100; i++) {
            String dataKey = "data_" + i;
            String nodeId = nodes.get(nodeIndex).getName();
            dataStore.put(dataKey, nodeId);
            System.out.println("Stored " + dataKey + " in " + nodeId);
            nodeIndex = (nodeIndex + 1) % TOTAL_NODES; // Move to the next node
        }
    }

    private static void performConsistentHashing() {
        System.out.println("\nPerforming Consistent Hashing Data Distribution...");
        for (int i = 0; i < 100; i++) {
            String dataKey = "data_" + i;
            String nodeId = consistentHash(dataKey);
            dataStore.put(dataKey, nodeId);
            System.out.println("Stored " + dataKey + " in " + nodeId);
        }
    }

    private static String consistentHash(String key) {
        int hash = Math.abs(key.hashCode()) % TOTAL_NODES;
        return "Node_" + (hash + 1);
    }

    static class Node {
        private final String name;

        public Node(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
