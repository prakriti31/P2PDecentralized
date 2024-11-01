package com.example.p2pdecentralized.experiments.requestforwarding;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class RequestForwarding {

    public static void main(String[] args) {
        int numPeers = 8;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numPeers);

        long totalResponseTime = 0; // Total response time across all peers
        int totalOperations = 0;     // Total operations performed across all peers

        for (int i = 0; i < numPeers; i++) {
            int peerId = i + 1;
            // Create a PeerTask and pass shared metrics storage
            PeerTask task = new PeerTask(peerId, startLatch, doneLatch);
            new Thread(task).start();

            // Accumulate total operations
            totalOperations += task.getTotalOperations();
        }

        System.out.println("Starting peer communication...");
        startLatch.countDown();  // Start all peers
        try {
            doneLatch.await();  // Wait for all peers to complete

            // Calculate total response time from all PeerTasks
            for (int i = 0; i < numPeers; i++) {
                totalResponseTime += PeerTask.getTotalResponseTime(i + 1); // Access each PeerTask's total response time
            }

            // Calculate and print metrics
            double averageResponseTime = (double) totalResponseTime / totalOperations;
            double throughput = (totalOperations * 1000.0) / totalResponseTime; // operations per second

            System.out.printf("Average Response Time: %.2f ms%n", averageResponseTime);
            System.out.printf("Maximum Throughput: %.2f ops/sec%n", throughput);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class PeerTask implements Runnable {
    private static long[] totalResponseTimes; // Static array to store total response times for each peer
    private static int[] totalOperations;      // Static array to store total operations for each peer

    private final int peerId;
    private final CountDownLatch startLatch;
    private final CountDownLatch doneLatch;
    private final RestTemplate restTemplate;
    private final String baseUrl;

    static {
        totalResponseTimes = new long[8]; // Assuming 8 peers, adjust accordingly
        totalOperations = new int[8];      // Assuming 8 peers, adjust accordingly
    }

    public PeerTask(int peerId, CountDownLatch startLatch, CountDownLatch doneLatch) {
        this.peerId = peerId;
        this.startLatch = startLatch;
        this.doneLatch = doneLatch;
        this.restTemplate = new RestTemplate();
        this.baseUrl = "http://localhost:" + (8080 + (peerId - 1)); // Base URL for each peer
    }

    public int getTotalOperations() {
        return 30; // Each peer performs 30 operations
    }

    public static long getTotalResponseTime(int peerId) {
        return totalResponseTimes[peerId - 1]; // Fetch total response time for a specific peer
    }

    @Override
    public void run() {
        try {
            startLatch.await();  // Wait until all peers are ready to start
            System.out.println("Peer " + peerId + " starting operations...");

            for (int i = 1; i <= 30; i++) {
                System.out.println("\nOperation " + i + " for Peer " + peerId + ":");

                long startTime = System.currentTimeMillis(); // Start timing
                switch (i % 7) {
                    case 0 -> createTopic("topic_" + peerId + "_" + i);
                    case 1 -> subscribeToTopic("topic_" + (peerId % 8 + 1)); // Each peer subscribes to topics of others
                    case 2 -> publishMessage("topic_" + peerId, "Message " + i + " from Peer " + peerId);
                    case 3 -> pullMessages("topic_" + (peerId % 8 + 1)); // Pull messages from another topic
                    case 4 -> queryTopic("topic_" + peerId);
                    case 5 -> deleteTopic("topic_" + peerId + "_" + (i - 5));
                    case 6 -> fetchEventLogs();
                }
                long endTime = System.currentTimeMillis(); // End timing
                long responseTime = endTime - startTime; // Calculate response time
                totalResponseTimes[peerId - 1] += responseTime; // Update total response time for this peer
            }

            totalOperations[peerId - 1] = getTotalOperations(); // Update total operations for this peer
            System.out.println("Peer " + peerId + " completed all operations.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            doneLatch.countDown();  // Mark this peer as done
        }
    }

    private void createTopic(String topicName) {
        executeRequest(() -> restTemplate.postForObject(baseUrl + "/api/topic?topic=" + topicName, null, String.class),
                "Peer " + peerId + " created topic '" + topicName + "'");
    }

    private void subscribeToTopic(String topicName) {
        executeRequest(() -> restTemplate.postForObject(baseUrl + "/api/subscribe?topic=" + topicName, null, String.class),
                "Peer " + peerId + " subscribed to topic '" + topicName + "'");
    }

    private void publishMessage(String topicName, String message) {
        String url = baseUrl + "/api/publish?topic=" + topicName + "&message=" + message;
        executeRequest(() -> restTemplate.postForObject(url, null, String.class),
                "Peer " + peerId + " published message to topic '" + topicName + "'");
    }

    private void pullMessages(String topicName) {
        executeRequest(() -> restTemplate.getForObject(baseUrl + "/api/pull?topic=" + topicName, String.class),
                "Peer " + peerId + " pulled messages from topic '" + topicName + "'");
    }

    private void queryTopic(String topicName) {
        executeRequest(() -> restTemplate.getForObject(baseUrl + "/api/query?topic=" + topicName, String.class),
                "Peer " + peerId + " queried topic location for '" + topicName + "'");
    }

    private void deleteTopic(String topicName) {
        executeRequest(() -> restTemplate.postForObject(baseUrl + "/api/delete?topic=" + topicName, null, String.class),
                "Peer " + peerId + " deleted topic '" + topicName + "'");
    }

    private void fetchEventLogs() {
        executeRequest(() -> restTemplate.getForObject(baseUrl + "/api/logs", Map.class),
                "Peer " + peerId + " fetched event logs");
    }

    private void executeRequest(Runnable request, String successMessage) {
        try {
            Object response = request;
            System.out.println(successMessage + ": " + response);
        } catch (HttpStatusCodeException e) {
            HttpStatus status = e.getStatusCode();
            System.err.println("Error: " + status + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
