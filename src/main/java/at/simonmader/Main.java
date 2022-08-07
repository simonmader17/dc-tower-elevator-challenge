package at.simonmader;

import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    ElevatorSystem elevatorSystem = new ElevatorSystem(true);

    elevatorSystem.addRequest(new Request(55, 0, Direction.DOWN));
    elevatorSystem.addRequest(new Request(0, 40, Direction.UP));
    System.out.println("After 2 requests, the following elevators are available: "
        + elevatorSystem.checkAvailableElevators().stream().map((e) -> e.getName())
            .collect(Collectors.joining(", ")));
    elevatorSystem.addRequest(new Request(0, 34, Direction.UP));
    elevatorSystem.addRequest(new Request(23, 0, Direction.DOWN));
    elevatorSystem.addRequest(new Request(0, 3, Direction.UP));
    elevatorSystem.addRequest(new Request(8, 0, Direction.DOWN));
    elevatorSystem.addRequest(new Request(0, 4, Direction.UP));
    elevatorSystem.addRequest(new Request(55, 0, Direction.DOWN));
    elevatorSystem.addRequest(new Request(0, 40, Direction.UP));
    elevatorSystem.addRequest(new Request(0, 34, Direction.UP));
    elevatorSystem.addRequest(new Request(0, 12, Direction.UP));
    elevatorSystem.addRequest(new Request(43, 0, Direction.DOWN));
    elevatorSystem.addRequest(new Request(8, 0, Direction.DOWN));
    elevatorSystem.addRequest(new Request(33, 0, Direction.DOWN));
    elevatorSystem.addRequest(new Request(0, 53, Direction.UP));
    elevatorSystem.addRequest(new Request(0, 40, Direction.UP));
    elevatorSystem.addRequest(new Request(55, 0, Direction.DOWN));
    elevatorSystem.addRequest(new Request(0, 12, Direction.UP));
    elevatorSystem.addRequest(new Request(43, 0, Direction.DOWN));
    elevatorSystem.addRequest(new Request(0, 10, Direction.UP));
    elevatorSystem.addRequest(new Request(33, 0, Direction.DOWN));
    elevatorSystem.addRequest(new Request(55, 0, Direction.DOWN));
    elevatorSystem.addRequest(new Request(0, 40, Direction.UP));
    elevatorSystem.addRequest(new Request(55, 0, Direction.DOWN));
    elevatorSystem.addRequest(new Request(0, 12, Direction.UP));
    elevatorSystem.addRequest(new Request(0, 3, Direction.UP));
    elevatorSystem.addRequest(new Request(8, 0, Direction.DOWN));
    elevatorSystem.addRequest(new Request(0, 4, Direction.UP));

    while (!elevatorSystem.isReadyForShutdown()) {
      Thread.sleep(1000);
    }
    elevatorSystem.shutdown();
  }

}
