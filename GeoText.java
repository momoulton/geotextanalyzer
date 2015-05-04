// File: GeoText.java
// Mo Moulton
// CSciE10b Final Project
// TF David Habermehl
// 8 May 2015

/** 
 *  This class run the program GeoText Analysis. It invites the user to load
 * a plain-text file, then process and analyzes that file. Users can add to the
 * pre-loaded dictionary of geographical place names included in the analysis.
 *
 * @author: Mo Moulton
 * @version: 8 May 2015
**/

import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class GeoText {
		
	public static void main(String[] args)
	{
		Window window = new Window();
		window.setVisible(true);
	}
	
}

class Window extends JFrame {
	
	String filename = null;
	File file = null;
	String year = null;
	String fileTitle = null;
	ArrayList<String> allWords = new ArrayList<String>();
	ArrayList<String> procWords = new ArrayList<String>();
	ArrayList<String> uniqueWords = new ArrayList<String>();
	ArrayList<Word> wordFreqs = new ArrayList<Word>();
	String[][] wordCounter;
	int totalWords = 0;
	int uniqueWordTotal = 0;
	
	JPanel headerPanel = new JPanel();
	JLabel greeting = new JLabel("Welcome to the GeoText Analyzer!");
	
	JPanel onePanel = new JPanel();
	JLabel chooseLabel = 
		new JLabel("First, choose a text to analyze. Must be plaintext (.txt). The Analyzer will look at ALL text in the file. You may wish to remove header and footer material first. ");
	JButton browseButton = new JButton("Browse");
	JPanel filePanel = new JPanel();
	JLabel fileLabel = new JLabel("File: ");
	JTextArea fileArea = new JTextArea();
	
	JPanel twoPanel = new JPanel();
	JLabel titleLabel = new JLabel("Second, enter the text's title.");
	JTextArea titleArea = new JTextArea();
	
	JPanel threePanel = new JPanel();
	JLabel dateLabel = new JLabel
		("Third, enter the text's date of publication or creation. ");
	JTextArea dateArea = new JTextArea();
	
	JPanel fourPanel = new JPanel();
	JLabel analyzeLabel = new JLabel("Finally, analyze! ");
	JButton analyzeButton = new JButton("Analyze");
	
	JPanel blankPanel = new JPanel();
	
	
	public Window()
	{
		do_layout();
		do_plumbing();
	}
	
	private void do_layout()
	{
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("GeoText Analyzer");
		this.setLocationRelativeTo(null);
		this.setLayout(new GridLayout(0,1));
		
		greeting.setFont(new Font("Serif", Font.BOLD, 28));
		headerPanel.setLayout(new FlowLayout());
		headerPanel.add(greeting);
		
		onePanel.setLayout(new FlowLayout());
		onePanel.add(chooseLabel);
		onePanel.add(browseButton);
		
		filePanel.setLayout(new BorderLayout());
		filePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		filePanel.add(fileLabel, BorderLayout.LINE_START);
		filePanel.add(fileArea, BorderLayout.CENTER);
		
		twoPanel.setLayout(new BorderLayout());
		twoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		twoPanel.add(titleLabel, BorderLayout.LINE_START);
		twoPanel.add(titleArea, BorderLayout.CENTER);
		
		threePanel.setLayout(new BorderLayout());
		threePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		threePanel.add(dateLabel, BorderLayout.LINE_START);
		threePanel.add(dateArea, BorderLayout.CENTER);
		
		fourPanel.setLayout(new FlowLayout());
		fourPanel.add(analyzeLabel);
		fourPanel.add(analyzeButton);
		
		this.add(headerPanel);
		this.add(onePanel);
		this.add(filePanel);
		this.add(twoPanel);
		this.add(threePanel);
		this.add(fourPanel);
		
		this.pack();
	}
	
