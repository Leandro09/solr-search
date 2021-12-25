package com.example.restservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.*;
import org.apache.solr.common.*;
import org.apache.solr.common.params.GroupParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.noggit.JSONUtil;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class TitlesResource {

	HttpSolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/searchProject").build();

	public TitlesResource() {

		client.setParser(new XMLResponseParser());
	}


	@GetMapping(value="/titles")
	public ResponseEntity<Object> getTitles(@RequestParam("title") String title,@RequestParam("start") Integer start) {
		SolrQuery query = new SolrQuery();
		String queryString = "name:*" + title + "*";
        query.set("q", queryString);
        query.set("facet", true);
        query.set("facet.field", "genre");
        query.set("start", start);
        query.set("rows", 5);
        query.addOrUpdateSort( "date", org.apache.solr.client.solrj.SolrQuery.ORDER.desc );

        String returnValue = "";

        try {
          //  Block of code to try
          QueryResponse response = client.query(query);
          SolrDocumentList results = response.getResults();

          returnValue = JSONUtil.toJSON(results);
        }
        catch(Exception e) {

            return new ResponseEntity<Object>(e.toString(), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<Object>(returnValue, HttpStatus.OK);

	}
	

	
	@GetMapping(value="/groupTitles")
	public ResponseEntity<Object> getGroupTitles(@RequestParam("firstParamater") Integer p1,@RequestParam("start") Integer start,@RequestParam("secondParameter") Integer p2,@RequestParam(defaultValue = "") String genre) {
		SolrQuery query = new SolrQuery();
		
		String queryString = "";
		queryString += "genre:"+genre+" ";
		

		
		if(p1 == -1) {
			queryString += "rate:[*";
		}else {
			queryString += "rate:[" + p1;
		}
		
		if(p2 == -1) {
			queryString += " TO *]";
		}else {
			queryString += " TO " + p2 + "}";
		}
		
        query.set("q", queryString);
        query.set("start", start);
        query.set("rows", 20);
        
        ArrayList<String> titles = new ArrayList<>();

        try {
          //  Block of code to try
            QueryResponse response = client.query(query);
            
            SolrDocumentList results = response.getResults();
            for (int i = 0; i < results.size(); ++i) {
            	titles.add(results.get(i).getFieldValue("name").toString());
            }
            		      
        }
        catch(Exception e) {

            return new ResponseEntity<Object>(e.toString(), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<Object>(titles.toString(), HttpStatus.OK);

	
	}
	
	@GetMapping(value="/count")
	public ResponseEntity<Object> getCount(@RequestParam("firstParamater") Integer p1,@RequestParam("start") Integer start,@RequestParam("secondParameter") Integer p2) {
		SolrQuery query = new SolrQuery();
		
		String queryString = "";
		

		
		if(p1 == -1) {
			queryString += "rate:[*";
		}else {
			queryString += "rate:[" + p1;
		}
		
		if(p2 == -1) {
			queryString += " TO *]";
		}else {
			queryString += " TO " + p2 + "}";
		}
        query.set("q", queryString);
        query.set("rows", 20);
        query.set("start", start);
        
        query.setParam(GroupParams.GROUP, true);
        query.setParam(GroupParams.GROUP_FIELD, "rate");
        query.setParam(GroupParams.GROUP_SORT, "rate desc");
        query.set("fl", "doclist.numFound");
        
        List<JSONObject> docs = new ArrayList<JSONObject>();


        try {
          //  Block of code to try
            QueryResponse response = client.query(query);
            
            
            
		    GroupResponse gr = response.getGroupResponse();
		    for (GroupCommand gc : gr.getValues()) {
		      for (Group g : gc.getValues()) {
		    	
		    	JSONObject jo = new JSONObject();
		    	jo.put("group", g.getGroupValue());
		    	jo.put("count", g.getResult().getNumFound());
		    	docs.add(jo);
		    	
		      }
		    }
		    Collections.sort(docs, new MyJSONComparator());

		      
        }
        catch(Exception e) {

            return new ResponseEntity<Object>(e.toString(), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<Object>(docs.toString(), HttpStatus.OK);

	}
}


class MyJSONComparator implements Comparator<JSONObject> {

public int compare(JSONObject o1, JSONObject o2) {
    String v1 = (String) o1.get("group");
    String v3 = (String) o2.get("group");
    return v3.compareTo(v1);
}

}
