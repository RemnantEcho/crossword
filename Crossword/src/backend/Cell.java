package backend;
public class Cell {
	Coordinates coords;
	Letter letter;
	boolean doesIntersect;
	
	public void setCoords(int x, int y) { coords = new Coordinates(x, y); }
	public void setCoords(Coordinates c) { coords = c; }
	public void setLetter(Letter l) { letter = l; }
	public void setIntersect(boolean inter) { doesIntersect = inter; }
	
	public Coordinates getCoords() { return coords; }
	public Letter getLetter() { return letter; }
	public boolean getIntersect() { return doesIntersect; }
	
	public Cell(int x, int y, Letter l, boolean inter) {
		coords = new Coordinates(x, y);
		letter = l;
		doesIntersect = inter;
	}
	
	public Cell(int x, int y, Letter l) {
		coords = new Coordinates(x, y);
		letter = l;
		doesIntersect = false;
	}
	
	public Cell(int x, int y) {
		coords = new Coordinates(x, y);
		doesIntersect = false;
	}
	
}
