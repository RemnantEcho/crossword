package customui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import backend.ActiveWord;
import backend.Cell;
import backend.Coordinates;
import backend.Grid;
import backend.LabelPair;
import backend.Letter;
import main.Main;

public class CustomJTable extends JTable {
	Grid grid;
	Main main;
	DefaultTableModel dftModel;
	MyCellRenderer cellRenderer;
	MyListSelectionListener listSelectListener;
	
	String[][] data;
	boolean isActiveWord;
	int rows;
	int columns;
	
	ArrayList<ActiveWord> wordsListRef;
	ActiveWord foundWord;
	
	ArrayList<LabelPair> labelList;
	ArrayList<Coordinates> highlightList;

	public void setRows(int r) {
		rows = r;
		dftModel.setRowCount(r);
	}

	public void setColumns(int c) {
		columns = c;
		dftModel.setColumnCount(c);
	}

	public void setWordsListRef(ArrayList<ActiveWord> wordsListRef) {
		this.wordsListRef = wordsListRef;
	}

	public void setFoundWord(ActiveWord foundWord) {
		this.foundWord = foundWord;
	}
	
	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public ArrayList<ActiveWord> getWordsListRef() {
		return wordsListRef;
	}

	public ActiveWord getFoundWord() {
		return foundWord;
	}
	
	public Grid getGrid() { return grid; }
	
	public void copyToTable(String[][] data) {
		if (dftModel == null)
			return;
		for (int r = 0; r < data.length; r++) {
			for (int c = 0; c < data[r].length; c++) {
				dftModel.setValueAt(data[r][c], r, c);
			}
		}
	}

	public String getValueByPosition(int r, int c) {
		if (dftModel == null)
			return "";
		return dftModel.getValueAt(r, c).toString();
	}

	public Cell getValueByPositionCell(int r, int c) {
		if (dftModel == null)
			return null;
		return new Cell(r, c, new Letter(dftModel.getValueAt(r, c).toString()));
	}

