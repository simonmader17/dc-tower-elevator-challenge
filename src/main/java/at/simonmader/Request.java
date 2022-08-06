package at.simonmader;

public class Request {
  private int from;
  private int to;
  private Direction direction;

  public Request(int from, int to) {
    this(from, to, to > from ? Direction.UP : Direction.DOWN);
  }

  public Request(int from, int to, Direction direction) {
    if (from < 0 || to < 0) {
      throw new IllegalArgumentException("There are no basement floors!");
    }
    if (from > ElevatorSystem.NUM_FLOORS || to > ElevatorSystem.NUM_FLOORS) {
      throw new IllegalArgumentException(
          "There are only " + ElevatorSystem.NUM_FLOORS + " floors!");
    }
    if (from != 0 && to != 0) {
      // According to the task description, a request can only come from the DC Tower entrance on
      // the ground floor or from any floor to the ground floor.
      throw new IllegalArgumentException("The request has to include the ground floor!");
    }
    this.from = from;
    this.to = to;
    this.direction = direction;
  }

  public int getFrom() {
    return from;
  }

  public void setFrom(int from) {
    this.from = from;
  }

  public int getTo() {
    return to;
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

  @Override
  public String toString() {
    return this.from + " -> " + this.to;
  }
}
