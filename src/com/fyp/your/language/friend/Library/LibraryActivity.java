package com.fyp.your.language.friend.Library;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.fyp.your.language.friend.R;

import com.fyp.your.language.friend.HomeActivity;
import com.fyp.your.language.friend.StartActivity;
import com.fyp.your.language.friend.TestResultsActivity;
import com.fyp.your.language.friend.Database.VocabDataSource;
import com.fyp.your.language.friend.Input.SearchActivity;
import com.fyp.your.language.friend.ListAdapters.VocabAdapter;
import com.pinyin4android.PinyinUtil;
import com.pinyin4android.PinyinSource;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.*;
import net.sourceforge.pinyin4j.format.exception.*;

public class LibraryActivity extends Activity implements OnItemSelectedListener {
	
	private VocabDataSource datasource;
	private ListView listView1;
	VocabAdapter _adapter;
	Spinner sp1;
	  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vocabulary_list_main);

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
    
		List<Vocab> values = datasource.getVocabBySubject(list.get(0));
		VocabAdapter adapter = new VocabAdapter(LibraryActivity.this, R.layout.vocabulary_list_row,values);
		_adapter = adapter;
        
		listView1 = (ListView)findViewById(R.id.list2);
     
		listView1.setAdapter(adapter); 
    
  
		sp1= (Spinner) findViewById(R.id.spinner1);
    
		ArrayAdapter<String> adp1=new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,list);
		adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp1.setAdapter(adp1);
		sp1.setOnItemSelectedListener(this);
		datasource.close();
	}
  
	public void onBackPressed() 
	{
	} 


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_home, menu);
		return true;
	} 
  
  
	//action bar
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home:
  	  
			Intent intent = new Intent(LibraryActivity.this,HomeActivity.class);
			startActivity(intent);
			finish();
			//Log.d("dpi", Integer.toString(getResources().getDisplayMetrics().densityDpi));
  	  
			break;
			
		case R.id.search:    	  
			  intent = new Intent(LibraryActivity.this,SearchActivity.class);
			  startActivity(intent);  	  
			  finish();
			  break;   	  
			
	  
		default:
			break;
		}

		return true;
	} 

	// Will be called via the onClick attribute
	// of the buttons in main.xml
	public void onClick(View view) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<Vocab> adapter = _adapter;
		Vocab comment = null;

		adapter.notifyDataSetChanged();
	}
	
	@Override
  	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
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
//		Toast.makeText(parent.getContext(), 
//				"OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
//				Toast.LENGTH_SHORT).show();

		datasource = new VocabDataSource(this);
		datasource.open();
		
		List<Vocab> values = datasource.getVocabBySubject(parent.getItemAtPosition(position).toString());
		VocabAdapter adapter = new VocabAdapter(LibraryActivity.this, R.layout.vocabulary_list_row,values);
        
		listView1 = (ListView)findViewById(R.id.list2);
     
		listView1.setAdapter(adapter); 

		updateListAdapter();
		datasource.close();
		
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	
	}

} 
