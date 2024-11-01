package com.example.p2pdecentralized.experiments.dht;

import com.example.p2pdecentralized.model.DHT;
import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DHTSpaceComplexityExperiment {

    private static final int NUMBER_OF_TRIALS = 1000;
    private static final int[] STRING_LENGTHS = {10, 50, 100, 500, 1000, 5000, 10000}; // Different string lengths to test

    public static void main(String[] args) {
        DHT dht = new DHT();
        Random random = new Random();

        // Lists to store data for plotting
        List<Integer> lengths = new ArrayList<>();
        List<Double> timeData = new ArrayList<>();
        List<Long> memoryData = new ArrayList<>();

        Runtime runtime = Runtime.getRuntime();

        for (int length : STRING_LENGTHS) {
            long totalDuration = 0;

            // Measure time complexity
            for (int i = 0; i < NUMBER_OF_TRIALS; i++) {
                String topic = generateRandomString(length, random);

                long startTime = System.nanoTime();
                dht.getNodeForTopic(topic);
                long endTime = System.nanoTime();

                totalDuration += (endTime - startTime);
            }

            double averageDuration = (double) totalDuration / NUMBER_OF_TRIALS;
            timeData.add(averageDuration); // Add time data for the current length
            lengths.add(length);

            // Measure space complexity
            runtime.gc(); // Run garbage collector to get a cleaner memory reading
            long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

            // Insert topics to observe memory growth
            for (int i = 0; i < NUMBER_OF_TRIALS; i++) {
                String topic = generateRandomString(length, random);
                dht.getNodeForTopic(topic);
            }

            runtime.gc();
            long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
            long memoryUsed = memoryAfter - memoryBefore;
            memoryData.add(memoryUsed); // Add memory data for the current length

            System.out.printf("Length: %d | Average Time: %.2f ns | Memory Used: %d bytes%n", length, averageDuration, memoryUsed);
        }

        // Plot the results using XChart
        plotTimeComplexity(lengths, timeData);
        plotSpaceComplexity(lengths, memoryData);
    }

    // Helper method to generate a random string of a given length
    private static String generateRandomString(int length, Random random) {
        StringBuilder builder = new StringBuilder(length);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < length; i++) {
            builder.append(characters.charAt(random.nextInt(characters.length())));
        }

        return builder.toString();
    }

    // Method to plot time complexity
    private static void plotTimeComplexity(List<Integer> lengths, List<Double> timeData) {
        XYChart chart = new XYChartBuilder().width(800).height(600).title("DHT Time Complexity").xAxisTitle("String Length").yAxisTitle("Time (ns)").build();
        chart.addSeries("Average Time", lengths, timeData).setMarker(SeriesMarkers.CIRCLE);
        try {
            BitmapEncoder.saveBitmap(chart, "experiments/DHTTimeComplexity", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to plot space complexity
    private static void plotSpaceComplexity(List<Integer> lengths, List<Long> memoryData) {
        XYChart chart = new XYChartBuilder().width(800).height(600).title("DHT Space Complexity").xAxisTitle("String Length").yAxisTitle("Memory (bytes)").build();
        chart.addSeries("Memory Used", lengths, memoryData).setMarker(SeriesMarkers.CIRCLE);
        try {
            BitmapEncoder.saveBitmap(chart, "experiments/DHTSpaceComplexity", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
