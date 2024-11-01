package com.example.p2pdecentralized.bonusexperiments;

import java.util.Random;

public class NetworkLatencyExperiment {

    private static final int TOTAL_NODES = 8;

    public static void main(String[] args) {
        for (int i = 0; i < TOTAL_NODES; i++) {
            new Thread(new NodeTask("Node_" + (i + 1))).start();
        }
    }

    static class NodeTask implements Runnable {
        private final String nodeName;
        private final Random random;

        public NodeTask(String nodeName) {
            this.nodeName = nodeName;
            this.random = new Random();
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    int latency = simulateNetworkLatency();
                    Thread.sleep(latency); // Simulate network delay
                    performDHTOperation();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private int simulateNetworkLatency() {
            // Simulate latency between 100 and 1000 ms
            return random.nextInt(900) + 100;
        }

        private void performDHTOperation() {
            // Simulate a DHT operation
            System.out.println(nodeName + " performed a DHT operation after simulating latency.");
        }
    }
}
