package com.fyp.your.language.friend.Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fyp.your.language.friend.GlobalVariables;
import com.fyp.your.language.friend.StartActivity;
import com.fyp.your.language.friend.Library.Vocab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class VocabDataSource {
	
	// Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = {MySQLiteHelper.COLUMN_SUBJECT, MySQLiteHelper.COLUMN_ID,
	      MySQLiteHelper.COLUMN_CHINESE, MySQLiteHelper.COLUMN_ENGLISH, MySQLiteHelper.COLUMN_PRONUNCIATION
	      , MySQLiteHelper.COLUMN_IMAGE, MySQLiteHelper.COLUMN_AUDIO, MySQLiteHelper.COLUMN_FAMILARITY};
	  
	  private String[] allColumnsGrammar = { MySQLiteHelper.COLUMN_SUBJECT, MySQLiteHelper.COLUMN_ID,
		      MySQLiteHelper.COLUMN_CHINESE, MySQLiteHelper.COLUMN_ENGLISH, MySQLiteHelper.COLUMN_PRONUNCIATION
		      ,MySQLiteHelper.COLUMN_AUDIO};


	  public VocabDataSource(Context context) {

		  dbHelper = MySQLiteHelper.getInstance(context);
	    Log.d("database", "after vocabdatasource");
	  }

	  public void open() throws SQLException {
		  try
	    {
			
			  database = dbHelper.getWritableDatabase();
			  
			  Log.d("database", "database opened from getwritabledatabase after");
			  
			 
	    }
		  catch(Exception e)
		  {
			  Log.d("database", e.toString());
		  }
		
	  }
	  

	  
	  public void create() throws SQLException {
	   
	    try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }

	  public void close() {
	    dbHelper.close();
	  }
	  
	  
	  public void updateColumns() {
		  
			Cursor cursor;
			//SQLiteDatabase result = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
			  cursor = database.rawQuery("SELECT * from vocab_test", null); 
			  int check = cursor.getColumnIndex("total");
			  if(check == -1)
			  {
				  //Does not exist
				  Log.d("database", "column total does not exist");	
				  database.execSQL("PRAGMA user_version = " + MySQLiteHelper.DATABASE_VERSION);
				  database.execSQL("ALTER TABLE vocab_test ADD last_selected TEXT");
				  database.execSQL("ALTER TABLE vocab_test ADD total NUMERIC");
				  database.execSQL("ALTER TABLE vocab_test ADD percent NUMERIC");
				  database.execSQL("ALTER TABLE vocab_test ADD score TEXT");
				  database.execSQL("ALTER TABLE vocab_test ADD score_int NUMERIC");			  
			  }
			 
			  else
			  {
				  //Exist
			  }	   
		  }
	  
	  public void createTestInfoTable()
	  {
		  String sql = "CREATE TABLE test_info (_id integer primary key autoincrement,  subject text, date text, attempt numeric, score text)";
		  database.execSQL(sql);
	  }
	  
	  public void insertVocabTestData()
	  {
		  createVocabTest("5: 餐厅在哪儿","餐厅", "dining room","dormitory","toilet","hotel");
		  createVocabTest("5: 餐厅在哪儿","哪儿", "where", "this", "that","what");
		  createVocabTest("5: 餐厅在哪儿","请问", "May I ask…?", "to ask", "this", "where");
		  createVocabTest("5: 餐厅在哪儿","问", "to ask", "May I ask…?", "this", "where");
		  createVocabTest("5: 餐厅在哪儿","这", "this", "May I ask…?", "that", "where");
		  createVocabTest("5: 餐厅在哪儿","宿舍", "dormitory", "hotel", "kitchen", "dining room");
		  createVocabTest("5: 餐厅在哪儿","女", "female", "male", "mother", "father");
		  createVocabTest("5: 餐厅在哪儿","学生", "student", "teacher", "lawyer", "doctor");
		  createVocabTest("5: 餐厅在哪儿","进", "to enter", "to exit", "to jump", "to run");
		  createVocabTest("5: 餐厅在哪儿","坐", "sit", "stand", "jump", "run");
		  createVocabTest("5: 餐厅在哪儿","谢谢", "to thank", "to apologize", "to cry", "to laugh");
		  createVocabTest("5: 餐厅在哪儿","对不起", "I am sorry", "to thank", "to cry", "to laugh");
		  createVocabTest("5: 餐厅在哪儿","知道", "to know", "do not know", "to cry", "to laugh");
		  createVocabTest("5: 餐厅在哪儿","没关系", "never mind / it doesn't matter", "I do mind", "I do not like it", "it puzzles me");
		  createVocabTest("5: 餐厅在哪儿","再见", "good-bye", "hello", "welcome", "how are you");
		  createVocabTest("5: 餐厅在哪儿","小姐", "Miss / young lady", "Mr / young man", "old man", "young boy");
		  createVocabTest("5: 餐厅在哪儿","二", "two", "three", "four", "five");
		  createVocabTest("5: 餐厅在哪儿","层", "storey / floor", "number", "amount", "quantity");
		  createVocabTest("5: 餐厅在哪儿","零", "zero", "one", "two", "three");
		  createVocabTest("5: 餐厅在哪儿","四", "four", "one", "two", "three");
		  createVocabTest("5: 餐厅在哪儿","号", "number", "storeys", "amount", "quantity");
		  createVocabTest("5: 餐厅在哪儿","不用", "need not", "need", "might have", "might not have");
		  createVocabTest("5: 餐厅在哪儿","这儿", "here", "there", "that", "those");
		  createVocabTest("5: 餐厅在哪儿","晚", "late", "early", "punctual", "morning");
		  createVocabTest("6: 我们去游泳","去", "to go", "to come", "to jump", "to run");
		  createVocabTest("6: 我们去游泳","游泳", "swim", "jog", "climb", "cycle");
		  createVocabTest("6: 我们去游泳","昨天", "yesterday", "today", "the day after tomorrow", "two days later");
		  createVocabTest("6: 我们去游泳","京剧", "Beijing opera", "musical", "movie", "concert");
		  createVocabTest("6: 我们去游泳","怎么样", "how is it?", "is that right?", "do you agree?", "am I right?");
		  createVocabTest("6: 我们去游泳","有意思", "interesting", "exciting", "boring", "surprising");
		  createVocabTest("6: 我们去游泳","今天", "today", "yesterday", "fortnight", "day after tomorrow");
		  createVocabTest("6: 我们去游泳","天气", "weather", "temperature", "night", "day");
		  createVocabTest("6: 我们去游泳","太", "too / extremely", "little / minimal", "just right", "understated");
		  createVocabTest("6: 我们去游泳","什么", "what", "where", "who", "which");
		  createVocabTest("6: 我们去游泳","时候", "time / moment", "temperature / degree", "weather", "conditions");
		  createVocabTest("6: 我们去游泳","现在", "now", "later", "previously", "tomorrow");
		  createVocabTest("6: 我们去游泳","明天", "tomorrow", "later", "previously", "now");
		  createVocabTest("6: 我们去游泳","时间", "time", "temperature", "weather", "conditions");
		  createVocabTest("6: 我们去游泳","说", "to say / to speak", "to not say / to not speak", "to shout", "to scold");
		  createVocabTest("6: 我们去游泳","打球", "to play ball", "to swim", "to jog", "to cycle");
		  createVocabTest("6: 我们去游泳","球", "ball", "racket", "helmet", "bike");
		  createVocabTest("6: 我们去游泳","抱歉", "to feel sorry / sorry", "to be thankful", "to be ungrateful", "to be honest");
		  createVocabTest("6: 我们去游泳","忙", "busy", "free", "ok", "not ok");
		  createVocabTest("7: 你认识不认识他","开学","to start school","to end school","to study","to open");
		  createVocabTest("7: 你认识不认识他","开","to open","to close","to ask","to watch");
		  createVocabTest("7: 你认识不认识他","很","very","a bit","little","too");
		  createVocabTest("7: 你认识不认识他","高兴","happy","sad","angry","confused");
		  createVocabTest("7: 你认识不认识他","高","high","short","low","far");
		  createVocabTest("7: 你认识不认识他","看","to watch","to eat","to stare","to open");
		  createVocabTest("7: 你认识不认识他","问","to ask","to think","to answer","to know");
		  createVocabTest("7: 你认识不认识他","学院","institute","park","garden","court");
		  createVocabTest("7: 你认识不认识他","名片","calling card","gift card","telephone card","to call");
		  createVocabTest("7: 你认识不认识他","教授","professor","student","researcher","teacher");
		  createVocabTest("7: 你认识不认识他","谁","who","what","where","when");
		  createVocabTest("7: 你认识不认识他","来","to come","to go","to enter","to exit");
		  createVocabTest("7: 你认识不认识他","介绍","to introduce","to know","to understand","to call");
		  createVocabTest("7: 你认识不认识他","名字","name","age","gender","");
		  createVocabTest("7: 你认识不认识他","中文","Chinese","English","Japanese","French");
		  createVocabTest("7: 你认识不认识他","爸爸","dad","mum","grandfather","grandmother");
		  createVocabTest("7: 你认识不认识他","学习","to study","to understand","to know","to read");
		  createVocabTest("7: 你认识不认识他","学","to learn","to read","to speak","to understand");
		  createVocabTest("7: 你认识不认识他","专业","major","minor","profession","career");
		  createVocabTest("7: 你认识不认识他","美术","fine arts","physics","mathematics","philosophy");
		  createVocabTest("7: 你认识不认识他","美","beautiful","ugly","happy","cherish");
		  createVocabTest("7: 你认识不认识他","文学","literature","english","drama","movies");
		  createVocabTest("7: 你认识不认识他","系","faculty","institution","group","class");
		  createVocabTest("7: 你认识不认识他","加拿大","Canada","China","America","Britain");
		  createVocabTest("7: 你认识不认识他","美国","the United States","Canada","Mexico","Argentina");
		  createVocabTest("7: 你认识不认识他","文化","culture","language","history","politics");
		  createVocabTest("7: 你认识不认识他","历史","history","culture","language","story");
		  createVocabTest("7: 你认识不认识他","哲学","philosophy","politics","sicology","history");
		  createVocabTest("7: 你认识不认识他","音乐","music","drama","dance","performance");
		  createVocabTest("7: 你认识不认识他","经济","economy","business","politics","money");
		  createVocabTest("7: 你认识不认识他","数学","mathematics","english","language","physics");
		  createVocabTest("7: 你认识不认识他","物理","physics","mathematics","chemistry","statistics");
		  createVocabTest("7: 你认识不认识他","化学","chemistry","physics","mathematics","statistics");
		  createVocabTest("7: 你认识不认识他","教育","education","teaching","learning","lecturing");
		  createVocabTest("7: 你认识不认识他","选修","to take an elective course","to take a main course","to major","to choose");
		  createVocabTest("8: 你们家有几口人","家","family","group","house","parents");
		  createVocabTest("8: 你们家有几口人","几","how many","how long","how tall","how expensive");
		  createVocabTest("8: 你们家有几口人","照片","picture","painting","camera","photographer");
		  createVocabTest("8: 你们家有几口人","和","and","without","as well","too");
		  createVocabTest("8: 你们家有几口人","姐姐","elder sister","elder brother","younger sister","younger brother");
		  createVocabTest("8: 你们家有几口人","两","two","one","three","four");
		  createVocabTest("8: 你们家有几口人","弟弟","younger brother","elder brother","younger sister","elder sister");
		  createVocabTest("8: 你们家有几口人","还","in addition","too","as well","plus");
		  createVocabTest("8: 你们家有几口人","一共","altogether","together","one","plus");
		  createVocabTest("8: 你们家有几口人","妹妹","younger sister","elder sister","younger brother","elder brother");
		  createVocabTest("8: 你们家有几口人","小","little","big","huge","cute");
		  createVocabTest("8: 你们家有几口人","狗","dog","cat","mouse","bird");
		  createVocabTest("8: 你们家有几口人","当然","as it should be","should","altogether","together");
		  createVocabTest("8: 你们家有几口人","真","real/really","fake","in reality","fact");
		  createVocabTest("8: 你们家有几口人","可爱","lovely","happily","pretty","nice");
		  createVocabTest("8: 你们家有几口人","爱","to love","to hate","to know","to feel");
		  createVocabTest("8: 你们家有几口人","没","not","yes","have","nobody");
		  createVocabTest("8: 你们家有几口人","男","male","female","boy","girl");
		  createVocabTest("8: 你们家有几口人","做","to do","to know","to eat","to perform");
		  createVocabTest("8: 你们家有几口人","工作","to work","to study","to learn","to earn");
		  createVocabTest("8: 你们家有几口人","大","big","small","tiny","short");
		  createVocabTest("8: 你们家有几口人","多少","how many","how long","when","what");
		  createVocabTest("8: 你们家有几口人","多","many","little","less","few");
		  createVocabTest("8: 你们家有几口人","少","less","many","a lot","short");
		  createVocabTest("8: 你们家有几口人","喜欢","to like","to hate","to know","to love");
		  createVocabTest("8: 你们家有几口人","外语","foreign language","mother tongue","language","Chinese");
		  createVocabTest("8: 你们家有几口人","外","outside","inside","enter","exit");
		  createVocabTest("8: 你们家有几口人","外国","foreign country","home country","country","China");
		  createVocabTest("8: 你们家有几口人","百","hundred","thousand","million","billion");
		  createVocabTest("8: 你们家有几口人","车"," vehicle","boat","aeroplane","bicycle");
		  createVocabTest("8: 你们家有几口人","词典","dictionary","book","story book","encyclopedia");
		  createVocabTest("8: 你们家有几口人","电脑","computer","calculator","handphone","tablet");
		  createVocabTest("8: 你们家有几口人","孩子","child","elderly","adult","youngster");
		  createVocabTest("8: 你们家有几口人","爷爷","grandfather on the father's side","grandmother on the father's side","grandmother on the father's side","grandfather on the mother's side");
		  createVocabTest("8: 你们家有几口人","外公","grandfather on the mother's side","grandfather on the father's side","grandmother on the mother's side","grandmother on the father's side");
		  createVocabTest("8: 你们家有几口人","系主任","chairman of the department","chairman","department","professor");
		  createVocabTest("8: 你们家有几口人","助教","teaching assistance","teacher","professor","student");
		  createVocabTest("8: 你们家有几口人","律师","lawyer","teacher","reporter","pilot");
		  createVocabTest("8: 你们家有几口人","工程师","engineer","lawyer","teacher","scentist");




	  }
	  
	  public void createVocab(String chinese, String english, String pronunciation, String imagePath, String audioPath) {
		  
		Log.d("Vocab", chinese + "," + english + "," + pronunciation + "," + imagePath + "," + audioPath);
		
	    ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.COLUMN_CHINESE, chinese);
	    values.put(MySQLiteHelper.COLUMN_ENGLISH, english);
	    values.put(MySQLiteHelper.COLUMN_PRONUNCIATION, pronunciation);
	    values.put(MySQLiteHelper.COLUMN_IMAGE, imagePath);
	    values.put(MySQLiteHelper.COLUMN_AUDIO, audioPath);
	    values.put(MySQLiteHelper.COLUMN_FAMILARITY, "red");
	    
	    long insertId = database.insert(MySQLiteHelper.TABLE_VOCAB, null,
	        values);

	   
	  }
	  
	  public void createVocabTest(String subject, String word, String answer, String choice1, String choice2, String choice3) {
		  	
		    ContentValues values = new ContentValues();
		    values.put("subject", subject);
		    values.put("chinese", word);
		    values.put("english", answer);
		    values.put("choice1", choice1);
		    values.put("choice2", choice2);
		    values.put("choice3", choice3);
		    
		    long insertId = database.insert("vocab_test", null,
		        values);
		    //Log.d("insert test", Long.toString(insertId));
		   
		  }
	  
	  public void insertTestInfo(String date, int attempt, String score, String subject) {
		  	
		    ContentValues values = new ContentValues();
		    values.put("date", date);
		    values.put("attempt", attempt);
		    values.put("score", score);
		    values.put("subject", subject);
		    
		    long insertId = database.insert("test_info", null,
		        values);
		    //Log.d("insert test_info", Long.toString(insertId));
		   
		  }
	  
	  public Vocab createGrammar(String chinese, String english, String pronunciation, String audioPath) {
		  
			//Log.d("Grammar", chinese + "," + english + "," + pronunciation + "," + audioPath);
			
		    ContentValues values = new ContentValues();
		    values.put(MySQLiteHelper.COLUMN_CHINESE, chinese);
		    values.put(MySQLiteHelper.COLUMN_ENGLISH, english);
		    values.put(MySQLiteHelper.COLUMN_PRONUNCIATION, pronunciation);
		    values.put(MySQLiteHelper.COLUMN_AUDIO, audioPath);
		    
		    long insertId = database.insert(MySQLiteHelper.TABLE_GRAMMAR, null,
		        values);
		    Cursor cursor = database.query(MySQLiteHelper.TABLE_GRAMMAR,
		    		allColumnsGrammar, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
		        null, null, null);
		    cursor.moveToFirst();
		    Vocab newVocab = cursorToGrammar(cursor);
		    cursor.close();
		    return newVocab;
		  }

	  public void deleteVocab(Vocab vocab) {
	    long id = vocab.getId();
	    System.out.println("Vocab deleted with id: " + id);
	    database.delete(MySQLiteHelper.TABLE_VOCAB, MySQLiteHelper.COLUMN_ID
	        + " = " + id, null);
	  }

	  public Vocab getVocab(String word)
	  {
		  //Log.d("getVocabzz", word);
		  Vocab vocabs = new Vocab();
		  Cursor cursor;
		  cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.TABLE_VOCAB + " WHERE " + MySQLiteHelper.COLUMN_CHINESE + " = " + "'" +  word + "'", null);
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
		      vocabs = cursorToVocab(cursor);
		      cursor.moveToNext();
		      //Log.d("getVocabzz", vocabs.getEnglish());
		    }
		 
		  // Make sure to close the cursor
		  cursor.close();
		  return vocabs;
	  }
	  
	  public List<Vocab> getVocabBySubject(String word)
	  {
		  //Log.d("getVocabBySubject", word);
		  List<Vocab> vocabs = new ArrayList<Vocab>();
		  Cursor cursor;
		  cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.TABLE_VOCAB + " WHERE subject = " + "'" +  word + "'", null);
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  Vocab vocab = cursorToVocabSubject(cursor);
		      
		      vocabs.add(vocab);
		      cursor.moveToNext();
		    }
		 
		  // Make sure to close the cursor
		  cursor.close();
		    
		  return vocabs;
	  }
	  
	  public List<Vocab> getVocabTestBySubject(String word, String order)
	  {
		  //Log.d("getVocabBySubjectTEST", word);
		  List<Vocab> vocabs = new ArrayList<Vocab>();
		  Cursor cursor;
		  cursor = database.rawQuery("SELECT * FROM vocab_test" + " WHERE subject = " + "'" +  word + "' ORDER BY percent " + order, null);
		  //Log.d("getVocabBySubjectcountTEST", Integer.toString(cursor.getCount()));
		  cursor.getCount();
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  Vocab vocab = cursorToVocabTestSubject(cursor);
		      
		      vocabs.add(vocab);
		      cursor.moveToNext();
		    }
		 
		  // Make sure to close the cursor
		  cursor.close();
		  return vocabs;
	  }
	  
	  public List<Vocab> getGrammarBySubject(String word)
	  {
		  Log.d("getVocabBySubject", word);
		  List<Vocab> vocabs = new ArrayList<Vocab>();
		  Cursor cursor;
		  cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.TABLE_GRAMMAR + " WHERE subject = " + "'" +  word + "'", null);
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  Vocab vocab = cursorToGrammarSubject(cursor);
		      
		      vocabs.add(vocab);
		      cursor.moveToNext();
		    }
		 
		  // Make sure to close the cursor
		  cursor.close();
		    
		  return vocabs;
	  }
	  
	  public List<String> getSubjects()
	  {
		  Log.d("getVocabBySubject", "subjects");
		  List<String> vocabs = new ArrayList<String>();
		  Cursor cursor;
		  cursor = database.rawQuery("SELECT DISTINCT subject FROM " + MySQLiteHelper.TABLE_VOCAB, null);
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
//			  Vocab vocab = cursorToVocab(cursor);
//		      
		      vocabs.add(cursor.getString(0));
			  Log.d("getVocabBySubject",cursor.getString(0));
		      cursor.moveToNext();
		    }
		 
		  // Make sure to close the cursor
		  cursor.close();
		  return vocabs;
	  }
	  
	  public List<String> getSubjectsTest()
	  {
		  Log.d("getVocabBySubjectTest", "subjects");
		  List<String> vocabs = new ArrayList<String>();
		  Cursor cursor;
		  cursor = database.rawQuery("SELECT DISTINCT subject FROM vocab_test", null);
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
//			  Vocab vocab = cursorToVocab(cursor);
//		      
		      vocabs.add(cursor.getString(0));
			  Log.d("getSubjectsTest",cursor.getString(0));
		      cursor.moveToNext();
		      
		    }
		 
		  // Make sure to close the cursor
		  cursor.close();
		  return vocabs;
	  }
	  
	  public List<Vocab> getProgress()
	  {
		  List<Vocab> vocabs = new ArrayList<Vocab>();
		  Cursor cursor;
		  cursor = database.rawQuery("SELECT * FROM subjects", null);
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  Vocab vocab = cursorToVocabProgress(cursor);
		      
		      vocabs.add(vocab);
		      cursor.moveToNext();
		    }
		 
		  // Make sure to close the cursor
		  cursor.close();
		  return vocabs;
	  }
	  
	  public int getVocabTestInfoCount(String subject)
	  {
		 
		  int count = 0;
		  Cursor cursor;
		  cursor = database.rawQuery("SELECT count(*) FROM test_info GROUP BY subject HAVING subject='" + subject + "'", null);
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  count = cursor.getInt(0);
		      cursor.moveToNext();
		    }
		 
		  // Make sure to close the cursor
		  cursor.close();
		  return count;
	  }
	  
	  public int getVocabTotalCount(String chinese)
	  {
		 
		  int count = 0;
		  Cursor cursor;
		  cursor = database.rawQuery("SELECT total FROM vocab_test WHERE chinese='" + chinese + "'", null);
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  count = cursor.getInt(0);
		      cursor.moveToNext();
		    }
		 
		  // Make sure to close the cursor
		  cursor.close();
		  return count;
	  }
	  
	  public int getVocabTotalScoreIntCount(String chinese)
	  {
		 
		  int count = 0;
		  Cursor cursor;
		  cursor = database.rawQuery("SELECT score_int FROM vocab_test WHERE chinese='" + chinese + "'", null);
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  count = cursor.getInt(0);
		      cursor.moveToNext();
		    }
		 
		  // Make sure to close the cursor
		  cursor.close();
		  return count;
	  }
	  
	  public List<Vocab> getTestInfo(String subject)
	  {
		  Log.d("getTestInfo", subject);
		  List<Vocab> vocabs = new ArrayList<Vocab>();
		  Cursor cursor;
		  cursor = database.rawQuery("SELECT * FROM test_info" + " WHERE subject = " + "'" +  subject + "'", null);
		  Log.d("getVocabBySubjectcountTEST", Integer.toString(cursor.getCount()));
		  cursor.getCount();
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  Vocab vocab = cursorToTestInfo(cursor);
		      
		      vocabs.add(vocab);
		      cursor.moveToNext();
		    }
		 
		  // Make sure to close the cursor
		  cursor.close();

		  return vocabs;
	  }
	  
	  
	  
	  public List<Vocab> getAllVocab() {
	    Log.d("Vocab","IN");
	    List<Vocab> vocabs = new ArrayList<Vocab>();

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_VOCAB,
	        allColumns, null, null, null, null, null);

	  
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Vocab vocab = cursorToVocab(cursor);
	      
	      vocabs.add(vocab);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();

	    return vocabs;
	  }
	  
	  public List<Vocab> getAllGrammar() {
		    Log.d("Vocab","IN");
		    List<Vocab> vocabs = new ArrayList<Vocab>();

		    Cursor cursor = database.query(MySQLiteHelper.TABLE_GRAMMAR,
		    		allColumnsGrammar, null, null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		      Vocab vocab = cursorToGrammar(cursor);
		      vocabs.add(vocab);
		      cursor.moveToNext();
		    }
		    // Make sure to close the cursor
		    cursor.close();
		   
		    return vocabs;
		  }

	  private Vocab cursorToVocab(Cursor cursor) {

		    Vocab vocab = new Vocab();
		    vocab.setId(cursor.getLong(1));
		    vocab.setChinese(cursor.getString(2));
		    vocab.setEnglish(cursor.getString(3));
		    vocab.setPronunciation(cursor.getString(4));
		    vocab.setImagePath(changePath(cursor.getString(5)));
		    vocab.setAudioPath(changePath(cursor.getString(6)));
		    //vocab.setFamilarity(cursor.getString(7));
		  
		    return vocab;
		  }
		  
		  private Vocab cursorToVocabSubject(Cursor cursor) {

			    Vocab vocab = new Vocab();
			    vocab.setId(cursor.getLong(1));
			    vocab.setChinese(cursor.getString(2));
			    vocab.setEnglish(cursor.getString(3));
			    vocab.setPronunciation(cursor.getString(4));
			    vocab.setImagePath(changePath(cursor.getString(5)));
			    vocab.setAudioPath(changePath(cursor.getString(6)));
			    //vocab.setFamilarity(cursor.getString(7));
			  
			    return vocab;
			  }
		  
		  private Vocab cursorToGrammarSubject(Cursor cursor) {

			    Vocab vocab = new Vocab();
			    vocab.setId(cursor.getLong(1));
			    vocab.setChinese(cursor.getString(2));
			    vocab.setEnglish(cursor.getString(3));
			    vocab.setPronunciation(cursor.getString(4));
			    vocab.setAudioPath(changePath(cursor.getString(5)));
			  
			    return vocab;
			  }
	  
	  private Vocab cursorToVocabTestSubject(Cursor cursor) {

		    Vocab vocab = new Vocab();
		   
		    vocab.setId(cursor.getLong(0));
		    vocab.setChinese(cursor.getString(2));
		    vocab.setEnglish(cursor.getString(3));
		    vocab.addChoice(cursor.getString(4));
		    vocab.addChoice(cursor.getString(5));
		    vocab.addChoice(cursor.getString(6));
		    vocab.addChoice(cursor.getString(3));
		    vocab.last_selected = cursor.getString(7);
		    vocab.total = cursor.getInt(8);
		    vocab.percent = cursor.getInt(9);
		    vocab.score = cursor.getString(10);
		    vocab.score_int = cursor.getInt(11);
		    
		  
		    return vocab;
		  }
	  
	  private Vocab cursorToVocabProgress(Cursor cursor) {

		    Vocab vocab = new Vocab();
		    
		    vocab.setId(cursor.getLong(0));
		    vocab.setChinese(cursor.getString(1));
		    vocab.setEnglish(cursor.getString(2));
		   
  
		    return vocab;
		  }
	  
	  private Vocab cursorToTestInfo(Cursor cursor) {

		    Vocab vocab = new Vocab();
		    
		    vocab.setId(cursor.getLong(0));
		    vocab.date = cursor.getString(2);
		    vocab.attempt = cursor.getInt(3);
		    vocab.score = cursor.getString(4);
		   

		    return vocab;
		  }
	  
	  
	  private Vocab cursorToGrammar(Cursor cursor) {

		    Vocab vocab = new Vocab();
		    vocab.setId(cursor.getLong(1));
		    vocab.setChinese(cursor.getString(2));
		    vocab.setEnglish(cursor.getString(3));
		    vocab.setPronunciation(cursor.getString(4));
		    vocab.setAudioPath(changePath(cursor.getString(5)));
		    return vocab;
		  }
	  
	  public void setPinyin(String table, String chinese, String pinyin, String audio)
	  {
		  String statement = "UPDATE " + table + " SET pronunciation = '" + pinyin + "' WHERE chinese = '" + chinese + "'";
		  Log.d("vocabdatasource", statement);
		 // database.execSQL(statement, null);
		  String where = "chinese" + "='" + chinese + "'";
		  
		  ContentValues values = new ContentValues();
	      values.put(MySQLiteHelper.COLUMN_PRONUNCIATION, pinyin);
	      values.put(MySQLiteHelper.COLUMN_AUDIO, audio);
	      database.update(table, values, where, null);
		    
	  }
	  
	  public void setVocabTestScore(int total, int percent, String score, String chinese, String last_selected, int score_int)
	  {
		
		  String where = "chinese" + "='" + chinese + "'";
		  
		  ContentValues values = new ContentValues();
	      values.put("total", total);
	      values.put("percent", percent);
	      values.put("score", score);
	      values.put("last_selected", last_selected);
	      values.put("score_int", score_int);
	      database.update("vocab_test", values, where, null);
	      
	      Log.d("setVocabTestScore", "updated");
		    
	  }
	  
	  public void setScore(String table, String subject, String score)
	  {
		 
		  String where = "subject" + "='" + subject + "'";
		  
		  ContentValues values = new ContentValues();
	      values.put("score", score);
	      database.update(table, values, where, null);
		    
	  }
	  
	  public void setLastSelected(String table, String chinese, String selectedanswer)
	  {
		 
		  String where = "chinese" + "='" + chinese + "'";
		  
		  ContentValues values = new ContentValues();
	      values.put("last_selected", selectedanswer);
	      database.update(table, values, where, null);
		    
	  }
	  
	  private String changePath(String path)
	  {
		  
		  //Log.d("check path in changepath()1", ""+path);
		  if(path!=null)
		  {
		  String newString = path.substring(37, path.length());
		  //Log.d("check path in changepath()2", ""+newString);
		  String finalString = GlobalVariables.rootDir + newString;
		  //Log.d("check path in changepath()3", ""+finalString);
		  return finalString;
		  }
		  
		  return null;
	  }
	  

}
