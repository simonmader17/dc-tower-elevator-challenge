package at.simonmader;

public class Request implements Runnable {
  private Elevator elevator;
  private int from;
  private int to;
  private boolean onTheWayToFrom;

  public Request(int from, int to) {
    if (from < 0 || from > ElevatorSystem.NUM_FLOORS || to < 0 || to > ElevatorSystem.NUM_FLOORS) {
      throw new IllegalArgumentException(
          "There are only " + ElevatorSystem.NUM_FLOORS + " floors!");
    }
    this.from = from;
    this.to = to;
    this.onTheWayToFrom = true;
  }

  public Elevator getElevator() {
    return this.elevator;
  }

  public void setElevator(Elevator elevator) {
    this.elevator = elevator;
  }

  public int getFrom() {
    return this.from;
  }

  public void setFrom(int from) {
    this.from = from;
  }

  public int getTo() {
    return this.to;
  }

  public void setTo(int to) {
    this.to = to;
  }

  public boolean isOnTheWayToFrom() {
    return this.onTheWayToFrom;
  }

  @Override
  public void run() {
    synchronized (elevator) {
      int distanceToFrom = Math.abs(this.from - elevator.getCurrentFloor());
      int distanceBetweenFromAndTo = Math.abs(this.to - this.from);

      try {
        for (int i = 0; i < distanceToFrom; i++) {
          Thread.sleep(1000);
          if (this.from > elevator.getCurrentFloor()) {
            elevator.goUp();
          } else {
            elevator.goDown();
          }
        }
        onTheWayToFrom = false;

        for (int i = 0; i < distanceBetweenFromAndTo; i++) {
          Thread.sleep(1000);
          if (this.to > elevator.getCurrentFloor()) {
            elevator.goUp();
          } else {
            elevator.goDown();
          }
        }

        elevator.handleNextRequest();
        this.elevator = null;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public String toString() {
    return this.from + " -> " + this.to;
  }
}
