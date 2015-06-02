// File: GeoText.java
// Mo Moulton
// CSciE10b Final Project
// TF David Habermehl
// 8 May 2015

/** 
 *  This class runs the program GeoText Analysis. It invites the user to load
 * a plain-text file, then process and analyzes that file. Users can create
 * customized dictionaries of words and place-names, then export their results
 * in comma-separated-variable format.
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

/** 
 *  This class sets up the initial window. It also contains the data-analysis
 * window code as an inner class.
**/

class Window extends JFrame { 
	
	String filename = null;
	File file = null;
	String year = null;
	String fileTitle = null;
	
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

/**
* This method does the layout of the initial window.
**/
	
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

/**
* Links the two buttons to their business logic.
**/

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

	
/**
* When the browse button is clicked, open a file browser. If the user chooses
* a file, save the file's information in the relevant variables (file, filename)
* and set up the intial window for easy entry of file's information.
**/
	private void browse_clicked()
	{
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(Window.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) //if this user chooses a file
		{
			file = fc.getSelectedFile();
			filename = fc.getSelectedFile().getName();
			fileArea.setText(filename);
			titleArea.setText(""); //reset the title & date fields
			dateArea.setText("");
		}		
	}

/**
* When the analyze button is clicked, saves the file's information as entered.
* Tests for presence and readability of file. If file can be analyzed, opens 
* analysis window.
**/
	private void analyze_clicked()
	{
		year = dateArea.getText().trim();
		fileTitle = titleArea.getText().trim();
		
		if (file == null || filename == null) //if there is no file, give warning
		{
			JOptionPane.showMessageDialog(null, "You haven't entered a file. Try again.");
		}
		else if (!file.exists()) //if file doesn't exist, give warning
		{
			JOptionPane.showMessageDialog(null, "That file doesn't exist. Try again.");
		}
		else
		{
			ArrayList<String> testWords = readData(file); //try reading data
			
			if (testWords.size() == 0) //if reading data produces no information, give warning
			{
				JOptionPane.showMessageDialog(null, "The file can't be read. Please try again.");
			}
			else //launch new analysis window
			{
				DataWindow dataWindow = new DataWindow();
				dataWindow.setVisible(true);
			}	
		}
	}

/**
* Reads in data from file, creating an ArrayList of tokens. The method uses both
* whitespace and "-" as delimiters because hyphens often serve to delimit words in
* English prose.
* @param File file the selected file.
* @return ArrayList<String> allWords containing all tokens in the file.
**/
	private ArrayList<String> readData(File file)
	{
		Scanner inFile = null;
		ArrayList<String> allWords = new ArrayList<String>();
		
		try
		{
			inFile = new Scanner(file).useDelimiter("-|\\s");
		}
		catch (IOException e) //give warning of any i/o difficulties
		{
			JOptionPane.showMessageDialog(null, "There's a problem with the file. Please try again.");
		}
		
		while (inFile.hasNext()) //add tokens to ArrayList
		{
			allWords.add(inFile.next());
		}
		
		return allWords;
	}	

/**
* This inner class launches the analysis window for a given file. Note that the
* program can launch multiple such windows per session, while only one initial
* window is launched per session.
**/
	class DataWindow extends JFrame {
	
		String title = String.format("%s (%s)", fileTitle, year); //for window header
	
		// the main analysis is called here, with results stored in variables initialized here
		// this runs the analysis once per window-creation
		ArrayList<Place> placeDictionary = new ArrayList<Place>(); //custom dictionary of places
		ArrayList<Word> custDictionary = new ArrayList<Word>(); //custom word bank
		ArrayList<String> allWords = readData(file); //creates an ArrayList of all the words
		ArrayList<String> procWords = processWords(allWords); //creates an ArrayList of processed words
		ArrayList<String> uniqueWords = getUnique(procWords); //creates an ArrayList of unique words
		int totalWords = procWords.size(); //count total words
		int uniqueWordTotal = uniqueWords.size(); //count unique words
		ArrayList<Word> wordFreqs = countWords(procWords); //creates an ArrayList of Word objects
														//storing words & frequencies
	