	public String[][] getAllData() {
		String[][] tempData = null;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				tempData[r][c] = dftModel.getValueAt(r, c).toString();
			}
		}
		return tempData;
	}
	
	public boolean isCellEmpty(int r, int c) {
		if (wordsListRef != null) {
			for (ActiveWord aWord : wordsListRef) {
				for (Letter l : aWord.getLetters()) {
					if (l.getCoords().getX() == c && l.getCoords().getY() == r) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public boolean isCellSolved(int r, int c) {
		if (wordsListRef != null) {
			for (ActiveWord aWord : wordsListRef) {
				if (aWord.getIsSolved()) {
					for (Letter l : aWord.getLetters()) {
						if (l.getCoords().getX() == c && l.getCoords().getY() == r) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public void generateLabelOverlay() {
		if (labelList != null) {
			for (int i = 0; i < labelList.size(); i++) {
				this.remove(labelList.get(i).getLabel());
				this.revalidate();
				this.repaint();
			}
		}
		
		labelList = new ArrayList<LabelPair>();
		JLabel label;
		
		if (wordsListRef != null) {
			for (int i = 0; i < wordsListRef.size(); i++) {
				Letter letter = wordsListRef.get(i).getLetters().get(0);
				int x = letter.getCoords().getX();
				int y = letter.getCoords().getY();
				
				Rectangle rect = this.getCellRect(y, x, true);
				
				label = new JLabel();
				label.setText(String.valueOf(wordsListRef.get(i).getPosition()));
				label.setFont(new Font("Calibri", Font.PLAIN, 10));
				label.setHorizontalAlignment(JLabel.CENTER);
				
				this.add(label);
				
				int incr = 0;
				for (int j = 0; j < labelList.size(); j++) {
					if (labelList.get(j).getX() == x && labelList.get(j).getY() == y) {
						incr++;
					}
				}
				label.setBounds(rect.x + (incr * 7), rect.y, 10, 15);
				labelList.add(new LabelPair(label, x, y));
			}
		}
		
	}
	
	public void clearData() {
		int row = this.getRowCount();
		int col = this.getColumnCount();
		
		this.clearSelection();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				this.setValueAt("", i, j);
			}
		}
		this.revalidate();
		this.repaint();
	}

	public void setCellSize(int w, int h, int row, int col) {
		int columnWidth = w / row;
		int rowHeight = h / col;
		this.setRowHeight(rowHeight);
		
		for (int c = 0; c < columns; c++) {
//			this.getColumnModel().getColumn(c).setPreferredWidth(columnWidth);
			this.getColumnModel().getColumn(c).setMinWidth(columnWidth);
//			this.getColumnModel().getColumn(c).setMaxWidth(columnWidth);
		}
	}
	
	public void setHighlight(ActiveWord aw) {
		ArrayList<Letter> letters = aw.getLetters();
		highlightList.clear();
		for (int i = 0; i < letters.size(); i++) {
			highlightList.add(letters.get(i).getCoords());
		}
		
		this.revalidate();
		this.repaint();
	}
	
	public void clearHighlight() {
		highlightList.clear();
		this.revalidate();
		this.repaint();
	}

	public CustomJTable(Main main, int w, int h, int r, int c) {
		this.main = main;
		highlightList = new ArrayList<Coordinates>();
		this.setSize(new Dimension(w, h));
		this.setMinimumSize(new Dimension(w, h));
		this.setMaximumSize(new Dimension(w, h));
//		this.getTableHeader().setUI(null);
		
		this.setGridColor(new Color(128, 128, 128));

		rows = r;
		columns = c;
		foundWord = null;

		cellRenderer = new MyCellRenderer(this, true);
		listSelectListener = new MyListSelectionListener(this);
		
		String[] header = new String[r];
		String[][] adata = new String[r][c];
				
		this.setBorder(new LineBorder(Color.GRAY, 3));
		this.setRowSelectionAllowed(false);
		
		DefaultCellEditor singleclick = new DefaultCellEditor(new CustomJTextField(this, w / r, h / c, r, c));
	    singleclick.setClickCountToStart(1);
		
		
		dftModel = new MyDefaultTableModel(this, r, c);
		this.setModel(dftModel);
		for (int i = 0; i < this.getColumnCount(); i++) {
			this.getColumnModel().getColumn(i).setCellRenderer(new MyCellRenderer(this, true));
			this.setDefaultEditor(this.getColumnClass(i), singleclick);
		}
		
		setCellSize(w, h, r, c);
	}

	public class MyDefaultTableModel extends DefaultTableModel {
		CustomJTable custTable = null;

		public MyDefaultTableModel(CustomJTable table, Object[][] tableData, Object[] colNames) {
			super(tableData, colNames);
			custTable = table;
		}

		public MyDefaultTableModel(CustomJTable table, int r, int c) {
			super(r, c);
			custTable = table;
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			if (custTable == null) return false;
			if (custTable.getWordsListRef() == null) return false;
			if (custTable.isCellEmpty(row, column)) {
				return false;
			}
			if (custTable.isCellSolved(row, column)) {
				return false;
			}
			return true;
		}
	}

	public class MyListSelectionListener implements ListSelectionListener {
		CustomJTable table;

		public MyListSelectionListener(CustomJTable table) {
			this.table = table;
		}

		public void valueChanged(ListSelectionEvent e) {
			if (table == null)
				return;
		}

	}

	// Render Cells
	public class MyCellRenderer extends JLabel implements TableCellRenderer {
		Border selectedBorder, unselectedBorder, solvedBorder, highlightBorder = null;
		CustomJTable custTable;

		boolean isBordered = true;

		public MyCellRenderer(CustomJTable table, boolean isBordered) {
			this.custTable = table;
			this.isBordered = isBordered;
			this.setFont(new Font("Calibri", Font.BOLD, 16));
			this.setVerticalAlignment(SwingConstants.CENTER);
			this.setHorizontalAlignment(JLabel.CENTER);
			setOpaque(true); // MUST do this for background to show up.
			
			selectedBorder = BorderFactory.createLineBorder(new Color(70, 188, 239), 1);
			unselectedBorder = BorderFactory.createLineBorder(new Color(90, 90, 90), 1);
			solvedBorder = BorderFactory.createLineBorder(new Color(103, 224, 66), 2);
			highlightBorder = BorderFactory.createLineBorder(new Color(255, 170, 0), 2);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			// Cell is Empty
			if (custTable.isCellEmpty(row, column)) {
				setBackground(new Color(90, 90, 90));
				setBorder(unselectedBorder);
			}
			// Cell is solved
			else if (custTable.isCellSolved(row, column)) {
				boolean found = false;
				// Highlight
				if (highlightList.size() != 0 && highlightList != null) {
					for (Coordinates c : highlightList) {
						if (c.getX() == column && c.getY() == row) {
							found = true;
							setBorder(highlightBorder);
						}
					}
				}
				if (found == false) {
//					System.out.println("Row: " + row + " Col: " + column);
					setBorder(solvedBorder);
				}
				setBackground(new Color(255, 255, 255));
			}
			// Otherwise
			else {
				boolean found = false;
				// Highlight
				if (highlightList.size() != 0 && highlightList != null) {
					for (Coordinates c : highlightList) {
						if (c.getX() == column && c.getY() == row) {
							found = true;
							setBorder(highlightBorder);
						}
					}
				}
				if (found == false) {
					setBorder(unselectedBorder);
				}
				
				setBackground(new Color(255, 255, 255));
			}
			
			if (value == null) {
				setText("");
			}
			else {
				setText(String.valueOf(value));
			}
			return this;
		}
	}
}
