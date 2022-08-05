package at.simonmader;

import java.util.LinkedList;
import java.util.List;

public class Elevator {
  private String name;
  private int currentFloor;
  private String visualColor = "\u001B[30m";

  private Request handlingRequest;
  private List<Request> requestQueue = new LinkedList<>();

  public Elevator(String name) {
    this(name, 0);
  }

  public Elevator(String name, int currentFloor) {
    if (currentFloor < 0 || currentFloor > ElevatorSystem.NUM_FLOORS) {
      throw new IllegalArgumentException(
          "There are only " + ElevatorSystem.NUM_FLOORS + " floors!");
    }
    this.name = name;
    this.currentFloor = currentFloor;
  }

  public void goDown() {
    this.currentFloor -= 1;
  }

  public void goUp() {
    this.currentFloor += 1;
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

  public Request getHandlingRequest() {
    return handlingRequest;
  }

  public void setHandlingRequest(Request handlingRequest) {
    this.handlingRequest = handlingRequest;
  }

  public void setCurrentFloor(int currentFloor) {
    this.currentFloor = currentFloor;
  }

  public List<Request> getRequestQueue() {
    return this.requestQueue;
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

  public void handleNextRequest() {
    if (this.requestQueue.isEmpty()) {
      setHandlingRequest(null);
    } else {
      Request request = requestQueue.remove(0);
      setHandlingRequest(request);
      request.setElevator(this);
    }
  }

  @Override
  public String toString() {
    StringBuilder visual =
        new StringBuilder(new String(new char[ElevatorSystem.NUM_FLOORS + 1]).replace("\0", "="));
    visual.setCharAt(getCurrentFloor(), 'O');
    return String.format("%s, %3s [%s], %s, Queue: %s", getName(), "E" + getCurrentFloor(),
        visual.toString().replace("O", this.visualColor + "O\u001B[0m"), this.handlingRequest,
        this.requestQueue.toString().length() > 20
            ? this.requestQueue.toString().substring(0, 20) + "..."
            : this.requestQueue.toString());
  }
}
