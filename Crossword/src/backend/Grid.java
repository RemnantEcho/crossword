package backend;
import java.util.ArrayList;
import java.util.List;

public class Grid {
	ArrayList<Cell> cellList;
	int height;
	int width;
	
	public void setHeight(int h) { height = h; }
	public void setWidth(int w) { width = w; }
	public void setCellList(ArrayList<Cell> cells) { cellList = cells; }
	
	public int getHeight() { return height; }
	public int getWidth() { return width; }
	public ArrayList<Cell> getCellList() { return cellList; }
	
	public boolean checkEmpty(int r, int c) {
		for (int i = 0; i < cellList.size(); i++) {
			if (cellList.get(i).getCoords().getY() == r && cellList.get(i).getCoords().getX() == c) {
				Letter tempLetter = cellList.get(i).getLetter();
				if (tempLetter == null) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		
		return false;
	}
	
	public boolean checkSolved(int r, int c) {
		for (int i = 0; i < cellList.size(); i++) {
			if (cellList.get(i).getCoords().getY() == r && cellList.get(i).getCoords().getX() == c) {
				Letter tempLetter = cellList.get(i).getLetter();
				ActiveWord aw = (ActiveWord) tempLetter.getWord();
				if (aw.getIsSolved()) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		
		return false;
	}
	
	public void createGrid(int h, int w) {
		height = h;
		width = w;
		
		if (cellList == null) {
			cellList = new ArrayList<Cell>();
		}
		else if (cellList.size() > 0) {
			cellList.clear();
		}
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				cellList.add(new Cell(w, h));
			}
		}
	}
	
	public Grid(int h, int w) {
		createGrid(h, w);
	}
	
	public Grid() {
	}
}