	private void do_plumbing()
	{
		browseButton.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					browse_clicked();
				}
		} );
		
		analyzeButton.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					analyze_clicked();
				}
		} );
	}
	
	private void browse_clicked()
	{
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(Window.this);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			file = fc.getSelectedFile();
			filename = fc.getSelectedFile().getName();
			fileArea.setText(filename);
		}		
	}
	
	private void analyze_clicked()
	{
		year = dateArea.getText().trim();
		fileTitle = titleArea.getText().trim();
		
		if (file == null || filename == null)
		{
			JOptionPane.showMessageDialog(null, "You haven't entered a file. Try again.");
		}
		else if (!file.exists())
		{
			JOptionPane.showMessageDialog(null, "That file doesn't exist. Try again.");
		}
		else
		{
			allWords = readData(file);
			procWords = processWords(allWords);
			uniqueWords = getUnique(procWords);
			totalWords = procWords.size();
			uniqueWordTotal = uniqueWords.size();
			wordFreqs = countWords(procWords);
			
			if (allWords.size() == 0)
			{
				JOptionPane.showMessageDialog(null, "The file can't be read. Please try again.");
			}
			else
			{
				DataWindow dataWindow = new DataWindow();
				dataWindow.setVisible(true);
			}
		}
	}
	
	private ArrayList<String> readData(File file)
	{
		Scanner inFile = null;
		ArrayList<String> allWords = new ArrayList<String>();
		
		try
		{
			inFile = new Scanner(file).useDelimiter("-|\\s");
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "There's a problem with the file. Please try again.");
		}
		
		while (inFile.hasNext())
		{
			allWords.add(inFile.next());
		}
		
		return allWords;
	}
	
	private ArrayList<String> processWords(ArrayList<String> allWords)
	{
		String letterWord = "";
		ArrayList<String> lowWords = new ArrayList<String>();
		
		for (String word : allWords)
		{
			String lowerWord = word.toLowerCase();
			lowWords.add(lowerWord);
		}
		
		for (String word: lowWords)
		{
			for (int i = 0; i < word.length(); i++)
			{
				if (Character.isLetterOrDigit(word.charAt(i)))
				{
					letterWord = letterWord + word.charAt(i);
				}
			}
			
			if (letterWord.length() > 0)
			{
				procWords.add(letterWord);
			}
			
			letterWord = "";
		}

		return procWords;	
	}
	
	private ArrayList<String> getUnique(ArrayList<String> procWords)
	{
		for (String word : procWords)
		{
			if (!uniqueWords.contains(word))
			{
				uniqueWords.add(word);
			}
		}
		
		return uniqueWords;
	}
	
	private ArrayList<Word> countWords(ArrayList<String> procWords)
	{
		ArrayList<Word> wordFreqs = new ArrayList<Word>();
		
		for (String word : uniqueWords)
		{
			Word wordObj = new Word(word);
			wordFreqs.add(wordObj);
		}
		
		for (String word : procWords)
		{
			for (Word wordObj : wordFreqs)
			{
				if (word.equals(wordObj.getWord()))
				{
					wordObj.addFreq();
				}			
			}
		}
		
		Collections.sort(wordFreqs);
		return wordFreqs;
	}
	

	
class DataWindow extends JFrame {
	
	String title = String.format("%s (%s)", fileTitle, year);
	ArrayList<Place> placeDictionary = new ArrayList<Place>();
	ArrayList<Word> custDictionary = new ArrayList<Word>();
	
	JPanel textPanel = new JPanel();
	JLabel textLabel = new JLabel("Text Analysis");
	JLabel totalLabel = new JLabel("Total Words: ");
	JLabel totalWordsLabel = new JLabel(Integer.toString(totalWords));
	JLabel uniqueLabel = new JLabel("Unique Words: ");
	JLabel uniqueWordsLabel = new JLabel(Integer.toString(uniqueWordTotal));
	
	JPanel top100Panel = new JPanel();
	JLabel top100Label = new JLabel("All words by frequency");
	JButton saveAllButton = new JButton("Save as CSV File");
	JTextArea top100Area = new JTextArea();
	JScrollPane top100Pane = new JScrollPane(top100Area);
	
