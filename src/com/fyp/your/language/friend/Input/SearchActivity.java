package com.fyp.your.language.friend.Input;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.fyp.your.language.friend.R;
import com.fyp.your.language.friend.HomeActivity;
import com.fyp.your.language.friend.StartActivity;
import com.fyp.your.language.friend.TranslateHelperClass;
import com.fyp.your.language.friend.Database.VocabDataSource;


public class SearchActivity extends Activity implements View.OnClickListener, OnGesturePerformedListener {

	private EditText word, meaning, pronunciation;
	private ImageView translate, audio, picture;
	private String rootDir;
	private ProgressDialog mProgressDialog;
	private View promptsView;
	private int currentPicIndex;
	private GestureLibrary mLibrary;
	private String selectedPicPath;
	private String selectedAudioPath;
	private int noOfFiles;
	private Object syncToken;
	private Boolean ready;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        
        selectedPicPath = null;
        selectedAudioPath = null;
    
        //Initialising views from the UI
        word = (EditText) findViewById(R.id.et_newWord);
        meaning = (EditText) findViewById(R.id.et_meaning);
        pronunciation = (EditText) findViewById(R.id.et_pronunciation);
        
        translate = (ImageView) findViewById(R.id.image_translate);
        audio = (ImageView) findViewById(R.id.image_sound);
        picture = (ImageView) findViewById(R.id.image_picture);
        
        rootDir = Environment.getExternalStorageDirectory()
                .getAbsolutePath()+"/Your Language Friend"; 
        
