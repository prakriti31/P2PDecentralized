package com.example.p2pdecentralized.bonusexperiments;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NodeFailureExperiment {

    private static final int TOTAL_NODES = 8;
    private static final List<Node> nodes = new ArrayList<>();

    public static void main(String[] args) {
        initializeNodes();
        simulateNodeFailures(3); // Simulate failure of 3 nodes
        recoverNodes();
    }

    private static void initializeNodes() {
        for (int i = 0; i < TOTAL_NODES; i++) {
            nodes.add(new Node("Node_" + (i + 1)));
        }
        System.out.println("Initialized nodes: " + nodes);
    }

    private static void simulateNodeFailures(int failures) {
        System.out.println("Simulating node failures...");
        Random random = new Random();
        Set<Node> failedNodes = new HashSet<>();

        for (int i = 0; i < failures; i++) {
            Node failedNode = nodes.get(random.nextInt(TOTAL_NODES));
            failedNodes.add(failedNode);
            failedNode.fail();
        }

        System.out.println("Failed nodes: " + failedNodes);
        performDHTOperations(); // Perform operations after failure
    }

    private static void performDHTOperations() {
        ExecutorService executor = Executors.newFixedThreadPool(TOTAL_NODES);
        CountDownLatch latch = new CountDownLatch(TOTAL_NODES);

        for (Node node : nodes) {
            executor.execute(() -> {
                node.performOperation();
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

    private static void recoverNodes() {
        System.out.println("Recovering nodes...");
        for (Node node : nodes) {
            node.recover();
        }
        System.out.println("All nodes recovered.");
    }

    static class Node {
        private final String name;
        private boolean isFailed;

        public Node(String name) {
            this.name = name;
            this.isFailed = false;
        }

        public void fail() {
            isFailed = true;
            System.out.println(name + " has failed.");
        }

        public void recover() {
            isFailed = false;
            System.out.println(name + " has recovered.");
        }

        public void performOperation() {
            if (isFailed) {
                System.out.println(name + " cannot perform operation due to failure.");
                return;
            }
            // Simulate DHT operation
            System.out.println(name + " is performing a DHT operation.");
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