	JPanel freqPanel = new JPanel();
	JButton findButton = new JButton("Find Frequency");
	JLabel freqLabel = new JLabel("Word Search & Build Custom Word Bank");
	JLabel findWordLabel = new JLabel("Enter Word: ");
	JTextArea findWordArea = new JTextArea();
	JTextArea wordResults = new JTextArea();
	JPanel wordFind = new JPanel();
	JScrollPane freqPane = new JScrollPane(wordResults);
	JButton addWord = new JButton("Add to Custom Word Bank");
	JButton delWord = new JButton("Delete from Custom Word Bank");
	
	JPanel custPanel = new JPanel();
	JLabel custLabel = new JLabel("Custom Word Bank");
	JTextArea custArea = new JTextArea();
	JScrollPane custPane = new JScrollPane(custArea);
	JButton runCust = new JButton("Run Analysis");
	JButton saveCust = new JButton("Save as CSV File");
	
	JPanel geoPanel = new JPanel();
	JLabel geoLabel = new JLabel("Place Name Analysis");
	JButton runGeo = new JButton("Run Analysis");
	JTextArea geoData = new JTextArea();
	JScrollPane geoPane = new JScrollPane(geoData);
	JButton saveGeo = new JButton("Save as CSV File");
	
	JPanel dictPanel = new JPanel();
	JLabel dictLabel = new JLabel("Customize the Place Name Dictionary");
	JButton addButton = new JButton("Add this place");
	JButton delButton = new JButton("Delete this place");
	JLabel nameField = new JLabel("Place Name: ");
	JLabel latField = new JLabel("Latitude: ");
	JLabel longField = new JLabel("Longitude: ");
	JTextArea nameArea = new JTextArea();
	JTextArea latArea = new JTextArea();
	JTextArea longArea = new JTextArea();
		
	public DataWindow()
	{
		do_layout();
		do_plumbing();
	}
	
	private void do_layout()
	{	
		textPanel.setLayout(new GridLayout(3,1));
		textPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		textPanel.add(textLabel);
		JPanel totalPanel = new JPanel();
		totalPanel.setLayout(new FlowLayout());
		totalPanel.add(totalLabel);
		totalPanel.add(totalWordsLabel);
		JPanel uniquePanel = new JPanel();
		uniquePanel.setLayout(new FlowLayout());
		uniquePanel.add(uniqueLabel);
		uniquePanel.add(uniqueWordsLabel);
		textPanel.add(totalPanel);
		textPanel.add(uniquePanel);
		
		String allResults = displayResults(wordFreqs);
		top100Panel.setLayout(new BorderLayout());
		top100Panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		top100Panel.add(top100Label, BorderLayout.PAGE_START);
		top100Area.setEditable(false);
		top100Area.setText(allResults);
		top100Panel.add(top100Pane, BorderLayout.CENTER);
		top100Panel.add(saveAllButton, BorderLayout.PAGE_END);
		
		wordFind.setLayout(new BorderLayout());
		wordFind.add(findWordLabel, BorderLayout.LINE_START);
		wordFind.add(findWordArea, BorderLayout.CENTER);
		wordFind.add(findButton, BorderLayout.LINE_END);
		JPanel findButtons = new JPanel();
		findButtons.setLayout(new GridLayout(1,2));
		findButtons.add(addWord);
		findButtons.add(delWord);
		wordFind.add(findButtons, BorderLayout.PAGE_END);
		freqPanel.setLayout(new BorderLayout());
		freqPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		freqPanel.add(freqLabel, BorderLayout.PAGE_START);
		freqPanel.add(wordFind, BorderLayout.PAGE_END);
		wordResults.setEditable(false);
		freqPanel.add(freqPane, BorderLayout.CENTER);
		
		custPanel.setLayout(new BorderLayout());
		custPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		custPanel.add(custLabel, BorderLayout.PAGE_START);
		custArea.setEditable(false);
		custPanel.add(custPane, BorderLayout.CENTER);
		JPanel custButtons = new JPanel();
		custButtons.setLayout(new GridLayout(1,2));
		custButtons.add(runCust);
		custButtons.add(saveCust);
		custPanel.add(custButtons, BorderLayout.PAGE_END);
		
		geoData.setEditable(false);
		geoPanel.setLayout(new BorderLayout());
		geoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		geoPanel.add(geoLabel, BorderLayout.PAGE_START);
		geoPanel.add(geoPane, BorderLayout.CENTER);
		JPanel geoButtons = new JPanel();
		geoButtons.setLayout(new GridLayout(1,2));
		geoButtons.add(runGeo);
		geoButtons.add(saveGeo);
		geoPanel.add(geoButtons, BorderLayout.PAGE_END);
		
		dictPanel.setLayout(new BorderLayout());
		dictPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel enterAddData = new JPanel();
		enterAddData.setLayout(new GridLayout(3,1));
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout());
		namePanel.add(nameField, BorderLayout.LINE_START);
		namePanel.add(nameArea, BorderLayout.CENTER);
		JPanel latPanel = new JPanel();
		latPanel.setLayout(new BorderLayout());
		latPanel.add(latField, BorderLayout.LINE_START);
		latPanel.add(latArea, BorderLayout.CENTER);
		JPanel longPanel = new JPanel();
		longPanel.setLayout(new BorderLayout());
		longPanel.add(longField, BorderLayout.LINE_START);
		longPanel.add(longArea, BorderLayout.CENTER);
		enterAddData.add(namePanel);
		enterAddData.add(latPanel);
		enterAddData.add(longPanel);
		JPanel dictButtons = new JPanel();
		dictButtons.setLayout(new GridLayout(1,2));
		dictButtons.add(addButton);
		dictButtons.add(delButton);
		dictPanel.add(dictLabel, BorderLayout.PAGE_START);
		dictPanel.add(enterAddData, BorderLayout.CENTER);
		dictPanel.add(dictButtons, BorderLayout.PAGE_END);
		
