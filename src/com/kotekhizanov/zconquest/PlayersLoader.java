package com.kotekhizanov.zconquest;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;

public class PlayersLoader {

	Players PlayerList;
	
	public PlayersLoader() {
		super();
		
		MyAsyncTask asyncTask = new MyAsyncTask();
		asyncTask.execute();
		
		
	}
	
	
	
	private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
  	  @Override
  	  protected Double doInBackground(String... params) {
  		// TODO Auto-generated method stub
  	        postData();
  	        return null;
  	  }
  	 
  	  protected void onPostExecute(Double result){
  	  }
  	}
    
    public void postData() {
  		try {
  			DefaultHttpClient client = new DefaultHttpClient();
  			HttpGet request = new HttpGet("http://kotetest.ilc.ge/xmlTest.html");
  			HttpResponse response = client.execute(request);
  			InputStream in = response.getEntity().getContent();
  			  			
  			XmlPullParserFactory pullParserFactory;
  			try {
  				pullParserFactory = XmlPullParserFactory.newInstance();
  				XmlPullParser parser = pullParserFactory.newPullParser();

			    //parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
	            parser.setInput(in, null);

	            parseXML(parser);

  			} catch (XmlPullParserException e) {

  				e.printStackTrace();
  			} catch (IOException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}

  			
  		} catch (ClientProtocolException e) {
  			// process execption
  		} catch (IOException e) {
  			// process execption
  		}
  	}
    
    
    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
	{
    	PlayerList = null;
        int eventType = parser.getEventType();
        Player currentPlayer = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
        	
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	PlayerList = new Players();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("player")){
                        currentPlayer = new Player();
                    } else if (currentPlayer != null){
                        if (name.equals("PlayerId")){
                            currentPlayer.PlayerId = Integer.parseInt(parser.nextText());
                        } else if (name.equals("PlayerName")){
                        	currentPlayer.PlayerName = parser.nextText();
                        }  
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("player") && currentPlayer != null){
                    	PlayerList.AddPlayer(currentPlayer);
                    } 
            }
            eventType = parser.next();
        }

        PlayerList.LogPlayers();
	}
}
