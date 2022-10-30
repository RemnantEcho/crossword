package backend;
import java.util.ArrayList;

public class Word {
	ArrayList<Letter> letters;
	String word;
	String clue;
	int length;
	
	public void setLetters(ArrayList<Letter> l) { letters = l; }
	public void setWord(String w) { word = w; }
	public void setClue(String c) { clue = c; }
	public void setLength(int l) { length = l; }
	
	public ArrayList<Letter> getLetters() { return letters; }
	public String getWord() { return word; }
	public String getClue() { return clue; }
	public int getLength() { return length; }
	
	public String getDictionaryEntry()
	{
		return word + ";" + clue + ";" + length;
	}
	
	public Word(String w, String c, int l)
	{
		word = w;
		clue = c;
		length = l;
		
		String[] wordArray = w.split("");
		int i = 0;
		for (String letter : wordArray)  {
			i++;
			letters.add(new Letter(this, letter, i));
		}
	}
	
	public Word(String w, String c)
	{
		word = w;
		clue = c;
		length = word.length();
		
		letters = new ArrayList<Letter>();
		
		String[] wordArray = w.split("");
		int i = 0;
		for (String letter : wordArray)  {
			i++;
			letters.add(new Letter(this, letter, i));
		}
	}
}
