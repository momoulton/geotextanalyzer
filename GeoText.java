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
	ArrayList<String> allWords = new ArrayList<String>();
	ArrayList<String> procWords = new ArrayList<String>();
	ArrayList<String> uniqueWords = new ArrayList<String>();
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
	JLabel dateLabel = new JLabel
		("Second, enter the file's date of publication or creation. ");
	JTextArea yearArea = new JTextArea();
	
	JPanel threePanel = new JPanel();
	JLabel analyzeLabel = new JLabel("Finally, analyze! ");
	JButton analyzeButton = new JButton("Analyze");
	
	
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
		this.setLayout(new GridLayout(5,1));
		
		greeting.setFont(new Font("Serif", Font.BOLD, 28));
		headerPanel.setLayout(new FlowLayout());
		headerPanel.add(greeting);
		
		onePanel.setLayout(new BorderLayout());
		onePanel.add(chooseLabel, BorderLayout.LINE_START);
		onePanel.add(browseButton, BorderLayout.CENTER);
		
		filePanel.setLayout(new BorderLayout());
		filePanel.add(fileLabel, BorderLayout.LINE_START);
		filePanel.add(fileArea, BorderLayout.CENTER);
		
		twoPanel.setLayout(new BorderLayout());
		twoPanel.add(dateLabel, BorderLayout.LINE_START);
		twoPanel.add(yearArea, BorderLayout.CENTER);
		
		threePanel.setLayout(new BorderLayout());
		threePanel.add(analyzeLabel, BorderLayout.LINE_START);
		threePanel.add(analyzeButton, BorderLayout.CENTER);
		
		this.add(headerPanel);
		this.add(onePanel);
		this.add(filePanel);
		this.add(twoPanel);
		this.add(threePanel);
		
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
		//FileNameExtensionFilter filter = new FileNameExtensionFilter("txt");
		//fc.addChoosableFileFilter(filter);
		//maybe add in filter later
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
		year = yearArea.getText();
		
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
			inFile = new Scanner(file);
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

	
class DataWindow extends JFrame {
	
	String title = "File: " + filename + "   Date: " + year;
	PlaceDictionary<Place> placeDictionary = new PlaceDictionary<Place>();	
	
	JPanel textPanel = new JPanel();
	JLabel textLabel = new JLabel("Text Analysis");
	JLabel totalLabel = new JLabel("Total Words: ");
	JLabel totalWordsLabel = new JLabel(Integer.toString(totalWords));
	JLabel uniqueLabel = new JLabel("Unique Words: ");
	JLabel uniqueWordsLabel = new JLabel(Integer.toString(uniqueWordTotal));
	
	JPanel freqPanel = new JPanel();
	JButton findButton = new JButton("Find Frequency");
	JLabel freqLabel = new JLabel("Word Frequencies");
	JLabel findWordLabel = new JLabel("Word: ");
	JTextArea findWordArea = new JTextArea();
	JTextArea wordResults = new JTextArea("Results will go here");
	JPanel wordFind = new JPanel();
	
	JPanel geoPanel = new JPanel();
	JLabel geoLabel = new JLabel("Place Name Analysis");
	JButton runGeo = new JButton("Run Analysis");
	JTextArea geoData = new JTextArea("Results will go here");
	JButton saveGeo = new JButton("Save As Comma-Separated-Variable (CSV) File");
	
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
		//readData(file);
	}
	
	private void do_layout()
	{	
		textPanel.setLayout(new BorderLayout());
		textPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		textPanel.add(textLabel, BorderLayout.PAGE_START);
		JPanel totalPanel = new JPanel();
		totalPanel.setLayout(new FlowLayout());
		totalPanel.add(totalLabel);
		totalPanel.add(totalWordsLabel);
		JPanel uniquePanel = new JPanel();
		uniquePanel.setLayout(new FlowLayout());
		uniquePanel.add(uniqueLabel);
		uniquePanel.add(uniqueWordsLabel);
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new GridLayout(0,2));
		dataPanel.add(totalPanel);
		dataPanel.add(uniquePanel);
		textPanel.add(dataPanel, BorderLayout.CENTER);
		
		wordFind.setLayout(new GridLayout(1,3));
		wordFind.add(findWordLabel);
		wordFind.add(findWordArea);
		wordFind.add(findButton);
		
		freqPanel.setLayout(new BorderLayout());
		freqPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		freqPanel.add(freqLabel, BorderLayout.PAGE_START);
		freqPanel.add(wordFind, BorderLayout.CENTER);
		freqPanel.add(wordResults, BorderLayout.PAGE_END);
		
		geoPanel.setLayout(new BorderLayout());
		geoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		geoPanel.add(geoLabel, BorderLayout.PAGE_START);
		geoPanel.add(runGeo, BorderLayout.LINE_END);
		geoPanel.add(geoData, BorderLayout.CENTER);
		geoPanel.add(saveGeo, BorderLayout.PAGE_END);
		
		dictPanel.setLayout(new BorderLayout());
		dictPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JPanel enterAddData = new JPanel();
		enterAddData.setLayout(new GridLayout(4,2));
		enterAddData.add(nameField);
		enterAddData.add(nameArea);
		enterAddData.add(latField);
		enterAddData.add(latArea);
		enterAddData.add(longField);
		enterAddData.add(longArea);
		enterAddData.add(addButton);
		enterAddData.add(delButton);
		dictPanel.add(dictLabel, BorderLayout.PAGE_START);
		dictPanel.add(enterAddData, BorderLayout.CENTER);
		
		this.setTitle(title);
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setLayout(new GridLayout(2,2));
		
		this.add(textPanel);
		this.add(freqPanel);
		this.add(geoPanel);
		this.add(dictPanel);
		
	}
	
	private void do_plumbing()
	{
		findButton.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					find_clicked();
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
	
	private void find_clicked()
	{
		System.out.println("Find Frequency Clicked");
	}
	
	private void runGeo_clicked()
	{
		System.out.println("Run GeoAnalysis Clicked");
	}
	
	private void saveGeo_clicked()
	{
		System.out.println("Save GeoAnalysis Clicked");
	}
	
	private void add_clicked()
	{	
		String name = null;
		double latitude = 0;
		double longitude = 0;
		Place newplace = null;
		
		if (nameArea.getText().equals("") || latArea.getText().equals("") || longArea.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Please enter values for all fields.");
		}
		else
		{
			try 
			{
				name = nameArea.getText();
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
		
		if (nameArea.getText().equals("") || latArea.getText().equals("") || longArea.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Please enter values for all fields.");
		}
		else
		{
			try 
			{
				name = nameArea.getText();
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
				if (newplace.equals(place))
				{
					count++;
					placeDictionary.remove(place);
					JOptionPane.showMessageDialog(null, "Removed dictionary: " + place);
					nameArea.setText("");
					latArea.setText("");
					longArea.setText("");
				}
			}
			
			if (count == 0)
			{
			
				JOptionPane.showMessageDialog(null, "That place is not in the dictionary.");
			}
			
			System.out.println(placeDictionary);
		}
	}
	
}
}


class Place {
	
	private String name;
	private double latitude;
	private double longitude;
	
	public Place() 
	{	
		this(null, 0, 0);
	}
	
	public Place(String name, double latitude, double longitude) 
	{
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
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
	
	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return name + "," + latitude + "," + longitude;
	}
}

class PlaceDictionary<E> extends ArrayList<E> {
	
	public PlaceDictionary()
	{
		super();
	}					
}


