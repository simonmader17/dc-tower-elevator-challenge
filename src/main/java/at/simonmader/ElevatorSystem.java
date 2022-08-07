package at.simonmader;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ElevatorSystem {
  public static final int NUM_ELEVATORS = 7;
  public static final int NUM_FLOORS = 55;

  private List<Elevator> elevators = new LinkedList<>();
  private ExecutorService elevatorPool = Executors.newFixedThreadPool(NUM_ELEVATORS);

  public ElevatorSystem() {
    this(true);
  }

  public ElevatorSystem(boolean printState) {
    for (int i = 1; i <= NUM_ELEVATORS; i++) {
      elevators.add(new Elevator(String.valueOf(i)));
    }
    for (Elevator e : elevators) {
      elevatorPool.execute(e);
    }
    if (printState) {
      this.printState();
    }
  }

  /**
   * Prints the state of all elevators every second in a seperate thread.
   */
  public void printState() {
    new Thread(() -> {
      System.out.println(this);
      while (!this.elevatorPool.isTerminated()) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        for (int i = 0; i < NUM_ELEVATORS; i++) {
          System.out.print("\033[1A"); // Move up
          System.out.print("\033[2K"); // Erase line content
        }
        System.out.println(this);
      }
    }).start();
  }

  public void addRequest(Request request) {
    Elevator fastestElevator = getFastestElevatorToDest(request.getFrom());
    fastestElevator.addRequestToQueue(request);
  }

  public boolean isReadyForShutdown() {
    for (Elevator e : elevators) {
      if (e.getHandlingRequest() != null || !e.getRequestQueue().isEmpty()) {
        return false;
      }
    }
    return true;
  }

  public void shutdown() {
    elevators.stream().forEach((e) -> e.shutdown());
    elevatorPool.shutdown();
  }

  /**
   * Returns the elevator that has to travel the smallest distance to the specific destination
   * floor.
   * 
   * @param dest the destination floor
   * @return the fastest elevator
   */
  public Elevator getFastestElevatorToDest(int dest) {
    Elevator fastestElevator = elevators.stream().min((a, b) -> {
      int distanceA = getTotalDistanceOf(a, dest);
      int distanceB = getTotalDistanceOf(b, dest);
      return Integer.compare(distanceA, distanceB);
    }).get();
    return fastestElevator;
  }

  /**
   * Calculates the total distance an elevator has to travel before getting to a specific
   * destination floor.
   * 
   * @param e the elevator
   * @param dest the destination floor
   * @return the total distance
   */
  public static int getTotalDistanceOf(Elevator e, int dest) {
    int distance = 0;
    Request nextRequest = e.getHandlingRequest();
    List<Request> remainingQueue = e.getRequestQueue();
    if (nextRequest == null && remainingQueue.isEmpty()) {
      distance = Math.abs(dest - e.getCurrentFloor());
    } else {
      if (nextRequest == null) {
        nextRequest = remainingQueue.remove(0);
      }
      if (e.isOnTheWayToFrom()) {
        distance += Math.abs(e.getCurrentFloor() - nextRequest.getFrom());
        distance += Math.abs(nextRequest.getTo() - nextRequest.getFrom());
      } else {
        distance += Math.abs(e.getCurrentFloor() - nextRequest.getTo());
      }
      int prev = nextRequest.getTo();
      for (Request req : remainingQueue) {
        distance += Math.abs(prev - req.getFrom());
        distance += Math.abs(req.getTo() - req.getFrom());
        prev = req.getTo();
      }
      distance += Math.abs(dest - prev);
    }
    return distance;
  }

  public List<Elevator> checkAvailableElevators() {
    return elevators.stream()
        .filter((e) -> e.getHandlingRequest() == null && e.getRequestQueue().isEmpty())
        .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return elevators.stream().map(Elevator::toString).collect(Collectors.joining("\n"));
  }
}
