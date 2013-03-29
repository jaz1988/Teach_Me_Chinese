package com.fyp.your.language.friend.Library;

public class VocabTest {
	
	public long id;
	public String english,chinese,pronunciation;
	public String imagePath,audioPath,familarity;
	public int pos;
	public String alpha;
	public String choice1,choice2,choice3;
	public String selectedAnswer;
	
	public VocabTest(){
    	super();
    }
	
	public VocabTest(long id, String english, String chinese, String pronunciation, String imagePath,
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
	
	public String getChoice1() {
		return choice1;
	}
	
	public String getChoice2() {
		return choice2;
	}
	
	public String getChoice3() {
		return choice3;
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
	
	public void setChoice1(String choice1) {
		this.choice1 = choice1;
	}
	
	public void setChoice2(String choice2) {
		this.choice2 = choice2;
	}
	
	public void setChoice3(String choice3) {
		this.choice3 = choice3;
	}
	  
}
