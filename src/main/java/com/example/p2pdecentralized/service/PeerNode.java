package com.example.p2pdecentralized.service;

import com.example.p2pdecentralized.model.HypercubeTopology;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PeerNode {
    private final HypercubeTopology topology = new HypercubeTopology();
    private int nodeId; // Changed to non-final to allow setting after instantiation

    // No-arg constructor
    public PeerNode() {}

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public List<Integer> getNeighbors() {
        return topology.getNeighbors(nodeId);
    }

    public int getNodeId() {
        return nodeId;
    }
}
