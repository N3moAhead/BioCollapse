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
