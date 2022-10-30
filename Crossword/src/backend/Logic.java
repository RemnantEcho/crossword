package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import customui.CustomJList;

public class Logic {
	Grid grid;
	Dictionary dict;

	CustomJList listAcross;
	CustomJList listDown;

	ArrayList<ActiveWord> chosenWordsList = null;
	ArrayList<Cell> checkedStartPos = null;
	
	int width;
	int height;

	// Generate the Crossword
	public void generateActiveWords(int amount) {
		boolean isHorizontal = true;
		boolean init = true;
		
		ArrayList<Word> dictionaryCopy = null;
		ArrayList<Word> dictionaryCopyCopy = null;
		ArrayList<Cell> startPositions = new ArrayList<>();
		Cell chosenCell = null;
		
		// Reset list
		chosenWordsList.clear();

		for (int i = 0; i < amount; i++) {
			init = (i == 0) ? true : false; // Check if initialising

			// Dictionary Copy
			dictionaryCopy = new ArrayList<Word>(dict.getDictionary());
			
			// Generate a List of Possible Starting Positions
			startPositions = generateStartPositionList(init);
			
//			for (int j = 0; j < startPositions.size(); j++) {
//				System.out.println("Start X: " + startPositions.get(j).getCoords().getX() + ", Start Y: " + startPositions.get(j).getCoords().getY());
//			}
			boolean wordFound = false;
			
			while (wordFound == false && startPositions.size() > 0) {
				// If No More Start Positions
				if (startPositions.size() <= 0) break;	
			
				// Get a Random Start Position
				chosenCell = determineStartPosition(startPositions, init);
				// Check if Starting Position has a Horizontal or Vertical Word
				isHorizontal = determineDirection(chosenCell, init);
				
				// Generates a Boundaries List
				ArrayList<Cell> boundaries = determineBoundaries(chosenCell, isHorizontal);
				
				/*
				for (int j = 0; j < boundaries.size(); j++) {
					System.out.println("Boundaries X: " + boundaries.get(j).getCoords().getX() + ", Boundaries Y: " + boundaries.get(j).getCoords().getY());
				}
				*/
				// Copy of Copy
				dictionaryCopyCopy = new ArrayList<Word>(dictionaryCopy);
				
				// Loop until it finds a word that fits
				while (wordFound == false) {
					if (dictionaryCopyCopy.size() <= 0) break;
					Word chosenWord = getRandomWord(dictionaryCopyCopy);
					Cell trueStartPosition = null;
					wordFound = true;
					boolean maxCharCheck = false;
					
					// Check if the word fits the table size
					if (isHorizontal) {
						if (chosenWord.getLength() > width) {
							wordFound = false;
						}
						else {
							maxCharCheck = true;
						}
					}
					else {
						if (chosenWord.getLength() > height) {
							wordFound = false;
						}
						else {
							maxCharCheck = true;
						}
					}
					
					// If the word will at least fit the Table
					if (maxCharCheck) {
						if (init) {
							if (chosenWord.getLength() > 3) {
								int half = (int) Math.ceil(chosenWord.getLength() / 2);
								wordFound = true;
								// Calculate Start Position of Chosen Word
								if (chosenCell.getCoords().getX() > half) {
									trueStartPosition = new Cell(chosenCell.getCoords().getX() - half, chosenCell.getCoords().getY());
								}
								else {
									trueStartPosition = new Cell(0, chosenCell.getCoords().getY());
								}
							}
							else {
								wordFound = false;
							}
						}
						else {
							ArrayList<Letter> existingLetters = new ArrayList<Letter>();
							for (int j = 0; j < chosenWord.getLetters().size(); j++) {
								if (chosenWord.getLetters().get(j).getLetterValue().equals(chosenCell.getLetter().getLetterValue())) {
									existingLetters.add(chosenWord.getLetters().get(j));
								}
							}
							
							// If the Random Word has letters that match the Starting Position Letter
							if (existingLetters.size() > 0) {
								int existLength = existingLetters.size(); // snapshot of size value
								
								// Loop through all the found existing Letters and check if they are within bounds
								for (int j = 0; j < existLength; j++) {
									int r = 0;
									if (existingLetters.size() - 1 != 0) {
										r = generateRandomNumber(0, existingLetters.size() - 1);
									}
									Letter l = existingLetters.get(r);
									existingLetters.remove(r);
									
									if (isHorizontal) {
										int startIndex = chosenCell.getCoords().getX();
										// If below bounds from the left and above bounds from the right
//										System.out.println("Letter Position: " + l.getPos());
//										System.out.println("Start Index: " + startIndex);
//										System.out.println("Chosen Word Length: " + chosenWord.getLength());
//										System.out.println("Chosen Word Upper: " + (chosenWord.getLength() - l.getPos()));
//										System.out.println("Chosen Word Lower: " + (l.getPos() - 1));
										if (startIndex < l.getPos() - 1 || chosenWord.getLength() - l.getPos() > (boundaries.size() - 1) - startIndex) {
											wordFound = false;
										}
										else {
											// Calculate Start Position of Chosen Word
											trueStartPosition = new Cell(startIndex - (l.getPos() - 1), chosenCell.getCoords().getY());
											for (int k = 0; k < chosenWord.getLength(); k++) {
												if (!boundaries.get(trueStartPosition.getCoords().getX() + k).getLetter().getLetterValue().equals(chosenWord.getLetters().get(k).getLetterValue()) &&
														boundaries.get(trueStartPosition.getCoords().getX() + k).getLetter().getLetterValue().length() > 0) {
													wordFound = false;
												}
											}
										}
									}
									else {
										int startIndex = chosenCell.getCoords().getY();
										// If above bounds from the top and below bounds from the bottom
//										System.out.println("Letter Position: " + l.getPos());
//										System.out.println("Start Index: " + startIndex);
//										System.out.println("Chosen Word Length: " + chosenWord.getLength());
//										System.out.println("Chosen Word Upper: " + (chosenWord.getLength() - l.getPos()));
//										System.out.println("Chosen Word Lower: " + (l.getPos() - 1));
										
										if (startIndex < l.getPos() - 1 || chosenWord.getLength() - l.getPos() > (boundaries.size() - 1) - startIndex) {
											wordFound = false;
										}
										else {
											// Calculate Start Position of Chosen Word
											trueStartPosition = new Cell(chosenCell.getCoords().getX(), startIndex - (l.getPos() - 1));
											for (int k = 0; k < chosenWord.getLength(); k++) {
												if (!boundaries.get(trueStartPosition.getCoords().getY() + k).getLetter().getLetterValue().equals(chosenWord.getLetters().get(k).getLetterValue()) &&
														boundaries.get(trueStartPosition.getCoords().getY() + k).getLetter().getLetterValue().length() > 0) {
													wordFound = false;
												}
											}
										}
									}
									if (wordFound) break; // If word found before loop finishes
								}
							}
							else {
								wordFound = false;
							}
						}
						
						if (wordFound) {
							ActiveWord aw = new ActiveWord(chosenWord.getWord(), chosenWord.getClue(), trueStartPosition.getCoords(), isHorizontal);
							chosenWordsList.add(aw);
							if (!init) {
								for (ActiveWord taw : chosenWordsList) {
									ArrayList<Letter> tempLetters = taw.getLetters();
									for (int j = 0; j < tempLetters.size(); j++) {
										if (tempLetters.get(j).getCoords().getX() == chosenCell.getCoords().getX() &&
												tempLetters.get(j).getCoords().getY() == chosenCell.getCoords().getY()) {
											
											tempLetters.get(j).getCell().setIntersect(true);
										}
									}
								}
							}
							// Remove Chosen Word from Dictionary Copy
							for (int j = 0; j < dictionaryCopy.size(); j++) {
								if (dictionaryCopy.get(j).getWord().equals(chosenWord.getWord())) {
									dictionaryCopy.remove(j);
									break;
								}
							}
							
						}
					}
				}
			}
		}

		initListPositions();
	}
	
