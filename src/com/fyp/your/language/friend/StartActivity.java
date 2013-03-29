package com.fyp.your.language.friend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.fyp.your.language.friend.R;
import com.fyp.your.language.friend.Database.VocabDataSource;
import com.fyp.your.language.friend.HomeActivity.buildAssets;
import com.fyp.your.language.friend.Library.LibraryActivity;
import com.fyp.your.language.friend.Library.Vocab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class StartActivity extends Activity {

	private ProgressDialog mProgressDialog;
	List<Vocab> allVocab;
	VocabDataSource datasource;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_main);
		
		GlobalVariables.rootDir = this.getExternalFilesDir(null).getAbsolutePath();
		
		Log.d("Sd card path getAbsPath", "" + this.getExternalFilesDir(null).getAbsolutePath());
//		
		Log.d("Sd card path getPath", Environment.getExternalStorageDirectory().getPath());
//		
		Log.d("Sd card path toString", Environment.getExternalStorageState ());
		
        File file = new File(Environment.getExternalStorageDirectory().toString() +"/Your Language Friend");
        GlobalVariables.rootDirTest = Environment.getExternalStorageDirectory().toString() +"/Your Language Friend";
        if(!file.exists())
        {		
//        	file.mkdir();
//        	file = new File(Environment.getExternalStorageDirectory().toString() +"/Your Language Friend/Audio");
//        	file.mkdir();
//        	
//        	file = new File(Environment.getExternalStorageDirectory().toString() +"/Your Language Friend/Images");
//        	file.mkdir();      	
        }
        else
        {
        	file = new File(Environment.getExternalStorageDirectory().toString() +"/Your Language Friend/Audio");
        	File files[] = file.listFiles();
        	for(int i = 0; i < files.length; i++)
        	{
        		files[i].delete();
        	}
        	file.delete();
        	
        	file = new File(Environment.getExternalStorageDirectory().toString() +"/Your Language Friend/Images");
        	File files2[] = file.listFiles();
        	for(int i = 0; i < files2.length; i++)
        	{
        		files2[i].delete();
        	}
        	file.delete();
        	
        	file = new File(Environment.getExternalStorageDirectory().toString() +"/Your Language Friend");
        	file.delete();
        }
        
        file = new File(GlobalVariables.rootDir + "/Audio");
        if(!file.exists())
        {		
        	file.mkdir();
        	
        	file = new File(GlobalVariables.rootDir + "/Images");
        	file.mkdir();       		
        }
        
        
        new buildAssets().execute();
		
		
        
        
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_start, menu);
		return true;
	}
	
	 public void onClick(View view) {
	     
        switch (view.getId()) {
	        case R.id.btn_start:
	            
	        	startActivity(new Intent(StartActivity.this,HomeActivity.class));
	        	finish();
	            break;
        }
    }
	 
	    public void onBackPressed() {
	        
	    	finish();
	        return;
	    }   
	    
	  //Initialise the progress spinner
	  	  public void progress(String msg)
	  	  {
	  	  	mProgressDialog = new ProgressDialog(this);
	  	  	mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);         
	  	  	mProgressDialog.setMessage(msg);                      
	  	  	mProgressDialog.setCancelable(false);    
	  	  }

	  	public class buildAssets extends AsyncTask<Void, Integer, List<Vocab>>
	    {
	    	@Override
			protected List<Vocab> doInBackground(Void... arg0) 
			{
	    		 try
	    	      {	    	  	    	 
	    			 
	    			publishProgress(1);
	    			//CopyAssets();
	    			CopyAssets2();
	    			//TranslateHelperClass.downloadDB("http://jason88-z.comp.nus.edu.sg/index.php");
	    			
	    			
	    			
	    	       	         	
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
	    		new dBOpen().execute();    		
				mProgressDialog.cancel();				
			}
		}
	  	
	  	public class dBOpen extends AsyncTask<Void, Integer, List<Vocab>>
	    {
	    	@Override
			protected List<Vocab> doInBackground(Void... arg0) 
			{
	    		 try
	    	      {	    	  	  
	    			 datasource = new VocabDataSource(StartActivity.this);
	 	    		 datasource.create();    		
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
	    		datasource.open();
//	    		datasource.updateColumns();    
//	    		datasource.createTestInfoTable();
//	    		datasource.insertVocabTestData();
	    		datasource.close();		
	    		
			}
		}
	    
	    private void CopyAssets() {
	        AssetManager assetManager = this.getAssets();
	        String[] files = null;
	        String[] files2 = null;
	        try {
	            files = assetManager.list("Audio");
	            files2 = assetManager.list("Images");
	           
	           
	        } catch (IOException e) {
	            Log.e("tag", e.getMessage());
	        }

	        for(String filename : files) {

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
	        
	        for(String filename : files2) {

	            InputStream in = null;
	            OutputStream out = null;
	            try {
	              in = assetManager.open("Images/" + filename);   // if files resides inside the "Files" directory itself
	              out = new FileOutputStream(Environment.getExternalStorageDirectory().toString() +"/Your Language Friend/Images/" + filename);
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
	    
	    private void CopyAssets2() {
	        AssetManager assetManager = this.getAssets();
	        String[] files = null;
	        String[] files2 = null;
	        try {
	            files = assetManager.list("Audio");
	            files2 = assetManager.list("Images");
	           
	           
	        } catch (IOException e) {
	            Log.e("tag", e.getMessage());
	        }

	        for(String filename : files) {

	            InputStream in = null;
	            OutputStream out = null;
	            try {
	              in = assetManager.open("Audio/" + filename);   // if files resides inside the "Files" directory itself
	              out = new FileOutputStream(GlobalVariables.rootDir + "/Audio/" + filename);
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
	        
	        for(String filename : files2) {

	            InputStream in = null;
	            OutputStream out = null;
	            try {
	              in = assetManager.open("Images/" + filename);   // if files resides inside the "Files" directory itself
	              out = new FileOutputStream(GlobalVariables.rootDir + "/Images/" + filename);
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
