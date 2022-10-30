package backend;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Dictionary {
	public final String fileDirectory = "./assets/dictionary.txt";
	File txtFile;
	ArrayList<Word> dictionaryList;
	
	public void saveToDictionary(Word w) throws IOException {
		try {
			/*FileWriter fw = new FileWriter(fileDirectory);
			//fw.write(w.getDictionaryEntry());
			fw.write("people;human beings;6");*/
			FileOutputStream fos = new FileOutputStream(fileDirectory, true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			PrintWriter pw = new PrintWriter(bw);
			pw.print("\n" + w.getDictionaryEntry());
			
			pw.close();
			
			readDictionary();
		}
		catch(FileNotFoundException e) {
			System.out.println("No Dictionary Found");
		}
	}
	
	public ArrayList<Word> getDictionary() {
		if (dictionaryList != null) return dictionaryList;
		return null;
	}
	
	
	public void readDictionary() {
		try {
			if (!txtFile.exists()) {
				try {
					txtFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Scanner sc = new Scanner(txtFile);
			while (sc.hasNextLine()) {
				String data = sc.nextLine();
//				System.out.println(data);
				String[] splitList = data.split(";");
				
				dictionaryList.add(new Word(splitList[0], splitList[1]));
			}
			/*
			for (Word w : dictionaryList)
			{
				System.out.println(w.getWord() + ", " + w.getClue() + ", " + w.getLength());
			}
			*/
		}
		catch(FileNotFoundException e) {
			System.out.println("No Dictionary Found");
		}
	}
	
	public Dictionary() {
		txtFile = new File(fileDirectory);
		dictionaryList = new ArrayList<Word>();
		readDictionary();
	}
}