														//initialize window widgets
		//for the text analysis & proper nouns area
		JPanel textPanel = new JPanel();
		JLabel textLabel = new JLabel("Text Analysis");
		JLabel totalLabel = new JLabel("Total Words: ");
		JLabel totalWordsLabel = new JLabel(Integer.toString(totalWords));
		JLabel uniqueLabel = new JLabel("Unique Words: ");
		JLabel uniqueWordsLabel = new JLabel(Integer.toString(uniqueWordTotal));
		JButton properButton = new JButton("Get approximate list of proper nouns");
		JTextArea properArea = new JTextArea();
		JScrollPane properPane = new JScrollPane(properArea);
	
		//for the all-words area
		JPanel top100Panel = new JPanel();
		JLabel top100Label = new JLabel("All words by frequency");
		JButton saveAllButton = new JButton("Save as CSV File");
		JTextArea top100Area = new JTextArea();
		JScrollPane top100Pane = new JScrollPane(top100Area);
	
		//for the search & build word bank area
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
	
		//for the presentation and saving of custom word bank area
		JPanel custPanel = new JPanel();
		JLabel custLabel = new JLabel("Custom Word Bank");
		JTextArea custArea = new JTextArea();
		JScrollPane custPane = new JScrollPane(custArea);
		JButton runCust = new JButton("Run Analysis");
		JButton saveCust = new JButton("Save as CSV File");
	
		//for the presentation and saving of place name area
		JPanel geoPanel = new JPanel();
		JLabel geoLabel = new JLabel("Place Name Analysis");
		JButton runGeo = new JButton("Run Analysis");
		JTextArea geoData = new JTextArea();
		JScrollPane geoPane = new JScrollPane(geoData);
		JButton saveGeo = new JButton("Save as CSV File");
	
