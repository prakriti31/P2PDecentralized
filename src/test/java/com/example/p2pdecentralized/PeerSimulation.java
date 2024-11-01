package com.example.p2pdecentralized;

import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class PeerSimulation {

    public static void main(String[] args) {
        int numPeers = 8;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numPeers);

        for (int i = 0; i < numPeers; i++) {
            int peerId = i + 1;
            new Thread(new ApiBenchmarking(peerId, startLatch, doneLatch)).start();
        }

        System.out.println("Starting peer communication...");
        startLatch.countDown();  // Start all peers
        try {
            doneLatch.await();  // Wait for all peers to complete
            System.out.println("All peers have finished communication.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class PeerTask implements Runnable {
    private final int peerId;
    private final CountDownLatch startLatch;
    private final CountDownLatch doneLatch;
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/api";

    public PeerTask(int peerId, CountDownLatch startLatch, CountDownLatch doneLatch) {
        this.peerId = peerId;
        this.startLatch = startLatch;
        this.doneLatch = doneLatch;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void run() {
        try {
            startLatch.await();  // Wait until all peers are ready to start
            System.out.println("Peer " + peerId + " starting operations...");

            for (int i = 1; i <= 30; i++) {
                System.out.println("\nOperation " + i + " for Peer " + peerId + ":");

                // Perform a random operation from the list
                switch (i % 7) {
                    case 0 -> createTopic("topic_" + peerId + "_" + i);
                    case 1 -> subscribeToTopic("topic_" + (peerId % 8 + 1));
                    case 2 -> publishMessage("topic_" + peerId, "Message " + i + " from Peer " + peerId);
                    case 3 -> pullMessages("topic_" + (peerId % 8 + 1));
                    case 4 -> queryTopic("topic_" + peerId);
                    case 5 -> deleteTopic("topic_" + peerId + "_" + (i - 5));
                    case 6 -> fetchEventLogs();
                }
            }

            System.out.println("Peer " + peerId + " completed all operations.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            doneLatch.countDown();  // Mark this peer as done
        }
    }

    private void createTopic(String topicName) {
        String response = restTemplate.postForObject(baseUrl + "/topic?topic=" + topicName, null, String.class);
        System.out.println("Peer " + peerId + " created topic '" + topicName + "': " + response);
    }

    private void subscribeToTopic(String topicName) {
        String response = restTemplate.postForObject(baseUrl + "/subscribe?topic=" + topicName, null, String.class);
        System.out.println("Peer " + peerId + " subscribed to topic '" + topicName + "': " + response);
    }

    private void publishMessage(String topicName, String message) {
        String url = baseUrl + "/publish?topic=" + topicName + "&message=" + message;
        String response = restTemplate.postForObject(url, null, String.class);
        System.out.println("Peer " + peerId + " published message to topic '" + topicName + "': " + response);
    }

    private void pullMessages(String topicName) {
        String response = restTemplate.getForObject(baseUrl + "/pull?topic=" + topicName, String.class);
        System.out.println("Peer " + peerId + " pulled messages from topic '" + topicName + "': " + response);
    }

    private void queryTopic(String topicName) {
        String response = restTemplate.getForObject(baseUrl + "/query?topic=" + topicName, String.class);
        System.out.println("Peer " + peerId + " queried topic location for '" + topicName + "': " + response);
    }

    private void deleteTopic(String topicName) {
        String response = restTemplate.postForObject(baseUrl + "/delete?topic=" + topicName, null, String.class);
        System.out.println("Peer " + peerId + " deleted topic '" + topicName + "': " + response);
    }

    private void fetchEventLogs() {
        Map<String, String> logs = restTemplate.getForObject(baseUrl + "/logs", Map.class);
        System.out.println("Peer " + peerId + " fetched event logs: " + logs);
    }
}
