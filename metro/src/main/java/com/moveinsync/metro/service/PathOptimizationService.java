package com.moveinsync.metro.service;

import com.moveinsync.metro.graph.Edge;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PathOptimizationService {

    public List<Long> findShortestPath(
            Map<Long, List<Edge>> graph,
            Long source,
            Long destination) {

        PriorityQueue<Node> pq =
                new PriorityQueue<>(Comparator
                        .comparingInt((Node n) -> n.cost)
                        .thenComparingLong(n -> n.id));

        Map<Long, Integer> dist = new HashMap<>();
        Map<Long, Long> prev = new HashMap<>();

        if (source.equals(destination)) {
            return List.of(source);
        }

        pq.add(new Node(source, 0));
        dist.put(source, 0);

        while (!pq.isEmpty()) {

            Node current = pq.poll();

            if (current.id.equals(destination))
                break;

            for (Edge edge : graph.getOrDefault(current.id, new ArrayList<>())) {

                int newCost = current.cost + edge.getWeight();

                if (newCost < dist.getOrDefault(edge.getTo(), Integer.MAX_VALUE)) {

                    dist.put(edge.getTo(), newCost);
                    prev.put(edge.getTo(), current.id);
                    pq.add(new Node(edge.getTo(), newCost));
                }
            }
        }

        if (!dist.containsKey(destination)) {
            return Collections.emptyList();
        }

        List<Long> path = new ArrayList<>();
        Long step = destination;

        while (step != null) {
            path.add(step);
            step = prev.get(step);
        }

        Collections.reverse(path);
        return path;
    }

    private static class Node {
        Long id;
        int cost;

        Node(Long id, int cost) {
            this.id = id;
            this.cost = cost;
        }
    }
}