		this.setTitle(title);
		this.setLocationRelativeTo(null);
		this.setLayout(new GridLayout(3,3));
		
		this.add(textPanel);
		this.add(top100Panel);
		this.add(freqPanel);
		this.add(custPanel);
		this.add(dictPanel);
		this.add(geoPanel);
		
		this.pack();
	}
	
	private void do_plumbing()
	{
		saveAllButton.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					saveAll_clicked();
				}
		} );
		
		findButton.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					find_clicked();
				}
		} );
		
		addWord.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					addWord_clicked();
				}
		} );
		
		delWord.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					delWord_clicked();
				}
		} );
		
		runCust.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					runCust_clicked();
				}
		} );
		
		saveCust.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					saveCust_clicked();
				}
		} );
		
		runGeo.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					runGeo_clicked();
				}
		} );
		
		saveGeo.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					saveGeo_clicked();
				}
		} );
		
		addButton.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					add_clicked();
				}
		} );
		
		delButton.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					del_clicked();
				}
		} );	
	}
	
	private String displayResults(ArrayList<Word> wordFreqs)
	{
		String results = "";
		
		for (Word wordFreq : wordFreqs)
		{
			results = results + String.format("%s: %d\n", 
						wordFreq.getWord(), wordFreq.getFreq());
		}
		
		return results;
	}
	
	private void find_clicked()
	{
		String word = findWordArea.getText().trim();
		String lowWord = word.toLowerCase();
		int freq = 0;
		
		if (word.equals(""))
		{
			JOptionPane.showMessageDialog(null, 
				"Please enter a word.");
		}
		else
		{
			for (String text : procWords)
			{
				if (lowWord.equals(text))
				{
					freq++;
				}
			}
		
			double rate = ((double) freq / totalWords) * 100;
			String results = String.format("Word: %s\n Total appearances: %d\n Percent of text: %.6f%%", 
							word, freq, rate);
		
			wordResults.setText(results);
		}
	}
	
	private void runCust_clicked()
	{
		String results = "";

		Collections.sort(custDictionary);
		
		for (Word word : custDictionary)
		{
			results = results + String.format("%s: %d appearances\n",
							word.getWord(), word.getFreq());
		}
			
		custArea.setText(results);
	}
	
	private void saveCust_clicked()
	{
		runCust_clicked();
		
		String header = title;
		String results = "Word,Frequency\n";
		
		for (Word word : custDictionary)
		{	
			results = results + String.format("%s,%d\n",
							word.getWord(), word.getFreq());	
		}
		
		try
		{
			String outputName = "CustomTextResults.csv";
			FileOutputStream os = new FileOutputStream(outputName);
			PrintWriter pw = new PrintWriter(os);
			pw.println(header);
			pw.print(results);
			pw.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, 
				"There's a problem with the file. Please try again.");
		}
	}
	
	private void runGeo_clicked()
	{
		String results = "";
		
		Collections.sort(placeDictionary);
		
		for (Place place : placeDictionary)
		{
			results = results + String.format("%s (%.2f, %.2f): %d appearances\n",
							place.getName(), place.getLat(), place.getLong(), place.getFreq());
		}
			
		geoData.setText(results);
		
	}
	
	private void saveAll_clicked()
	{
		String header = title;
		String results = "Word,Frequency\n";
		
		for (Word wordFreq : wordFreqs)
		{
			
			results = results + String.format("%s,%d\n",
							wordFreq.getWord(), wordFreq.getFreq());	
		}
		
		try
		{
			String outputName = "AllWordTextResults.csv";
			FileOutputStream os = new FileOutputStream(outputName);
			PrintWriter pw = new PrintWriter(os);
			pw.println(header);
			pw.print(results);
			pw.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, 
				"There's a problem with the file. Please try again.");
		}
	}
	
	
	private void saveGeo_clicked()
	{
		String header = title;
		String results = "Place,Frequency,Latitude,Longitude\n";
		
		for (Place place : placeDictionary)
		{	
			results = results + String.format("%s,%d,%.6f,%.6f\n",
							place.getName(), place.getFreq(), place.getLat(), place.getLong());	
		}
		
		try
		{
			String outputName = "GeoTextResults.csv";
			FileOutputStream os = new FileOutputStream(outputName);
			PrintWriter pw = new PrintWriter(os);
			pw.println(header);
			pw.print(results);
			pw.close();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, 
				"There's a problem with the file. Please try again.");
		}
	}
	
	private void addWord_clicked()
	{	
		String word = findWordArea.getText().trim();
		String lowWord = word.toLowerCase();
		Word newWord = null;
		
		if (word.equals(""))
		{
			JOptionPane.showMessageDialog(null, "Please enter a word.");
		}
		else
		{
			newWord = new Word(lowWord);

			int count = 0;
			
			for (Word oldWord : custDictionary)
			{
				if (newWord.equals(oldWord)) count++;
			}
					
		
			if (count == 0)
			{
				custDictionary.add(newWord);
				for (String text: procWords)
				{
					if (lowWord.equals(text))
					{
						newWord.addFreq();
					}
				}
				JOptionPane.showMessageDialog(null, "Added to word bank: " + word);
				findWordArea.setText("");
				wordResults.setText("");	
			}
			else
			{
				JOptionPane.showMessageDialog(null, "That word is already in the word bank.");
			}
		}
	}
	
	private void delWord_clicked()
	{
		String word = findWordArea.getText().trim();
		String lowWord = word.toLowerCase();
		Word newWord = null;
		Word removeWord = null;
		
		if (word.equals(""))
		{
			JOptionPane.showMessageDialog(null, "Please enter a word.");
		}
		else
		{
			newWord = new Word(lowWord);
			
			for (Word oldWord : custDictionary)
			{
				if (newWord.equals(oldWord)) 
				{
					removeWord = oldWord;
				}
			}
			
			if (removeWord != null)
			{
				custDictionary.remove(removeWord);
				JOptionPane.showMessageDialog(null, "Removed from word bank: " 
									+ removeWord);
				findWordArea.setText("");
				wordResults.setText("");
			}
			else
			{
				JOptionPane.showMessageDialog(null, newWord + " is not in the word bank.");
			}
		}
	}
	
	private void add_clicked()
	{	
		String name = null;
		double latitude = 0;
		double longitude = 0;
		Place newplace = null;
		
		if (nameArea.getText().trim().equals("") || latArea.getText().equals("") || longArea.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Please enter values for all fields.");
		}
		else
		{
			try 
			{
				name = nameArea.getText().trim();
				latitude = Double.parseDouble(latArea.getText());
				longitude = Double.parseDouble(longArea.getText());
				newplace = new Place(name, latitude, longitude);
			}
			catch (NumberFormatException e)
			{
				JOptionPane.showMessageDialog(null, "Please enter numbers only for latitude and longitude.");
			}
			
			int count = 0;
			
			for (Place place : placeDictionary)
			{
				if (newplace.equals(place)) count++;
			}
			
			if (count == 0)
			{
				placeDictionary.add(newplace);
				String lowPlace = newplace.getName().toLowerCase();
				for (String text : procWords)
				{
					if (lowPlace.equals(text))
					{
						newplace.addFreq();
					}	
				}
				JOptionPane.showMessageDialog(null, "Added to dictionary: " + newplace);
				nameArea.setText("");
				latArea.setText("");
				longArea.setText("");	
			}
			else
			{
				JOptionPane.showMessageDialog(null, "That place is already in the dictionary. Note: place names must be unique.");
			}
			
			System.out.println(placeDictionary);
		}
	}
	
	private void del_clicked()
	{
		String name = null;
		double latitude = 0;
		double longitude = 0;
		Place newplace = null;
		Place removePlace = null;
		
		if (nameArea.getText().trim().equals("") || latArea.getText().equals("") || longArea.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Please enter values for all fields.");
		}
		else
		{
			try 
			{
				name = nameArea.getText().trim();
				latitude = Double.parseDouble(latArea.getText());
				longitude = Double.parseDouble(longArea.getText());
				newplace = new Place(name, latitude, longitude);
			}
			catch (NumberFormatException e)
			{
				JOptionPane.showMessageDialog(null, "Please enter numbers only for latitude and longitude.");
			}
			
			
			for (Place place : placeDictionary)
			{
				if (newplace.equals(place))
				{
					removePlace = place;
				}
			}
			
			if (removePlace != null)
			{
				placeDictionary.remove(removePlace);
				JOptionPane.showMessageDialog(null, "Removed from dictionary: " 
									+ removePlace);
				nameArea.setText("");
				latArea.setText("");
				longArea.setText("");
			}
			else
			{
			
				JOptionPane.showMessageDialog(null, newplace + "is not in the dictionary.");
			}
		}
	}
	
}
}


