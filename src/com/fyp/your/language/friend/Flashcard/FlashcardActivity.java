package com.fyp.your.language.friend.Flashcard;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;


import com.fyp.your.language.friend.R;
import com.fyp.your.language.friend.HomeActivity;
import com.fyp.your.language.friend.StartActivity;
import com.fyp.your.language.friend.TestResultsActivity;
import com.fyp.your.language.friend.TranslateHelperClass;
import com.fyp.your.language.friend.Animation.AnimationFactory;
import com.fyp.your.language.friend.Animation.AnimationFactory.FlipDirection;
import com.fyp.your.language.friend.Database.VocabDataSource;
import com.fyp.your.language.friend.Input.SearchActivity;
import com.fyp.your.language.friend.Input.VocabularyInputActivity;
import com.fyp.your.language.friend.Library.LibraryActivity;
import com.fyp.your.language.friend.Library.Vocab;
import com.fyp.your.language.friend.ListAdapters.VocabAdapter;
import com.fyp.your.language.friend.Scramble.ScrambleActivity;


public class FlashcardActivity extends Activity implements OnClickListener, OnGesturePerformedListener, OnItemSelectedListener {


	ViewFlipper flippy; //ViewFlipper used to flip the card with animation
	Boolean side; //Which face of the card is currently showing (true==foreign word side, false==meaning side)
	private GestureLibrary mLibrary; //Gesture library to detect gesture motions 
	private TextView word, meaning, pronunciation, onImage; //Respective text views
	private ImageView audio, picture; //Respective image views	
	private ViewFlipper vfContainer;
	private VocabDataSource datasource;
	private ProgressDialog mProgressDialog;
	private int currentVocab;
	String audioPath, picturePath;
	List<Vocab> values;
	List<String> subjects;
	int started, cardsCount, cardsIndex;
	Animation animationFlipIn;
    Animation animationFlipOut;
    Spinner sp1;
    CheckBox cb;
    private final int[] colors = new int[] {R.drawable.color1, R.drawable.color2, R.drawable.color3, R.drawable.color4, R.drawable.color5 };
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              
        setContentView(R.layout.flashcard_main);
        
        started = 0;
        
        //onImage = (TextView) findViewById(R.id.tv_onimage);
        audio = (ImageView) findViewById(R.id.iv_sound);
        picture = (ImageView) findViewById(R.id.iv_picture);
        
        vfContainer = (ViewFlipper) findViewById(R.id.vfContainer);
        
        
              
