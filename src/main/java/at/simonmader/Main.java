package at.simonmader;

import java.util.LinkedList;
import java.util.List;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    ElevatorSystem elevatorSystem = new ElevatorSystem(true);

    List<Request> requests = new LinkedList<>();
    requests.add(new Request(55, 0, Direction.DOWN));
    requests.add(new Request(0, 40, Direction.UP));
    requests.add(new Request(0, 34, Direction.UP));
    requests.add(new Request(23, 0, Direction.DOWN));
    requests.add(new Request(0, 3, Direction.UP));
    requests.add(new Request(8, 0, Direction.DOWN));
    requests.add(new Request(0, 4, Direction.UP));
    requests.add(new Request(55, 0, Direction.DOWN));
    requests.add(new Request(0, 40, Direction.UP));
    requests.add(new Request(0, 34, Direction.UP));
    requests.add(new Request(0, 12, Direction.UP));
    requests.add(new Request(43, 0, Direction.DOWN));
    requests.add(new Request(8, 0, Direction.DOWN));
    requests.add(new Request(33, 0, Direction.DOWN));
    requests.add(new Request(0, 53, Direction.UP));
    requests.add(new Request(0, 40, Direction.UP));
    requests.add(new Request(55, 0, Direction.DOWN));
    requests.add(new Request(0, 12, Direction.UP));
    requests.add(new Request(43, 0, Direction.DOWN));
    requests.add(new Request(0, 10, Direction.UP));
    requests.add(new Request(33, 0, Direction.DOWN));
    requests.add(new Request(55, 0, Direction.DOWN));
    requests.add(new Request(0, 40, Direction.UP));
    requests.add(new Request(55, 0, Direction.DOWN));
    requests.add(new Request(0, 12, Direction.UP));
    requests.add(new Request(0, 3, Direction.UP));
    requests.add(new Request(8, 0, Direction.DOWN));
    requests.add(new Request(0, 4, Direction.UP));

    for (Request req : requests) {
      elevatorSystem.execute(req);
    }

    while (!elevatorSystem.isReadyForShutdown()) {
      Thread.sleep(1000);
    }
    elevatorSystem.shutdown();
  }

}
