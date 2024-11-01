package com.example.p2pdecentralized.config;

import com.example.p2pdecentralized.service.PeerNode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public PeerNode peerNode1() {
        PeerNode peerNode = new PeerNode();
        peerNode.setNodeId(1);
        return peerNode;
    }

    @Bean
    public PeerNode peerNode2() {
        PeerNode peerNode = new PeerNode();
        peerNode.setNodeId(2);
        return peerNode;
    }

    @Bean
    public PeerNode peerNode3() {
        PeerNode peerNode = new PeerNode();
        peerNode.setNodeId(3);
        return peerNode;
    }

    @Bean
    public PeerNode peerNode4() {
        PeerNode peerNode = new PeerNode();
        peerNode.setNodeId(4);
        return peerNode;
    }

    @Bean
    public PeerNode peerNode5() {
        PeerNode peerNode = new PeerNode();
        peerNode.setNodeId(5);
        return peerNode;
    }

    @Bean
    public PeerNode peerNode6() {
        PeerNode peerNode = new PeerNode();
        peerNode.setNodeId(6);
        return peerNode;
    }

    @Bean
    public PeerNode peerNode7() {
        PeerNode peerNode = new PeerNode();
        peerNode.setNodeId(7);
        return peerNode;
    }

    @Bean
    public PeerNode peerNode8() {
        PeerNode peerNode = new PeerNode();
        peerNode.setNodeId(8);
        return peerNode;
    }
}
