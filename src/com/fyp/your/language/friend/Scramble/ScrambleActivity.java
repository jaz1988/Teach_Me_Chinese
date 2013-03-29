package com.fyp.your.language.friend.Scramble;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import com.fyp.your.language.friend.R;

import com.fyp.your.language.friend.HomeActivity;
import com.fyp.your.language.friend.StartActivity;
import com.fyp.your.language.friend.TranslateHelperClass;
import com.fyp.your.language.friend.Database.VocabDataSource;
import com.fyp.your.language.friend.Flashcard.FlashcardActivity;
import com.fyp.your.language.friend.Input.SearchActivity;
import com.fyp.your.language.friend.Library.Vocab;
import com.fyp.your.language.friend.ListAdapters.VocabAdapter;
import com.fyp.your.language.friend.ListAdapters.VocabAdapterSummary;
import com.fyp.your.language.friend.Pairs.PairsActivity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class ScrambleActivity extends Activity implements OnItemSelectedListener, OnClickListener {
  
/** Called when the activity is first created. */

	List<View> LinearTopList;
	View LinearBot,LinearTop,draggedView;
	private ProgressDialog mProgressDialog;
	private VocabDataSource datasource;
	List<Vocab> values;
	private int grammarIndex;
	String answer;
	ImageView imageResult;
	List<Vocab> helpList;
	AlertDialog.Builder alertDialogBuilder;
	List<String> subjects;
	Spinner sp1;
	String randomedGrammar;
	VocabAdapterSummary _adapter;
	String audioPath;
	CheckBox cb;
	
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.scramble_main);
    
    LinearTopList = new ArrayList<View>();
    helpList = new ArrayList<Vocab>();
    
    LinearTop = findViewById(R.id.LinearTop);
    LinearBot = findViewById(R.id.LinearBot);
    imageResult = (ImageView) findViewById(R.id.result);
    
    SharedPreferences statePref = getSharedPreferences("Pref", 0);
	Boolean show = statePref.getBoolean("scramble", true);
         
	if(show)
	{
	    LayoutInflater li = LayoutInflater.from(ScrambleActivity.this);
		View promptsView = li.inflate(R.layout.scamble_instruction,null);
		cb = (CheckBox) promptsView.findViewById(R.id.cb_scramble);
		  					
		alertDialogBuilder = new AlertDialog.Builder(ScrambleActivity.this);
		alertDialogBuilder.setView(promptsView);
		alertDialogBuilder.setTitle("Instructions");
		  				   		
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				grammarIndex = 0;
			    new getGrammar().execute();
			}
		});
		
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	else
	{
		grammarIndex = 0;
	    new getGrammar().execute();
	}

  }
  
  public void onBackPressed() 
	{
	} 
  
  public void checkClick(View view)
  {
  	//Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
  	if(cb.isChecked())
  	{
  		SharedPreferences statePref = getSharedPreferences("Pref", 0);
  		SharedPreferences.Editor stateEditor = statePref.edit();
  		stateEditor.putBoolean("scramble", false);
			stateEditor.commit();    	
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
  	  Intent intent = new Intent(ScrambleActivity.this,HomeActivity.class);
  	  startActivity(intent);  	  
  	finish();
  	  break;   	  
    case R.id.search:    	  
	  intent = new Intent(ScrambleActivity.this,SearchActivity.class);
	  startActivity(intent);  	  
	  finish();
	  break;   	  
    default:
      break;
    }
    return true;
  } 
  
  @Override
	public void onClick(View view) {
	  
	  switch (view.getId()) {
      case R.id.next:    	  
    	  imageResult.setImageResource(R.drawable.cross);
    	  grammarIndex++;
    	  if(grammarIndex == values.size())
    	  {
    		  grammarIndex = 0;
    	  }
    	  LinearTopList = new ArrayList<View>();
    	  buildScramble();
    	  //Toast.makeText(this, "zz", Toast.LENGTH_SHORT).show();
    	  break;   	  
      case R.id.previous: 
    	  imageResult.setImageResource(R.drawable.cross);
    	  grammarIndex--;
    	  if(grammarIndex == -1)
    	  {
    		  grammarIndex = values.size() - 1;
    	  }
    	  LinearTopList = new ArrayList<View>();
    	  buildScramble();
    	  break;
      case R.id.hint:
    	  
    	 
      	//Toast.makeText(ScrambleActivity.this, "shown!", Toast.LENGTH_SHORT).show();
      	// custom dialog
		LayoutInflater li = LayoutInflater.from(ScrambleActivity.this);
		View promptsView = li.inflate(R.layout.pairs_hint_dialog,null);
		  					
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScrambleActivity.this);
		alertDialogBuilder.setView(promptsView);
		alertDialogBuilder.setTitle("Answer");
		
		TextView chinese = (TextView) promptsView.findViewById(R.id.pairs_hint_chinese);
		TextView english = (TextView) promptsView.findViewById(R.id.pairs_hint_english);
		TextView pinyin = (TextView) promptsView.findViewById(R.id.pairs_hint_pinyin);
		ImageView audio = (ImageView) promptsView.findViewById(R.id.pairs_audio);    				   		
		
		Vocab vocab = values.get(grammarIndex);
		chinese.setText(vocab.getChinese());
		english.setText(vocab.getEnglish());
        pinyin.setText(vocab.getPronunciation());
		alertDialogBuilder.setTitle(vocab.getChinese());
		
        audioPath = vocab.getAudioPath();
     
        
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
        
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				
			}
		});
		
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
    	  	  
    	  break;   	  
      default:
        break;
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
  
  public class getGrammar extends AsyncTask<Void, Integer, List<Vocab>>
  {
  	@Override
		protected List<Vocab> doInBackground(Void... arg0) 
		{
  		 try
  	      {	    	  	    	 
  			publishProgress(1);
  			
  			//Connect to the database to retrieve the vocabs from the library
  	        datasource = new VocabDataSource(ScrambleActivity.this);
  	        datasource.open();
					   
  	        subjects = datasource.getSubjects();
  	        values = datasource.getGrammarBySubject(subjects.get(0));
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
          progress("Setting up game...");
			mProgressDialog.show();
      }
  	
  	//Process the results
  	@Override
		protected void onPostExecute(List<Vocab> result)
		{	
	  		//List<String> randomSet = getRandomSet(type);
	  		String subject;
			String number;
	
			List<String> list=new ArrayList<String>();
	    
			for(int i = 0; i < subjects.size(); i++)
			{
				subject = subjects.get(i);
				//Log.d("subjects", subject);
				list.add(subject);
			}
			//list.add("All");
					      			
			sp1= (Spinner) findViewById(R.id.spinner1);
		    
			ArrayAdapter<String> adp1= new ArrayAdapter<String>(ScrambleActivity.this,android.R.layout.simple_list_item_1,list);
			adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp1.setAdapter(adp1);
			sp1.setOnItemSelectedListener(ScrambleActivity.this);
			
			getRandomSet();
	  		buildScramble();
//	  		AlertDialog alertDialog = alertDialogBuilder.create();
//	  		alertDialog.show();
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
	  
	  private void buildScramble()
	  {
		  Vocab vocabGrammar = values.get(grammarIndex);
		  String chineseGrammar = vocabGrammar.getChinese();
		  answer = chineseGrammar;
		  //Log.d("chinese grammar", chineseGrammar);
		  int type = chineseGrammar.length();
		  double multiplier_125 = 1.25;
		  
		  randomedGrammar = getRandomSet(chineseGrammar);
		  
		  Resources res = getResources();
		  LayoutInflater inflater = getLayoutInflater();
		  
		  LinearLayout LL_Blank, LL_Filled;
		  RelativeLayout RL;
		  TextView TV;
		  int outRL_w;
		  
		  Display display = getWindowManager().getDefaultDisplay(); 
		  int linear_w = (int) (display.getWidth() * 0.7);
		  	  
		  if(type < 3)
		  {
			  multiplier_125 = 1.00;
			  
		  }
		  else
		  {
			  multiplier_125 = 1.25;
			 
		  }
		  outRL_w = linear_w / type;
		  int outRL_h = (int) (outRL_w * multiplier_125);
		  int linear_h = outRL_h;
		  
		  int inRL_w = (int) (outRL_w * 0.8);
		  int inRL_h = (int) (inRL_w * 1.25);
      	
		  int image_w_h;
		  if(type > 5)
		  {
			  image_w_h = (int) (outRL_h * 0.7);
		  }
		  else
			  image_w_h = (int) (outRL_h * 0.5);
		  
		  int textSize = inRL_w / 3 ;
		  
		  LinearLayout linearTop = (LinearLayout) findViewById(R.id.LinearTop);
		  LinearLayout linearBot = (LinearLayout) findViewById(R.id.LinearBot);
		  ImageView imageResult = (ImageView) findViewById(R.id.result);
		  ImageView imageHint = (ImageView) findViewById(R.id.hint);
		  
		  imageHint.getLayoutParams().width = image_w_h;
		  imageHint.getLayoutParams().height = image_w_h;
		  
		  imageResult.getLayoutParams().width = image_w_h;
		  imageResult.getLayoutParams().height = image_w_h;
		  
		  linearTop.getLayoutParams().width = linear_w;
		  linearTop.getLayoutParams().height = linear_h;
		  
		  linearBot.getLayoutParams().width = linear_w;
		  linearBot.getLayoutParams().height = linear_h;
		  
		  linearTop.removeAllViews();
		  linearBot.removeAllViews();
		  		  
//		  LinearLayout.LayoutParams llParamCard = new LinearLayout.LayoutParams(linear_w, linear_h, 0);
//		  llParamCard.gravity = Gravity.CENTER;
		  
//		  LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(inRL_w, inRL_h, 0);
//		  linearTop.setLayoutParams(llParam);
//		  linearBot.setLayoutParams(llParam);
//		  linearTop.setGravity(Gravity.CENTER);

		  	  
		  for(int i = 0; i < type; i++)
		  {
			  LL_Blank = (LinearLayout) inflater.inflate(R.layout.scramble_blank_card, null);
			  LL_Filled = (LinearLayout) inflater.inflate(R.layout.scramble_text_card, null);
			  
			  linearTop.addView(LL_Blank);
			  linearBot.addView(LL_Filled);
			  
			  LL_Blank.getLayoutParams().width = outRL_w;
			  LL_Blank.getLayoutParams().height = outRL_h;
			  LL_Filled.getLayoutParams().width = outRL_w;
			  LL_Filled.getLayoutParams().height = outRL_h;
			  
			  LL_Blank.setGravity(Gravity.CENTER);
			  LL_Filled.setGravity(Gravity.CENTER);
			  
			  LL_Blank.getChildAt(0).getLayoutParams().width = inRL_w;
			  LL_Blank.getChildAt(0).getLayoutParams().height = inRL_h;
			  
			  LL_Filled.getChildAt(0).getLayoutParams().width = inRL_w;
			  LL_Filled.getChildAt(0).getLayoutParams().height = inRL_h;
			  
			  RL = (RelativeLayout) LL_Filled.getChildAt(0);
			  TV = (TextView) RL.getChildAt(0);
			  TV.setTextSize(textSize);
			  TV.setText(Character.toString(randomedGrammar.charAt(i)));
			  			  
			  LL_Filled.setOnTouchListener(new MyTouchListener());
			  LL_Blank.setOnDragListener(new MyDragListener());
			  			
		      LinearTopList.add(LL_Blank);
		      
		  }
		  
		  linearTop.invalidate();
		  linearBot.invalidate();
		  
		  
		  new getVocab().execute(randomedGrammar);
		  
		  TV = (TextView) findViewById(R.id.tv_scramble_count);
		  TV.setTextSize((float) (textSize*0.5));
		  String text = TV.getText().toString();
		  int total = values.size();
		  String show = Integer.toString((grammarIndex + 1)) + "/" + Integer.toString(total);
		  TV.setText(show);
	  }
	  
	  public class getVocab extends AsyncTask<String, Integer, List<Vocab>>
	  {
	  	@Override
			protected List<Vocab> doInBackground(String... arg0) 
			{
	  		 try
	  	      {	    	  	    	 
	  			publishProgress(1);
	  			
	  			//Connect to the database to retrieve the vocabs from the library
	  	        datasource = new VocabDataSource(ScrambleActivity.this);
	  	        datasource.open();
	  	        
	  	        Vocab temp;
	  	        //Log.d("help list check", arg0[0].length() + "," + arg0[0]);
	  	        helpList = new ArrayList<Vocab>();
	  	        
	  	        for(int i = 0; i < arg0[0].length(); i++)
	  	        {
	  	        	temp = datasource.getVocab(String.valueOf(arg0[0].charAt(i)));
	  	        	if(temp.getChinese()!=null) helpList.add(temp);
	  	        	//Log.d("help list check", temp.getChinese());
	  	        	//Log.d("help list check", i + "");
	  	        }
	  	        
	  	        //datasource.close();
	  	       	         	
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
	  			_adapter = new VocabAdapterSummary(ScrambleActivity.this, R.layout.summary_vocab_row,helpList);
	  			ListView listView1 = (ListView)findViewById(R.id.scramble_list);
			    listView1.setAdapter(_adapter);
			    updateListAdapter();
				mProgressDialog.cancel();				
			}
		}
	  
	  private String getRandomSet(String grammar)
	    {
		  	List<String> grammarList = new ArrayList<String>();
		  	for(int i = 0; i < grammar.length(); i++)
	    	{
		  		grammarList.add(Character.toString(grammar.charAt(i)));		  	
	    	}
	    	Random rand;
	    	Vocab vocab;
	    	String newString = "";
	    	String tempString;
	    	int count = grammar.length();
	    	
	    	for(int i = 0; i < count; i++)
	    	{
	    		//Log.d("rand grammar", grammar);
	    		//Log.d("new string", newString);
	    		rand = new Random();
	    		int x = rand.nextInt(grammarList.size());
	    		newString += grammarList.get(x);
	    		
	    		grammarList.remove(x);    
	    		
	    		for(int j = 0; j < grammarList.size(); j++)
		    	{
	    			//Log.d("grammar list", grammarList.get(j));
		    	}
	    		//grammar = grammar.trim();		
	    		//Log.d("rand grammar 2", grammarList.get(i));
	    		//Log.d("new string 2", newString);
	    	}
	    	return newString;
	    }
	  
	  String replaceFirstFrom(String str, int from, String regex, String replacement)
	  {
	      String prefix = str.substring(0, from);
	      String rest = str.substring(from);
	      rest = rest.replaceFirst(regex, replacement);
	      return prefix+rest;
	  }
	
	  private final class MyTouchListener implements OnTouchListener {
	    public boolean onTouch(View view, MotionEvent motionEvent) {
	      if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
	        ClipData data = ClipData.newPlainText("", "");
	        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
	        view.startDrag(data, shadowBuilder, view, 0);
	        view.setVisibility(View.INVISIBLE);
	        return true;
	      } else {
	        return false;
	      }
	    }
	  }
	
	  class MyDragListener implements OnDragListener {
	    Drawable enterShape = getResources().getDrawable(R.drawable.item_body);
	    Drawable normalShape = getResources().getDrawable(R.drawable.item_body);
	
	    @Override
	    public boolean onDrag(View v, DragEvent event) {
	      int action = event.getAction();
	      View dropView = v;
	      switch (event.getAction()) {
	      case DragEvent.ACTION_DRAG_STARTED:
	        // Do nothing
	        break;
	      case DragEvent.ACTION_DRAG_ENTERED:
	        //v.setBackgroundDrawable(enterShape);
	        break;
	      case DragEvent.ACTION_DRAG_EXITED:
	        //v.setBackgroundDrawable(normalShape);
	        break;
	      case DragEvent.ACTION_DROP:
	        // Dropped, reassign View to ViewGroup
	        draggedView = (View) event.getLocalState();
	        
	        
	        //Check if dragged item is from LinearTop or LinearBot
	        int fromTop=0,fromBot=0;
	        int toTop=0,toBot=0;
	        int draggedIndex=0,dropIndex=0;
	        
	        for(int i = 0; i < LinearTopList.size(); i++)
	        {
	        	//view found in droplist, the view is from LinearTop
	        	if(LinearTopList.get(i) == draggedView)
	        	{
	        		fromTop = 1;
	        		draggedIndex = i;
	        		break;
	        	}
	        }
	        
	        for(int i = 0; i < LinearTopList.size(); i++)
	        {
	        	//view found in droplist, the view is from LinearTop
	        	if(LinearTopList.get(i) == dropView)
	        	{
	        		toTop = 1;
	        		dropIndex = i;
	        		break;
	        	}
	        }
	        
	        //Check if dragged item dropped to LinearTop or LinearBot
	        if(toTop==1)
	        {
	        	//Check if dragged item is from LinearTop or LinearBot
	        	if(fromTop == 1)
	        	{
	        		//Log.d("Drag n Drop", "Dragged item from Top, dropped at LinearTop");
	        		//swap dragged view with drop view
	        		LinearTopList.set(dropIndex, draggedView);
	        		LinearTopList.set(draggedIndex, dropView);
	            	
	            	//Remove all views
	            	ViewGroup owner = (ViewGroup) dropView.getParent();
	                owner.removeAllViews();
	                
	                //Add all again in the right order
	                for(int i = 0; i < LinearTopList.size(); i++)
	                {
	                	owner.addView(LinearTopList.get(i));
	                }
	                draggedView.setVisibility(View.VISIBLE);
	                
	                //Check for match in answer
	                LinearLayout LL;
	                RelativeLayout RL;
	                TextView TV;
	                String checkAns = "";
	                for(int i = 0; i < LinearTopList.size(); i++)
	                {
	                	LL = (LinearLayout) LinearTopList.get(i);
	                	RL = (RelativeLayout) LL.getChildAt(0);
	                	TV = (TextView) RL.getChildAt(0);
	                	checkAns += TV.getText();
	                }
	                if(checkAns.equalsIgnoreCase(answer))
	                {
	                	Toast.makeText(ScrambleActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
	                	imageResult.setImageResource(R.drawable.tick);
	                }
	                else
	                {
	                	Toast.makeText(ScrambleActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
	                	imageResult.setImageResource(R.drawable.cross);
	                }
	        	}
	        	else //fromBot
	        	{
	        		//Log.d("Drag n Drop", "Dragged item from Bot, dropped at LinearTop");
	            	ViewGroup owner = (ViewGroup) draggedView.getParent();
	                owner.removeView(draggedView);
	                View tempView = dropView;
	                
	                int index = 0;
	                //Get location of dropped location
	                for(int i = 0; i < LinearTopList.size(); i++)
	                {
	                	if(LinearTopList.get(i) == dropView)
	                	{
	                		index = i;
	                		break;
	                	}
	                }
//	                for(int i = 0; i < LinearTopList.size(); i++)
//	                {
//	                	Log.d("Before Replace", LinearTopList.get(i).toString());
//	                }
	                //Replace it with the drag view
	                LinearTopList.set(index, draggedView);
	                for(int i = 0; i < LinearTopList.size(); i++)
	                {
	                	//Log.d("After Replace", LinearTopList.get(i).toString());
	                }
	                
	                //Remove all views
	                owner = (ViewGroup) dropView.getParent();
	                owner.removeAllViews();
	                
	                //Add all again in the right order
	                for(int i = 0; i < LinearTopList.size(); i++)
	                {
	                	owner.addView(LinearTopList.get(i));
	                }             
	                draggedView.setVisibility(View.VISIBLE);
	                draggedView.setOnDragListener(new MyDragListener());
	                
	                //dropView.setOnDragListener(null);
	                
	                //Add the blank card to LinearBot
	                ViewGroup LB = (ViewGroup) LinearBot;              
	                LB.addView(tempView);    
	                
	                //Check for match in answer
	                LinearLayout LL;
	                RelativeLayout RL;
	                TextView TV;
	                String checkAns = "";
	                for(int i = 0; i < LinearTopList.size(); i++)
	                {
	                	LL = (LinearLayout) LinearTopList.get(i);
	                	RL = (RelativeLayout) LL.getChildAt(0);
	                	TV = (TextView) RL.getChildAt(0);
	                	checkAns += TV.getText();
	                }
	                if(checkAns.equalsIgnoreCase(answer))
	                {
	                	//Toast.makeText(ScrambleActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
	                	imageResult.setImageResource(R.drawable.tick);
	                }
	                else
	                {
	                	//Toast.makeText(ScrambleActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
	                	imageResult.setImageResource(R.drawable.cross);
	                }
	        	}
	        }
	        else //dragged item dropped to LinearBot
	        {
	        	//Check if dragged item is from LinearTop or LinearBot
	        	if(fromTop == 1)
	        	{
	        		//Log.d("Drag n Drop", "Dragged item from Top, dropped at LinearBot");
	        		
	        		//Remove drop view from LinearBot
	        		ViewGroup owner = (ViewGroup) dropView.getParent();
	        		owner.removeView(dropView);
	        		
	        		//Replace item from LinearTop with view at LinearBot
	        		LinearTopList.set(draggedIndex, dropView);
	        		
	        		
	        		//Build all again       		
	        		owner = (ViewGroup) draggedView.getParent();
	        		owner.removeAllViews();
	        		for(int i = 0; i < LinearTopList.size(); i++)
	                {
	        			owner.addView(LinearTopList.get(i));
	                }        		
	        		
	        		//Add again
	        		//Add the blank card to LinearBot
	                ViewGroup LB = (ViewGroup) LinearBot;              
	                LB.addView(draggedView);      
	        		draggedView.setVisibility(View.VISIBLE);
	        		draggedView.setOnDragListener(null);
	        	}
	        	else //fromBot
	        	{
	        		draggedView.setVisibility(View.VISIBLE);
	        	}
	        }
	        
	        
	       
	        break;
	      case DragEvent.ACTION_DRAG_ENDED:
				if (dropEventNotHandled(event)) {
					
					draggedView = (View) event.getLocalState();
					draggedView.post(new Runnable(){
	
						@Override
						public void run() {
							// TODO Auto-generated method stub
							draggedView.setVisibility(View.VISIBLE);
						}
				       
				    });
	          }
	      default:
	        break;
	      }
	      return true;
	    }
	  }
	  private boolean dropEventNotHandled(DragEvent dragEvent) {
	      return !dragEvent.getResult();
		}
	  
	  private void updateListAdapter()
		{
		   
		    runOnUiThread(new Runnable()
		    {
		        public void run()
		        {
		           
		            _adapter.notifyDataSetChanged();
		        }
		    });
		}
	  
	  @Override
		public void onItemSelected(AdapterView<?> parent, View v, int position,
				long id) {
			// TODO Auto-generated method stub
//			Toast.makeText(parent.getContext(), 
//					"OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
//					Toast.LENGTH_SHORT).show();

			datasource = new VocabDataSource(this);
			datasource.open();
			
			values = datasource.getGrammarBySubject(parent.getItemAtPosition(position).toString());
			grammarIndex = 0;
			LinearTopList = new ArrayList<View>();
			getRandomSet();
			buildScramble();
			
			datasource.close();
			
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		
		}
		
} 