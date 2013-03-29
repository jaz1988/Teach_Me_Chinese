package com.fyp.your.language.friend.Database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.fyp.your.language.friend.GlobalVariables;
import com.fyp.your.language.friend.StartActivity;
import com.fyp.your.language.friend.Library.Vocab;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper{

	private static MySQLiteHelper mInstance = null;
	
	public static final String TABLE_VOCAB = "vocab";
	public static final String TABLE_GRAMMAR = "grammar";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_CHINESE = "chinese";
	public static final String COLUMN_ENGLISH = "english";
	public static final String COLUMN_PRONUNCIATION = "pronunciation";
	public static final String COLUMN_IMAGE = "image";
	public static final String COLUMN_AUDIO = "audio";
	public static final String COLUMN_FAMILARITY = "familarity";
	public static final String COLUMN_SUBJECT = "subject";

	private static final String DATABASE_NAME = "vocab.db";
	public static final int DATABASE_VERSION = 10;
	
	private static String DB_PATH = "/data/data/YOUR_PACKAGE/databases/";
	
	private final Context myContext;

	public static MySQLiteHelper getInstance(Context ctx) {
	      
	    // Use the application context, which will ensure that you 
	    // don't accidentally leak an Activity's context.
	    // See this article for more information: http://bit.ly/6LRzfx
	    if (mInstance == null) {
	      mInstance = new MySQLiteHelper(ctx.getApplicationContext());
	    }
	    return mInstance;
	  }
	     
	  /**
	   * Constructor should be private to prevent direct instantiation.
	   * make call to static factory method "getInstance()" instead.
	   */
	  private MySQLiteHelper(Context ctx) {
	    super(ctx, ctx.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);
	    DB_PATH =  ctx.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME;
	    this.myContext = ctx;
	  }
	  
	

	  @Override
	  public void onCreate(SQLiteDatabase database) {
		Log.d("database", "oncreate: " + database.isOpen());
	
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		  Log.d("database", "onupgrade");
	    Log.w(MySQLiteHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");

	    try
	      {	    	  	  
			 File file = new File(DB_PATH);	    			 
			 if(file.exists()) 
			 {
				 Log.d("database", "delete db");		
				 file.delete();
			 }
			 createDataBase();   
			 SQLiteDatabase DB = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
			 DB.execSQL("PRAGMA user_version = " + DATABASE_VERSION);
	      }
	      catch (Exception e) 
	      {
	        
	      }
			
	  }
	  public class createDB extends AsyncTask<SQLiteDatabase, Integer, SQLiteDatabase>
	    {
	    	@Override
			protected SQLiteDatabase doInBackground(SQLiteDatabase... arg0) 
			{
	    		 try
	    	      {	    	  	  
	    			 File file = new File(DB_PATH);	    			 
	    			 if(file.exists()) 
	    			 {
	    				 Log.d("database", "delete db");		
	    				 file.delete();
	    			 }
	    			 createDataBase();    		
	    	      }
	    	      catch (Exception e) 
	    	      {
	    	        
	    	      }
	    	 		    		   		
				  return arg0[0];
			}
	    	
	    	@Override
	        protected void onProgressUpdate(Integer... p) {
	            super.onProgressUpdate(p);
	            
	        }
	    	
	    	//Process the results
	    	@Override
			protected void onPostExecute(SQLiteDatabase result)
			{	
	    		result.execSQL("PRAGMA user_version = " + DATABASE_VERSION);
			}
		}
	  
	  /**
	     * Creates a empty database on the system and rewrites it with your own database.
	     * */
	    public void createDataBase() throws IOException{
	 
	    	File file = new File(DB_PATH);
	    	//boolean dbExist = checkDataBase();
	 
	    	if(file.exists()){
	    		Log.d("database", "db exist");
	    		//do nothing - database already exist
	    	}else{
	 
	    		//By calling this method and empty database will be created into the default system path
	               //of your application so we are gonna be able to overwrite that database with our database.
	        	//this.getReadableDatabase();
	        	Log.d("database", "db !exist");
	 
	        	try {
	 
	    			copyDataBase();
	    			Log.d("database", "db copy done");

	 
	    		} catch (IOException e) {
	 
	        		throw new Error("Error copying database");
	 
	        	}
	    	}
	 
	    }
	    
	    /**
	     * Check if the database already exist to avoid re-copying the file each time you open the application.
	     * @return true if it exists, false if it doesn't
	     */
	    private boolean checkDataBase(){
	 
	    	SQLiteDatabase checkDB = null;
	 
	    	try{
	    		String myPath = DB_PATH;
	    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	 
	    	}catch(SQLiteException e){
	 
	    		//database does't exist yet.
	 
	    	}
	 
	    	if(checkDB != null){
	 
	    		checkDB.close();
	 
	    	}
	 
	    	return checkDB != null ? true : false;
	    }
	    
	    /**
	     * Copies your database from your local assets-folder to the just created empty database in the
	     * system folder, from where it can be accessed and handled.
	     * This is done by transfering bytestream.
	     * */
	    private void copyDataBase() throws IOException{
	 
	    	//Open your local db as the input stream
	    	InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
	 
	    	// Path to the just created empty db
	    	String outFileName = DB_PATH;
	 
	    	//Open the empty db as the output stream
	    	OutputStream myOutput = new FileOutputStream(outFileName);
	 
	    	//transfer bytes from the inputfile to the outputfile
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = myInput.read(buffer))>0){
	    		myOutput.write(buffer, 0, length);
	    	}
	 
	    	//Close the streams
	    	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
	 
	    }
	    
	    public SQLiteDatabase openDataBase() throws SQLException{
	    	 
	    	//Open the database
	        String myPath = DB_PATH;
	    	return SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	 
	    }
	 
	  
}
