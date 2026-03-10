import java.util.*;

public class DijkstraAlgorithm {

    public static class Result {
        private final List<String> path;
        private final double totalEffectiveTime;
        private final int totalDistance;
        private final int baseTravelTime;

        public Result(List<String> path, double totalEffectiveTime, int totalDistance, int baseTravelTime) {
            this.path = path;
            this.totalEffectiveTime = totalEffectiveTime;
            this.totalDistance = totalDistance;
            this.baseTravelTime = baseTravelTime;
        }

        public List<String> getPath() {
            return path;
        }

        public double getTotalEffectiveTime() {
            return totalEffectiveTime;
        }

        public int getTotalDistance() {
            return totalDistance;
        }

        public int getBaseTravelTime() {
            return baseTravelTime;
        }
    }

    public Result findFastestPath(Graph graph, String source, String destination) {
        Map<String, Double> times = new HashMap<>();
        Map<String, Integer> distances = new HashMap<>();
        Map<String, Integer> baseTimes = new HashMap<>();
        Map<String, String> previous = new HashMap<>();

        PriorityQueue<CityCost> pq = new PriorityQueue<>(Comparator.comparingDouble(c -> c.cost));

        for (String city : graph.getCities()) {
            times.put(city, Double.MAX_VALUE);
            distances.put(city, Integer.MAX_VALUE);
            baseTimes.put(city, Integer.MAX_VALUE);
        }

        times.put(source, 0.0);
        distances.put(source, 0);
        baseTimes.put(source, 0);

        pq.offer(new CityCost(source, 0.0));

        while (!pq.isEmpty()) {
            CityCost current = pq.poll();

            if (current.cost > times.get(current.city)) {
                continue;
            }

            for (Graph.Edge edge : graph.getNeighbors(current.city)) {
                double newTime = times.get(current.city) + edge.getEffectiveTime();
                int newDistance = distances.get(current.city) + edge.distance;
                int newBaseTime = baseTimes.get(current.city) + edge.travelTime;

                if (newTime < times.get(edge.destination)) {
                    times.put(edge.destination, newTime);
                    distances.put(edge.destination, newDistance);
                    baseTimes.put(edge.destination, newBaseTime);
                    previous.put(edge.destination, current.city);
                    pq.offer(new CityCost(edge.destination, newTime));
                }
            }
        }

        if (!times.containsKey(destination) || times.get(destination) == Double.MAX_VALUE) {
            return new Result(new ArrayList<>(), -1, -1, -1);
        }

        List<String> path = new ArrayList<>();
        String current = destination;

        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }

        Collections.reverse(path);

        return new Result(path, times.get(destination), distances.get(destination), baseTimes.get(destination));
    }

    static class CityCost {
        String city;
        double cost;

        CityCost(String city, double cost) {
            this.city = city;
            this.cost = cost;
        }
    }
}