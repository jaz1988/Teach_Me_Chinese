package com.fyp.your.language.friend;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.ByteArrayBuffer;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import com.fyp.your.language.friend.R;
import com.memetix.mst.language.Language;
import com.memetix.mst.language.SpokenDialect;
import com.memetix.mst.speak.Speak;
import com.memetix.mst.translate.Translate;



/**
 * author: Mark Watson
 */
public class TranslateHelperClass 
{
	private static String rootDir;
	private static final String RIFF_HEADER = "RIFF";
	private static final String WAVE_HEADER = "WAVE";
	private static final String FMT_HEADER = "fmt ";
	private static final String DATA_HEADER = "data";
   
	private static final int HEADER_SIZE = 44;
   
	private static final String CHARSET = "ASCII";
	
	public static String translateText(String input,String lang) throws Exception
	{
		// Set your Windows Azure Marketplace client info - See http://msdn.microsoft.com/en-us/library/hh454950.aspx
	    Translate.setClientId("10b728d2-1b82-4526-afdb-d553edb6b8de");
		Translate.setClientSecret("P8AUgRpEkvQjP4YrSyBaOqCxzTXCg/YJWweuIRqCOc=");
		//Log.d("Translate", "Translating text2");
		
		String translated;
		
		if(lang.equalsIgnoreCase("eng"))
		{
			translated = Translate.execute(input, Language.ENGLISH, Language.CHINESE_SIMPLIFIED);
		}
		else translated = Translate.execute(input, Language.CHINESE_SIMPLIFIED, Language.ENGLISH);
		
		return translated;
	}
	
	public static InputStream speakText(String input,String lang) throws Exception
	{		
		String sWavUrl;
		// Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
		Speak.setClientId("10b728d2-1b82-4526-afdb-d553edb6b8de");
		Speak.setClientSecret("P8AUgRpEkvQjP4YrSyBaOqCxzTXCg/YJWweuIRqCOc=");
		
		// Calls the speak service with text to be spoken and specifies the dialect in which to speak it
		if(lang.equalsIgnoreCase("eng"))
		{
			sWavUrl = Speak.execute(input, SpokenDialect.ENGLISH_UNITED_STATES);
		}
		else sWavUrl = Speak.execute(input, SpokenDialect.CHINESE_SIMPLIFIED_PEOPLES_REPUBLIC_OF_CHINA);
		
		//Log.d("wavurl", "1" + sWavUrl);
		
		// Now, makes an HTTP Connection to get the InputStream
        final URL waveUrl = new URL(sWavUrl);
        final HttpURLConnection uc = (HttpURLConnection) waveUrl.openConnection();
        
        // Pass the input stream to the playClip method       
        //BufferedInputStream bis = new BufferedInputStream(uc.getInputStream());
        //playClip(bis);	
        return uc.getInputStream();
	}
	