       //Load gesture library
        mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!mLibrary.load()) {
            finish();
        }
        
      
        //Initialize face of the card to true (Foreign Word card)
        side = true;
        
        SharedPreferences statePref = getSharedPreferences("Pref", 0);
		Boolean show = statePref.getBoolean("flashcard", true);
		
		if(show)
		{
	        LayoutInflater li = LayoutInflater.from(FlashcardActivity.this);
	    	View promptsView = li.inflate(R.layout.flashcard_instruction,null);
	    	cb = (CheckBox) promptsView.findViewById(R.id.cb_flashcard);
	    	  					
	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FlashcardActivity.this);
	    	alertDialogBuilder.setView(promptsView);
	    	alertDialogBuilder.setTitle("Instructions");
	    	
	    	  				   		
	    	alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
	    	{
	    		public void onClick(DialogInterface dialog, int id)
	    		{
	    			
	    			new buildCards().execute();
	    		}
	    	});
	    	
	    	AlertDialog alertDialog = alertDialogBuilder.create();
	    	alertDialog.show();
		}
		else 
			new buildCards().execute();
 
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
    		stateEditor.putBoolean("flashcard", false);
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
    	  Intent intent = new Intent(FlashcardActivity.this,HomeActivity.class);
    	  startActivity(intent);  	  
    	  finish();
    	  break;   	  
      case R.id.search:    	  
		  intent = new Intent(FlashcardActivity.this,SearchActivity.class);
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

		//Log.d("On Image", view.toString());
		switch (view.getId()) {
		//Translate button
	    case R.id.btn_flip:
	    	RelativeLayout rl1 = (RelativeLayout) vfContainer.getChildAt(currentVocab);
	    	ViewFlipper flip = (ViewFlipper) rl1.getChildAt(0);
	    	//Log.d("btn flip", flip.toString());
	    	
	    	AnimationFactory.flipTransition(flip, FlipDirection.LEFT_RIGHT, 500);
	    	
	    	//Card is flipped, update the face/side of the card
	    	if(side)
	    		side = false;
	    	else side = true;
	    	break;
	    	
//	    case R.id.tv_onimage:
//	    	Log.d("On Image", "clicked");
//	    	RelativeLayout parent = (RelativeLayout) view.getParent();
//	    	ImageView image = (ImageView) parent.getChildAt(1);
//	    	String path = image.getTag().toString();
//	    	Log.d("On Image", path);
//	    	if(path!=null)
//	    	{
//	    		Bitmap bmp = BitmapFactory.decodeFile(path);				
//				image.setImageBitmap(bmp);
//	    	}
//	    	break;
	    	
	    case R.id.iv_sound:
	    	String path = view.getTag().toString();
	    	if(path!=null)
	    	{
	    		try {
					TranslateHelperClass.playWav(path);
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
	    	break;
		}
	}
	
	
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
        if (predictions.size() > 0 && predictions.get(0).score > 1.0) 
		{
        	ViewFlipper vfContainer1 = (ViewFlipper) findViewById(R.id.vfContainer);
        	//Log.d("number of childs", vfContainer.getCurrentView().toString() + ", " + Integer.toString(values.size()));
        	//Log.d("number of childs 2", vfContainer.toString() + ", " + Integer.toString(vfContainer.getChildCount()));

        	
  		  	
			String path = null;
		    String action = predictions.get(0).name;
		    if ("left".equals(action)) 
		    {
		        currentVocab++;

		        if(currentVocab==values.size())
		        	currentVocab = 0;
		        	
		        animationFlipIn  = AnimationUtils.loadAnimation(this, R.anim.in_from_right);
		        animationFlipOut = AnimationUtils.loadAnimation(this, R.anim.out_to_left);
		        vfContainer.setInAnimation(animationFlipIn);
		        vfContainer.setOutAnimation(animationFlipOut);
		        
		    	vfContainer.showNext();
		    	
		    	
		    } 
		    else if ("right".equals(action)) 
		    {
		        currentVocab--;
		        
		        if(currentVocab==-1)
		        	currentVocab = values.size() - 1;

		        animationFlipIn  = AnimationUtils.loadAnimation(this, R.anim.in_from_left);
		        animationFlipOut = AnimationUtils.loadAnimation(this, R.anim.out_to_right);
		        vfContainer.setInAnimation(animationFlipIn);
		        vfContainer.setOutAnimation(animationFlipOut);
		        
		    	vfContainer.showPrevious();
		    } 
		    
		    TextView TV = (TextView) findViewById(R.id.tv_flashcard_count);
            //TV.setTextSize((float) (textSize*0.5));
  		  	String text = TV.getText().toString();
  		  	int total = values.size();
  		  	String show = Integer.toString((currentVocab + 1)) + "/" + Integer.toString(total);
  		  	TV.setText(show);

		}
        
    }
	
	//Async task to build the flash cards
	public class buildCards extends AsyncTask<Void, Integer, List<Vocab>>
    {
    	@Override
		protected List<Vocab> doInBackground(Void... arg0) 
		{
    		 try
    	      {	    	  	    	 
    			publishProgress(1);
    			
    			//Connect to the database to retrieve the vocabs from the library
    	        datasource = new VocabDataSource(FlashcardActivity.this);
    	        datasource.open();
 				
    	        //values = datasource.getAllVocab();
    	        //currentVocab = 0;	         	
    	        
    	        subjects = datasource.getSubjects();
    	        
    	        values = datasource.getVocabBySubject(subjects.get(0));
    	        currentVocab = 0;
    	        
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
            progress("Building Flashcards from Vocabulary Library...");
			mProgressDialog.show();
        }
    	
    	//Process the results
    	@Override
		protected void onPostExecute(List<Vocab> result)
		{	
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
		    
			ArrayAdapter<String> adp1= new ArrayAdapter<String>(FlashcardActivity.this,android.R.layout.simple_list_item_1,list);
			adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp1.setAdapter(adp1);
			sp1.setOnItemSelectedListener(FlashcardActivity.this);
			//datasource.close();
			
			getRandomSet();			
    		buildCardView();		
    		
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
	
	//Initialise the progress spinner
    public void progress(String msg)
    {
    	mProgressDialog = new ProgressDialog(this);
    	mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);         
    	mProgressDialog.setMessage(msg);                      
    	mProgressDialog.setCancelable(false);    
    }
    
    private void buildCardView()
    {
		  
    	LayoutInflater inflater = getLayoutInflater();
    	vfContainer = (ViewFlipper) findViewById(R.id.vfContainer);
    	vfContainer.removeAllViews();
    	
    	RelativeLayout newItemSide1 = null, newItemSide2 = null, child, vf_Container;
    	//RelativeLayout newItemSide1, newItemSide2, vf_Container;
    	TextView tvWord, tvPinYin, tvMeaning, onImage;
    	ImageView picture, audio;
    	Vocab vocab;
    	ViewFlipper vf;
    	
    	//LinearLayout child;
    	int corners_H = 0;
    	
    	currentVocab = 0;
    	
    	Display display = getWindowManager().getDefaultDisplay(); 
	  	int RL_w = (int) (display.getWidth() * 0.7);
	  	int image_w_h = (int) (display.getWidth() * 0.3);
	  	corners_H = RL_w;
		  	
    	for(int i = 0; i < values.size(); i++)
    	{
    		vocab = values.get(i);
    		
    		if(vocab.getImagePath()!=null)
    		{
    			//Log.d("Image check", "Contains Image");
    			
	    		newItemSide1 = (RelativeLayout) inflater.inflate(R.layout.flashcard_1st_item_pic, null);
	    		newItemSide2 = (RelativeLayout) inflater.inflate(R.layout.flashcard_2nd_item_pic, null);
	    		
	    		//First item
	    		child = (RelativeLayout) newItemSide1.getChildAt(0);
	    		child = (RelativeLayout) child.getChildAt(0);
	    		
	            picture = (ImageView) child.getChildAt(2);
	            audio = (ImageView) child.getChildAt(3);
	            
	            picture.setTag(vocab.getImagePath());
	            audio.setTag(vocab.getAudioPath());
	            
	            //Second item
	            child = (RelativeLayout) newItemSide2.getChildAt(0);
	    		child = (RelativeLayout) child.getChildAt(0);
	    		
	            picture = (ImageView) child.getChildAt(1);
	            audio = (ImageView) child.getChildAt(2);
	            
	            picture.setTag(vocab.getImagePath());
	            audio.setTag(vocab.getAudioPath());
	            
	            child = (RelativeLayout) newItemSide1.getChildAt(0);
	            child = (RelativeLayout) child.getChildAt(0);
			  	picture = (ImageView) child.getChildAt(2);
			  	picture.getLayoutParams().width = image_w_h;
			  	picture.getLayoutParams().height = image_w_h;
			  	//Log.d("Image Height", Integer.toString(picture.getLayoutParams().height));
			  	
			  	Bitmap bmp = BitmapFactory.decodeFile(vocab.getImagePath());		
			  	//Log.d("Image path check", vocab.getImagePath());
			  	picture.setImageBitmap(bmp);
			  	
			  	
			  	child = (RelativeLayout) newItemSide2.getChildAt(0);
	            child = (RelativeLayout) child.getChildAt(0);
			  	picture = (ImageView) child.getChildAt(1);
			  	picture.getLayoutParams().width = image_w_h;
			  	picture.getLayoutParams().height = image_w_h;
			  	
			  	bmp = BitmapFactory.decodeFile(vocab.getImagePath());		
			  	//Log.d("Image path check", vocab.getImagePath());
			  	picture.setImageBitmap(bmp);

	            
    		}
    		else
    		{
    			//Log.d("Image check", "No Image");
			  			
    			newItemSide1 = (RelativeLayout) inflater.inflate(R.layout.flashcard_1st_item_no_pic, null);
	    		newItemSide2 = (RelativeLayout) inflater.inflate(R.layout.flashcard_2nd_item_no_pic, null);
	    		
	    		//Log.d("newItemSide1 check", newItemSide1.toString());
	    		
	    		//First item
	    		child = (RelativeLayout) newItemSide1.getChildAt(0);
	    		//int colorPos = i % colors.length;
	    		//child.setBackgroundResource(colors[colorPos]);
	    		child = (RelativeLayout) child.getChildAt(0);   
	    		
	            audio = (ImageView) child.getChildAt(2);	            
	            audio.setTag(vocab.getAudioPath());
	            
	            //Second item
	            child = (RelativeLayout) newItemSide2.getChildAt(0);
	    		child = (RelativeLayout) child.getChildAt(0);   
	    		
	            audio = (ImageView) child.getChildAt(1);	            
	            audio.setTag(vocab.getAudioPath());

	           
    		}
    		//Log.d("newItemSide1 check2", newItemSide1.toString());
    		
    		

    		child = (RelativeLayout) newItemSide1.getChildAt(0);
    		child = (RelativeLayout) child.getChildAt(0);
            tvWord = (TextView) child.getChildAt(0);
            tvPinYin = (TextView) child.getChildAt(1);
            tvWord.setText(vocab.getChinese());
            tvPinYin.setText(vocab.getPronunciation());
            

            child = (RelativeLayout) newItemSide2.getChildAt(0);
            child = (RelativeLayout) child.getChildAt(0);
            tvMeaning = (TextView) child.getChildAt(0);
            
            if(vocab.getEnglish().length() > 20)
            {
            	
            	float temp1 = tvMeaning.getTextSize();
            	float temp2 = (float) (temp1 * 0.8);
            	//Log.d("change text size", Float.toString(temp1));
            	tvMeaning.setTextSize((float) (tvMeaning.getTextSize() * 0.3));
            }
            tvMeaning.setText(vocab.getEnglish());
                     
            
            vf = new ViewFlipper(this);
            vf.addView(newItemSide1);
            vf.addView(newItemSide2);
            
            vf_Container = new RelativeLayout(this);
            vf_Container.addView(vf);
            
            vfContainer.addView(vf_Container);
            vfContainer.invalidate();
            
            TextView TV = (TextView) findViewById(R.id.tv_flashcard_count);
  		  	String text = TV.getText().toString();
  		  	int total = values.size();
  		  	String show = Integer.toString((currentVocab + 1)) + "/" + Integer.toString(total);
  		  	TV.setText(show);
  		  	
  		  	newItemSide1.getLayoutParams().width = RL_w;
		  	newItemSide1.getLayoutParams().height = RL_w;
		  	
		  	newItemSide2.getLayoutParams().width = RL_w;
		  	newItemSide2.getLayoutParams().height = RL_w;

//		  	int colorPos = i % colors.length;
//		  	newItemSide1.setBackgroundResource(colors[colorPos]);
//		  	newItemSide1.setBackgroundColor(Color.parseColor("#000000"));
    	}
	
    	ImageView corner = (ImageView) findViewById(R.id.iv_left_corner);
    	corner.getLayoutParams().height = corners_H;
    	
    	corner = (ImageView) findViewById(R.id.iv_right_corner);
    	corner.getLayoutParams().height = corners_H;
    	
    	GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gesturesFlashcard);
       
    	gestures.removeAllOnGesturePerformedListeners();
        gestures.addOnGesturePerformedListener(FlashcardActivity.this);
		gestures.setGestureColor(Color.TRANSPARENT);
		

        
  
    }
    
    private void updateListAdapter()
	{
	   
	    runOnUiThread(new Runnable()
	    {
	        public void run()
	        {
	           
	            //_adapter.notifyDataSetChanged();
	        }
	    });
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		// TODO Auto-generated method stub
//		Toast.makeText(parent.getContext(), 
//				"OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
//				Toast.LENGTH_SHORT).show();

		datasource = new VocabDataSource(this);
		datasource.open();
		
		values = datasource.getVocabBySubject(parent.getItemAtPosition(position).toString());
		currentVocab = 0;
		
		//GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gesturesFlashcard);
	       
        //gestures.removeAllOnGestureListeners();
        
		getRandomSet();
		buildCardView();
		
		datasource.close();
		
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	
	}
    
	
}