        //Load gesture library
        mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!mLibrary.load()) {
            finish();
        }        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu_home, menu);   
      return true;
    } 
    
    public void onBackPressed() 
	{
	} 
    
    //action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      case R.id.home:
    	  
    	  Intent intent = new Intent(SearchActivity.this,HomeActivity.class);
    	  startActivity(intent);  	  
    	  finish();
    	  break;
    	  
      default:
        break;
      }

      return true;
    } 
    
    //On click events
	@Override
	synchronized public void onClick(View view) {
		// TODO Auto-generated method stub
	
		switch (view.getId()) {
		//Translate button
	    
	    //Audio button
	    case R.id.image_sound:
	    	
	    	try {
	    		if(selectedAudioPath!=null)
	    			TranslateHelperClass.playWav(selectedAudioPath);
	    		else
	    			Toast.makeText(SearchActivity.this, "Please search first", Toast.LENGTH_SHORT).show();
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
			
	    case R.id.btn_add:
	    	 
	    	meaning.setText("");
	    	pronunciation.setText("");
	    	selectedAudioPath = null;
	    	
	    	String addWord = word.getText().toString();
	    
	    	if(addWord.equalsIgnoreCase(""))
	    	{
	    		Toast.makeText(SearchActivity.this, "Please type in a new word", Toast.LENGTH_SHORT).show();
	    	}
	    	else
	    	{    		
	    		new buildAll().execute();	    		    					
	    	}	    		    	
	    	break;
	    	
		//Image button
	    case R.id.image_picture:
	    	
			
	    	try {

	    		//Delete all files in images folder
	    		File file = new File(rootDir + "/images");      
    	        String[] myFiles;    
	            if(file.isDirectory()){
	                myFiles = file.list();
	                for (int i=0; i<myFiles.length; i++) {
	                    File myFile = new File(file, myFiles[i]); 
	                    myFile.delete();
	                }
	             }
	    	            
	    	            
	    		ConnectivityManager connMgr = (ConnectivityManager) 
	    		        getSystemService(Context.CONNECTIVITY_SERVICE);
	    		    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    		    if (networkInfo != null && networkInfo.isConnected()) {
	    		    	
	    		    	LayoutInflater li = LayoutInflater.from(this);
	    				promptsView = li.inflate(R.layout.vocabulary_input_picture_selection, null);

	    				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
	    						this);
	    				
	    				alertDialogBuilder.setView(promptsView);
	    				
	    				alertDialogBuilder.setTitle("Choose a picture");
	    				
    				    				
	    				//set dialog message
	    				alertDialogBuilder
	    					.setCancelable(false)
	    					.setPositiveButton("OK",
	    					  new DialogInterface.OnClickListener() {
	    					    public void onClick(DialogInterface dialog,int id) {
	    					    	Log.d("Entered OK", selectedPicPath);
	    					    	
	    					    	try {
	    					    		
	    					    		File src = new File(selectedPicPath);
	    					    		File dir = new File(rootDir + "/savedImages");
	    					    		File dst = new File(rootDir + "/savedImages/" + word.getText().toString() + ".jpg"  );
	    					    		if(!dir.exists())
	    					    			dir.mkdir();
										TranslateHelperClass.copy(src,dst);
										
										Bitmap bmp = BitmapFactory.decodeFile(dst.getCanonicalPath());
										
										picture.setImageBitmap(bmp);
										
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
	    					    }
	    					    
	    					  })
	    					.setNegativeButton("CANCEL",
	    					  new DialogInterface.OnClickListener() {
	    					    public void onClick(DialogInterface dialog,int id) {
	    					    	
	    						dialog.cancel();
	    					    }
	    					  });
	    				
	    				

	    				//create alert dialog
	    				AlertDialog alertDialog = alertDialogBuilder.create();

	    				//show it
	    				alertDialog.show();
	    				progress("Retrieving Pictures...");
	    				mProgressDialog.show();
	    					    		
	    				GestureOverlayView gestures = (GestureOverlayView) alertDialog.findViewById(R.id.gestures);
	    				gestures.addOnGesturePerformedListener(SearchActivity.this);
	    				gestures.setGestureColor(Color.TRANSPARENT);
	    				
	    		        // fetch data
	    		    	new getImages().execute();
	    		    	
	    		    } else {
	    		        // display error
	    		    	Toast.makeText(SearchActivity.this, "no internet connection detected", Toast.LENGTH_SHORT).show();
	    		    }
	    		    
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	    	
		}
	}

	public class buildAll extends AsyncTask<Void, String, List<String>>
    {
    	@Override
		protected List<String> doInBackground(Void... arg0) 
		{
    		String translated = null;
    		InputStream is = null;
    		String path = null;
    		
    		List<String> results = new ArrayList<String>();
    		String toTranslate = word.getText().toString();
    		
    		publishProgress("Searching...");
    		
    		try
    	      {	    	  	    	 

	    		
 	    		
    			Boolean valid = toTranslate.matches("^[\u0000-\u0080]+$");
    			if(!valid)
    			{
    				//Get Hanyu Pin Yin

    	    		results.add(TranslateHelperClass.getPinYin(toTranslate));

    	    		
    				//Translate text
    				//Get audio file

    				translated = TranslateHelperClass.translateText(toTranslate,"chi");
     	    		results.add(translated);

     	    		  	    		
    	    		//Get audio file

    	    		is = TranslateHelperClass.speakText(toTranslate, "chi");

    			}
    			else
    			{
    				//Get Hanyu Pin Yin

    	    		results.add(toTranslate);
    	    		
    				//Translate text

    				translated = TranslateHelperClass.translateText(toTranslate,"eng");
     	    		results.add(translated);

     	    		
     	    		//Get audio file

    				is = TranslateHelperClass.speakText(toTranslate, "eng");

    			} 	    			
    			path = TranslateHelperClass.saveWav(toTranslate,is);
    			results.add(path);

    	
    	      }
    	      catch (Exception e) 
    	      {
    	        
    	      }
    	 		    		   		
			  return results;
		}
    	
    	@Override
        protected void onProgressUpdate(String... p) {
            super.onProgressUpdate(p);
            progress(p[0]);
			mProgressDialog.show();
        }
    	
    	//Process the results
    	@Override
		protected void onPostExecute(List<String> results)
		{	
			mProgressDialog.cancel();
			if(results.get(0)!=null)
			{
				pronunciation.setText(results.get(0));
			}
			if(results.get(1)!=null)
			{
				meaning.setText(results.get(1));
			}
			if(results.get(2)!=null)
			{
				selectedAudioPath = results.get(2);
			}		
			
			VocabDataSource datasource = new VocabDataSource(SearchActivity.this);
	    	datasource.open();
	    	String addWord = word.getText().toString();
	    	String addMeaning = meaning.getText().toString();
	    	String addPronunciation = pronunciation.getText().toString();
	    	if(selectedPicPath == null) selectedPicPath = "No Picture";
	    	
			Log.d("add", selectedPicPath);
	    	Log.d("add", word.getText().toString());
	    	Log.d("add", meaning.getText().toString());
	    	Log.d("add", pronunciation.getText().toString());
	    	Log.d("add", selectedAudioPath);
	    	
	    	//datasource.createVocab(addWord, addMeaning, addPronunciation, selectedPicPath, selectedAudioPath);
		}
	}
	//Async task to translate text
	public class translateText extends AsyncTask<Object, Integer, String>
    {
    	@Override
		protected String doInBackground(Object... arg0) 
		{
    		String translated = null;
    		 try
    	      {	    	  	    	 
    			publishProgress(1);
 	    		Log.d("Translate", "Translating text");
 	    		String toTranslate = word.getText().toString();
 	    		Log.d("Translate", toTranslate);
 	    		if(!toTranslate.equalsIgnoreCase(""))
 	    			translated = TranslateHelperClass.translateText(toTranslate,"chi");
 	    		else return null;
 				Log.d("Translate", translated);
 				
    	
    	      }
    	      catch (Exception e) 
    	      {
    	        
    	      }
    	 		    		   		
			  return translated;
		}
    	
    	@Override
        protected void onProgressUpdate(Integer... p) {
            super.onProgressUpdate(p);
            progress("Translating text...");
			mProgressDialog.show();
        }
    	
    	//Process the results
    	@Override
		protected void onPostExecute(String result)
		{	
			mProgressDialog.cancel();
			if(result!=null)
				meaning.setText(result);
			
			ready = true;
			
			
		}
	}
	
	//Async task to download images
	public class getImages extends AsyncTask<Void, Integer, String>
    {
    	@Override
		protected String doInBackground(Void... arg0) 
		{
    		 try
    	      {	
    			 String toGetImage = word.getText().toString();
    			 Log.d("Image",toGetImage);
    			 if(!toGetImage.equalsIgnoreCase(""))
    			 {
    				 JSONArray item = TranslateHelperClass.getImageLinks(toGetImage);
    	    			int count;
    	   			  	if(item.length()>3)
    	   			  		count = 3;
    	   			  	else count = item.length();
    	   			  	for(int i = 0; i < count; i++)
    	   			  	{
    	   			  		publishProgress(i+1);
    	   			  		JSONObject link = item.getJSONObject(i);
    	   			  		//System.out.println(link.get("link"));
    	   			  		Log.d("Image Links", link.get("link").toString());
    	   			  		TranslateHelperClass.downloadImage(link.getString("link").toString());
    	   			  	}
    			 }
    			 else
    			 {
    				 return null;
    			 }   	
    	      }
    	      catch (Exception e) 
    	      {
    	        
    	      }
    	 		    		   		
			  return "Success";
		}
    	
    	@Override
        protected void onProgressUpdate(Integer... p) {
            super.onProgressUpdate(p);
          
            mProgressDialog.setMessage("Retrieving picture (" + Integer.toString(p[0]) + "/" + "3" + ")");
            
        }
    	
    	//Process the results
    	@Override
		protected void onPostExecute(String result)
		{	   	
    		if(result!=null)
    		{
				try {
					currentPicIndex = 0;
					File imageDir = new File(rootDir + "/images");
					List<File> files = (List<File>) FileUtils.listFiles(imageDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
					noOfFiles = files.size();
					String path = files.get(currentPicIndex).getCanonicalPath();
					Bitmap bmp = BitmapFactory.decodeFile(path);
					Log.d("imagelink", path);
					ImageView picture = (ImageView) promptsView.findViewById(R.id.picture_shown);
					
					picture.setImageBitmap(bmp);
					selectedPicPath = path;
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
			mProgressDialog.cancel();
		}
	}
	
	//Async task to translate text
	public class speakText extends AsyncTask<Void, Integer, String>
    {
    	@Override
		protected String doInBackground(Void... arg0) 
		{
    		InputStream is = null;
    		String path = null;
    		 try
    	      {	    	  	    	 
 	    		Log.d("Speak", "Speaking text");
 	    		String toTranslate = word.getText().toString();
 	    		
 	    		if(!toTranslate.equalsIgnoreCase(""))
 	    		{
 	    			publishProgress(1);
 	    			boolean valid = toTranslate.matches("^[\u0000-\u0080]+$");
 	    			if(!valid)
 	    			{
 	    				is = TranslateHelperClass.speakText(toTranslate, "chi");
 	    			}
 	    			else
 	    			{
 	    				is = TranslateHelperClass.speakText(toTranslate, "eng");
 	    			} 	    			
 	    			path = TranslateHelperClass.saveWav(toTranslate,is);
 	    		}
 	    		else return null;
 				
    	
    	      }
    	      catch (Exception e) 
    	      {
    	        
    	      }
    	 		    		   		
			  return path;
		}
    	
    	@Override
        protected void onProgressUpdate(Integer... p) {
            super.onProgressUpdate(p);
            progress("Retrieving audio file...");
			mProgressDialog.show();
			
        }
    	
    	//Process the results
    	@Override
		protected void onPostExecute(String result)
		{	
			mProgressDialog.cancel();
			if(result!=null)
				try {
					TranslateHelperClass.playWav(result);
					selectedAudioPath = result;
					//this.notify();
					//TranslateHelperClass.playClip(result);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public class speakText2 extends AsyncTask<Void, Integer, String>
    {
    	@Override
		protected String doInBackground(Void... arg0) 
		{
    		InputStream is = null;
    		String path = null;
    		 try
    	      {	    	  	    	 
 	    		Log.d("Speak", "Speaking text");
 	    		String toTranslate = word.getText().toString();
 	    		
 	    		if(!toTranslate.equalsIgnoreCase(""))
 	    		{
 	    			publishProgress(1);
 	    			boolean valid = toTranslate.matches("^[\u0000-\u0080]+$");
 	    			if(!valid)
 	    			{
 	    				is = TranslateHelperClass.speakText(toTranslate, "chi");
 	    			}
 	    			else
 	    			{
 	    				is = TranslateHelperClass.speakText(toTranslate, "eng");
 	    			} 	    			
 	    			path = TranslateHelperClass.saveWav(toTranslate,is);
 	    		}
 	    		else return null;
 				
    	
    	      }
    	      catch (Exception e) 
    	      {
    	        
    	      }
    	 		    		   		
			  return path;
		}
    	
    	@Override
        protected void onProgressUpdate(Integer... p) {
            super.onProgressUpdate(p);
            progress("Retrieving audio file...");
			mProgressDialog.show();
			
        }
    	
    	//Process the results
    	@Override
		protected void onPostExecute(String result)
		{	
			mProgressDialog.cancel();
			if(result!=null)
				try {
					
					selectedAudioPath = result;
								
					String addWord = word.getText().toString();
			    	String addMeaning = meaning.getText().toString();
			    	String addPronunciation = pronunciation.getText().toString();
			    	
			    	VocabDataSource datasource = new VocabDataSource(SearchActivity.this);
			    	datasource.open();
			    	
			    	if(selectedPicPath == null) selectedPicPath = "No Picture";			  		
			    	datasource.createVocab(addWord, addMeaning, addPronunciation, selectedPicPath, selectedAudioPath);
			    	
			    	resetFields();
		    		Toast.makeText(SearchActivity.this, "New word: " + addWord + " added!", Toast.LENGTH_SHORT).show();
			    	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
    
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
        try
        {
	        if (predictions.size() > 0 && predictions.get(0).score > 1.0) 
	        {
	        	String path = null;
	            String action = predictions.get(0).name;
	            if ("left".equals(action)) 
	            {
	                //Toast.makeText(this, "Adding a contact", Toast.LENGTH_SHORT).show();
	                
	                currentPicIndex++;
	                if(currentPicIndex == noOfFiles)
	                	currentPicIndex = 0;
	                
				
	            } 
	            else if ("right".equals(action)) 
	            {
	                //Toast.makeText(this, "Removing a contact", Toast.LENGTH_SHORT).show();
	                currentPicIndex--;
	                if(currentPicIndex == -1)
	                	currentPicIndex = noOfFiles -1;
	                
	            } 
	            
	        	File imageDir = new File(rootDir + "/images");
				List<File> files = (List<File>) FileUtils.listFiles(imageDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
				path = files.get(currentPicIndex).getCanonicalPath();
				Bitmap bmp = BitmapFactory.decodeFile(path);
				Log.d("imagelink", path);
				ImageView picture = (ImageView) promptsView.findViewById(R.id.picture_shown);
				
				picture.setImageBitmap(bmp);
				selectedPicPath = path;
	            
	        }
        }
        catch (IOException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    private void resetFields()
    {
    	word.setText("");
    	meaning.setText("");
    	pronunciation.setText("");
    	selectedAudioPath = null;
    	selectedPicPath = null;
    }
   
    
    
    
}