	public static JSONArray getImageLinks(String searchText) throws JSONException, URISyntaxException
	{
		rootDir = Environment.getExternalStorageDirectory()
                .getAbsolutePath()+"/Your Language Friend"; 
		
		File dir = new File(rootDir);
		if(!dir.exists())
		{
			dir.mkdir();
		}
		//Log.d("helper before",searchText);
		
		searchText = searchText.replaceAll(" ", "+");
		
		//Log.d("helper after", searchText);
		String key="AIzaSyCmNuCygD1deYsuyhkEdB2ZkYJVMHwoe40";
		String cx = "007519588869388327554:anwe_ucuikw";
		URL url;
		int start = 0;
		for(start = 1; start < 11; start+=10)
		{
			try {
				url = new URL(
		  			"https://www.googleapis.com/customsearch/v1?key="+key+ "&cx="+ cx +"&q="+ searchText 
		  			+ "&alt=json&searchType=image&start=" + start);
				Log.d("helper url", "https://www.googleapis.com/customsearch/v1?key="+key+ "&cx="+ cx +"&q="+ searchText 
			  			+ "&alt=json&searchType=image&start=" + start);
				
				
				URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());

				url = new URL(uri.toASCIIString());
				
				
			  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			  conn.setRequestMethod("GET");
			  conn.setRequestProperty("Accept", "application/json");
			  
			  BufferedReader br = new BufferedReader(new InputStreamReader(
			  		(conn.getInputStream())));
			  String output;
			  
			  FileWriter fWriter = null;
			  BufferedWriter writer = null; 
			  
			  File imageFile = new File(rootDir + "/imageLinks.txt");
			  if(!imageFile.exists())
			  {
				  imageFile.createNewFile();
			  }
			  fWriter = new FileWriter(rootDir + "/imageLinks.txt",false);
			  writer = new BufferedWriter(fWriter);
			  		
			  while ((output = br.readLine()) != null) 
			  {
				  writer.write(output);
				  writer.newLine();
				  //System.out.println(output + "\n");		  	
			  }
			  writer.close();
			  
			
			  InputStream is2 = new FileInputStream(imageFile);
			  //Log.d("Image Links", "Pass convert to IS");
			  
			  //InputStream is = TranslateHelperClass.class.getResourceAsStream("imageLinks.txt");
			  String jsonTxt = IOUtils.toString( is2 );
			 // Log.d("Image Links", "Pass convert to STRING");
		      
			  JSONObject json = new JSONObject(jsonTxt); 
			  //Log.d("Image Links", "Pass convert to JSONObject");
			  
			  JSONArray item = json.getJSONArray("items");
			  //Log.d("Image Links", "Pass convert to JSONArray");
			  
			  conn.disconnect();
			  
			  return item;

			  } catch (ClientProtocolException e) {
			      e.printStackTrace();
			      
			    } catch (IOException e) {
			      e.printStackTrace();
			      
			    }
		}
		return null;
	}
	
	public static void downloadImage(String urlString)
	{
		try 
		{
			rootDir = Environment.getExternalStorageDirectory()
	                .getAbsolutePath()+"/Your Language Friend"; 
			
			String fileDir = rootDir + "/images";
			File fd = new File(fileDir);
			if(!fd.exists())
			{
				fd.mkdir();
			}
			
            URL url = new URL(urlString); 
            String fileName = urlString.substring(urlString.lastIndexOf('/')+1, urlString.lastIndexOf('.')) + ".jpg";
            File file = new File(fileDir + "/" + fileName);

            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            /*
             * Read bytes to the Buffer until there is nothing more to read(-1).
             */
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
            
	    } 
		catch (IOException e) 
		{
	         
	    }
	}
	
	public static void downloadDB(String urlString)
	{
		try 
		{
			rootDir = Environment.getExternalStorageDirectory()
	                .getAbsolutePath()+"/Your Language Friend"; 
			
			String fileDir = rootDir;
			File fd = new File(fileDir);
			if(!fd.exists())
			{
				fd.mkdir();
			}
			
            URL url = new URL(urlString); 
            //String fileName = urlString.substring(urlString.lastIndexOf('/')+1, urlString.lastIndexOf('.')) + ".db";
            String fileName = "test.db";
            File file = new File(fileDir + "/" + fileName);

            //Log.d("open connection", "before connect ");
            URLConnection ucon = url.openConnection();
            //Log.d("open connection", "after connect");
            
//            URL url = new URL(urlString);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
// 
//            urlConnection.setRequestMethod("GET");
//            urlConnection.setDoOutput(true);
// 
//            //connect
//            urlConnection.connect();
            
            
            InputStream is = ucon.getInputStream();
            //Log.d("open connection", "after get inputstream");
            BufferedInputStream bis = new BufferedInputStream(is);
            /*
             * Read bytes to the Buffer until there is nothing more to read(-1).
             */
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
            
	    } 
		catch (IOException e) 
		{
	         Log.d("download exception", e.toString());
	    }
	}
	
	public static void copy(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	}
	
	public static String saveWav(String filename, InputStream in) throws IOException
	{
		rootDir = Environment.getExternalStorageDirectory()
                .getAbsolutePath()+"/Your Language Friend"; 
		File audioDir = new File(rootDir + "/Audio" );
		if(!audioDir.exists())
			audioDir.mkdir();
		
		File wavPath = new File(audioDir + "/" + filename + ".wav");
		
		OutputStream out = new FileOutputStream(wavPath);
		
		// Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    
	    out.close();
	    return wavPath.getCanonicalPath();
		
	}
	
	public static void playWav(String path) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
	{
		MediaPlayer mpPlayProgram = new MediaPlayer();
		mpPlayProgram.setDataSource(path);
		mpPlayProgram.prepare();
		mpPlayProgram.start();
		//mpPlayProgram.release();
	}
	
	public static String getPinYin(String text)
	{
		if(!text.equalsIgnoreCase(""))
    	{
    		//check if English string
    		boolean valid = text.matches("^[\u0000-\u0080]+$");
    		
    		if(!valid)
    		{
		    	String[] pinyinArray;
		    	List<String> pinyin = new ArrayList<String>();
		    	for(int i = 0; i < text.length(); i++)
		    	{
		    		pinyin.add(PinyinHelper.toHanyuPinyinStringArray(text.charAt(i))[0]);
		    	}
		    	
		    	text = pinyin.get(0);
		    	for(int i = 1; i < pinyin.size(); i ++)
		    	{
		    		text += (" " + pinyin.get(i));
		    	}
		    	return text;
    		}
    		else return "";
    	}
		return null;		
	}
	
	
	
}
