package com.moveinsync.metro.graph;

import java.util.*;

public class DijkstraUtil {

    public static List<Long> findShortestPath(
            Map<Long, List<Edge>> graph,
            Long source,
            Long destination) {

        PriorityQueue<Node> pq =
                new PriorityQueue<>(Comparator.comparingInt(Node::getCost));

        Map<Long, Integer> distance = new HashMap<>();
        Map<Long, Long> previous = new HashMap<>();
        Set<Long> visited = new HashSet<>();

        pq.add(new Node(source, 0));
        distance.put(source, 0);

        while (!pq.isEmpty()) {

            Node current = pq.poll();
            Long currentStop = current.getStopId();

            if (visited.contains(currentStop))
                continue;

            visited.add(currentStop);

            if (currentStop.equals(destination))
                break;

            for (Edge edge : graph.getOrDefault(currentStop, new ArrayList<>())) {

                Long neighbor = edge.getTo();
                int newCost = distance.get(currentStop) + edge.getWeight();

                if (newCost < distance.getOrDefault(neighbor, Integer.MAX_VALUE)) {

                    distance.put(neighbor, newCost);
                    previous.put(neighbor, currentStop);
                    pq.add(new Node(neighbor, newCost));
                }
            }
        }

        // No path case
        if (!distance.containsKey(destination)) {
            return Collections.emptyList();
        }

        // Build path
        List<Long> path = new ArrayList<>();
        Long step = destination;

        while (step != null) {
            path.add(step);
            step = previous.get(step);
        }

        Collections.reverse(path);

        return path;
    }
}