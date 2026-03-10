import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class RoutePlanner {
    private final Graph graph;
    private final DijkstraAlgorithm dijkstra;
    private final Scanner scanner;

    public RoutePlanner() {
        graph = new Graph();
        dijkstra = new DijkstraAlgorithm();
        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Smart Route Planner with Traffic Optimization ===");
        System.out.println("1. Load sample map");
        System.out.println("2. Enter custom city map manually");
        System.out.println("3. Load city map from file");
        System.out.print("Choose an option: ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
            return;
        }

        switch (choice) {
            case 1:
                loadSampleMap();
                break;
            case 2:
                loadCustomMapFromUser();
                break;
            case 3:
                loadMapFromFile("map.txt");
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (graph.getCities().isEmpty()) {
            System.out.println("No cities loaded.");
            return;
        }

        graph.displayMap();
        findRoute();
    }

    private void loadSampleMap() {
        graph.addRoad("A", "B", 5, 10, TrafficLevel.LOW);
        graph.addRoad("A", "C", 4, 8, TrafficLevel.HIGH);
        graph.addRoad("B", "D", 7, 12, TrafficLevel.MEDIUM);
        graph.addRoad("C", "D", 3, 6, TrafficLevel.LOW);
        graph.addRoad("C", "E", 6, 10, TrafficLevel.HIGH);
        graph.addRoad("D", "E", 2, 4, TrafficLevel.MEDIUM);
        graph.addRoad("D", "F", 8, 14, TrafficLevel.LOW);
        graph.addRoad("E", "F", 3, 5, TrafficLevel.HIGH);
    }

    private void loadCustomMapFromUser() {
        System.out.print("Enter number of roads: ");
        int roads;

        try {
            roads = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return;
        }

        System.out.println("Enter each road in format:");
        System.out.println("source destination distance travelTime trafficLevel");
        System.out.println("Example: Kolkata Delhi 1500 25 MEDIUM");

        for (int i = 1; i <= roads; i++) {
            System.out.print("Road " + i + ": ");
            String line = scanner.nextLine().trim();
            String[] parts = line.split("\\s+");

            if (parts.length != 5) {
                System.out.println("Invalid input. Please enter exactly 5 values.");
                i--;
                continue;
            }

            String source = parts[0];
            String destination = parts[1];

            int distance;
            int travelTime;
            TrafficLevel trafficLevel;

            try {
                distance = Integer.parseInt(parts[2]);
                travelTime = Integer.parseInt(parts[3]);
                trafficLevel = TrafficLevel.valueOf(parts[4].toUpperCase());
            } catch (NumberFormatException e) {
                System.out.println("Distance and travel time must be numbers.");
                i--;
                continue;
            } catch (IllegalArgumentException e) {
                System.out.println("Traffic level must be LOW, MEDIUM, or HIGH.");
                i--;
                continue;
            }

            graph.addRoad(source, destination, distance, travelTime, trafficLevel);
        }
    }

    private void loadMapFromFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("\\s+");
                if (parts.length != 5) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                String source = parts[0];
                String destination = parts[1];
                int distance = Integer.parseInt(parts[2]);
                int travelTime = Integer.parseInt(parts[3]);
                TrafficLevel trafficLevel = TrafficLevel.valueOf(parts[4].toUpperCase());

                graph.addRoad(source, destination, distance, travelTime, trafficLevel);
            }

            fileScanner.close();
            System.out.println("Map loaded successfully from " + fileName);

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in file.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid traffic level in file. Use LOW, MEDIUM, or HIGH.");
        }
    }

    private void findRoute() {
        System.out.print("\nEnter source city: ");
        String source = scanner.nextLine().trim();

        System.out.print("Enter destination city: ");
        String destination = scanner.nextLine().trim();

        if (!graph.containsCity(source) || !graph.containsCity(destination)) {
            System.out.println("Invalid city name entered.");
            return;
        }

        DijkstraAlgorithm.Result result = dijkstra.findFastestPath(graph, source, destination);

        if (result.getTotalEffectiveTime() == -1) {
            System.out.println("No route found between " + source + " and " + destination);
            return;
        }

        System.out.println("\nFastest path: " + String.join(" -> ", result.getPath()));
        System.out.println("Total distance: " + result.getTotalDistance() + " km");
        System.out.println("Base travel time: " + result.getBaseTravelTime() + " min");
        System.out.printf("Traffic-adjusted travel time: %.1f min%n", result.getTotalEffectiveTime());

        visualizePath(result.getPath());
        showStepByStepPath(result.getPath());
    }

    private void visualizePath(List<String> path) {
        System.out.println("\nConsole Visualization:");
        for (int i = 0; i < path.size(); i++) {
            System.out.print("[" + path.get(i) + "]");
            if (i < path.size() - 1) {
                System.out.print(" ==> ");
            }
        }
        System.out.println();
    }

    private void showStepByStepPath(List<String> path) {
        System.out.println("\nRoute Breakdown:");
        for (int i = 0; i < path.size() - 1; i++) {
            String current = path.get(i);
            String next = path.get(i + 1);
            int distance = graph.getDistanceBetween(current, next);

            if (distance != -1) {
                System.out.println(current + " -> " + next + " = " + distance + " km");
            }
        }
    }
}