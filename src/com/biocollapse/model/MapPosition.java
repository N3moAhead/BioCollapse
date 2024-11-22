package src.com.biocollapse.model;

public class MapPosition {
  private int row;
  private int col;

  public MapPosition(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }

  public void add(MapPosition pos) {
    this.row += pos.row;
    this.col += pos.col;
  }

  public void subtract(MapPosition pos) {
    this.row -= pos.row;
    this.col -= pos.col;
  }

  // Move position up one field 
  public void moveUp() {
    this.row -= 1;
  }

  // Move position one field to the right
  public void moveRight() {
    this.col += 1;
  }

  // Move position down one field
  public void moveDown() {
    this.row += 1;
  }

  // Move position one field to the left
  public void moveLeft() {
    this.col -= 1;
  }

  public void moveIntoDirection(MovementAction direction) {
    switch (direction) {
      case UP:
        moveUp();
        break;
      case RIGHT:
        moveRight();
        break;
      case DOWN:
        moveDown();
        break;
      case LEFT:
        moveLeft();
        break;
      default:
        break;
    }
  }

  // Returns the position above the current position
  public MapPosition getTop() {
    return new MapPosition(row - 1, col);
  }

  // Returns the position to the right of the current position
  public MapPosition getRight() {
    return new MapPosition(row, col + 1);
  }

  // Returns the position below the current position
  public MapPosition getBot() {
    return new MapPosition(row + 1, col);
  }

  // Returns the position to the left of the current position
  public MapPosition getLeft() {
    return new MapPosition(row, col - 1);
  }

  @Override
  public String toString() {
    return "MapPosition{" + "row=" + row + ", col=" + col + '}';
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    MapPosition that = (MapPosition) obj;
    return this.row == that.row && this.col == that.col;
  }
}
