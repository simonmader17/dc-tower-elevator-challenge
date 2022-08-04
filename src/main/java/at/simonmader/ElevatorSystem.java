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
      int distanceA = 0;
      if (a.getHandlingRequest() == null) {
        distanceA = Math.abs(dest - a.getCurrentFloor());
      } else {
        if (a.getHandlingRequest().isOnTheWayToFrom()) {
          distanceA += Math.abs(a.getCurrentFloor() - a.getHandlingRequest().getFrom());
          distanceA += Math.abs(a.getHandlingRequest().getTo() - a.getHandlingRequest().getFrom());
        } else {
          distanceA += Math.abs(a.getCurrentFloor() - a.getHandlingRequest().getTo());
        }
        int prevA = a.getHandlingRequest().getTo();
        for (Request req : a.getRequestQueue()) {
          distanceA += Math.abs(prevA - req.getFrom());
          distanceA += Math.abs(req.getTo() - req.getFrom());
          prevA = req.getTo();
        }
        distanceA += Math.abs(dest - prevA);
      }
      // if (dest == 1) System.out.println(a.getName() + ", " + distanceA);

      int distanceB = 0;
      if (b.getHandlingRequest() == null) {
        distanceB = Math.abs(dest - b.getCurrentFloor());
      } else {
        if (b.getHandlingRequest().isOnTheWayToFrom()) {
          distanceB += Math.abs(b.getCurrentFloor() - b.getHandlingRequest().getFrom());
          distanceB += Math.abs(b.getHandlingRequest().getTo() - b.getHandlingRequest().getFrom());
        } else {
          distanceB += Math.abs(b.getCurrentFloor() - b.getHandlingRequest().getTo());
        }
        int prevB = b.getHandlingRequest().getTo();
        for (Request req : b.getRequestQueue()) {
          distanceB += Math.abs(prevB - req.getFrom());
          distanceB += Math.abs(req.getTo() - req.getFrom());
          prevB = req.getTo();
        }
        distanceB += Math.abs(dest - prevB);
      }
      // if (dest == 1) System.out.println(b.getName() + ", " + distanceB);

      return Integer.compare(distanceA, distanceB);
    }).findFirst().get();
    return fastestElevator;
  }

  @Override
  public String toString() {
    return elevators.stream().map((e) -> e.toString()).collect(Collectors.joining("\n"));
  }
}
