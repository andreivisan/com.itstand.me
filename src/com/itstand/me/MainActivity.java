package com.itstand.me;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends ExpandableListActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	try {
	    	super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy); 
	        
	        ExpandableListAdapter expListAdapter =
	                new SimpleExpandableListAdapter(this,
							                        createGroupList(),              // Creating group List.
							                        R.layout.group_row,             // Group item layout XML.
							                        new String[] {"Group Item"},    // the key of group item.
							                        new int[] { R.id.row_name },    // ID of each group item.-Data under the key goes into this TextView.
							                        createChildList(),              // childData describes second-level entries.
							                        R.layout.child_row,             // Layout for sub-level entries(second level).
							                        new String[] {"Sub Item"},      // Keys in childData maps to display.
							                        new int[] { R.id.grp_child}){   // Data under the keys above go into these TextViews.
	        	@Override
                public View getChildView(int groupPosition, int childPosition,
                        				 boolean isLastChild, View convertView, ViewGroup parent) {
                    TextView myChildView = (TextView)super.getChildView(groupPosition, childPosition, isLastChild,convertView, parent).findViewById(R.id.grp_child);
                    Typeface oswaldFont = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Regular.ttf");
                    myChildView.setTypeface(oswaldFont);
                    myChildView.setTextColor(Color.parseColor("#fbc1b6"));
                    return myChildView;
                }

                @Override
                public View getGroupView(int groupPosition, boolean isExpanded,
                        				 View convertView, ViewGroup parent) {
                    TextView myGroupView = (TextView)super.getGroupView(groupPosition, isExpanded, convertView, parent).findViewById(R.id.row_name);
                    Typeface oswaldFont = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
                    myGroupView.setTypeface(oswaldFont);
                    return myGroupView;
                }
	        };   
	        
	        final ExpandableListView listView = this.getExpandableListView();
	        
	        final Map<String, String> newsMap = createNewsNameURLMap();
	        
	        listView.setAdapter(expListAdapter); 
	        
	        listView.setOnChildClickListener(new OnChildClickListener() {
	            @SuppressWarnings("unchecked")
				@Override
	            public boolean onChildClick(ExpandableListView parent, View v,
	                    				    int groupPosition, int childPosition, long id) {
	            	Map<String, String> childText = (Map<String, String>)parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
	            	String url = newsMap.get(childText.get("Sub Item"));
	            	Intent in = new Intent(getApplicationContext(), NewsDetailsActivity.class);
	            	in.putExtra("page_url", url);
	                startActivity(in);
	            	return true;
	            }
	        });

    	} catch(Exception e) {
            System.out.println("Error Message: " + e.getMessage());
        } 
    	
    }
    
	private List<HashMap<String, String>> createGroupList() {
    	ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
    	JSONObject json = JsonUtil.getJSONfromURL("http://itstand.me/restserver/sources.json");
    	try {
        	JSONArray sources = (JSONArray)json.get("stringList");
        	for(int i=0; i<sources.length(); i++) {
        		String source = sources.getString(i);
        		HashMap<String, String> map = new HashMap<String, String>();
        		map.put("Group Item", source);
        		result.add(map);
        	}	
         } catch(Exception e) {        
         	Log.e("log_tag", "Error parsing data " + e.getMessage());
         }
         return (List<HashMap<String, String>>)result;
    }
    
    private List<ArrayList<HashMap<String, String>>> createChildList() {
        ArrayList<ArrayList<HashMap<String, String>>> result = new ArrayList<ArrayList<HashMap<String, String>>>();
    	JSONObject json = JsonUtil.getJSONfromURL("http://itstand.me/restserver/sources.json");
    	try {
        	JSONArray sources = (JSONArray)json.get("stringList");
        	for(int i=0; i<sources.length(); i++) {
        		String source = sources.getString(i);
        		JSONObject newsJson = JsonUtil.getJSONfromURL("http://itstand.me/restserver/news/" + source + ".json");
        		JSONArray newsList = newsJson.getJSONArray("newsDataList");
        		ArrayList<HashMap<String, String>> secList = new ArrayList<HashMap<String, String>>();
        		for(int j=0; j<newsList.length(); j++) {
        			HashMap<String, String> child = new HashMap<String, String>();
        			JSONObject news = newsList.getJSONObject(j);
        			child.put( "Sub Item", news.getString("title"));
        			secList.add( child );
        		}
        		result.add(secList);
        	}	
         } catch(Exception e) {        
         	Log.e("log_tag", "Error parsing data " + e.getMessage());
         }
        return result;
    }
    
    public void onContentChanged() {
        System.out.println("onContentChanged");
        super.onContentChanged();
    }

    public void onGroupExpand(int groupPosition) {
        try {
        	System.out.println("Group exapanding Listener => groupPosition = " + groupPosition);
        } catch(Exception e) {
        	Log.e("log_tag", "Error parsing data " + e.getMessage());
        }
    }
    
    public Map<String, String> createNewsNameURLMap() {
    	Map<String, String> newsNameURLMap = new HashMap<String, String>();
    	JSONObject newsJson = JsonUtil.getJSONfromURL("http://itstand.me/restserver/news.json");
    	try {
        	JSONArray newsList = (JSONArray)newsJson.get("newsDataList");
        	for(int i=0; i<newsList.length(); i++) {
        		JSONObject news = newsList.getJSONObject(i);
        		newsNameURLMap.put(news.getString("title"), news.getString("url"));
        	}	
         } catch(Exception e) {        
         	Log.e("log_tag", "Error parsing data " + e.getMessage());
         }
    	return newsNameURLMap;
    }
    
}