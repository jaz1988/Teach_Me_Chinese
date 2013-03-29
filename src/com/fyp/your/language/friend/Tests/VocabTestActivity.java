package com.fyp.your.language.friend.Tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fyp.your.language.friend.R;

import com.fyp.your.language.friend.GlobalVariables;
import com.fyp.your.language.friend.HomeActivity;
import com.fyp.your.language.friend.StartActivity;
import com.fyp.your.language.friend.TestResultsActivity;
import com.fyp.your.language.friend.TranslateHelperClass;
import com.fyp.your.language.friend.Database.VocabDataSource;
import com.fyp.your.language.friend.Input.SearchActivity;
import com.fyp.your.language.friend.Library.Vocab;
import com.fyp.your.language.friend.ListAdapters.TestAdapter;
import com.fyp.your.language.friend.ListAdapters.VocabAdapterSummary;
import com.fyp.your.language.friend.Pairs.PairsActivity;
import com.fyp.your.language.friend.Pairs.PairsActivity.buildCards;
import com.fyp.your.language.friend.Pairs.PairsActivity.buildChoice;
import com.fyp.your.language.friend.Scramble.ScrambleActivity;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class VocabTestActivity extends Activity implements OnClickListener, OnItemSelectedListener {

	LinearLayout container;
	private ProgressDialog mProgressDialog;
	private VocabDataSource datasource;
	List<Vocab> values;
	int index;
	Spinner sp1,sp2;
	View promptsView; 
	String selected_subject;
	AlertDialog choiceDialog;
	List<String> subjects;
	String typeString;
	String selectedChoice;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vocab_test_main);
		
		container = (LinearLayout) findViewById(R.id.answerContainer);
		
		index = 0;
		
		LayoutInflater li = LayoutInflater.from(VocabTestActivity.this);
    	promptsView = li.inflate(R.layout.vocab_test_dialog,null);
    	new buildChoice().execute();
    	
 

        
	}
	
	public void onBackPressed() {
	          	
	        
	    } 
	
    public class buildChoice extends AsyncTask<Void, Integer, List<Vocab>>
    {
    	@Override
		protected List<Vocab> doInBackground(Void... arg0) 
		{
    		 try
    	      {	    	  	    	 
    			publishProgress(1);
    			
    			//Connect to the database to retrieve the vocabs from the library
    	        datasource = new VocabDataSource(VocabTestActivity.this);
    	        datasource.open();
 				
    	        //values = datasource.getAllVocab();
    	        subjects = datasource.getSubjectsTest();
      	        //values = datasource.getVocabBySubject(subjects.get(0));
    	        
    	        datasource.close();
    	       	         	
    	      }
    	      catch (Exception e) 
    	      {
    	        
    	      }
    	 		    		   		
			  return values;
		}
    	
    	@Override
        protected void onProgressUpdate(Integer... p) {
            super.onProgressUpdate(p);
            progress("Populating choices");
			mProgressDialog.show();
        }
    	
    	//Process the results
    	@Override
		protected void onPostExecute(List<Vocab> result)
		{	
    		List<String> list=new ArrayList<String>();
    	    
    		String subject;
			for(int i = 0; i < subjects.size(); i++)
			{
				subject = subjects.get(i);
				Log.d("subjects", subject);
				list.add(subject);
			}
			//list.add("All");
					      			
			sp1= (Spinner) promptsView.findViewById(R.id.spinner_subject);
		    Log.d("spinner", sp1.toString());
			ArrayAdapter<String> adp1= new ArrayAdapter<String>(VocabTestActivity.this,android.R.layout.simple_list_item_1,list);
			adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp1.setAdapter(adp1);
			sp1.setOnItemSelectedListener(VocabTestActivity.this);
			
			list = new ArrayList<String>();
			list.add("Vocab");
			//list.add("Grammar");
			
			sp2= (Spinner) promptsView.findViewById(R.id.spinner_type);
		    
			adp1= new ArrayAdapter<String>(VocabTestActivity.this,android.R.layout.simple_list_item_1,list);
			adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp2.setAdapter(adp1);
			sp2.setOnItemSelectedListener(VocabTestActivity.this);

			mProgressDialog.cancel();		
			
			//Open dialog for user to choose subject and difficulty
//			LayoutInflater li = LayoutInflater.from(PairsActivity.this);
//	    	View promptsView = li.inflate(R.layout.choose_subject,null);
	    	  					
			AlertDialog.Builder choiceBuilder = new AlertDialog.Builder(VocabTestActivity.this);
			choiceBuilder.setView(promptsView);
			choiceBuilder.setTitle("Choose the Subject and type of test");
	    	
			choiceBuilder.setCancelable(false).setPositiveButton("Start Test!", new DialogInterface.OnClickListener()
	    	{
	    	public void onClick(DialogInterface dialog, int id)
    		{
	    		selected_subject = sp1.getSelectedItem().toString();
	    		GlobalVariables.subject = selected_subject;
	    		typeString = sp2.getSelectedItem().toString();
	    		
	    		new getAnswers().execute();
	    		
	    		
    		}
	    	});
			choiceDialog = choiceBuilder.create();
			choiceDialog.show();
		}
	}
    
    private void getRandomSet()
    {
    	Random rand;
    	Vocab vocab;
    	int x;
    	List<Vocab> newList = new ArrayList<Vocab>();
    	List<Vocab> copyList = new ArrayList<Vocab>();
    	 	
    	for(int i = 0; i < values.size(); i++)
    	{
    		copyList.add(values.get(i));
    	}
    	
    	for(int i = 0; i < values.size(); i++)
    	{
    		rand = new Random();
    		
    		x = rand.nextInt(copyList.size());  	 		
    		
    		vocab = copyList.get(x);
    		newList.add(vocab);
    		copyList.remove(vocab);   		   		
    	} 	
    	
    	values = newList;
    }
    
	public class getAnswers extends AsyncTask<Void, Integer, List<Vocab>>
	  {
	  	@Override
			protected List<Vocab> doInBackground(Void... arg0) 
			{
	  		 try
	  	      {	    	  	    	 
	  			publishProgress(1);
	  			
	  			//Connect to the database to retrieve the vocabs from the library
	  	        datasource = new VocabDataSource(VocabTestActivity.this);
	  	        datasource.open();
						   
	  	        if(typeString.equalsIgnoreCase("vocab"))
	  	        {
	  	        	values = datasource.getVocabTestBySubject(selected_subject, "ASC");
	  	        }
	  	        else
	  	        {
	  	        	
	  	        }
	  	        //subjects = datasource.getSubjects();
	  	        //values = datasource.getGrammarBySubject(subjects.get(0));
	  	        //values = datasource.getAllVocab();
	  	        
	  	        datasource.close();
	  	       	         	
	  	      }
	  	      catch (Exception e) 
	  	      {
	  	        
	  	      }
	  	 		    		   		
				  return values;
			}
	  	
	  	@Override
	      protected void onProgressUpdate(Integer... p) {
	          super.onProgressUpdate(p);
	          progress("Setting up test...");
				mProgressDialog.show();
	      }
	  	
	  	//Process the results
	  	@Override
			protected void onPostExecute(List<Vocab> result)
			{	
	  			getRandomSet();
	  			buildAnswers();
				mProgressDialog.cancel();				
			}
		}
		
		//Initialise the progress spinner
		  public void progress(String msg)
		  {
		  	mProgressDialog = new ProgressDialog(this);
		  	mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);         
		  	mProgressDialog.setMessage(msg);                      
		  	mProgressDialog.setCancelable(false);    
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
	  	  Intent intent = new Intent(VocabTestActivity.this,HomeActivity.class);
	  	  startActivity(intent);  	  
	  	finish();
	  	  break;   	  
	    case R.id.search:    	  
		  intent = new Intent(VocabTestActivity.this,SearchActivity.class);
		  startActivity(intent);  	  
		  finish();
		  break;   	  
	    default:
	      break;
	    }
	    return true;
	  } 
	
	public void buildAnswers()
	{
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout answerLL;
		Random rand;
				
		String[] alpha = {"A","B","C","D"};
		List<String> choices;
		String alphaChoice, answer;
		
		TextView tv_alpha, tv_answer;

		TextView tv_count = (TextView) findViewById(R.id.count);
		tv_count.setText(Integer.toString(index+1) + "/" + Integer.toString(values.size()));

		TextView tv_chinese = (TextView) findViewById(R.id.chinese);
		tv_chinese.setText(values.get(index).getChinese());
		
		TextView tv_number = (TextView) findViewById(R.id.number);
		tv_number.setText(Integer.toString(index+1) + ")");
		
		List<String> tempList = new ArrayList<String>();

		for(int i = 0; i < 4; i++)
		{
			//Log.d("choices values:", values.get(index).getChoices().get(i));
			tempList.add(values.get(index).getChoices().get(i));	
		}
		values.get(index).clearChoices();
		for(int i = 0; i < 4; i++)
		{
			rand = new Random();		    		
    		int x = rand.nextInt(tempList.size());  
    		values.get(index).addChoice(tempList.get(x));
    		tempList.remove(tempList.get(x));
		}
		
		for(int i = 0; i < 4; i++)
		{
			Log.d("choices values:", values.get(index).getChoices().get(i));
		}
		container.removeAllViews();
		for(int i = 0; i < 4; i++)
		{
		
			alphaChoice = alpha[i];
			answerLL = (LinearLayout) inflater.inflate(R.layout.vocab_test_answer_row, null);
			
			tv_alpha = (TextView) answerLL.getChildAt(0);
			tv_alpha.setText(alphaChoice);
			
			tv_answer = (TextView) answerLL.getChildAt(1);
			tv_answer.setText(values.get(index).getChoices().get(i));
			
			
			container.addView(answerLL);
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
		
		switch (v.getId()) {
	      case R.id.answer:    	  
	    	  LinearLayout answerLL;
	    	  TextView tv;
	    	  //Toast.makeText(this, "clicked " + v.toString(), Toast.LENGTH_SHORT).show();
	    	  for(int i = 0; i < container.getChildCount(); i++ )
	    	  {
	    		  answerLL = (LinearLayout) container.getChildAt(i);
	    		  tv = (TextView) answerLL.getChildAt(0);
	    		  if(v == answerLL)
	    		  {
	    			  tv.setBackgroundResource(R.drawable.item_body_green);
	    			  tv = (TextView) answerLL.getChildAt(1);
	    			  values.get(index).selectedAnswer = tv.getText().toString();
	    			  
	    		  }
	    		  else tv.setBackgroundResource(R.drawable.item_body);
	    	  }
	    	  
	    	  break;   	  
	      case R.id.next:  	
	    	  //Check if the answer is correct
	    	  int temp = index;
	    	  temp++;
	    	  if(temp == values.size())
	    	  {
	    		  //Last question
	    		  if(values.get(index).selectedAnswer==null)
	    		  {
	    			  Toast.makeText(this, "Please choose an answer.", Toast.LENGTH_SHORT).show();
	    		  }
	    		  else
	    		  {
	    			  index = 0;
		    		  Toast.makeText(this, "Test Ended!", Toast.LENGTH_SHORT).show();
		    		  
		    		  GlobalVariables.values = values;
		    		  
//		    		  datasource = new VocabDataSource(VocabTestActivity.this);
//	  	  	          datasource.open();
//	  	  	          datasource.setLastSelected("vocab_test", values.get(index).getChinese(), values.get(index).selectedAnswer);
//	  	  	          datasource.close();
	  	  	          
		    		  //Show results page
		    		  startActivity(new Intent(VocabTestActivity.this,TestResultsActivity.class));
		    		  finish();
		 
	    		  }
	    		 
	    	  }
	    	  else
	    	  {
	    		  //Go next question
	    		  if(values.get(index).selectedAnswer==null)
	    		  {
	    			  Toast.makeText(this, "Please choose an answer.", Toast.LENGTH_SHORT).show();
	    		  }
	    		  else
	    		  {
//	    			  datasource = new VocabDataSource(VocabTestActivity.this);
//	  	  	          datasource.open();
//	  	  	          datasource.setLastSelected("vocab_test", values.get(index).getChinese(), values.get(index).selectedAnswer);
//	  	  	          datasource.close();
	    			  index++;
	    			  buildAnswers();
	    		  }
	    	  }
	    		  
	    	  
	    	  break;   	  
	      default:
	        break;
		  }
		
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		// TODO Auto-generated method stub
//		Toast.makeText(parent.getContext(), 
//				"OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
//				Toast.LENGTH_SHORT).show();

//		datasource = new VocabDataSource(this);
//		datasource.open();
//		
//		values = datasource.getGrammarBySubject(parent.getItemAtPosition(position).toString());
//		grammarIndex = 0;
//		LinearTopList = new ArrayList<View>();
//		buildScramble();
//		
//		datasource.close();
		
		
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	
	}
	
	
//	private List<String> getRandomSet(int type)
//    {
//    	Random rand;
//    	Vocab vocab;
//    	List<Vocab> tempList = new ArrayList<Vocab>();
//    	List<String> stringSet = new ArrayList<String>();
//    	List<String> newSet = new ArrayList<String>();
//    	answer = new ArrayList<String>();
//    	
//    	
//    	for(int i = 0; i < (type*type)/2; i++)
//    	{
//    		Vocab temp;
//    		rand = new Random();
//    		int x = rand.nextInt(values.size());
//    		if(values.get(x).getEnglish().length() > 15)
//    		{
//    			x = rand.nextInt(values.size());
//    		}
//    		
//    		temp = values.get(x);
//    		tempList.add(temp);
//    		
//    		stringSet.add(temp.getChinese());
//    		stringSet.add(temp.getEnglish());
//    		answer.add(temp.getChinese());
//    		answer.add(temp.getEnglish());
//    	}
//    	
//    	values = tempList;
////    	for(int i = 0; i < (type*type)/2; i++)
////    	{
////    		vocab = values.get(i);
////    		stringSet.add(vocab.getChinese());
////    		stringSet.add(vocab.getEnglish());
////    		answer.add(vocab.getChinese());
////    		answer.add(vocab.getEnglish());
////    		Log.d("vocab", vocab.getChinese() + "," + vocab.getEnglish());
////    		
////    	}
//    	for(int i = 0; i < (type*type); i++)
//    	{
//    		rand = new Random();
//    		int x = rand.nextInt(stringSet.size());
//    		newSet.add(stringSet.get(x));
//    		stringSet.remove(stringSet.get(x));  		
//    	}
//    	return newSet;
//    }
    

}
