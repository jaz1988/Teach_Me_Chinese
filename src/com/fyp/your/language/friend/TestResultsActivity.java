package com.fyp.your.language.friend;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fyp.your.language.friend.R;
import com.fyp.your.language.friend.Database.VocabDataSource;
import com.fyp.your.language.friend.Input.SearchActivity;
import com.fyp.your.language.friend.Library.Vocab;
import com.fyp.your.language.friend.Tests.VocabTestActivity;
import com.fyp.your.language.friend.Tests.VocabTestActivity.getAnswers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class TestResultsActivity extends Activity {

	List<Vocab> values;
	int score;
	String subject;
	private VocabDataSource datasource;
	LinearLayout container;
	
	private ProgressDialog mProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_results_main);
		
		score = 0;
		values = GlobalVariables.values;
		subject = GlobalVariables.subject;
		Log.d("results pages", Integer.toString(values.size()));
		container = (LinearLayout) findViewById(R.id.container);
		
		new buildResults().execute();

		
	}

	public void onBackPressed() {
	          	
	        
	    } 

	 @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    //com.actionbarsherlock.view.
	  	
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_home, menu);
	    return true;
	  } 
	      
	  //action bar
	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.home:    	  
	  	  Intent intent = new Intent(TestResultsActivity.this,HomeActivity.class);
	  	  startActivity(intent);  	  
	  	  finish();
	  	  break;   	  
	    case R.id.search:    	  
		  intent = new Intent(TestResultsActivity.this,SearchActivity.class);
		  startActivity(intent);  	 
		  finish();
		  break;   	  
	    default:
	      break;
	    }
	    return true;
	  } 
	
	public class buildResults extends AsyncTask<Void, Integer, List<Vocab>>
    {
    	@Override
		protected List<Vocab> doInBackground(Void... arg0) 
		{
    	 		    		   		
			  return values;
		}
    	
    	@Override
        protected void onProgressUpdate(Integer... p) {
            super.onProgressUpdate(p);
            progress("Computing results...");
			//mProgressDialog.show();
        }
    	
    	//Process the results
    	@Override
		protected void onPostExecute(List<Vocab> result)
		{	

    		Vocab vocab;
			 String chinese, english, selectedanswer, choice1, choice2, choice3, choice4;
			 Boolean correct = false;
			 String[] alpha = {"A","B","C","D"};
			 
			 LayoutInflater inflater = getLayoutInflater();
			 LinearLayout LLAnswerRow;
			 TextView tv_chinese, tv_alpha, tv_meaning;
			 LinearLayout LL_choice1, LL_choice2, LL_choice3,LL_choice4;
			 
			 int total,percentInt,scoreInt;
			 String scoreString;
			 double percent;
			 
			 for(int i = 0; i < values.size(); i++)
			 {
				 correct = false;
				 vocab = values.get(i);
			 
				 chinese =  vocab.getChinese();
				 english = vocab.getEnglish();
				 selectedanswer = vocab.selectedAnswer;
				 choice1 =  vocab.getChoices().get(0);
				 choice2 =  vocab.getChoices().get(1);
				 choice3 =  vocab.getChoices().get(2);
				 choice4 =  vocab.getChoices().get(3);
				 
				 //Check if answer is correct
				 if(english.equalsIgnoreCase(selectedanswer))
				 {
					 //Correct, add 1 point to score
					 score++;
					 
					 correct = true;
				 }
				 else
				 {
					 //Wrong
				 }
				 
				 LLAnswerRow = (LinearLayout) inflater.inflate(R.layout.test_results_row, null);
				 tv_chinese = (TextView) LLAnswerRow.getChildAt(0);
				 tv_chinese.setText(chinese);
				 
				 //Choice 1
				 LL_choice1 = (LinearLayout) LLAnswerRow.getChildAt(1);
				 tv_alpha = (TextView) LL_choice1.getChildAt(0);
				 tv_alpha.setText("A");
				 if(english.equalsIgnoreCase(choice1))
				 {
					 tv_alpha.setBackgroundResource(R.drawable.item_body_green);
				 }
				 else if(selectedanswer.equalsIgnoreCase(choice1))
				 {
					 tv_alpha.setBackgroundResource(R.drawable.item_body_red);
				 }
				 
				 tv_meaning = (TextView) LL_choice1.getChildAt(1);
				 tv_meaning.setText(choice1);
				 
				 //Choice 2
				 LL_choice2 = (LinearLayout) LLAnswerRow.getChildAt(2);
				 tv_alpha = (TextView) LL_choice2.getChildAt(0);
				 tv_alpha.setText("B");
				 
				 tv_meaning = (TextView) LL_choice2.getChildAt(1);
				 tv_meaning.setText(choice2);
				 if(english.equalsIgnoreCase(choice2))
				 {
					 tv_alpha.setBackgroundResource(R.drawable.item_body_green);
				 }
				 else if(selectedanswer.equalsIgnoreCase(choice2))
				 {
					 tv_alpha.setBackgroundResource(R.drawable.item_body_red);
				 }
				 
				 //Choice 3
				 LL_choice3 = (LinearLayout) LLAnswerRow.getChildAt(3);
				 tv_alpha = (TextView) LL_choice3.getChildAt(0);
				 tv_alpha.setText("C");
				 
				 tv_meaning = (TextView) LL_choice3.getChildAt(1);
				 tv_meaning.setText(choice3);
				 if(english.equalsIgnoreCase(choice3))
				 {
					 tv_alpha.setBackgroundResource(R.drawable.item_body_green);
				 }
				 else if(selectedanswer.equalsIgnoreCase(choice3))
				 {
					 tv_alpha.setBackgroundResource(R.drawable.item_body_red);
				 }
				 
				 //Choice 4
				 LL_choice4 = (LinearLayout) LLAnswerRow.getChildAt(4);
				 tv_alpha = (TextView) LL_choice4.getChildAt(0);
				 tv_alpha.setText("D");
				 
				 tv_meaning = (TextView) LL_choice4.getChildAt(1);
				 tv_meaning.setText(choice4);
				 if(english.equalsIgnoreCase(choice4))
				 {
					 tv_alpha.setBackgroundResource(R.drawable.item_body_green);
				 }
				 else if(selectedanswer.equalsIgnoreCase(choice4))
				 {
					 tv_alpha.setBackgroundResource(R.drawable.item_body_red);
				 }
				 
				 //Add to container
				 container.addView(LLAnswerRow);
				 
				 //Updating total, percent and score in database				 
				 datasource = new VocabDataSource(TestResultsActivity.this);
		         datasource.open();
		         
			     total = datasource.getVocabTotalCount(chinese);
			     total++;
			     //Log.d("total", Integer.toString(total));
			     
			     scoreInt = datasource.getVocabTotalScoreIntCount(chinese);
			     if(correct)
			     {
			    	 scoreInt++;
			     }
			     //Log.d("score", Integer.toString(scoreInt));
			     
			     percent = ((double)scoreInt/(double)total) * 100;
				 percentInt = (int) percent;
					
				 scoreString = Integer.toString(scoreInt) + "/" + Integer.toString(total) + " (" + Integer.toString(percentInt) + "%)";
			     datasource.setVocabTestScore(total, percentInt, scoreString, chinese, selectedanswer, scoreInt);
			     datasource.close();
			     
			     
			 }
	       	         	
	      
	     

			 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			 Date date = new Date();
			 String string_date = dateFormat.format(date);
			 //Log.d("Date", string_date);
			
			 scoreString = calculateScore();
			 TextView tv_score = (TextView) findViewById(R.id.score);
			 tv_score.setText(scoreString);
			 
			//Connect to the database to retrieve the vocabs from the library
 	        datasource = new VocabDataSource(TestResultsActivity.this);
 	        datasource.open();
				
 	        //values = datasource.getAllVocab();
 	        //subjects = datasource.getSubjectsTest();
   	        //values = datasource.getVocabBySubject(subjects.get(0));
 	        //datasource.setScore("subjects", subject, scoreString);
 	        int count = datasource.getVocabTestInfoCount(subject);
 	   
 	        //Log.d("Count rows", Integer.toString(count));
 	        datasource.insertTestInfo(string_date, count + 1, scoreString, subject);
 	        
 	        datasource.close();
		}
	}
	
	private String calculateScore()
	{        
		int total = values.size();
		double percent = ((double)score/(double)total) * 100;
		
		int percentInt = (int) percent;
		
		return Integer.toString(score) + "/" + Integer.toString(total) + " (" + Integer.toString(percentInt) + "%)";
		//return null;		
		
		
	}
	
	public void progress(String msg)
	  {
	  	mProgressDialog = new ProgressDialog(this);
	  	mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);         
	  	mProgressDialog.setMessage(msg);                      
	  	mProgressDialog.setCancelable(false);    
	  }

}
