package at.simonmader;

public class Request implements Runnable {
  public static final double TIME_MULTIPLIER = 0.2;

  private Elevator elevator;
  private int from;
  private int to;
  private Direction direction;
  private boolean onTheWayToFrom;

  public Request(int from, int to) {
    this(from, to, to > from ? Direction.UP : Direction.DOWN);
  }

  public Request(int from, int to, Direction direction) {
    if (from < 0 || from > ElevatorSystem.NUM_FLOORS || to < 0 || to > ElevatorSystem.NUM_FLOORS) {
      throw new IllegalArgumentException(
          "There are only " + ElevatorSystem.NUM_FLOORS + " floors!");
    }
    // if (from != 0 && to != 0) {
    // throw new IllegalArgumentException("Request has to include the ground floor!");
    // }
    this.from = from;
    this.to = to;
    this.direction = direction;
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

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public boolean isOnTheWayToFrom() {
    return this.onTheWayToFrom;
  }

  @Override
  public void run() {
    while (this.elevator == null) {
      Thread.yield();
    }
    synchronized (this.elevator) {
      int distanceToFrom = Math.abs(this.from - elevator.getCurrentFloor());
      int distanceBetweenFromAndTo = Math.abs(this.to - this.from);

      try {
        this.elevator.setVisualColor("\u001B[31m"); // sets elevator color to red for empty runs
        for (int i = 0; i < distanceToFrom; i++) {
          Thread.sleep((int) (1000 * TIME_MULTIPLIER));
          if (this.from > elevator.getCurrentFloor()) {
            elevator.goUp();
          } else {
            elevator.goDown();
          }
        }
        onTheWayToFrom = false;

        this.elevator.setVisualColor("\u001B[32m"); // sets elevator color to green when on the way
                                                    // to destination
        for (int i = 0; i < distanceBetweenFromAndTo; i++) {
          Thread.sleep((int) (1000 * TIME_MULTIPLIER));
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
