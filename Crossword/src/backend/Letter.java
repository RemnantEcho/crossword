package backend;
public class Letter {
	String letter = "";
	Coordinates coord;
	Word word;
	Cell cell;
	int pos;
	
	public void setCoord(int x, int y) {
		if (coord == null) {
			coord = new Coordinates(x, y);
		}
		else {
			coord.setX(x);
			coord.setY(y);
		}
		
	}
	
	public void setLetterValue(String l) { this.letter = l; }
	public void setWord(Word w) { word = w; }
	public void setCell(Cell c) { cell = c; }
	public void setPos(int p) { pos = p; }
	
	public String getLetterValue() { return letter; }
	public Coordinates getCoords() { return coord; }
	public Word getWord() { return word; }
	public Cell getCell() { return cell; }
	public int getPos() { return pos; }
	
	public Letter(Word w, String l, Cell c) {
		word = w;
		letter = l;
		cell = c;
		coord = new Coordinates(c.getCoords().getX(), c.getCoords().getY());
	}
	
	public Letter(Word w, String l, int p) {
		word = w;
		letter = l;
		pos = p;
	}
	
	public Letter(Word w, String l, int x, int y) {
		word = w;
		letter = l;
		coord = new Coordinates(x, y);
	}
	
	public Letter(String l, ActiveWord w) {
		letter = l;
		word = w;
	}
	
	public Letter(String l) {
		letter = l;
	}
}
