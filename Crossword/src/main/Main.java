package main;
import javax.swing.*;

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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	AddToDictionaryWindow addWindow;
	JFrame window;
	JPanel panel;
	JButton openAddWindowBtn;
	CustomJList clueAcrossList;
	CustomJList clueDownList;
	DefaultListModel<String> model;
	CustomButton generateBtn, solveBtn;
	Font calibriFont;
	GridBagLayout gbl;
	GridBagConstraints gbc;
	Container panelPane;
	JLabel listTitle;
	CustomScrollPane scrollPaneAcross;
	CustomScrollPane scrollPaneDown;
	JScrollPane tableScrollPane;
	CustomJTable customJTable;
	Logic logic;
	
	int baseWidth, baseHeight;
	
	
	public Main() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			calibriFont = Font.createFont(Font.TRUETYPE_FONT, new File("./assets/CalibriRegular.ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./assets/CalibriRegular.ttf")));
		} catch (ClassNotFoundException | 
				InstantiationException | 
				IllegalAccessException | 
				UnsupportedLookAndFeelException |
				FontFormatException | 
				IOException e) {
			e.printStackTrace();
		}
		
		window = new JFrame("Crossword");
		addWindow = new AddToDictionaryWindow(this);
		panel = new JPanel();
				
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		
		logic = new Logic(10, 10);
		
		baseWidth = 600;
		baseHeight = 800;
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(baseWidth, baseHeight);
		window.setLocationRelativeTo(null);
		
		panel.setBackground(new Color(255, 255, 255));
		 
		panel.setLayout(new GridBagLayout());  
		GridBagConstraints gbc = new GridBagConstraints();  
		  
		generateBtn = new CustomButton("Generate", 300, 30);
		generateBtn.addActionListener(new GenerateBtnHandler());
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipady = 30;        
		gbc.weightx = 1.0;
		gbc.gridwidth = 4;  
		gbc.gridx = 0;  
		gbc.gridy = 0;  
		panel.add(generateBtn, gbc);
		
		ImageIcon dictIcon = new ImageIcon(((new ImageIcon("./assets/crossword_dict.png").getImage()
				.getScaledInstance(25, 25,
						java.awt.Image.SCALE_SMOOTH))));
		
		openAddWindowBtn = new JButton("", dictIcon);
		openAddWindowBtn.setPreferredSize(new Dimension(25, 25));
		openAddWindowBtn.setMinimumSize(new Dimension(25, 25));
		openAddWindowBtn.setMaximumSize(new Dimension(25, 25));
		openAddWindowBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
		openAddWindowBtn.setFocusable(false);
