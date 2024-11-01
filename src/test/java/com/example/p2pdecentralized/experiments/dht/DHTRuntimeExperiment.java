package com.example.p2pdecentralized.experiments.dht;

import com.example.p2pdecentralized.model.DHT;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DHTRuntimeExperiment {

    private static final int NUMBER_OF_TRIALS = 1000;
    private static final int[] STRING_LENGTHS = {10, 50, 100, 500, 1000}; // Different topic lengths to test

    public static void main(String[] args) {
        DHT dht = new DHT();
        Random random = new Random();

        List<Integer> stringLengths = new ArrayList<>();
        List<Double> averageDurations = new ArrayList<>();

        for (int length : STRING_LENGTHS) {
            long totalDuration = 0;

            for (int i = 0; i < NUMBER_OF_TRIALS; i++) {
                String topic = generateRandomString(length, random);

                // Measure time taken for getNodeForTopic
                long startTime = System.nanoTime();
                dht.getNodeForTopic(topic);
                long endTime = System.nanoTime();

                totalDuration += (endTime - startTime);
            }

            double averageDuration = (double) totalDuration / NUMBER_OF_TRIALS;
            System.out.printf("Average time for string length %d: %.2f ns%n", length, averageDuration);

            // Store the data for plotting
            stringLengths.add(length);
            averageDurations.add(averageDuration);
        }

        // Plot and save the graph
        createAndSaveChart(stringLengths, averageDurations);
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

    // Method to create and save the chart
    private static void createAndSaveChart(List<Integer> xData, List<Double> yData) {
        XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Average Cost at Runtime by String Length")
                .xAxisTitle("String Length")
                .yAxisTitle("Average Time (ns)")
                .build();

        // Add data to chart
        chart.addSeries("Runtime vs String Length", xData, yData);

        // Save the chart as an image
        try {
            File outputDir = new File("experiments");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            BitmapEncoder.saveBitmap(chart, "experiments/DHT_Runtime_Analysis", BitmapFormat.PNG);
            System.out.println("Chart saved as experiments/DHT_Runtime_Analysis.png");
        } catch (IOException e) {
            System.err.println("Error saving the chart: " + e.getMessage());
        }
    }
}
