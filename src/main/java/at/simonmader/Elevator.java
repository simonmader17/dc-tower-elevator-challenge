package at.simonmader;

import java.util.LinkedList;
import java.util.List;

public class Elevator implements Runnable {
  private String name;
  private int currentFloor;

  private Request handlingRequest;
  private List<Request> requestQueue = new LinkedList<>();

  private boolean onTheWayToFrom = true;
  private String visualColor = "\u001B[30m";

  private boolean shutdown = false;

  public Elevator(String name) {
    this(name, 0);
  }

  public Elevator(String name, int currentFloor) {
    this.name = name;
    this.currentFloor = currentFloor;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCurrentFloor() {
    return currentFloor;
  }

  public void setCurrentFloor(int currentFloor) {
    this.currentFloor = currentFloor;
  }

  public Request getHandlingRequest() {
    return handlingRequest;
  }

  public void setHandlingRequest(Request handlingRequest) {
    this.handlingRequest = handlingRequest;
  }

  public List<Request> getRequestQueue() {
    return new LinkedList<>(requestQueue);
  }

  public void addRequestToQueue(Request request) {
    this.requestQueue.add(request);
  }

  public String getVisualColor() {
    return visualColor;
  }

  public void setVisualColor(String visualColor) {
    this.visualColor = visualColor;
  }

  public boolean isOnTheWayToFrom() {
    return this.onTheWayToFrom;
  }

  private void goDown() {
    this.currentFloor -= 1;
  }

  private void goUp() {
    this.currentFloor += 1;
  }

  public void shutdown() {
    this.shutdown = true;
  }

  @Override
  public void run() {
    try {
      // Runs indefinitely until the shutdown() method of the elevator instance is called
      while (!this.shutdown) {
        // Checks the queue for new requests every second
        Thread.sleep(1000);
        if (this.handlingRequest == null && this.requestQueue.isEmpty()) {
          continue;
        } else if (this.handlingRequest == null) {
          this.handlingRequest = this.requestQueue.remove(0);
        }

        // Calculates the distance between the current floor and the pickup floor of the request
        int distanceToFrom = Math.abs(this.handlingRequest.getFrom() - this.currentFloor);
        // Calculates the distance between the destination and pickup floors of the request
        int distanceBetweenToAndFrom =
            Math.abs(this.handlingRequest.getTo() - this.handlingRequest.getFrom());

        // Elevator travels to the specified floors of the request by travelling 1 floor every
        // second
        this.setVisualColor("\u001B[31m"); // sets elevator color to red for empty runs
        this.onTheWayToFrom = true;
        for (int i = 0; i < distanceToFrom; i++) {
          Thread.sleep(1000);
          if (this.handlingRequest.getFrom() > this.currentFloor) {
            this.goUp();
          } else {
            this.goDown();
          }
        }
        this.setVisualColor("\u001B[32m"); // sets elevator color to green when on the way
                                           // to destination
        this.onTheWayToFrom = false;
        for (int i = 0; i < distanceBetweenToAndFrom; i++) {
          Thread.sleep(1000);
          if (this.handlingRequest.getTo() > this.currentFloor) {
            this.goUp();
          } else {
            this.goDown();
          }
        }

        this.setHandlingRequest(null);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    StringBuilder visual =
        new StringBuilder(new String(new char[ElevatorSystem.NUM_FLOORS + 1]).replace("\0", "="));
    visual.setCharAt(getCurrentFloor(), 'O');
    return String.format("%s, %3s [%s], %s, Queue: %s", getName(), "E" + getCurrentFloor(),
        visual.toString().replace("O", this.visualColor + "O\u001B[0m"), this.handlingRequest,
        this.getRequestQueue().toString().length() > 20
            ? this.getRequestQueue().toString().substring(0, 20) + "..."
            : this.getRequestQueue().toString());
  }

}