		//for the customization of place name dictionary area
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
	
/**
* This method does the layout of the analysis window.
**/	
		private void do_layout()
		{	
			//for the text analysis and proper-nouns area
			textPanel.setLayout(new BorderLayout());
			textPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			properArea.setEditable(false);
			JPanel totalPanel = new JPanel();
			totalPanel.setLayout(new FlowLayout());
			totalPanel.add(totalLabel);
			totalPanel.add(totalWordsLabel);
			JPanel uniquePanel = new JPanel();
			uniquePanel.setLayout(new FlowLayout());
			uniquePanel.add(uniqueLabel);
			uniquePanel.add(uniqueWordsLabel);
			JPanel figPanel = new JPanel();
			figPanel.setLayout(new GridLayout(1,2));
			figPanel.add(totalPanel);
			figPanel.add(uniquePanel);
			JPanel centerPanel = new JPanel();
			centerPanel.setLayout(new BorderLayout());
			centerPanel.add(figPanel, BorderLayout.PAGE_START);
			centerPanel.add(properPane, BorderLayout.CENTER);
			centerPanel.add(properButton, BorderLayout.PAGE_END);
			textPanel.add(textLabel, BorderLayout.PAGE_START);
			textPanel.add(centerPanel, BorderLayout.CENTER);
		
			//for the area displaying all words & frequencies
			String allResults = displayResults(wordFreqs);
			top100Panel.setLayout(new BorderLayout());
			top100Panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			top100Panel.add(top100Label, BorderLayout.PAGE_START);
			top100Area.setEditable(false);
			top100Area.setText(allResults);
			top100Panel.add(top100Pane, BorderLayout.CENTER);
			top100Panel.add(saveAllButton, BorderLayout.PAGE_END);
		
			//for the word search & word bank customization area
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
		
			//for the area displaying & saving custom word bank data
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
		
			//for the area displaying & saving custom place name dictionary data
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
		
			//for the customization of place name dictionary area
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
		
			//put it all together
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
/**
* This method connects the analysis buttons with their respective business logic.
*/
		private void do_plumbing()
		{
			properButton.addActionListener(new ActionListener() { 
					public void actionPerformed(ActionEvent e) {
						properButton_clicked();
					}
			} );
		
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
	
/**
* This method converts words to lower case and removes punctuation.
* @param ArrayList<String> allWords all the tokens from the file
* @return ArrayList<String> procWords processed tokens ready for analysis
*/
		private ArrayList<String> processWords(ArrayList<String> allWords)
		{
			String letterWord = "";
			ArrayList<String> lowWords = new ArrayList<String>();
			ArrayList<String> procWords = new ArrayList<String>();
		
			for (String word : allWords) //convert all uppercase to lowercase
			{
				String lowerWord = word.toLowerCase();
				lowWords.add(lowerWord);
			}
		
			for (String word: lowWords) //remove punctuation
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

/**
* This method produces an ArrayList of only unique words, removing duplicates.
* @param ArrayList<String> procWords the processed words (lowercase, no punctuation)
* @return ArrayList<String> uniqueWords procWords with duplicates removed
**/
		private ArrayList<String> getUnique(ArrayList<String> procWords)
		{	
			ArrayList<String> uniqueWords = new ArrayList<String>();
			for (String word : procWords) //for each word in procWords
			{
				if (!uniqueWords.contains(word)) //add only if it's not already in uniqueWords 
				{
					uniqueWords.add(word);
				}
			}
		
			return uniqueWords;
		}

/** 
* This method produces an ArrayList of Word objects, which contain the word
* itself and its frequency in the file.
* @param ArrayList<String> procWords the processed words
* @return ArrayList<Word> wordFreqs the Word objects
**/
		private ArrayList<Word> countWords(ArrayList<String> procWords)
		{
			ArrayList<Word> wordFreqs = new ArrayList<Word>();
		
			for (String word : uniqueWords) //create a new Word object for each unique word
			{
				Word wordObj = new Word(word);
				wordFreqs.add(wordObj);
			}
		
			for (String word : procWords) //count how many times the word appears
			{
				for (Word wordObj : wordFreqs)
				{
					if (word.equals(wordObj.getWord()))
					{
						wordObj.addFreq(); //add the the frequency count
					}			
				}
			}
		
			Collections.sort(wordFreqs); //sort in order of frequency
			return wordFreqs;
		}

/**
* This method produces a rough guess at a list of proper nouns in a file.
* It does this by returning capitalized words except for those that are at the 
* start of a sentence according to a rough algorithm. It prints the results to
* relevant JTextArea in the analysis window.
**/
		private void properButton_clicked()
		{
			ArrayList<String> words = allWords; //create new version of allWords that can be modified harmlessly
			ArrayList<String> capWords = new ArrayList<String>(); //capitalized words
			ArrayList<String> properWords = new ArrayList<String>(); //proper nouns
		
			for (int i = 0; i < words.size() - 1; i++)
			{
				String prevWord = words.get(i);
				String word = words.get(i+1);
			
				if (prevWord.endsWith(".|?|!")) //if a word follows this puntuation mark
				{ 								//set it to lower case
					String lowWord = word.toLowerCase();
					words.set(i+1, lowWord);
				}
			}
	
			for (String word : words) //for each word
			{
				String letterWord = "";
				for (int i = 0; i < word.length(); i++) //remove punctuation
				{
					if (Character.isLetterOrDigit(word.charAt(i)))
					{
						letterWord = letterWord + word.charAt(i);
					}
				}
			
				if (letterWord.length() > 1) //if the word starts with a capital letter
										//but is not ALLCAPS
			    {
					if (Character.isUpperCase(letterWord.charAt(0)) && !Character.isUpperCase(letterWord.charAt(1)))
					{
						capWords.add(letterWord); //add it to the capitalized words ArrayList
					}	
				}
			}
		
			for (String word : capWords) //for each capitalized word
			{
				if (!properWords.contains(word)) //add it exactly once to the the proper words ArrayList
				{
					properWords.add(word);
				}
			}
		
			String results = "";
		
			for (String word : properWords) //create attractive results format
			{
				results = results + word + "\n";
			}
		
			properArea.setText(results); //put results in window text area
		}

/**
* This method displays attractive results from an ArrayList of Word objects.
* @param ArrayList<Word> wordFreqs
* @return String results
**/
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

/**
* This method rapidly calculates the frequency of a given inputted word
* and prints the results to the window in response to a button click.
**/
		private void find_clicked()
		{
			String word = findWordArea.getText().trim(); //get the inputted word
			String lowWord = word.toLowerCase(); // set it to lower case
			int freq = 0;
		
			if (word.equals("")) //if no text was entered
			{
				JOptionPane.showMessageDialog(null, //generate warning
					"Please enter a word.");
			}
			else //count frequencies
			{
				for (String text : procWords)
				{
					if (lowWord.equals(text))
					{
						freq++;
					}
				}
		
				double rate = ((double) freq / totalWords) * 100; //calculate rate
				String results = String.format("Word: %s\n Total: %d\n Percent of text: %.6f%%", 
							word, freq, rate);
				wordResults.setText(results); //print formatted results to text area
			}
		}

/**
* This method displays the frequencies of the custom word bank words in the 
* relevant analysis window text area.
*/
		private void runCust_clicked()
		{
			String results = "";

			Collections.sort(custDictionary); //order the custom dictionary of Word objects
		
			for (Word word : custDictionary) //write results
			{
				results = results + String.format("%s: %d\n",
							word.getWord(), word.getFreq());
			}
			
			custArea.setText(results); //set text area to the results
		}

/**
* This method saves the custom word bank frequency data as a CSV file in the 
* user's working directory.
**/
		private void saveCust_clicked()
		{
			runCust_clicked(); //to ensure no discrepancy between displayed & saved data
		
			String results = "Word,Frequency,Title,Year\n"; //file's header line
		
			for (Word word : custDictionary) //results string contains each word's data
			{	
				results = results + String.format("%s,%d,%s,%s\n",
							word.getWord(), word.getFreq(), fileTitle, year);	
			}
		
			try //file output
			{
				String outputName = "CustomTextResults.csv";
				FileOutputStream os = new FileOutputStream(outputName);
				PrintWriter pw = new PrintWriter(os);
				pw.print(results);
				pw.close();
			}
			catch (IOException e) //display warning if there's an i/o problem
			{
				JOptionPane.showMessageDialog(null, 
					"There's a problem with the file. Please try again.");
			}
		}

/**
* This method displays the frequencies of each place in the custom place dictionary.
**/
		private void runGeo_clicked()
		{
			String results = "";
		
			Collections.sort(placeDictionary); //put in order of frequency
		
			for (Place place : placeDictionary) //write results for each place
			{
				results = results + String.format("%s (%.2f, %.2f): %d\n",
							place.getName(), place.getLat(), place.getLong(), place.getFreq());
			}
			
			geoData.setText(results); //set the text area equal to the results	
		}

/**
* This method saves the full list of unique words & their frequencies in a CSV file in the 
* user's working directory.
*/
		private void saveAll_clicked()
		{
			String results = "Word,Frequency,Title,Year\n"; //file header
		
			for (Word wordFreq : wordFreqs) //write results for each Word object
			{
			
				results = results + String.format("%s,%d,%s,%s\n",
							wordFreq.getWord(), wordFreq.getFreq(), fileTitle, year);	
			}
		
			try //file output
			{
				String outputName = "AllWordTextResults.csv";
				FileOutputStream os = new FileOutputStream(outputName);
				PrintWriter pw = new PrintWriter(os);
				pw.print(results);
				pw.close();
			}
			catch (IOException e) //display warning in case of i/o problem
			{
				JOptionPane.showMessageDialog(null, 
					"There's a problem with the file. Please try again.");
			}
		}
	
/**
* This method saves the place name dictionary results in a CSV file in the 
* user's working directory.
**/
		private void saveGeo_clicked()
		{
			String results = "Place,Frequency,Latitude,Longitude,Title,Year\n"; //file header
		
			for (Place place : placeDictionary) //create result line for each Place object
			{	
				results = results + String.format("%s,%d,%.6f,%.6f,%s,%s\n",
							place.getName(), place.getFreq(), place.getLat(), 
							place.getLong(), fileTitle, year);	
			}
		
			try //file output
			{
				String outputName = "GeoTextResults.csv";
				FileOutputStream os = new FileOutputStream(outputName);
				PrintWriter pw = new PrintWriter(os);
				pw.print(results);
				pw.close();
			}
			catch (IOException e) //display warning in case of i/o problem
			{
				JOptionPane.showMessageDialog(null, 
					"There's a problem with the file. Please try again.");
			}
		}

/**
* This method adds a Word to the custom word bank.
*/
		private void addWord_clicked()
		{		
			String word = findWordArea.getText().trim(); //get inputted word
			String lowWord = word.toLowerCase(); //create lowercae version
			Word newWord = null;
		
			if (word.equals("")) //if no text input, display warning
			{
				JOptionPane.showMessageDialog(null, "Please enter a word.");
			}
			else
			{
				newWord = new Word(lowWord); //create new Word object

				int count = 0;
			
				for (Word oldWord : custDictionary) //is the word in the dictionary already?
				{
					if (newWord.equals(oldWord)) count++;
				}
					
				if (count == 0) //if not, add it
				{
					custDictionary.add(newWord);
					for (String text: procWords) //and count its frequency in the file
					{                            //and add that to the Word object
						if (lowWord.equals(text))
						{
							newWord.addFreq(); 
						}
					}
					//display confirmation message & reset the entry areas
					JOptionPane.showMessageDialog(null, "Added to word bank: " + word);
					findWordArea.setText("");
					wordResults.setText("");	
				}
				else //display message instead
				{
					JOptionPane.showMessageDialog(null, "That word is already in the word bank.");
				}
			}
		}

/**
* This method deletes a word from the custom word bank.
**/
		private void delWord_clicked()
		{
			String word = findWordArea.getText().trim(); //get inputted word
			String lowWord = word.toLowerCase(); //set to lowercase
			Word newWord = null;
			Word removeWord = null;
		
			if (word.equals("")) //if no text entered, display warning
			{
				JOptionPane.showMessageDialog(null, "Please enter a word.");
			}
			else
			{
				newWord = new Word(lowWord);
			
				for (Word oldWord : custDictionary) //if word is in the dictionary
				{
					if (newWord.equals(oldWord)) 
					{
						removeWord = oldWord; //save it in removeWord variable
					}
				}
			
				if (removeWord != null) //if there's a word to remove
				{
					custDictionary.remove(removeWord); //remove it
					//display confirmation & reset text entry areas
					JOptionPane.showMessageDialog(null, "Removed from word bank: " 
									+ removeWord);
					findWordArea.setText("");
					wordResults.setText("");
				}
				else //there's no word to remove. Display message instead.
				{
					JOptionPane.showMessageDialog(null, newWord + " is not in the word bank.");
				}
			}
		}

/**
* This method adds a place to the place dictionary.
**/
		private void add_clicked()
		{	
			String name = null;
			double latitude = 0;
			double longitude = 0;
			Place newplace = null;
		
			if (nameArea.getText().trim().equals("") || latArea.getText().equals("") 
					|| longArea.getText().equals("")) //if fields are empty, display warning
			{
				JOptionPane.showMessageDialog(null, "Please enter values for all fields.");
			}
			else
			{
				try //set variable equal to relevant text entry
				{
					name = nameArea.getText().trim();
					latitude = Double.parseDouble(latArea.getText());
					longitude = Double.parseDouble(longArea.getText());
					newplace = new Place(name, latitude, longitude);
				}
				catch (NumberFormatException e) //catch if non-numbers entered for lat & long
				{
					JOptionPane.showMessageDialog(null, "Please enter numbers only for latitude and longitude.");
				}
			
				int count = 0; 
			
				for (Place place : placeDictionary) //is the place already in the dictionary?
				{
					if (newplace.equals(place)) count++;
				}
			
				if (count == 0) //if not, add it
				{
					placeDictionary.add(newplace);
					String lowPlace = newplace.getName().toLowerCase();
					for (String text : procWords) //count frequencies & add them to Place object
					{
						if (lowPlace.equals(text))
						{
							newplace.addFreq();
						}	
					}
					//display confirmation message & reset text entry areas
					JOptionPane.showMessageDialog(null, "Added to dictionary: " + newplace);
					nameArea.setText("");
					latArea.setText("");
					longArea.setText("");	
				}
				else //display message instead if place is already in dictionary
				{
					JOptionPane.showMessageDialog(null, "That place is already in the dictionary.");
				}
			}
		}

/**
* This method removes a place from the place dictionary. 
**/
		private void del_clicked()
		{
			String name = null;
			double latitude = 0; //this is just a placeholder; name is only relevant field here
			double longitude = 0; //user entry in lat & long areas will be ignored in this method
			Place newplace = null;
			Place removePlace = null;
		
			if (nameArea.getText().trim().equals("")) //if no text entered, display warning
			{
				JOptionPane.showMessageDialog(null, "Please enter a place name.");
			}
			else
			{
				name = nameArea.getText().trim();
				newplace = new Place(name, latitude, longitude);
			
				for (Place place : placeDictionary)
				{
					if (newplace.equals(place)) //if place is in dictionary
					{
						removePlace = place; //set removePlace variable equal to it
					}
				}
			
				if (removePlace != null) //if there's a place to remove
				{
					placeDictionary.remove(removePlace); //remove it
				//display confirmation message & reset text entry areas
					JOptionPane.showMessageDialog(null, "Removed from dictionary: " 
									+ removePlace);
					nameArea.setText("");
					latArea.setText("");
					longArea.setText("");
				}
				else //display message instead
				{
					JOptionPane.showMessageDialog(null, newplace + "is not in the dictionary.");
				}
			}
		}	
	}
}




