package com.fyp.your.language.friend;

import java.util.ArrayList;
import java.util.List;

import com.fyp.your.language.friend.R;
import com.fyp.your.language.friend.Database.VocabDataSource;
import com.fyp.your.language.friend.Flashcard.FlashcardActivity;
import com.fyp.your.language.friend.Input.SearchActivity;
import com.fyp.your.language.friend.Library.LibraryActivity;
import com.fyp.your.language.friend.Library.Vocab;
import com.fyp.your.language.friend.ListAdapters.VocabAdapter;
import com.fyp.your.language.friend.Pairs.PairsActivity;
import com.fyp.your.language.friend.Scramble.ScrambleActivity;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class ProgressActivity extends Activity implements OnItemSelectedListener {

	private VocabDataSource datasource;
	List<Vocab> subjects;
	private ProgressDialog mProgressDialog;
	List<Vocab> test_scores, last_test_results_desc, last_test_results_asc, results_breakdown;
	private int[] colors = new int[] {R.drawable.item_body, R.drawable.item_body_list };
	Spinner sp1;
	Spinner sp2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_progress);
		setContentView(R.layout.user_progress_main);
		
		datasource = new VocabDataSource(this);
		datasource.open();

		List<String> subjects = datasource.getSubjects();
   
		String subject;
		String number;

		List<String> list=new ArrayList<String>();
    
		for(int i = 0; i < subjects.size(); i++)
		{
			subject = subjects.get(i);
			list.add(subject);
		}
		//list.add("All");
		
		sp1= (Spinner) findViewById(R.id.spinner_subject);
	    
		ArrayAdapter<String> adp1=new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,list);
		adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp1.setAdapter(adp1);
		sp1.setOnItemSelectedListener(this);
    
		test_scores = datasource.getTestInfo(list.get(0));
		last_test_results_desc = datasource.getVocabTestBySubject(list.get(0), "DESC");
		last_test_results_asc = datasource.getVocabTestBySubject(list.get(0), "ASC");
	 
		datasource.close();
	        
        buildProgress();
        
	        
	}
	
	public void onClick(View view) {
	     
        switch (view.getId()) {
        
        case R.id.btn_show_scores:
         
        	ViewFlipper vf = (ViewFlipper) findViewById(R.id.vfContainer);
        	vf.setDisplayedChild(0);
        	
            break;
            
        case R.id.btn_show_last_test_results:
            
        	vf = (ViewFlipper) findViewById(R.id.vfContainer);
        	vf.setDisplayedChild(1);
        	
            break;
            
        case R.id.btn_show_breakdown:
            
        	vf = (ViewFlipper) findViewById(R.id.vfContainer);
        	vf.setDisplayedChild(2);
        	
            break;
         
        }
        
        
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
	  	  Intent intent = new Intent(ProgressActivity.this,HomeActivity.class);
	  	  startActivity(intent);  	  
	  	  finish();
	  	  break;   	  
	    case R.id.search:    	  
		  intent = new Intent(ProgressActivity.this,SearchActivity.class);
		  startActivity(intent);  	  
		  finish();
		  break;   	  
	    default:
	      break;
	    }
	    return true;
	  } 
	  