	// Gets a list for boundaries
	public ArrayList<Cell> determineBoundaries(Cell cell, boolean isHorizontal) {
		ArrayList<Cell> cells = new ArrayList<Cell>();
		
		if (isHorizontal) {
			for (int i = 0; i < width; i++) {
				cells.add(new Cell(i, cell.getCoords().getY(), new Letter("")));
			}
			
		}
		else {
			for (int i = 0; i < height; i++) {
				cells.add(new Cell(cell.getCoords().getX(), i, new Letter("")));
			}
			
		}
		
		if (chosenWordsList != null && chosenWordsList.size() > 0) {
			if (isHorizontal) {
				cells.set(cell.getCoords().getX(), cell);
			}
			else {
				cells.set(cell.getCoords().getY(), cell);
			}
			for (int i = 0; i < chosenWordsList.size(); i++) {
				ArrayList<Letter> letters = chosenWordsList.get(i).getLetters();
				for (int j = 0; j < letters.size(); j++) {
					if (isHorizontal) {
						if (letters.get(j).getCoords().getY() == cell.getCoords().getY()) {
							int x = letters.get(j).getCoords().getX();
							cells.set(x, letters.get(j).getCell());
						}
					}
					else {
						if (letters.get(j).getCoords().getX() == cell.getCoords().getX()) {
							int y = letters.get(j).getCoords().getY();
							cells.set(y, letters.get(j).getCell());
						}
					}
				}
				
			}
		}
		
		return cells;
	}
	
