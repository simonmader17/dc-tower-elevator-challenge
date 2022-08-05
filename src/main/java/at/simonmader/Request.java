package at.simonmader;

public class Request {
  private int from;
  private int to;
  private Direction direction;

  public Request(int from, int to) {
    this(from, to, to > from ? Direction.UP : Direction.DOWN);
  }

  public Request(int from, int to, Direction direction) {
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
