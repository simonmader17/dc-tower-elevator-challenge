package at.simonmader;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ElevatorSystem {
  public static final int NUM_ELEVATORS = 7;
  public static final int NUM_FLOORS = 55;

  private static ElevatorSystem instance;

  private List<Elevator> elevators = new LinkedList<>();
  private ExecutorService requestHandler = Executors.newFixedThreadPool(NUM_ELEVATORS);

  private ElevatorSystem() {
    this(true);
  }

  private ElevatorSystem(boolean printState) {
    for (int i = 1; i <= NUM_ELEVATORS; i++) {
      elevators.add(new Elevator(String.valueOf(i), i));
    }
    if (printState)
      printState();
  }

  public static synchronized ElevatorSystem getInstance() {
    return getInstance(true);
  }

  public static synchronized ElevatorSystem getInstance(boolean printState) {
    if (instance == null) {
      instance = new ElevatorSystem(printState);
    }
    return instance;
  }

  public List<Elevator> getElevators() {
    return new LinkedList<>(elevators);
  }

  public void printState() {
    new Thread(() -> {
      System.out.println(this);
      while (!this.requestHandler.isTerminated()) {
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

  public void execute(Request request) {
    if (request.getElevator() == null) {
      Elevator fastestElevator = getFastestElevatorToDest(request.getFrom());
      if (fastestElevator.getHandlingRequest() == null) {
        request.setElevator(fastestElevator);
        fastestElevator.setHandlingRequest(request);
        requestHandler.execute(request);
      } else {
        request.setElevator(fastestElevator);
        fastestElevator.addRequestToQueue(request);
        // requestHandler.execute(request);
      }
    } else {
      requestHandler.execute(request);
    }
  }

  public void shutdown() {
    requestHandler.shutdown();
  }

  public Elevator getFastestElevatorToDest(int dest) {
    Elevator fastestElevator = elevators.stream().sorted((a, b) -> {
      int distanceA = getTotalDistanceOf(a, dest);
      // if (dest == 1) System.out.println(a.getName() + ", " + distanceA);

      int distanceB = getTotalDistanceOf(b, dest);
      // if (dest == 1) System.out.println(b.getName() + ", " + distanceB);

      return Integer.compare(distanceA, distanceB);
    }).findFirst().get();
    return fastestElevator;
  }

  public static int getTotalDistanceOf(Elevator e, int dest) {
    int distance = 0;
    if (e.getHandlingRequest() == null) {
      distance = Math.abs(dest - e.getCurrentFloor());
    } else {
      if (e.getHandlingRequest().isOnTheWayToFrom()) {
        distance += Math.abs(e.getCurrentFloor() - e.getHandlingRequest().getFrom());
        distance += Math.abs(e.getHandlingRequest().getTo() - e.getHandlingRequest().getFrom());
      } else {
        distance += Math.abs(e.getCurrentFloor() - e.getHandlingRequest().getTo());
      }
      int prev = e.getHandlingRequest().getTo();
      for (Request req : e.getRequestQueue()) {
        distance += Math.abs(prev - req.getFrom());
        distance += Math.abs(req.getTo() - req.getFrom());
        prev = req.getTo();
      }
      distance += Math.abs(dest - prev);
    }
    return distance;
  }

  @Override
  public String toString() {
    return elevators.stream().map((e) -> e.toString()).collect(Collectors.joining("\n"));
  }
}