class Place implements Comparable<Place> {
	
	private String name;
	private double latitude;
	private double longitude;
	private int freq;
	
	public Place() 
	{	
		this(null, 0, 0);
	}
	
	public Place(String name, double latitude, double longitude) 
	{
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		freq = 0;
	}
	
	public boolean equals(Place that)
	{
		return this.name.equals(that.name);
	}
	
	public double getLat() 
	{
		return latitude;
	}
	
	public double getLong()
	{
		return longitude;
	}
	
	public int getFreq()
	{
		return freq;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return name + "," + latitude + "," + longitude;
	}
	
	public void addFreq()
	{
		freq++;
	}
	
	public int compareTo(Place that)
	{
        if  (this.freq < that.freq)  return 1;
        else if (this.freq > that.freq ) return -1;
        else return 0;
    }  
}

class Word implements Comparable<Word> {
	
	private String word;
	private int freq;
	
	public Word() 
	{	
		this(null);
	}
	
	public Word(String word) 
	{
		this.word = word;
		freq = 0;
	}
	
	public boolean equals(Word that)
	{
		return this.word.equals(that.word);
	}
	
	public int getFreq()
	{
		return freq;
	}
	
	public String getWord()
	{
		return word;
	}
	
	public String toString()
	{
		return word;
	}
	
	public void addFreq()
	{
		freq++;
	}
	
	public int compareTo(Word that)
	{
        if  (this.freq < that.freq)  return 1;
        else if (this.freq > that.freq ) return -1;
        else return 0;
    }  
}


