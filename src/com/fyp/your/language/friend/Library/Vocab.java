package com.fyp.your.language.friend.Library;

import java.util.ArrayList;
import java.util.List;

public class Vocab {
	
	public long id;
	public String english,chinese,pronunciation;
	public String imagePath,audioPath,familarity;
	public int pos;
	public String alpha;
	public List<String> choices;
	public String selectedAnswer;
	public String score;
	public int total;
	public int percent;
	public String last_selected;
	public String date;
	public int score_int;
	public int attempt;
	
	
	public Vocab(){
    	super();
    	this.choices = new ArrayList<String>();
    }
	
	public Vocab(long id, String english, String chinese, String pronunciation, String imagePath,
			String audioPath, String familarity ){
    	super();
    	this.id = id;
    	this.english = english;
    	this.chinese = chinese;
    	this.pronunciation = pronunciation;
    	this.imagePath = imagePath;
    	this.audioPath = audioPath;
    	this.familarity = familarity;
    	
    	
    }
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEnglish() {
		return english;
	}
	
	public String getChinese() {
		return chinese;
	}
	
	public String getPronunciation() {
		return pronunciation;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	public String getAudioPath() {
		return audioPath;
	}

	public String getFamilarity() {
		return audioPath;
	}
	
	public int getPos() {
		return pos;
	}
	
	public String getAlpha() {
		return alpha;
	}
	
	public List<String> getChoices() {
		return choices;
	}
	
	
	public void setEnglish(String english) {
		this.english = english;
	}
	
	public void setChinese(String chinese) {
		this.chinese = chinese;
	}
	
	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}
	
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public void setFamilarity(String familarity) {
		this.familarity = familarity;
	}
	
	public void setPronunciation(String pronunciation) {
		this.pronunciation = pronunciation;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}
	
	public void addChoice(String choice)
	{
		this.choices.add(choice);
	}
	
	public void clearChoices()
	{
		this.choices.clear();
	}
	
	  
}
