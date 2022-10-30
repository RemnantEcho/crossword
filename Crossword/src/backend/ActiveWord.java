package backend;
import java.util.ArrayList;

public class ActiveWord extends Word
{
	Coordinates start;
	Coordinates end;
	boolean isHorizontal = false;
	boolean isSolved = false;
	int position = -1;
	
	public void setStart(Coordinates coords) { start = coords; }
	public void setEnd(Coordinates coords) { end = coords; }
	public void setIsHorizontal(boolean horiz) { isHorizontal = horiz; }
	public void setIsSolved(boolean isSolved) { this.isSolved = isSolved; }
	public void setPosition(int position) { this.position = position; }
	
	public Coordinates getStart() { return start; }
	public Coordinates getEnd() { return end; }
	public boolean getIsHorizontal() { return isHorizontal; }
	public boolean getIsSolved() { return isSolved; }
	public int getPosition() { return position; }
	
	
	public ActiveWord(String w, String c, Coordinates s, Coordinates e , boolean horiz) {
		super(w, c);
		isHorizontal = horiz;
		start = s;
		end = e;
		
		if (s.getX() == e.getX()) isHorizontal = false;
		if (s.getY() == e.getY()) isHorizontal = true;
		
		for (int i = 0; i < length; i++) {
			if (isHorizontal) {
				letters.get(i).setCoord(s.getX() + i, s.getY());
				letters.get(i).setCell(new Cell(s.getX() + i, s.getY(), letters.get(i)));
				if (i == length - 1) end = new Coordinates(s.getX() + i, s.getY());
			}
			else {
				letters.get(i).setCoord(s.getX(), s.getY() + i);
				letters.get(i).setCell(new Cell(s.getX(), s.getY() + i, letters.get(i)));
				if (i == length - 1) end = new Coordinates(s.getX(), s.getY() + i);
			}
		}
	}
	
	public ActiveWord(String w, String c, Coordinates s, boolean horiz) {
		super(w, c);
		isHorizontal = horiz;
		start = s;
	
		for (int i = 0; i < length; i++) {
			if (isHorizontal) {
				letters.get(i).setCoord(s.getX() + i, s.getY());
				letters.get(i).setCell(new Cell(s.getX() + i, s.getY(), letters.get(i)));
				if (i == length - 1) end = new Coordinates(s.getX() + i, s.getY());
			}
			else {
				letters.get(i).setCoord(s.getX(), s.getY() + i);
				letters.get(i).setCell(new Cell(s.getX(), s.getY() + i, letters.get(i)));
				if (i == length - 1) end = new Coordinates(s.getX(), s.getY() + i);
			}
		}
	}

}
