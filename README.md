# Smart Route Planner

A **traffic-aware route planning system** implemented in **Java** using **Dijkstra’s shortest path algorithm**.  
The program computes the fastest route between cities by considering **distance, travel time, and traffic levels**.

---

## Features

- Shortest/Fastest Path Calculation using **Dijkstra’s Algorithm**
- **Traffic-aware routing** with configurable traffic levels
- Custom city map input through a file (`map.txt`)
- Distance and travel time calculation
- Console visualization of the route

---
## How to Run

### Compile the program

```
javac GRoute/*.java
```

### Run the program

```
java GRoute.Main
```

---

## Example Output

```
Enter source city: A
Enter destination city: F

Fastest path: A -> C -> D -> F
Total distance: 15 km
Base travel time: 28 min
Traffic-adjusted travel time: 36 min
```

---
