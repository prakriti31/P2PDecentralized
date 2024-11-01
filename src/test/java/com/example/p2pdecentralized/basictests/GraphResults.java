package com.example.p2pdecentralized.basictests;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphResults {

    private static final int PEER_COUNT = 8;
    private static final String BASE_PATH = "benchmarkingapis/";

    public static void main(String[] args) {
        plotOverallLatencyGraph();
        plotOverallThroughputGraph();
        plotApiLatencyAndThroughputGraphs();
    }

    private static void plotOverallLatencyGraph() {
        XYChart chart = new XYChartBuilder().width(800).height(600)
                .title("Average Latency per Peer")
                .xAxisTitle("Peer ID")
                .yAxisTitle("Average Latency (ms)")
                .build();

        List<Integer> peerIds = new ArrayList<>();
        List<Double> latencies = new ArrayList<>();

        for (int i = 1; i <= PEER_COUNT; i++) {
            String filePath = BASE_PATH + "latency_results_peer_" + i + ".csv";
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                reader.readLine(); // Skip header
                String line = reader.readLine();
                if (line != null) {
                    peerIds.add(i);
                    latencies.add(Double.parseDouble(line));
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + filePath);
                e.printStackTrace();
            }
        }

        chart.addSeries("Average Latency", peerIds, latencies);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);

        try {
            BitmapEncoder.saveBitmap(chart, BASE_PATH + "overall_average_latency_graph.png", BitmapFormat.PNG);
            System.out.println("Overall Average Latency graph saved to " + BASE_PATH + "overall_average_latency_graph.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void plotOverallThroughputGraph() {
        XYChart chart = new XYChartBuilder().width(800).height(600)
                .title("Overall Throughput per Peer")
                .xAxisTitle("Peer ID")
                .yAxisTitle("Throughput (requests/second)")
                .build();

        List<Integer> peerIds = new ArrayList<>();
        List<Double> throughputs = new ArrayList<>();

        for (int i = 1; i <= PEER_COUNT; i++) {
            String filePath = BASE_PATH + "throughput_results_peer_" + i + ".csv";
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                reader.readLine(); // Skip header
                String line = reader.readLine();
                if (line != null) {
                    peerIds.add(i);
                    throughputs.add(Double.parseDouble(line));
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + filePath);
                e.printStackTrace();
            }
        }

        chart.addSeries("Overall Throughput", peerIds, throughputs);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);

        try {
            BitmapEncoder.saveBitmap(chart, BASE_PATH + "overall_throughput_graph.png", BitmapFormat.PNG);
            System.out.println("Overall Throughput graph saved to " + BASE_PATH + "overall_throughput_graph.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void plotApiLatencyAndThroughputGraphs() {
        Map<String, List<Double>> apiLatencies = new HashMap<>();
        Map<String, List<Double>> apiThroughputs = new HashMap<>();

        for (int i = 1; i <= PEER_COUNT; i++) {
            String filePath = BASE_PATH + "api_benchmark_peer_" + i + ".csv";
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                reader.readLine(); // Skip header
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String apiName = parts[0];
                    double latency = Double.parseDouble(parts[1]);
                    double throughput = 1000.0 / latency; // Convert latency to throughput (requests per second)
                    apiLatencies.computeIfAbsent(apiName, k -> new ArrayList<>()).add(latency);
                    apiThroughputs.computeIfAbsent(apiName, k -> new ArrayList<>()).add(throughput);
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + filePath);
                e.printStackTrace();
            }
        }

        for (String apiName : apiLatencies.keySet()) {
            plotApiGraph(apiName, apiLatencies.get(apiName), "Latency (ms)", "latency");
            plotApiGraph(apiName, apiThroughputs.get(apiName), "Throughput (requests/second)", "throughput");
        }
    }

    private static void plotApiGraph(String apiName, List<Double> data, String yAxisTitle, String metricName) {
        XYChart chart = new XYChartBuilder().width(800).height(600)
                .title(apiName + " " + yAxisTitle)
                .xAxisTitle("Request Number")
                .yAxisTitle(yAxisTitle)
                .build();

        List<Integer> requestNumbers = new ArrayList<>();
        for (int i = 1; i <= data.size(); i++) {
            requestNumbers.add(i);
        }

        chart.addSeries(apiName, requestNumbers, data);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);

        try {
            BitmapEncoder.saveBitmap(chart, BASE_PATH + apiName + "_" + metricName + "_graph.png", BitmapFormat.PNG);
            System.out.println(apiName + " " + metricName + " graph saved to " + BASE_PATH + apiName + "_" + metricName + "_graph.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}