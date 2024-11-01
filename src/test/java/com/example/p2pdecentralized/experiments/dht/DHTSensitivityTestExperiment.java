package com.example.p2pdecentralized.experiments.dht;

import com.example.p2pdecentralized.model.DHT;

public class DHTSensitivityTestExperiment {

    public static void main(String[] args) {
        DHT dht = new DHT();
        String[] topics = {"Topic1", "Topic2", "TopicA", "TopicB", "Top1", "Top2"};

        System.out.println("Node assignment for small variations:");
        for (String topic : topics) {
            int node = dht.getNodeForTopic(topic);
            System.out.printf("Topic '%s' assigned to Node %d%n", topic, node);
        }
    }
}
