package at.simonmader;

import java.util.LinkedList;
import java.util.List;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    ElevatorSystem elevatorSystem = ElevatorSystem.getInstance(true);

    // elevatorSystem.execute(new Request(0, 20));
    // elevatorSystem.execute(new Request(0, 20));
    // elevatorSystem.execute(new Request(0, 20));
    // elevatorSystem.execute(new Request(0, 20));
    // elevatorSystem.execute(new Request(0, 20));
    // elevatorSystem.execute(new Request(0, 20));
    // elevatorSystem.execute(new Request(0, 20));
    // elevatorSystem.execute(new Request(1, 20));
    // elevatorSystem.execute(new Request(0, 20));
    // elevatorSystem.execute(new Request(0, 20));
    // elevatorSystem.execute(new Request(0, 20));

    List<Request> requests = new LinkedList<>();

    requests.add(new Request(55, 53));
    requests.add(new Request(0, 40));
    requests.add(new Request(55, 34));
    requests.add(new Request(23, 12));
    requests.add(new Request(43, 3));
    requests.add(new Request(8, 10));
    requests.add(new Request(33, 4));
    requests.add(new Request(55, 53));
    requests.add(new Request(0, 40));
    requests.add(new Request(55, 34));
    requests.add(new Request(23, 12));
    requests.add(new Request(43, 3));
    requests.add(new Request(8, 10));
    requests.add(new Request(33, 4));
    requests.add(new Request(55, 53));
    requests.add(new Request(0, 40));
    requests.add(new Request(55, 34));
    requests.add(new Request(23, 12));
    requests.add(new Request(43, 3));
    requests.add(new Request(8, 10));
    requests.add(new Request(33, 4));
    requests.add(new Request(55, 53));
    requests.add(new Request(0, 40));
    requests.add(new Request(55, 34));
    requests.add(new Request(23, 12));
    requests.add(new Request(43, 3));
    requests.add(new Request(8, 10));
    requests.add(new Request(33, 4));

    for (Request req : requests) {
      elevatorSystem.execute(req);
    }

    // elevatorSystem.shutdown();
  }

}