	// Get Random Word from list
	public Word getRandomWord(ArrayList<Word> words) {
		int r = 0;
		if (words.size() - 1 > 0) {
			r = generateRandomNumber(0, words.size() - 1);
		}
		Word w = words.get(r);
		words.remove(r);
		return w;
	}
	
	// Add Positions to List
	public void initListPositions() {
		if (chosenWordsList != null && !chosenWordsList.isEmpty()) {
			int acrossIncrement = 1;
			int downIncrement = 1;

			for (int i = 0; i < chosenWordsList.size(); i++) {
				if (chosenWordsList.get(i).getIsHorizontal()) {
					chosenWordsList.get(i).setPosition(acrossIncrement);
					acrossIncrement += 1;
				}
				else {
					chosenWordsList.get(i).setPosition(downIncrement);
					downIncrement += 1;
				}
			}
		}
	}

	// Check if its Duplicate
	public boolean checkDuplicate(String s) {
		for (ActiveWord word : chosenWordsList) {
			if (s == word.getWord()) {
				return true;
			}
		}
		return false;
	}

	// Randomly choose between a list of Start Positions
	public Cell determineStartPosition(ArrayList<Cell> cells, boolean init) {
		Cell tempCell;
		if (init) {
			return cells.get(0);
		}
		
		int randNum = 0;
		if (cells.size() - 1 > 0) {
			randNum = generateRandomNumber(0, cells.size() - 1);
		}
		tempCell = cells.get(randNum);

		cells.remove(randNum);

		return tempCell;
	}

	// Generate List of Start Positions
	public ArrayList<Cell> generateStartPositionList(boolean init) {
		ArrayList<Cell> startPosList = new ArrayList<>();
		int posX = 0;
		int posY = 0;
		if (init) {
			posX = (int) Math.floor((width + 1) / 2);
			posY = (int) Math.floor((height - 1)/ 2);

			startPosList.add(new Cell(posX, posY));
			return startPosList;
		}

		for (ActiveWord word : chosenWordsList) {
			ArrayList<Letter> letters = word.getLetters();
			for (int j = 0; j < letters.size(); j++) {
				if (j > 0 && j < letters.size() - 1) {
					if (!letters.get(j - 1).getCell().getIntersect() && !letters.get(j).getCell().getIntersect() && !letters.get(j + 1).getCell().getIntersect()) {
						startPosList.add(letters.get(j).getCell());
					}
				}
				else if (j == 0) {
					if (!letters.get(j).getCell().getIntersect() && !letters.get(j + 1).getCell().getIntersect()) {
						startPosList.add(letters.get(j).getCell());
					}
				}
				else if (j == letters.size() - 1) {
					if (!letters.get(j - 1).getCell().getIntersect() && !letters.get(j).getCell().getIntersect()) {
						startPosList.add(letters.get(j).getCell());
					}
				}
			}
		}

		return startPosList;
	}

	// Determines the direction the word will be
	// Horizontal - True
	// Vertical - False
	public boolean determineDirection(Cell chosenCell, boolean init) {
		// Starting Word
		if (init) {
			return true;
		}
		
		if (!((ActiveWord) chosenCell.getLetter().getWord()).getIsHorizontal()) return true;


		return false;
	}

	public int generateRandomNumber(int low, int high) {
		Random rand = new Random();
		return rand.nextInt(high - low) + low;
	}

	public void setChosenWordsList(ArrayList<ActiveWord> words) { chosenWordsList = words; }
	public void setGrid(Grid g) { grid = g; }
	public void setListAcross(CustomJList acrossList) { listAcross = acrossList; }
	public void setListDown(CustomJList downList) { listDown = downList; }
	
	public ArrayList<ActiveWord> getChosenWordsList() { return chosenWordsList; }
	public Grid getGrid() { return grid; }
	public CustomJList getListAcross() { return listAcross; }
	public CustomJList getListDown() { return listDown; }
	
	public void setWidth(int w) { this.width = w; }
	public int getWidth() { return width; }
	
	public void setHeight(int h) { this.height = h; }
	public int getHeight() { return height; }

	public Logic(Grid g) {
		chosenWordsList = new ArrayList<ActiveWord>();
		grid = g;
		dict = new Dictionary();
	}

	public Logic(int w, int h) {
		chosenWordsList = new ArrayList<ActiveWord>();
		this.width = w;
		this.height = h;
		
		grid = new Grid(10, 10);
		dict = new Dictionary();
	}

	public Dictionary getDictionary() {
		return dict;
	}
}
