package com.example.p2pdecentralized.controller;


import com.example.p2pdecentralized.model.TopicManager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class PeerController {
    private final TopicManager topicManager;

    public PeerController(TopicManager topicManager) {
        this.topicManager = topicManager;
    }

    @PostMapping("/topic")
    public String createTopic(@RequestParam String topic) {
        return topicManager.createTopic(topic);
    }

    @PostMapping("/subscribe")
    public String subscribeToTopic(@RequestParam String topic) {
        return topicManager.subscribeToTopic(topic);
    }

    @PostMapping("/publish")
    public String publishMessage(@RequestParam String topic, @RequestParam String message) {
        return topicManager.publishMessage(topic, message);
    }

    @GetMapping("/pull")
    public String pullMessages(@RequestParam String topic) {
        return topicManager.pullMessages(topic);
    }

    @PostMapping("/delete")
    public String deleteTopic(@RequestParam String topic) {
        return topicManager.deleteTopic(topic);
    }

    @GetMapping("/query")
    public String queryTopic(@RequestParam String topic) {
        return topicManager.queryTopicLocation(topic);
    }

    @GetMapping("/logs")
    public Map<String, String> fetchLogs() {
        return topicManager.fetchEventLogs();
    }
}
