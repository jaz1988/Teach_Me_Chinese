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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class ShowTestScoresAdapter extends ArrayAdapter<Vocab>{

	private int[] colors = new int[] {R.drawable.item_body, R.drawable.item_body_list };
	 Context context; 
	 int layoutResourceId;    
	 List<Vocab> data = null;
	 private final int[] bgColors = new int[] { R.color.list_bg_1, R.color.list_bg_2 };
	 
	 public ShowTestScoresAdapter(Context context, int layoutResourceId, List<Vocab> data) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;	        
	    }
	 
	 @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
		 //View view = super.getView(position, convertView, parent);
	        View row = convertView;
	        VocabHolder holder;
	        //Log.d("positions", Integer.toString(position));
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new VocabHolder();
	            holder.english= (TextView)row.findViewById(R.id.meaning);
	            holder.chinese = (TextView)row.findViewById(R.id.foreignWord);
	            holder.pronunciation = (TextView)row.findViewById(R.id.pronunciation);
	            holder.audio = (RelativeLayout) row.findViewById(R.id.rl_audio);
	            //holder.picture = (ImageView)row.findViewById(R.id.Picture);
	            //holder.familarity = (RelativeLayout)row.findViewById(R.id.rl_familarity);
	            

	            final Vocab vocab = data.get(position);

	        }
	        else
	        {
	            holder = (VocabHolder)row.getTag();        	
	        }

	        final Vocab vocab = data.get(position);
	        holder.english.setText(vocab.english);
	        holder.chinese.setText(vocab.chinese);
	        holder.pronunciation.setText(vocab.pronunciation);
       
	        row.setTag(holder);
	        
	        int colorPos = position % colors.length;
	        holder.chinese.setBackgroundResource(colors[colorPos]);
	        holder.english.setBackgroundResource(colors[colorPos]);
	        holder.pronunciation.setBackgroundResource(colors[colorPos]);
	        holder.audio.setBackgroundResource(colors[colorPos]);
	        //holder.familarity.setBackgroundResource(colors[colorPos]);
	        
	        holder.audio = (RelativeLayout) row.findViewById(R.id.rl_audio);
	        RelativeLayout RL = holder.audio;
	        ImageView IV = (ImageView) RL.getChildAt(0);
	        IV.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                
	            	String audioPath = vocab.getAudioPath();
	            	Log.d("Audio onclick", "clicked");
	            	 if(audioPath!=null)
						try {
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

	        return row;
	    }
	 
	 	static class VocabHolder
	    {
	        TextView english;
	        TextView chinese;
	        TextView pronunciation;
	        RelativeLayout audio;
	        ImageView picture;
	        //RelativeLayout familarity;
	    }
}