//		openAddWindowBtn.setRolloverEnabled(false);
//		openAddWindowBtn.setBorderPainted(false);
//		openAddWindowBtn.setContentAreaFilled(false);
//		openAddWindowBtn.setOpaque(false);
//		openAddWindowBtn.setFocusPainted(false);
//		openAddWindowBtn.setBackground(transparent);
		openAddWindowBtn.setToolTipText("Add To Dictionary");
		openAddWindowBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addWindow.setVisible(true);
			}
			
		});
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.ipady = 5;        
		gbc.ipadx = 5;        
		gbc.weightx = 0;
		gbc.gridwidth = 1;  
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.insets = new Insets(10, 5, 0, 5);
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		panel.add(openAddWindowBtn, gbc);
		
		listTitle = new JLabel("Across");
		listTitle.setFont(new Font("Calibri", Font.BOLD, 19));
		gbc.insets = new Insets(15, 10, -5, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipady = 0; 
		gbc.weightx = 0.5;
		gbc.gridwidth = 2;  
		gbc.gridx = 0;  
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(listTitle, gbc);
		
		listTitle = new JLabel("Down");
		listTitle.setFont(new Font("Calibri", Font.BOLD, 19));
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipady = 0; 
		gbc.weightx = 0.5;
		gbc.gridwidth = 2;  
		gbc.gridx = 2;  
		gbc.gridy = 1;  
		panel.add(listTitle, gbc);
		
		clueAcrossList = new CustomJList(this, 198, 200);
		scrollPaneAcross = new CustomScrollPane(clueAcrossList, 200, 180);
		clueAcrossList.setScrollPane(scrollPaneAcross);
		
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipady = 0; 
		gbc.weightx = 0.5;
		gbc.gridwidth = 2;  
		gbc.gridx = 0;  
		gbc.gridy = 2;
		panel.add(scrollPaneAcross, gbc);
		
		clueDownList = new CustomJList(this, 198, 200);
		scrollPaneDown = new CustomScrollPane(clueDownList, 200, 180);
		clueDownList.setScrollPane(scrollPaneDown);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipady = 0; 
		gbc.weightx = 0.5;
		gbc.gridwidth = 2;
		gbc.gridx = 2;  
		gbc.gridy = 2;
		
		panel.add(scrollPaneDown, gbc);
		
		clueAcrossList.setOtherListRef(clueDownList);
		clueDownList.setOtherListRef(clueAcrossList);
		
		customJTable = new CustomJTable(this, 360, 360, 10, 10);

		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.NONE;
		gbc.ipadx = 1;      
		gbc.ipady = 1;     
		gbc.weightx = 1.0;  
		gbc.weighty = 1.0;  
		gbc.gridx = 0;      
		gbc.gridy = 3;       
		gbc.gridwidth = 4;   
		panel.add(customJTable, gbc);
		
		solveBtn = new CustomButton("Check", 600, 30);
		solveBtn.addActionListener(new SolveBtnHandler());
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;  
		gbc.ipadx = 0;
		gbc.ipady = 30; 
		gbc.weightx = 1.0;   
		gbc.weighty = 1.0;   
		gbc.anchor = GridBagConstraints.PAGE_END; //bottom of space  
		gbc.gridx = 0;       
		gbc.gridy = 4;       
		gbc.gridwidth = 4;   
		panel.add(solveBtn, gbc);
		
		window.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				if (addWindow != null) {
					if (addWindow.isVisible()) {
						addWindow.requestFocus();
					}
				}
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				
			}
		      
		}); 
		
		window.add(panel);
		//window.pack();
		window.setVisible(true);
		

	}
	
	public void highlightWord(String clue) {
		if (customJTable != null) {
			try {
				customJTable.setHighlight(getWordFromClue(clue));
			} catch (Exception e) {
				System.out.println(e);
			}
			
		}
	}
	
	public ActiveWord getWordFromClue(String clue) {
		ArrayList<ActiveWord> awList = logic.getChosenWordsList();
		String[] splitList = clue.split("\\). ");
		String splitClue = splitList[1];
		splitClue = splitClue.substring(0, splitClue.length() - 1);
//		System.out.println(splitClue);
		String numSplitClue = splitClue.substring(splitClue.length() - 1, splitClue.length());
//		System.out.println(numSplitClue);
		while (numSplitClue.matches("^[0-9]+$")) {
			splitClue = splitClue.substring(0, splitClue.length() - 1);
			numSplitClue = splitClue.substring(splitClue.length() - 1, splitClue.length());
		}
		
		splitClue = splitClue.substring(0, splitClue.length() - 1);
		splitClue = splitClue.substring(0, splitClue.length() - 1);
		
//		System.out.println(splitClue);
		for (int i = 0; i < awList.size(); i++) {
			if (awList.get(i).getClue().equals(splitClue)) {
				return awList.get(i);
			}
		}
		return null;
	}
	
	public void enterActiveWordsToList(ArrayList<ActiveWord> awList) {
		if (clueAcrossList != null) ((DefaultListModel<String>) clueAcrossList.getModel()).removeAllElements();
		if (clueDownList != null) ((DefaultListModel<String>) clueDownList.getModel()).removeAllElements();
		
		for (int i = 0; i < awList.size(); i++) {
//			System.out.println(awList.size());
			if (awList.get(i).getIsHorizontal()) {
				if (clueAcrossList != null) {
					((DefaultListModel<String>) clueAcrossList.getModel()).addElement(String.valueOf(awList.get(i).getPosition()) + "). " + awList.get(i).getClue() + " (" + awList.get(i).getLength() + ")");
				}
			}
			else {
				if (clueDownList != null) {
					((DefaultListModel<String>) clueDownList.getModel()).addElement(String.valueOf(awList.get(i).getPosition()) + "). " + awList.get(i).getClue() + " (" + awList.get(i).getLength() + ")");
				}
			}
		}
	}
	
	
	public void clearHighlight() {
		if (customJTable != null) {
			customJTable.clearHighlight();
		}
	}
	
	public void solveCrossword() {
		try {
			if (customJTable.getCellEditor() != null) {
				customJTable.getCellEditor().cancelCellEditing();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		ArrayList<ActiveWord> awList = logic.getChosenWordsList();
		if (awList != null) {
			for (int i = 0; i < awList.size(); i++) {
				ArrayList<Letter> l = awList.get(i).getLetters();
				boolean isSolved = true;
				for (int j = 0; j < l.size(); j++) {
					Coordinates start = awList.get(i).getStart();
					
					try {					
						if (awList.get(i).getIsHorizontal()) {
							if (!((String) customJTable.getValueAt(start.getY(), start.getX() + j)).toUpperCase().equals(l.get(j).getLetterValue().toUpperCase())) {
								if (isSolved) isSolved = false;
							}
						}
						else {
							if (!((String) customJTable.getValueAt(start.getY() + j, start.getX())).toUpperCase().equals(l.get(j).getLetterValue().toUpperCase())) {
								if (isSolved) isSolved = false;
							}
						}
						
					} catch (Exception e) {
						System.out.println(e);
						isSolved = false;
					}
				}
//				System.out.println("Solved: " + isSolved);
				if (isSolved) awList.get(i).setIsSolved(true);
			}
		}
		
		clueAcrossList.clearSelection();
		clueAcrossList.revalidate();
		clueAcrossList.repaint();
		
		clueDownList.clearSelection();
		clueDownList.revalidate();
		clueDownList.repaint();
		
		customJTable.clearSelection();
		customJTable.clearHighlight();		
		customJTable.revalidate();
		customJTable.repaint();
	}
	
	
	public void generateCrossword(int x, int y) {
		logic.generateActiveWords(8);
		
		ArrayList<ActiveWord> chosenWords = logic.getChosenWordsList();
		/*
		for (int i = 0; i < chosenWords.size(); i++) {
			System.out.println("Word: " + chosenWords.get(i).getWord() + ", Clue: " + chosenWords.get(i).getClue());
			System.out.println("Start Position: " + chosenWords.get(i).getStart().getX() + ", " + chosenWords.get(i).getStart().getY() + ", "
					+ "End Position: " + chosenWords.get(i).getEnd().getX() + ", " + chosenWords.get(i).getEnd().getY());
			System.out.println("Length: " + chosenWords.get(i).getLength() + ", IsHorizontal: " + chosenWords.get(i).getIsHorizontal());
		}
		*/
		enterActiveWordsToList(chosenWords);
		
		clueAcrossList.clear();
		clueAcrossList.revalidate();
		clueAcrossList.repaint();
		
		clueDownList.clear();
		clueDownList.revalidate();
		clueDownList.repaint();
		
		if (customJTable != null) {
			customJTable.clearSelection();
			try {
				if (customJTable.getCellEditor() != null) {
					customJTable.getCellEditor().cancelCellEditing();
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			customJTable.clearData();
			customJTable.setWordsListRef(chosenWords);
			customJTable.generateLabelOverlay();
			customJTable.clearHighlight();
			customJTable.revalidate();
			customJTable.repaint();
		}
	}
	
	public int generateRandomInt(int min, int max) {
		return (int) Math.floor(Math.random() * (max - min + 1) + min);
	}
	
	public CustomJTable getJTable() { 
		if (customJTable != null) {
			return customJTable; 
		}
		return null;
	}
	
	public CustomJList getAcrossList() { 
		if (clueAcrossList != null) {
			return clueAcrossList; 
		}
		return null;
	}
	
	public CustomJList getDownList() { 
		if (clueDownList != null) {
			return clueDownList; 
		}
		return null;
	}
	
	public Logic getLogic() {
		if (logic != null) return logic;
		return null;
	}
	
	public static void main(String[] args) {
		System.setProperty("sun.java2d.uiScale", "1.0");
		new Main();
	}
	
	public class SolveBtnHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			solveCrossword();

		}
	}
	
	public class GenerateBtnHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			generateCrossword(10, 10);

		}
	}
}