//	  public class getData extends AsyncTask<Void, Integer, List<Vocab>>
//	    {
//	    	@Override
//			protected List<Vocab> doInBackground(Void... arg0) 
//			{
//	    		 try
//	    	      {	    	  	    	 
//	    			publishProgress(1);
//	    			
//	    			//Connect to the database to retrieve the vocabs from the library
//	    	        datasource = new VocabDataSource(ProgressActivity.this);
//	    	        datasource.open();
//	 				
//	    	        //values = datasource.getAllVocab();
//	    	        subjects = datasource.getSubjectsTest();
//	      	        //values = datasource.getVocabBySubject(subjects.get(0));
//	    	        
//	    	        datasource.close();
//	    	       	         	
//	    	      }
//	    	      catch (Exception e) 
//	    	      {
//	    	        
//	    	      }
//	    	 		    		   		
//				  return values;
//			}
//	    	
//	    	@Override
//	        protected void onProgressUpdate(Integer... p) {
//	            super.onProgressUpdate(p);
//	            progress("Computing results...");
//				mProgressDialog.show();
//	        }
//	    	
//	    	//Process the results
//	    	@Override
//			protected void onPostExecute(List<Vocab> result)
//			{	
//	    
//
//				mProgressDialog.cancel();		
//				
//		
//		}
//      }
	  
	  //Initialise the progress spinner
	  public void progress(String msg)
	  {
	  	mProgressDialog = new ProgressDialog(this);
	  	mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);         
	  	mProgressDialog.setMessage(msg);                      
	  	mProgressDialog.setCancelable(false);    
	  }
	  
	    	
	  
	  public void buildProgress()
	  {
		  //Get the values
		  Vocab vocab;
		  
		  //Build the layout
		  ViewFlipper vfContainer = (ViewFlipper) findViewById(R.id.vfContainer);
		  LayoutInflater inflater = getLayoutInflater();
		  LinearLayout testScoreLL = null, lastScoreLL = null, breakdownLL = null;
		  
		  //Edit the content in the layouts
		  LinearLayout ll_child,test_score_container,test_score_row,last_score_container,breakdown_container,breakdown_row;
		  TextView tv_child;
		  
		  //vfContainer.removeAllViews();
		  //lastScoreLL = (LinearLayout) inflater.inflate(R.layout.user_progress_last_score, null);
		  //breakdownLL = (LinearLayout) inflater.inflate(R.layout.user_progress_result_breakdown, null);
		  
		  //vfContainer.addView(testScoreLL);
		  //vfContainer.addView(lastScoreLL);
		  //vfContainer.addView(breakdownLL);
		  
		  test_score_container = (LinearLayout) findViewById(R.id.test_score_container);
		  test_score_container.removeAllViews();
		  
		  int colorPos;
		 
	        
		  for(int i = 0; i < test_scores.size(); i++)
		  {
			  colorPos = i % colors.length;
		        
			  //Log.d("build progress", Integer.toString(i));
			  vocab = test_scores.get(i);			  			 
			  
			  testScoreLL = (LinearLayout) inflater.inflate(R.layout.user_progress_test_scores, null);	
			  
			  //Date
			  tv_child = (TextView) testScoreLL.getChildAt(0);
			  tv_child.setText(vocab.date);
			  tv_child.setBackgroundResource(colors[colorPos]);
			  
			  //Attempts
			  tv_child = (TextView) testScoreLL.getChildAt(1);
			  tv_child.setText("" + vocab.attempt);
			  tv_child.setBackgroundResource(colors[colorPos]);
			  
			  //Score
			  tv_child = (TextView) testScoreLL.getChildAt(2);
			  tv_child.setText(vocab.score);
			  tv_child.setBackgroundResource(colors[colorPos]);
			  
			  test_score_container.addView(testScoreLL);		  	  
		  }
		  
		  //lastScoreLL
		  last_score_container = (LinearLayout) findViewById(R.id.last_score_container);
		  last_score_container.removeAllViews();
		  
		  String chinese, english, selectedanswer, choice1, choice2, choice3, choice4;
		  String[] alpha = {"A","B","C","D"};
		 
	 	  LinearLayout LLAnswerRow;
		  TextView tv_chinese, tv_alpha, tv_meaning;
		  LinearLayout LL_choice1, LL_choice2, LL_choice3,LL_choice4;
		 
		  for(int i = 0; i < last_test_results_desc.size(); i++)
		  {
			  vocab = last_test_results_desc.get(i);
		 
			  chinese =  vocab.getChinese();
			  english = vocab.getEnglish();
			  selectedanswer = vocab.last_selected;
			  if(selectedanswer == null)
				  break;
			  choice1 =  vocab.getChoices().get(0);
			  choice2 =  vocab.getChoices().get(1);
			  choice3 =  vocab.getChoices().get(2);
			  choice4 =  vocab.getChoices().get(3);
			 
			  //Check if answer is correct
			  if(english.equalsIgnoreCase(selectedanswer))
			  {
				  //Correct, add 1 point to score
				  //score++;
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
			  last_score_container.addView(LLAnswerRow);
		 }

		  //breakdownLL
		  breakdown_container = (LinearLayout) findViewById(R.id.breakdown_container);
		  breakdown_container.removeAllViews();
		  
		  for(int i = 0; i < last_test_results_desc.size(); i++)
		  {
			  colorPos = i % colors.length;
			  
			  vocab = last_test_results_desc.get(i);
			  if(vocab.score == null)
			  {
				  break;
			  }
			  breakdownLL = (LinearLayout) inflater.inflate(R.layout.user_progress_result_breakdown, null);	
			  			  
			  //Date
			  tv_child = (TextView) breakdownLL.getChildAt(0);
			  tv_child.setText(vocab.getChinese());
			  tv_child.setBackgroundResource(colors[colorPos]);
			  
			  //Attempts
			  tv_child = (TextView) breakdownLL.getChildAt(1);
			  tv_child.setText(vocab.score);
			  tv_child.setBackgroundResource(colors[colorPos]);
			  
			  breakdown_container.addView(breakdownLL);
			  
		  }
		  
		  List<String> list=new ArrayList<String>();
		    
		  list.add("Highest score");
		  list.add("Lowest score");
			
		  sp2= (Spinner) findViewById(R.id.spinner_sort);
		    
		  ArrayAdapter<String> adp1=new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,list);
		  adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		  sp2.setAdapter(adp1);
		  sp2.setOnItemSelectedListener(this);
			
			

	  }
	  
	  @Override
		public void onItemSelected(AdapterView<?> parent, View v, int position,
				long id) {
			// TODO Auto-generated method stub
//			Toast.makeText(parent.getContext(), 
//					"OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
//					Toast.LENGTH_SHORT).show();

		  //Log.d("spinner type", parent.toString());
		  if(parent == sp1)
		  {
			datasource = new VocabDataSource(this);
			datasource.open();
			
			test_scores = datasource.getTestInfo(parent.getItemAtPosition(position).toString());
			last_test_results_desc = datasource.getVocabTestBySubject(parent.getItemAtPosition(position).toString(), "DESC");
			last_test_results_asc = datasource.getVocabTestBySubject(parent.getItemAtPosition(position).toString(), "ASC");
			
			buildProgress();
			datasource.close();
		  }
		  else
		  {
			  if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Lowest score"))
			  {
				  LinearLayout breakdown_container, breakdownLL;
				  TextView tv_child;
				  int colorPos;
				  Vocab vocab;
				  LayoutInflater inflater = getLayoutInflater();
				  
				  //breakdownLL
				  breakdown_container = (LinearLayout) findViewById(R.id.breakdown_container);
				  breakdown_container.removeAllViews();
				  
				  for(int i = 0; i < last_test_results_asc.size(); i++)
				  {
					  colorPos = i % colors.length;
					  
					  vocab = last_test_results_asc.get(i);
					  if(vocab.score == null)
					  {
						  break;
					  }
					  breakdownLL = (LinearLayout) inflater.inflate(R.layout.user_progress_result_breakdown, null);	
					  			  
					  //Date
					  tv_child = (TextView) breakdownLL.getChildAt(0);
					  tv_child.setText(vocab.getChinese());
					  tv_child.setBackgroundResource(colors[colorPos]);
					  
					  //Attempts
					  tv_child = (TextView) breakdownLL.getChildAt(1);
					  tv_child.setText(vocab.score);
					  tv_child.setBackgroundResource(colors[colorPos]);
					  
					  breakdown_container.addView(breakdownLL);
					  
				  }
				  
	
			  }
			  else
			  {
				  LinearLayout breakdown_container, breakdownLL;
				  TextView tv_child;
				  int colorPos;
				  Vocab vocab;
				  LayoutInflater inflater = getLayoutInflater();
				  
				  //breakdownLL
				  breakdown_container = (LinearLayout) findViewById(R.id.breakdown_container);
				  breakdown_container.removeAllViews();
				  
				  for(int i = 0; i < last_test_results_desc.size(); i++)
				  {
					  colorPos = i % colors.length;
					  
					  vocab = last_test_results_desc.get(i);
					  if(vocab.score == null)
					  {
						  break;
					  }
					  breakdownLL = (LinearLayout) inflater.inflate(R.layout.user_progress_result_breakdown, null);	
					  			  
					  //Date
					  tv_child = (TextView) breakdownLL.getChildAt(0);
					  tv_child.setText(vocab.getChinese());
					  tv_child.setBackgroundResource(colors[colorPos]);
					  
					  //Attempts
					  tv_child = (TextView) breakdownLL.getChildAt(1);
					  tv_child.setText(vocab.score);
					  tv_child.setBackgroundResource(colors[colorPos]);
					  
					  breakdown_container.addView(breakdownLL);
					  
				  }
				  
				  
			  }
		  }
			
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		
		}


}









