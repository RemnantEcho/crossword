package backend;

import javax.swing.JLabel;

public class LabelPair {
	Coordinates coords;
	JLabel label;
	
	public void setCoords(int x, int y) { coords = new Coordinates(x, y); }
	public void setCoords(Coordinates c) { coords = c; }
	public void setLabel(JLabel l) { this.label = l; }
	
	public Coordinates getCoords() { return coords; }
	public int getX() { return coords.getX(); }
	public int getY() { return coords.getY(); }
	public JLabel getLabel() { return label; }
	
	public LabelPair(JLabel label, int x, int y) {
		this.label = label;
		coords = new Coordinates(x, y);
	}
	
	public LabelPair(JLabel label, Coordinates coord) {
		coords = coord;
		this.label = label;
	}
	
}
