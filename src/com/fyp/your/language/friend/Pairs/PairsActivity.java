package com.fyp.your.language.friend.Pairs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.fyp.your.language.friend.R;

import com.fyp.your.language.friend.HomeActivity;
import com.fyp.your.language.friend.StartActivity;
import com.fyp.your.language.friend.TestResultsActivity;
import com.fyp.your.language.friend.TranslateHelperClass;
import com.fyp.your.language.friend.Animation.AnimationFactory;
import com.fyp.your.language.friend.Animation.AnimationFactory.FlipDirection;
import com.fyp.your.language.friend.Database.VocabDataSource;
import com.fyp.your.language.friend.Flashcard.FlashcardActivity;
import com.fyp.your.language.friend.Input.SearchActivity;
import com.fyp.your.language.friend.Input.VocabularyInputActivity;
import com.fyp.your.language.friend.Library.LibraryActivity;
import com.fyp.your.language.friend.Library.Vocab;
import com.fyp.your.language.friend.ListAdapters.VocabAdapter;
import com.fyp.your.language.friend.ListAdapters.VocabAdapterSummary;
import com.fyp.your.language.friend.Scramble.ScrambleActivity;
import com.fyp.your.language.friend.Scramble.ScrambleActivity.getGrammar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemSelectedListener;

public class PairsActivity extends Activity implements OnClickListener, OnLongClickListener, OnItemSelectedListener {

