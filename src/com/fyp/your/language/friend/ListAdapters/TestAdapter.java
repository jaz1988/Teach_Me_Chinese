package com.fyp.your.language.friend.ListAdapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fyp.your.language.friend.R;

import com.fyp.your.language.friend.TranslateHelperClass;
import com.fyp.your.language.friend.Library.Vocab;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TestAdapter extends ArrayAdapter<Vocab>{

	private int[] colors = new int[] {R.drawable.item_body, R.drawable.item_body_list };
	 Context context; 
	 int layoutResourceId;    
	 List<Vocab> data = null;
	 TextView alpha;
     TextView english;
     TextView pinyin;
	 private final int[] bgColors = new int[] { R.color.list_bg_1, R.color.list_bg_2 };
	 
	 public TestAdapter(Context context, int layoutResourceId, List<Vocab> data) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;	        
	    }
	 
	 @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        VocabHolder holder;
	        //Log.d("positions", Integer.toString(position));
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new VocabHolder();
	            holder.english= (TextView)row.findViewById(R.id.meaning);
	            //holder.pronunciation = (TextView)row.findViewById(R.id.pinyin);   
	            holder.alpha = (TextView)row.findViewById(R.id.alpha);   

	        }
	        else
	        {
	            holder = (VocabHolder)row.getTag();        	
	        }

	        final Vocab vocab = data.get(position);
	        holder.english.setText(vocab.english);
	        holder.pronunciation.setText(vocab.pronunciation);
	        holder.alpha.setText(vocab.alpha);
       
	        row.setTag(holder);
	        
//	        int colorPos = position % colors.length;
//	        holder.chinese.setBackgroundResource(colors[colorPos]);
//	        holder.english.setBackgroundResource(colors[colorPos]);
//	        holder.pronunciation.setBackgroundResource(colors[colorPos]);
//	        holder.audio.setBackgroundResource(colors[colorPos]);
	        
	        alpha = (TextView)row.findViewById(R.id.alpha);   
	        english = holder.english;
	        pinyin = holder.pronunciation;
	        alpha.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                Log.d("alpha clicked", "yes");
	                  
	            	v.setBackgroundResource(R.drawable.item_body_green);
	            	
	            	
	            
	            }
	            });
//	        
	       
	        return row;
	    }
	 
	 	static class VocabHolder
	    {
	        TextView english;
	        TextView chinese;
	        TextView pronunciation;
	        TextView alpha;
	    }
}
