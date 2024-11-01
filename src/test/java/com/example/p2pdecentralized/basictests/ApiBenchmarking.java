package com.example.p2pdecentralized.basictests;

import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class ApiBenchmarking implements Runnable {
    private final int peerId;
    private final CountDownLatch startLatch;
    private final CountDownLatch doneLatch;
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/api";
    private final Random random = new Random();

    public ApiBenchmarking(int peerId, CountDownLatch startLatch, CountDownLatch doneLatch) {
        this.peerId = peerId;
        this.startLatch = startLatch;
        this.doneLatch = doneLatch;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void run() {
        // Update the file path to be relative to the project root
        String filePath = "benchmarkingapis/api_benchmark_peer_" + peerId + ".csv"; // Removed leading slash
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("API,Latency(ms),Timestamp");

            startLatch.await();  // Ensure all peers start together
            System.out.println("Peer " + peerId + " starting API benchmarking...");

            for (int i = 0; i < 30; i++) {
                benchmarkApi(writer);
            }

            System.out.println("Peer " + peerId + " completed all API benchmarking.");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            doneLatch.countDown();
        }
    }

    private void benchmarkApi(PrintWriter writer) {
        int apiChoice = random.nextInt(7);  // Choose from 7 possible APIs
        String topic = "topic_" + peerId + "_" + random.nextInt(10);
        String message = "Message from Peer " + peerId + " at " + System.currentTimeMillis();

        long startTime = System.nanoTime();
        String apiName = switch (apiChoice) {
            case 0 -> "createTopic";
            case 1 -> "subscribeToTopic";
            case 2 -> "publishMessage";
            case 3 -> "pullMessages";
            case 4 -> "queryTopic";
            case 5 -> "deleteTopic";
            case 6 -> "fetchLogs";
            default -> "unknown";
        };

        switch (apiChoice) {
            case 0 -> createTopic(topic);
            case 1 -> subscribeToTopic(topic);
            case 2 -> publishMessage(topic, message);
            case 3 -> pullMessages(topic);
            case 4 -> queryTopic(topic);
            case 5 -> deleteTopic(topic);
            case 6 -> fetchLogs();
        }

        long endTime = System.nanoTime();
        long latency = (endTime - startTime) / 1_000_000;  // Convert to milliseconds
        long timestamp = System.currentTimeMillis();

        writer.printf("%s,%d,%d%n", apiName, latency, timestamp);
    }

    private void createTopic(String topic) {
        restTemplate.postForObject(baseUrl + "/topic?topic=" + topic, null, String.class);
    }

    private void subscribeToTopic(String topic) {
        restTemplate.postForObject(baseUrl + "/subscribe?topic=" + topic, null, String.class);
    }

    private void publishMessage(String topic, String message) {
        restTemplate.postForObject(baseUrl + "/publish?topic=" + topic + "&message=" + message, null, String.class);
    }

    private String pullMessages(String topic) {
        return restTemplate.getForObject(baseUrl + "/pull?topic=" + topic, String.class);
    }

    private String queryTopic(String topic) {
        return restTemplate.getForObject(baseUrl + "/query?topic=" + topic, String.class);
    }

    private String deleteTopic(String topic) {
        return restTemplate.postForObject(baseUrl + "/delete?topic=" + topic, null, String.class);
    }

    private void fetchLogs() {
        restTemplate.getForObject(baseUrl + "/logs", Map.class);
    }

    public static void main(String[] args) throws InterruptedException {
        int peerCount = 8;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(peerCount);

        for (int i = 1; i <= peerCount; i++) {
            new Thread(new ApiBenchmarking(i, startLatch, doneLatch)).start();
        }

        System.out.println("Starting all peers...");
        startLatch.countDown();  // Signal all peers to start
        doneLatch.await();  // Wait for all peers to finish
        System.out.println("All API benchmarking tasks completed.");

        // After all peers are done, calculate and print throughput
        calculateAndPrintThroughput(peerCount);
    }

    private static void calculateAndPrintThroughput(int peerCount) {
        try {
            for (int i = 1; i <= peerCount; i++) {
                String latencyFilePath = "benchmarkingapis/api_benchmark_peer_" + i + ".csv";
                long totalLatency = 0;
                int totalRequests = 0;

                // Read latency from each peer's CSV file
                try (BufferedReader reader = new BufferedReader(new FileReader(latencyFilePath))) {
                    String line;
                    reader.readLine(); // Skip header line
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        totalLatency += Long.parseLong(parts[1]); // Add up latencies
                        totalRequests++; // Count total requests for this peer
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    continue; // Skip to the next peer if there's an error
                }

                // Calculate average latency and throughput for this peer
                long averageLatency = totalRequests > 0 ? totalLatency / totalRequests : 0;
                double throughput = totalRequests > 0 ? (double) totalRequests / (totalLatency / 1000.0) : 0; // requests per second

                // Save average latency to a CSV file
                String latencyOutputPath = "benchmarkingapis/latency_results_peer_" + i + ".csv";
                try (PrintWriter writer = new PrintWriter(new FileWriter(latencyOutputPath))) {
                    writer.println("Average Latency (ms)");
                    writer.printf("%d%n", averageLatency);
                    System.out.println("Latency results saved to " + latencyOutputPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Save throughput to a CSV file
                String throughputOutputPath = "benchmarkingapis/throughput_results_peer_" + i + ".csv";
                try (PrintWriter writer = new PrintWriter(new FileWriter(throughputOutputPath))) {
                    writer.println("Throughput (requests/second)");
                    writer.printf("%.2f%n", throughput);
                    System.out.println("Throughput results saved to " + throughputOutputPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
