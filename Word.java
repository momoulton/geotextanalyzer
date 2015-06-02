
/**
* This class creates the Word object.
*/
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

/**
* This method overrides the Object equals() method and defines equality solely
* in terms of name.
* @param Word that word to be compared
* @return boolean true if they are equal, false if not
**/	
	public boolean equals(Word that)
	{
		return this.word.equals(that.word);
	}

/**
* This method allows returns frequency of the Word.
* @return int freq
**/		
	public int getFreq()
	{
		return freq;
	}

/**
* This method allows returns the text of the Word.
* @return String word
**/		
	public String getWord()
	{
		return word;
	}

/**
* This method allows returns the Word as a String.
* @return String word
**/	
	public String toString()
	{
		return word;
	}

/**
* This method allows users to increment the frequency.
**/	
	public void addFreq()
	{
		freq++;
	}

/**
* This method defines Word comparability in terms of frequency.
* @param Word that
* @return int 1 if greater, 0 if equal, -1 if less
**/	
	public int compareTo(Word that)
	{
        if  (this.freq < that.freq)  return 1;
        else if (this.freq > that.freq ) return -1;
        else return 0;
    }  
}
