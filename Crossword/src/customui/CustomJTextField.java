package customui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.DocumentFilter.FilterBypass;

public class CustomJTextField extends JTextField {
	CustomJTable table = null;
	JLabel numLabel = null;
	MyFocusListener focusListener = null;
	Border unselectedBorder, selectedBorder, solvedBorder = null;
	boolean isSolved, isSelected, isBottomSolved, isRightSolved, bottom, right = false;
	int row, column = 0;
	int borderWidth = 1;
	Color unselectedColor, selectedColor, solvedColor = null;
	
	public void setRow(int r) { row = r; }
	public int getRow() { return row; }
	
	public void setColumn(int c) { column = c; }
	public int getColumn() { return column; }
	
	public void setRight(boolean r) { right = r; }
	public void setBottom(boolean b) { bottom = b; }
	
	public boolean getRight() { return right; }
	public boolean getBottom() { return bottom; }
	
	public void setSolved(boolean isSolved) { this.isSolved = isSolved; }
	public void setSelected(boolean isSelected) { this.isSelected = isSelected; }
	public void setLabelText(String text) { numLabel.setText(text); }
	
	public void checkSolved() {
		if (isSolved) {
			this.setBorder(solvedBorder);
			this.setFocusable(false);
		}
		else {
			this.setBorder(unselectedBorder);
			this.setFocusable(true);
		}
	}
	
	public void setBlank() {
		this.setBackground(new Color(128, 128, 128));
		this.setBorder(unselectedBorder);
		this.setFocusable(false);
		this.setEditable(false);
	}
	
	public boolean getSolved() { return isSolved; }
	public boolean getSelected() { return isSelected; }
	public String getLabelText() { return numLabel.getText(); }
	public Border getUnselectedBorder() { return unselectedBorder; }
	public Border getSelectedBorder() { return selectedBorder; }
	public Border getSolvedBorder() { return solvedBorder; }
	
	public CustomJTextField(CustomJTable table, int w, int h, int r, int c) {
		this.table = table;
		row = r;
		column = c;
		AbstractDocument document = (AbstractDocument) this.getDocument();
		borderWidth = 2;

		focusListener = new MyFocusListener(this);
		numLabel = new JLabel();
		
		unselectedColor = new Color(128, 128, 128);
		selectedColor = new Color(0, 174, 239);
		solvedColor = new Color(102, 231, 77);
		
		this.setPreferredSize(new Dimension(w, h));
		this.setMinimumSize(new Dimension(w, h));
		
		this.addFocusListener(focusListener);
		document.setDocumentFilter(new PatternFilter(1));
		
		this.setFont(new Font("Calibri", Font.BOLD, 18));
		this.setHorizontalAlignment(JTextField.CENTER);
		this.setOpaque(false);
		
		
		AbstractDocument doc = (AbstractDocument) this.getDocument();
		doc.setDocumentFilter(new PatternFilter(1));
		
		checkSolved();
		initBorders(r, c);
		
		//setBlank();
	}
	
	public void initBorders(int r, int c) {
		row = r;
		column = c;
		
		unselectedBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, unselectedColor);
		selectedBorder = BorderFactory.createMatteBorder(borderWidth, borderWidth, borderWidth, borderWidth, selectedColor);
		solvedBorder = BorderFactory.createMatteBorder(borderWidth, borderWidth, borderWidth, borderWidth, solvedColor);
		
		this.setBorder(unselectedBorder);
	}
	
	public class MyFocusListener implements FocusListener {
		CustomJTextField cell;
		
		public MyFocusListener(CustomJTextField cell) {
			this.cell = cell;
		}

		@Override
		public void focusGained(FocusEvent e) {
//			if (cell.getSolved()) return;
			cell.setBorder(cell.getSelectedBorder());
			if (cell.getText().length() > 0) {
				cell.selectAll();
			}
			cell.setSelected(true);
			cell.repaint();
		}

		@Override
		public void focusLost(FocusEvent e) {
//			if (cell.getSolved()) return;
			cell.setSelected(false);
			cell.setBorder(cell.getUnselectedBorder());
			cell.revalidate();
			cell.repaint();
			table.revalidate();
			table.repaint();
			
		}
	}
	
	public class PatternFilter extends DocumentFilter {
		private int max; // Max character limiter - Default = -1, means no max limit

		public PatternFilter() {
			this.max = -1;
		}

		public PatternFilter(int max) {
			this.max = max;
		}

		// On Replace of letters
		@Override
		public void replace(FilterBypass fb, int offs, int length,
				String str, AttributeSet a) throws BadLocationException {

			String text = fb.getDocument().getText(0, fb.getDocument().getLength());
			String ogText = text; // Original copy
			text += str;
			String newText = ""; // After replacement
			
			// Calculate resulting Text after Replacement
			if (offs > 0) {
				newText += text.substring(0, offs);
			}
			newText += str;
			int end = (offs - 1) + length;
			
			if (end != ogText.length() - 1) {
				newText += text.substring((offs) + length, ogText.length());
			}
				// Default Filter - detects positive and negative numbers
				if (newText.matches("^[a-zA-Z]*$") && newText.length() <= max || newText.matches("^[a-zA-Z]*$") && max == -1) {
					super.replace(fb, offs, length, str.toUpperCase(), a);
				}
			

		}
		
		
		// On remove of letters
		@Override
		public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
				throws BadLocationException {

			super.remove(fb, offset, length);
			
		}
		
		// On Inserting new letters
		@Override
		public void insertString(FilterBypass fb, int offs, String str,
				AttributeSet a) throws BadLocationException {

			String text = fb.getDocument().getText(0,
					fb.getDocument().getLength());
			text += str;

				// Default Filter - detects positive and negative numbers
				if (text.matches("^[a-zA-Z]*$") && text.length() <= max || text.matches("^[a-zA-Z]*$") && max == -1) {
					super.insertString(fb, offs, str.toUpperCase(), a);
					
				}
			
		}
	}
}
