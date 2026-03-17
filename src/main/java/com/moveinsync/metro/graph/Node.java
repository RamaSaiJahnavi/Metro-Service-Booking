package com.moveinsync.metro.graph;

public class Node {

    private Long stopId;
    private int cost;

    public Node(Long stopId, int cost) {
        this.stopId = stopId;
        this.cost = cost;
    }

    public Long getStopId() {
        return stopId;
    }

    public int getCost() {
        return cost;
    }
}