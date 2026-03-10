import java.util.*;

public class Graph {
    private final Map<String, List<Edge>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public void addCity(String city) {
        adjacencyList.putIfAbsent(city, new ArrayList<>());
    }

    public void addRoad(String source, String destination, int distance, int travelTime, TrafficLevel trafficLevel) {
        addCity(source);
        addCity(destination);

        adjacencyList.get(source).add(new Edge(destination, distance, travelTime, trafficLevel));
        adjacencyList.get(destination).add(new Edge(source, distance, travelTime, trafficLevel));
    }

    public List<Edge> getNeighbors(String city) {
        return adjacencyList.getOrDefault(city, Collections.emptyList());
    }

    public Set<String> getCities() {
        return adjacencyList.keySet();
    }

    public boolean containsCity(String city) {
        return adjacencyList.containsKey(city);
    }

    public int getDistanceBetween(String source, String destination) {
        for (Edge edge : getNeighbors(source)) {
            if (edge.destination.equals(destination)) {
                return edge.distance;
            }
        }
        return -1;
    }

    public void displayMap() {
        System.out.println("\nCity Map:");
        for (String city : adjacencyList.keySet()) {
            System.out.print(city + " -> ");
            for (Edge edge : adjacencyList.get(city)) {
                System.out.print(edge.destination
                        + "(distance=" + edge.distance + " km, time=" + edge.travelTime
                        + " min, traffic=" + edge.trafficLevel + ") ");
            }
            System.out.println();
        }
    }

    static class Edge {
        String destination;
        int distance;
        int travelTime;
        TrafficLevel trafficLevel;

        Edge(String destination, int distance, int travelTime, TrafficLevel trafficLevel) {
            this.destination = destination;
            this.distance = distance;
            this.travelTime = travelTime;
            this.trafficLevel = trafficLevel;
        }

        public double getEffectiveTime() {
            return travelTime * trafficLevel.getMultiplier();
        }
    }
}