	private VocabDataSource datasource;
	private ProgressDialog mProgressDialog;
	List<Vocab> values;
	List<String> answer;
	int cardsOpenCount;
	ViewFlipper cardsOpen;
	int completeCount;
	TextView chinese;
	TextView english;
	TextView pinyin;
	ImageView audio;
	String audioPath;
	int type;
	List<String> subjects;
	Spinner sp1,sp2;
	View promptsView; 
	String selected_subject;
	AlertDialog.Builder choiceBuilder;
	AlertDialog choiceDialog;
	CheckBox cb;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pairs_main);
        
        cardsOpenCount = 0;
        completeCount = 0;
        
        SharedPreferences statePref = getSharedPreferences("Pref", 0);
		Boolean show = statePref.getBoolean("pairs", true);
		
		if(show)
		{
	        LayoutInflater li = LayoutInflater.from(PairsActivity.this);
	    	promptsView = li.inflate(R.layout.pairs_instruction,null);
	    	cb = (CheckBox) promptsView.findViewById(R.id.cb_pairs);
	    	  					
	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PairsActivity.this);
	    	alertDialogBuilder.setView(promptsView);
	    	alertDialogBuilder.setTitle("Instructions");
	    	
	    	  				   		
	    	alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
	    	{
	    		public void onClick(DialogInterface dialog, int id)
	    		{
	    			LayoutInflater li = LayoutInflater.from(PairsActivity.this);
	    	    	promptsView = li.inflate(R.layout.pairs_dialog,null);
	    			new buildChoice().execute();
	    			
	
	    		}
	    	});
	    	
	    	AlertDialog alertDialog = alertDialogBuilder.create();
	    	alertDialog.show();
		}
		else
		{
			LayoutInflater li = LayoutInflater.from(PairsActivity.this);
	    	promptsView = li.inflate(R.layout.pairs_dialog,null);
			new buildChoice().execute();
		}
    	
      
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
    	  Intent intent = new Intent(PairsActivity.this,HomeActivity.class);
    	  startActivity(intent);  	  
    	  finish();
    	  break;   	  
    	  
      case R.id.search:    	  
		  intent = new Intent(PairsActivity.this,SearchActivity.class);
		  startActivity(intent);  	  
		  finish();
		  break;   	  
      default:
        break;
      }
      return true;
    } 
    
	public void onBackPressed() {
	          	
	        
	    } 
    
    public void checkClick(View view)
    {
    	//Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
    	if(cb.isChecked())
    	{
    		SharedPreferences statePref = getSharedPreferences("Pref", 0);
    		SharedPreferences.Editor stateEditor = statePref.edit();
    		stateEditor.putBoolean("pairs", false);
			stateEditor.commit();    	
    	}
    }
    
    
    @Override
	public void onClick(View view) {
    	
    	
    	
    	//Toast.makeText(this, "testing", Toast.LENGTH_SHORT).show();
    	ViewFlipper vf = (ViewFlipper) view;
    	String firstText, secondText;
    	int matched = 0;
    	
    	//AnimationFactory.flipTransition(vf, FlipDirection.LEFT_RIGHT, 200);
    	
    	//No cards opened
    	if(cardsOpenCount == 0)
    	{
	    	cardsOpenCount++;
	    	cardsOpen = vf;
	    	AnimationFactory.flipTransition(vf, FlipDirection.LEFT_RIGHT, 200);
	    	
    	}
    	
    	//1 card opened
    	else if(cardsOpenCount == 1)
    	{
    		if(view == cardsOpen)
    			return;
    		
    		cardsOpenCount++;
    		AnimationFactory.flipTransition(vf, FlipDirection.LEFT_RIGHT, 200);
        	
    		//Check if the 2 cards match
        	RelativeLayout rlTextView = (RelativeLayout) cardsOpen.getChildAt(1);
    		TextView TV = (TextView) rlTextView.getChildAt(0);
    		firstText = TV.getText().toString();
    		//Log.d("first text", firstText);
    		
    		rlTextView = (RelativeLayout) vf.getChildAt(1);
    		TV = (TextView) rlTextView.getChildAt(0);
    		secondText = TV.getText().toString();
    		//Log.d("second text", secondText);
    		
    		//Check if it is English
    		if(firstText.toString().matches("^[\u0000-\u0080]+$"))
    		{
    			//English, check if previous index is the correct match
    			for(int i = 0; i < answer.size(); i++)
    			{
    				//Log.d("English:", answer.get(i));
    				if(answer.get(i).equalsIgnoreCase(firstText))
    				{
    					//Log.d("English:", answer.get(i));
    					if(answer.get(i-1).equalsIgnoreCase(secondText))
    					{
    						Toast.makeText(this, "Matched!", Toast.LENGTH_SHORT).show();
    						matched = 1;
    						completeCount++;
    						break;
    					}
    				}
    			}
    		}
    		else
    		{
    			//Chinese, check if next index is the correct match
    			for(int i = 0; i < answer.size(); i++)
    			{
    				//Log.d("Chinese:", answer.get(i));
    				if(answer.get(i).equalsIgnoreCase(firstText))
    				{
    					//Log.d("Chinese:", answer.get(i));
    					if(answer.get(i+1).equalsIgnoreCase(secondText))
    					{
    						Toast.makeText(this, "Matched!", Toast.LENGTH_SHORT).show();
    						matched = 1;
    						completeCount++;
    						break;
    					}
    				}
    			}
    		}
    		
    		//No words matched, close both cards
    		if(matched == 0)
    		{
				MyTimer tim = new MyTimer(1000,1000,vf);
		        tim.start();
		        tim = new MyTimer(1000,1000,cardsOpen);
		        tim.start();
		        cardsOpenCount = 0;
    		}
    		else
    		{
    			cardsOpenCount = 0;
    		}
    		
    		if(completeCount == type*type/2)
    		{
    			completeCount = 0;
    			//show dialog, play again or proceed to summary page
    			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
    					PairsActivity.this);
    	 
    				// set title
    				alertDialogBuilder.setTitle("Well Done!");
    	 
    				// set dialog message
    				alertDialogBuilder
    					.setMessage(Html.fromHtml("<b>You have completed the game!</b> <br><br> 1) Click on Summary Page to revise what you have learnt <br><br> OR <br><br> 2) Click Play again to start a new game!"))
    					.setCancelable(false)
    					.setPositiveButton("Summary Page",new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog,int id) {
    							
    							LayoutInflater li = LayoutInflater.from(PairsActivity.this);
    	    					View promptsView = li.inflate(R.layout.summary_page,null);
    	    					  					
    	    					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PairsActivity.this);
    	    					alertDialogBuilder.setView(promptsView);
    	    					alertDialogBuilder.setTitle("Summary");
    	    					
    	    					VocabAdapterSummary adapter = new VocabAdapterSummary(PairsActivity.this, R.layout.summary_vocab_row,values);
    	    				    //_adapter = adapter;
    	    				    
    	    				    ListView listView1 = (ListView)promptsView.findViewById(R.id.list_summary);
    	    				     
    	    				    listView1.setAdapter(adapter);
    	    					
	    					
    	    					alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
    	    					{
    	    						public void onClick(DialogInterface dialog, int id)
    	    						{
    	    							
    	    						}
    	    					});
    	    					
    	    					AlertDialog alertDialog = alertDialogBuilder.create();
    	    					alertDialog.show();
    							
    						}
    					  })
    					.setNegativeButton("Play again",new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog,int id) {
    							// if this button is clicked, just close
    							// the dialog box and do nothing
    							choiceDialog.show();
    							dialog.cancel();
    						}
    					});
    	 
    					// create alert dialog
    					AlertDialog alertDialog = alertDialogBuilder.create();
    	 
    					// show it
    					alertDialog.show();
    				
    		}
    	}
    	
 
    }

    
    private void buildPairs(List<String> randomSet, int type)
    {
    	Vocab vocab;
    	TableRow TR;
    	int count = 0;
    	Resources res = getResources();
    	LayoutInflater inflater = getLayoutInflater();
    	TableLayout table = (TableLayout) findViewById(R.id.table_pairs);
    	//Log.d("table", table.toString());
    	table.removeAllViews();
    	
    	Display display = getWindowManager().getDefaultDisplay(); 
    	int screenWidth = display.getWidth();
    	int screenHeight = display.getHeight();
    	//Log.d("Size", Integer.toString(screenWidth));
    	//Log.d("Size", Integer.toString(screenHeight));
    	
    	int w = (int) ((screenWidth / type));
    	int h = (int) ((screenHeight / type));
    	
    	//Log.d("Size", Integer.toString(w));
    	//Log.d("Size", Integer.toString(h));
    	
    	ViewFlipper VF;
    	TextView TV;
    	TableLayout.LayoutParams tableParam = new TableLayout.LayoutParams
  			     (TableLayout.LayoutParams.WRAP_CONTENT,
  			     TableLayout.LayoutParams.WRAP_CONTENT,1f);
    	RelativeLayout RL;
    	TableRow.LayoutParams tableRowParam = new TableRow.LayoutParams
 			     (TableRow.LayoutParams.WRAP_CONTENT,
 			     TableRow.LayoutParams.WRAP_CONTENT,1f);
    	tableRowParam.gravity = Gravity.CENTER;
    	RelativeLayout.LayoutParams rlParam = new RelativeLayout.LayoutParams
			     (w, h);
 			     
    	
    	for(int i = 0; i < type; i++)
    	{   		
    		TR = new TableRow(this);
        	TR.setLayoutParams(tableParam);
        	TR.setGravity(Gravity.CENTER);
        	
    		for(int j = 0; j < type; j++)
    		{
    			//vocab = values.get(count);
    			//count++;
    			
    			VF = (ViewFlipper) inflater.inflate(R.layout.pairs_vf, null);
    			RelativeLayout rlImage = (RelativeLayout) VF.getChildAt(0);
    			ImageView image = (ImageView) rlImage.getChildAt(0);    			
    			TR.addView(VF);
    			image.setLayoutParams(rlParam);
    			VF.setLayoutParams(tableRowParam);
    			
    			RelativeLayout rlTextView = (RelativeLayout) VF.getChildAt(1);
    			TV = (TextView) rlTextView.getChildAt(0);
    			//TV.setText(vocab.getChinese());
    			TV.setText(randomSet.get(count));
    			count++;
    			
    			VF.setOnLongClickListener(new OnLongClickListener() { 
    		        @Override
    		        public boolean onLongClick(View v) {
    		        	
    		        	ViewFlipper vf = (ViewFlipper) v;
    		        	RelativeLayout rlTextView = (RelativeLayout) vf.getChildAt(1);
    	    			TextView TV = (TextView) rlTextView.getChildAt(0);
    		        	
    		            // TODO Auto-generated method stub
    		        	//Toast.makeText(PairsActivity.this, "shown!", Toast.LENGTH_SHORT).show();
    		        	// custom dialog
    					LayoutInflater li = LayoutInflater.from(PairsActivity.this);
    					View promptsView = li.inflate(R.layout.pairs_hint_dialog,null);
    					  					
    					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PairsActivity.this);
    					alertDialogBuilder.setView(promptsView);
    					alertDialogBuilder.setTitle("Explanation");
    					
    					chinese = (TextView) promptsView.findViewById(R.id.pairs_hint_chinese);
    					english = (TextView) promptsView.findViewById(R.id.pairs_hint_english);
    					pinyin = (TextView) promptsView.findViewById(R.id.pairs_hint_pinyin);
    					audio = (ImageView) promptsView.findViewById(R.id.pairs_audio);    				   		
    					
    					String CH = TV.getText().toString();
    					chinese.setText(CH);
    					
    					Boolean valid = CH.matches("^[\u0000-\u0080]+$");
    	    			if(valid)
    	    			{
    	    				return true;

    	    			}
    					
    					alertDialogBuilder.setTitle(CH);
    					
    					//audio.setOnClickListener(l)
    					datasource = new VocabDataSource(PairsActivity.this);
    	    	        datasource.open();
    	 				
    	    	        Vocab vocab = datasource.getVocab(CH);
    	    	        String EN = vocab.getEnglish();
    	    	        String PY = vocab.getPronunciation();
    	    	        audioPath = vocab.getAudioPath();
    	    	        
    	    	        english.setText(EN);
    	    	        pinyin.setText(PY);
    	    	        
    	    	        
    	    	        audio.setOnClickListener(new OnClickListener() {
 						   @Override
 						   public void onClick(View v) {
 							  try {
 								  if(audioPath!=null)
 									  TranslateHelperClass.playWav(audioPath);
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
 						   }
 						  });
    	    	        
    	    	        
    	    	        datasource.close();
    					
    					alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
    					{
    						public void onClick(DialogInterface dialog, int id)
    						{
    							
    						}
    					});
    					
    					AlertDialog alertDialog = alertDialogBuilder.create();
    					alertDialog.show();
						return true;
    					
    		        }
    		    });
    			  			
    		}
    		
    		table.addView(TR);
    	}
    	
    	
    	
    }

    public class buildCards extends AsyncTask<Void, Integer, List<Vocab>>
    {
    	@Override
		protected List<Vocab> doInBackground(Void... arg0) 
		{
    		 try
    	      {	    	  	    	 
    			publishProgress(1);
    			
    			//Connect to the database to retrieve the vocabs from the library
    	        datasource = new VocabDataSource(PairsActivity.this);
    	        datasource.open();
 				
    	        //values = datasource.getAllVocab();
    	        //subjects = datasource.getSubjects();
      	        values = datasource.getVocabBySubject(selected_subject);
    	        
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
            progress("Setting up game...");
			mProgressDialog.show();
        }
    	
    	//Process the results
    	@Override
		protected void onPostExecute(List<Vocab> result)
		{	
    		int count = 0;
    		for(int i = 0; i < values.size(); i++)
			{
				if(values.get(i).getEnglish().length() < 15)
				{
					count++;
					//Log.d("vocab count", Integer.toString(count));
				}
			}
			if(count > 8 || type == 2)
			{
				getRandomSet();
				List<String> randomSet = getRandomSet(type);
	    		buildPairs(randomSet,type);		
			}
			else
			{
				Toast.makeText(PairsActivity.this, "There are not enough words for 4x4 mode, please select 2x2", Toast.LENGTH_SHORT).show();

				choiceDialog.show();
			}
			
    		
			mProgressDialog.cancel();				
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
    
    public class buildChoice extends AsyncTask<Void, Integer, List<Vocab>>
    {
    	@Override
		protected List<Vocab> doInBackground(Void... arg0) 
		{
    		 try
    	      {	    	  	    	 
    			publishProgress(1);
    			
    			//Connect to the database to retrieve the vocabs from the library
    	        datasource = new VocabDataSource(PairsActivity.this);
    	        datasource.open();
 				
    	        //values = datasource.getAllVocab();
    	        subjects = datasource.getSubjects();
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
				//Log.d("subjects", subject);
				list.add(subject);
			}
			//list.add("All");
					      			
			sp1= (Spinner) promptsView.findViewById(R.id.spinner_subject);
		    //Log.d("spinner", sp1.toString());
			ArrayAdapter<String> adp1= new ArrayAdapter<String>(PairsActivity.this,android.R.layout.simple_list_item_1,list);
			adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp1.setAdapter(adp1);
			sp1.setOnItemSelectedListener(PairsActivity.this);
			
			list = new ArrayList<String>();
			list.add("Easy - 2x2");
			list.add("Hard - 4x4");
			
			sp2= (Spinner) promptsView.findViewById(R.id.spinner_difficulty);
		    
			adp1= new ArrayAdapter<String>(PairsActivity.this,android.R.layout.simple_list_item_1,list);
			adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp2.setAdapter(adp1);
			sp2.setOnItemSelectedListener(PairsActivity.this);
			
			type = 2;
		
			mProgressDialog.cancel();		
			
			//Open dialog for user to choose subject and difficulty
//			LayoutInflater li = LayoutInflater.from(PairsActivity.this);
//	    	View promptsView = li.inflate(R.layout.choose_subject,null);
	    	  					
			choiceBuilder = new AlertDialog.Builder(PairsActivity.this);
			choiceBuilder.setView(promptsView);
			choiceBuilder.setTitle("Choose subjects & difficulty");
	    	
			choiceBuilder.setCancelable(false).setPositiveButton("Start Game!", new DialogInterface.OnClickListener()
	    	{
	    	public void onClick(DialogInterface dialog, int id)
    		{
	    		selected_subject = sp1.getSelectedItem().toString();
	    		String typeString = sp2.getSelectedItem().toString();
	    		
	    		if(typeString.equalsIgnoreCase("Easy - 2x2"))
				{
	    			type = 2;

				}
	    		else 
    			{
	    			type = 4;

    			}
	    		new buildCards().execute();		
	    		
	    		
    		}
	    	});
			choiceDialog = choiceBuilder.create();
			choiceDialog.show();
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
    
    private List<String> getRandomSet(int type)
    {
    	Random rand;
    	Vocab vocab;
    	List<Vocab> tempList = new ArrayList<Vocab>();
    	List<Vocab> copyList = new ArrayList<Vocab>();
    	List<String> stringSet = new ArrayList<String>();
    	List<String> newSet = new ArrayList<String>();
    	answer = new ArrayList<String>();
    	
    	for(int i = 0; i < values.size(); i++)
    	{
    		if(values.get(i).getEnglish().length() < 15)
    		{
    			copyList.add(values.get(i));
    			//Log.d("added item", values.get(i).getChinese());
    		}
    	}
    	
    	for(int i = 0; i < (type*type)/2; i++)
    	{
    		Vocab temp;
    		rand = new Random();
    		
    		int x = rand.nextInt(copyList.size());  	 		
    		
    		temp = copyList.get(x);
    		tempList.add(temp);
    		copyList.remove(temp);
    		
    		stringSet.add(temp.getChinese());
    		stringSet.add(temp.getEnglish());
    		answer.add(temp.getChinese());
    		answer.add(temp.getEnglish());
    	}
    	
    	values = tempList;

    	for(int i = 0; i < (type*type); i++)
    	{
    		rand = new Random();
    		int x = rand.nextInt(stringSet.size());
    		newSet.add(stringSet.get(x));
    		stringSet.remove(stringSet.get(x));  		
    	}
    	return newSet;
    }
    

    public class MyTimer extends CountDownTimer {
    	private ViewFlipper vf;
        public MyTimer(long millisInFuture, long countDownInterval, ViewFlipper vf) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
            //tv.setText("changed by the constructor");
            this.vf = vf;
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
        	AnimationFactory.flipTransition(vf, FlipDirection.LEFT_RIGHT, 200);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            //tv.setText("time: " + millisUntilFinished);
        }

    }

	@Override
	public boolean onLongClick(View arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(PairsActivity.this, "shown!", Toast.LENGTH_SHORT).show();
		return false;
		
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
	
    
}


