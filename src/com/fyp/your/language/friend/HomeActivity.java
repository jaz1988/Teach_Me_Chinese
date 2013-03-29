package com.fyp.your.language.friend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sourceforge.pinyin4j.PinyinHelper;

import com.fyp.your.language.friend.R;
import com.fyp.your.language.friend.Database.VocabDataSource;
import com.fyp.your.language.friend.Flashcard.FlashcardActivity;
import com.fyp.your.language.friend.Input.SearchActivity;
import com.fyp.your.language.friend.Input.VocabularyInputActivity;
import com.fyp.your.language.friend.Library.LibraryActivity;
import com.fyp.your.language.friend.Library.Vocab;
import com.fyp.your.language.friend.Pairs.PairsActivity;
import com.fyp.your.language.friend.Scramble.ScrambleActivity;
import com.fyp.your.language.friend.Tests.VocabTestActivity;
import com.pinyin4android.PinyinUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class HomeActivity extends Activity {
	
	private ProgressDialog mProgressDialog;
	List<Vocab> allVocab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);

        //new build().execute();
//        VocabDataSource datasource = new VocabDataSource(HomeActivity.this);
//		datasource.create();
//		datasource.close();
//		
//        File file = new File(Environment.getExternalStorageDirectory().toString() +"/Your Language Friend");
//        if(!file.exists())
//        {
//        	
//			
//        	file.mkdir();
//        	file = new File(Environment.getExternalStorageDirectory().toString() +"/Your Language Friend/Audio");
//        	file.mkdir();
//        	new buildAssets().execute();
//        }
        //CopyAssets();

      
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //com.actionbarsherlock.view.
    	
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu_home_search, menu);
      return true;
    } 
  //action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      
      
      case R.id.search:    	  
    	  Intent intent = new Intent(HomeActivity.this,SearchActivity.class);
    	  startActivity(intent);  	  
    	  finish();
    	  break;   	  
      default:
        break;
      }
      return true;
    } 
    
    public void onBackPressed() {
        
    	finish();
        return;
    }   
    
    public class build extends AsyncTask<Void, Integer, Void>
    {
    	@Override
  		protected Void doInBackground(Void... arg0) 
  		{
    		 try
    	      {	    	  	    	
    			 
    			 VocabDataSource datasource = new VocabDataSource(HomeActivity.this);
    			 datasource.create();
   
    		        
    		        datasource.open();
    		        allVocab = datasource.getAllGrammar();
    		        
 
    		        
    		        //Log.d("size", Integer.toString(allVocab.size()));
    			String chinese;
    			String chinese2;
    			String pinyin;
    			String audio;
    			InputStream is;
    			for(int i = 0; i < allVocab.size(); i++)
    			{
    				//if(i==71 || i==178) continue;
    				if(i==20) continue;
    				//Log.d("index", Integer.toString(i));
    				if(allVocab.get(i).getAudioPath()==null)
    				{
	    				//publishProgress(i);
	    				//Log.d("index", Integer.toString(i));
	    				chinese = allVocab.get(i).getChinese();
	    				//chinese = "我很好,你呢";
	    				chinese2 = chinese;
	    				//Log.d("size", chinese);
	    				int index = chinese.indexOf("，");
	    				int index2 = chinese.indexOf(",");
	    				//Log.d("chinese index", Integer.toString(index));
	    				if(index!=-1)
	    				{   					
	    					chinese2 = chinese.substring(0, index) + chinese.substring(index+1,chinese.length());
	    					//Log.d("entered ',' present", chinese2);
	    				}
	    				if(index2!=-1)
	    				{   					
	    					chinese2 = chinese.substring(0, index2) + chinese.substring(index2+1,chinese.length());
	    					//Log.d("entered ',' present", chinese2);
	    				}
	    				Log.d("chinese2", chinese2);
	    				
	    				
	    				pinyin = TranslateHelperClass.getPinYin(chinese2);
	    				Log.d("pinyin", "1" + pinyin);
	    				
	    				is = TranslateHelperClass.speakText(chinese2, "chi");
	    				audio = TranslateHelperClass.saveWav(pinyin,is);
	    				Log.d("audio", "2" + audio);
	    				
	    				datasource.setPinyin("grammar", chinese, pinyin, audio);
    				}
    			}
    		  


    		      
    		        datasource.close();
    			
    	       	         	
    	      }
    	      catch (Exception e) 
    	      {
    	        
    	      }
			return null;
    	 		    		   		
  		}
    	
    	@Override
        protected void onProgressUpdate(Integer... p) {
            super.onProgressUpdate(p);
            progress(p[0]+1 + "");
  			mProgressDialog.show();
        }
    	
    	//Process the results
    	@Override
  		protected void onPostExecute(Void a)
  		{	
	
  			//mProgressDialog.cancel();		
    		Log.d("post execute", "done");
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


    
    public void onClick(View view) {
     
        switch (view.getId()) {
        
        case R.id.btn_library:
         
        	startActivity(new Intent(HomeActivity.this,LibraryActivity.class));
        	finish();
            break;
            
        case R.id.btn_flashcard:
            
        	startActivity(new Intent(HomeActivity.this, FlashcardActivity.class));
        	finish();
            break;
            
        case R.id.btn_pairs:
            
        	startActivity(new Intent(HomeActivity.this, PairsActivity.class));
        	finish();
            break;
            
        case R.id.btn_scramble:
            
        	startActivity(new Intent(HomeActivity.this, ScrambleActivity.class));
        	finish();
            break;
            
        case R.id.btn_test:
            
        	startActivity(new Intent(HomeActivity.this, VocabTestActivity.class));
        	finish();
            break;
            
        case R.id.btn_progress:
            
        	startActivity(new Intent(HomeActivity.this, ProgressActivity.class));
        	finish();
            break;
            
        }
        
      }
    
    public class buildAssets extends AsyncTask<Void, Integer, List<Vocab>>
    {
    	@Override
		protected List<Vocab> doInBackground(Void... arg0) 
		{
    		 try
    	      {	    	  	    	 
    			publishProgress(1);
    			CopyAssets();
    			
    	       	         	
    	      }
    	      catch (Exception e) 
    	      {
    	        
    	      }
    	 		    		   		
			  return null;
		}
    	
    	@Override
        protected void onProgressUpdate(Integer... p) {
            super.onProgressUpdate(p);
            progress("Loading files...");
			mProgressDialog.show();
        }
    	
    	//Process the results
    	@Override
		protected void onPostExecute(List<Vocab> result)
		{	
    		
			mProgressDialog.cancel();				
		}
	}
    
    private void CopyAssets() {
        AssetManager assetManager = this.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("Audio");
            Log.d("copy assets", Integer.toString(files.length));
            
            for(int i = 0; i < files.length; i++)
            {
            	Log.d("copy assets", files[i]);   
            }
           
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        for(String filename : files) {
            System.out.println("File name => "+filename);
            Log.d("copy assets", "File name => "+filename);
            InputStream in = null;
            OutputStream out = null;
            try {
              in = assetManager.open("Audio/" + filename);   // if files resides inside the "Files" directory itself
              out = new FileOutputStream(Environment.getExternalStorageDirectory().toString() +"/Your Language Friend/Audio/" + filename);
              copyFile(in, out);
              in.close();
              in = null;
              out.flush();
              out.close();
              out = null;
            } catch(Exception e) {
                Log.e("tag", e.toString());
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
          out.write(buffer, 0, read);
        }
    }
}
