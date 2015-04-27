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
	
	JPanel headerPanel = new JPanel();
	JLabel greeting = new JLabel("Welcome to the GeoText Analyzer!");
	
	JPanel onePanel = new JPanel();
	JLabel chooseLabel = 
		new JLabel("First, choose a text to analyze. Must be plaintext (.txt). ");
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
		
		if (year == null)
		{
			this.showMessageDialog(null, "Please enter a date value.");
		}
		else if (!f.exists())
		{
			this.showMessageDialog(null, "That file doesn't exist. Try again.");
		}
		else
		{
			DataWindow dataWindow = new DataWindow();
			dataWindow.setVisible(true);
		}
	}
	
}


