package com.example.p2pdecentralized.model;

import java.util.ArrayList;
import java.util.List;

public class HypercubeTopology {
    public List<Integer> getNeighbors(int nodeId) {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int neighbor = nodeId ^ (1 << i);
            if (neighbor < 8) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }
}
