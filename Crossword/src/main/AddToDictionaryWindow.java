package main;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

import backend.ActiveWord;
import backend.Cell;
import backend.Coordinates;
import backend.Letter;
import backend.Logic;
import backend.Word;
import customui.CustomButton;
import customui.CustomJList;
import customui.CustomJTable;
import customui.CustomScrollPane;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AddToDictionaryWindow extends JFrame {
	Main main;
	JPanel panel, leftPanel;
	
	CustomButton addButton;
	Font calibriFont;
	
	JLabel wordLabel, clueLabel;
	JTextField wordTextField; 
	
	JTextArea clueTextArea;
	JScrollPane scrollPane1;
	
	int baseWidth, baseHeight;
	
	
	public AddToDictionaryWindow(Main main) {
		this.main = main;

		int minHeight = 100;
		TitledBorder title;
		Border blackline = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
		title = BorderFactory.createTitledBorder(blackline, "Input");
		title.setTitleJustification(TitledBorder.CENTER);
		
		// Window
		this.setTitle("Add To Dictionary");
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setSize(440, minHeight);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));

		panel = new JPanel();
		panel.setSize(new Dimension(440, minHeight));
		panel.setPreferredSize(new Dimension(440, minHeight));
		panel.setMinimumSize(new Dimension(440, minHeight));
		panel.setMaximumSize(new Dimension(440, minHeight));
		panel.setBackground(new Color(255, 255, 255));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		leftPanel = new JPanel();
		leftPanel.setSize(new Dimension(300, minHeight));
		leftPanel.setPreferredSize(new Dimension(300, minHeight));
		leftPanel.setMinimumSize(new Dimension(300, minHeight));
		leftPanel.setMaximumSize(new Dimension(300, minHeight));
		leftPanel.setBackground(new Color(255, 255, 255));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		
		
		wordLabel = new JLabel("Word");
		wordLabel.setMinimumSize(new Dimension(50, minHeight));
		wordLabel.setPreferredSize(new Dimension(50, minHeight));
		wordLabel.setFont(new Font("Calibri", Font.PLAIN, 16));
		
		wordTextField = new JTextField();
		wordTextField.setMinimumSize(new Dimension(300, 30));
		wordTextField.setMaximumSize(new Dimension(300, 30));
		wordTextField.setPreferredSize(new Dimension(300, 30));
		wordTextField.setFont(new Font("Calibri", Font.PLAIN, 14));
		Border empty = new EmptyBorder(0, 2, 0, 0);
		Border b = new CompoundBorder(blackline, empty);
		wordTextField.setBorder(b);
		wordTextField.setToolTipText("Word - Accepts Letters Only");
//		wordTextField.setMargin(new Insets(0, 10, 10, 10));
		
		AbstractDocument document = (AbstractDocument) wordTextField.getDocument();
		document.setDocumentFilter(new PatternFilter());
		
		clueLabel = new JLabel("Clue");
		clueLabel.setMinimumSize(new Dimension(50, minHeight));
		clueLabel.setFont(new Font("Calibri", Font.PLAIN, 16));
		
		clueTextArea = new JTextArea(10, 10);
		clueTextArea.setFont(new Font("Calibri", Font.PLAIN, 14));
		
		document = (AbstractDocument) clueTextArea.getDocument();
		document.setDocumentFilter(new PatternFilter(true));
		clueTextArea.setWrapStyleWord(true);
		clueTextArea.setLineWrap(true);
		clueTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
		clueTextArea.setToolTipText("Clue - Accepts Everything but ';'");
		
		scrollPane1 = new JScrollPane(clueTextArea);
		scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		scrollPane1.setSize(new Dimension(200, 100));
		scrollPane1.setPreferredSize(new Dimension(300, 80));
//		scrollPane1.setMinimumSize(new Dimension(200, 100));
		scrollPane1.setMaximumSize(new Dimension(300, 80));
		
		scrollPane1.setBorder(blackline);
		scrollPane1.setOpaque(false);
//		scrollPane1.setBackground(new Color(0,0,0));
		
		addButton = new CustomButton("ADD", 100, minHeight);
		addButton.setFont(new Font("Calibri", Font.BOLD, 16));
		addButton.addActionListener(new AddBtnHandler());
		
		leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		leftPanel.add(wordTextField);
		leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		leftPanel.add(scrollPane1);
		leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		
		panel.add(Box.createRigidArea(new Dimension(20, 0)));
		panel.add(leftPanel);
		panel.add(Box.createRigidArea(new Dimension(20, 0)));
		panel.add(addButton);
		
//		this.setAlwaysOnTop(true);
		this.add(panel);
		
		this.pack();
		this.setLocationRelativeTo(null);
		
	}
	
	public void clearInputs() {
		wordTextField.setText("");
		clueTextArea.setText("");
		
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			wordTextField.requestFocusInWindow();
		}
		else {
			clearInputs();
		}
	}
	
	public class AddBtnHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			String folderPath = "./assets";
			String fileName = "dictionary.txt";
			
			File directory = new File(folderPath);
			
			if (!directory.exists()) {
				directory.mkdir();
			}
			
			File file = new File(folderPath + "/" + fileName);
			
			if (main != null) {
				try {
					if (wordTextField.getText() != null && wordTextField.getText() != "") {
						if (clueTextArea.getText() != null && clueTextArea.getText() != "") {
							main.getLogic().getDictionary().saveToDictionary(new Word(wordTextField.getText(), clueTextArea.getText()));
							clearInputs();
						}
					}
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}

		}
	}
	
	public class PatternFilter extends DocumentFilter {
		private int max; // Max character limiter - Default = -1, means no max limit
		private boolean isClue;

		public PatternFilter() {
			this.max = -1;
			this.isClue = false;
		}

		public PatternFilter(int max) {
			this.max = max;
			this.isClue = false;
		}

		public PatternFilter(boolean isClue, int max) {
			this.max = max;
			this.isClue = isClue;
		}

		public PatternFilter(boolean isClue) {
			this.isClue = isClue;
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
				
			if (isClue) {
				// Filter clue
				if (!str.equals(";")) {
					super.replace(fb, offs, length, str, a);
				}
			}
			else {
				// Default Filter - detects positive and negative numbers
				if (newText.matches("^[a-zA-Z]*$") && newText.length() <= max || newText.matches("^[a-zA-Z]*$") && max == -1) {
					super.replace(fb, offs, length, str, a);
				}
			}

		}
		
		// On remove of letters
		@Override
		public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
				throws BadLocationException {

			super.remove(fb, offset, length);
			
			// Ensures default value of 0 when empty
			if (fb.getDocument().getLength() == 0) {
				insertString(fb, 0, "0", null);
			}
		}
		
		// On Inserting new letters
		@Override
		public void insertString(FilterBypass fb, int offs, String str,
				AttributeSet a) throws BadLocationException {

			String text = fb.getDocument().getText(0,
					fb.getDocument().getLength());
			text += str;
			
			if (isClue) {
				// Filter on rotation input - 0-360
				if (!str.equals(";") && text.length() <= max || !str.equals(";") && max == -1) {
					super.insertString(fb, offs, str, a);
				}
			}
			else {
				if (text.matches("^[a-zA-Z]*$") && text.length() <= max || text.matches("^[a-zA-Z]*$") && max == -1) {
					super.insertString(fb, offs, str, a);
				}
			}

				
			
		}
		
	}
}
