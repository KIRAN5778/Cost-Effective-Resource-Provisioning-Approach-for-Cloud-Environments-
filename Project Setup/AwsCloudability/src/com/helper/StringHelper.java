package com.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletRequest;



import com.constant.ServerConstants;


/*
 */
public class StringHelper {
	public static String formatCatagory(String d){
		String dual="";
		if(d==null){
			dual =  "";
		}
		else{
			if(d.endsWith(",")){
				d=d.substring(0,d.length()-1);
			}
			String s="";
			String tokens[]=d.split(",");
			for (int i = 0; i < tokens.length; i++) {
				s+= n2s(ServerConstants.catagoryFriendlyNamesMap.get(tokens[i]))+",";
			}
			dual=s;
			if(dual.endsWith(",")){
				dual=dual.substring(0,dual.length()-1);
			}
		}
			
		
		return dual;
	}
	
	public static String dateTokkmm(String timeFilter) {

		if (timeFilter.equalsIgnoreCase("Hour")) {
			return "hh a"  ;
		} else if (timeFilter.equalsIgnoreCase("Minute")) {
			return "mm:ss";
		} else if (timeFilter.equalsIgnoreCase("day")) {
			return "dd:MMM:YYYY";
		} else if (timeFilter.equalsIgnoreCase("month")) {
			return "MMM:YY";
		}
		return "dd:MMM:YYYY";

	}
	
	public static String n2s(String d){
		String dual="";
		if(d==null){
			dual =  "";
		}
		else
			dual=d.toString().trim();
		
		return dual;
	}
	public static boolean  n2b(Object d){
		boolean  dual=false;
		if(d==null){  
			dual =  false;
		}
		else
			dual=new Boolean(d.toString()).booleanValue();
		
		return dual;
	}
	public static String n2s(Object d){
		String dual="";
		if(d==null){
			dual =  "";
		}
		else
			dual=d.toString().trim();
		
		return dual;
	}
	public static float  nullObjectToFloatEmpty(Object d){
		float i=0;
		if(d!=null){
			String dual=d.toString().trim();
			try{
				i=new Float(dual).floatValue();
			}catch (Exception e) {
				System.out.println("Unable to find integer value");	
			}
		}
		return i;
	}
	public static double  n2d(Object d){
		double i=0;
		if(d!=null){
			String dual=d.toString().trim();
			try{
				i=new Double(dual).doubleValue();
			}catch (Exception e) {
				System.out.println("Unable to find integer value");	
			}
		}
		return i;
	}
	public static float  n2f(Object d){  
		float i=0;
		if(d!=null){
			String dual=d.toString().trim();
			try{
				i=new Float(dual).floatValue();
			}catch (Exception e) {
				System.out.println("Unable to find integer value");	
			}
		}
		return i;
	}
	public static int n2i(Object d){
		int i=0;
		if(d!=null){
			String dual=d.toString().trim();
			try{
				i=new Integer(dual).intValue();
			}catch (Exception e) {
				System.out.println("Unable to find integer value");	
			}
		}
		return i;
	}
	public static HashMap displayRequest(ServletRequest request){

		  Enumeration paraNames = request.getParameterNames();

		   System. out.println(" ------------------------------");

		   System. out.println("parameters:");

		    HashMap parameters=new HashMap();
		    
		    String pname;

		    String pvalue;

		    while (paraNames.hasMoreElements()) {

		      pname = (String)paraNames.nextElement();

		      pvalue = request.getParameter(pname);

		      System.out.println(pname + " = " + pvalue + "");

		      parameters.put(pname, pvalue);
		    }
		    
		    Enumeration<String> requestAttributes = request.getAttributeNames();
	        while (requestAttributes.hasMoreElements()) {
	        	try{
	            String attributeName = n2s(requestAttributes.nextElement());
	            String attributeValue = n2s(request.getAttribute(attributeName));
	     
	            parameters.put(attributeName, attributeValue);
	        	}catch (Exception e) {
					// TODO: handle exception
				}
	        	
	        }

		   System.out.println("------------------------------");   
		   return parameters;
		  }
	public static StringBuffer readURLContent(String url) {
		System.out.println("URL " + url);
		StringBuffer json = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new URL(url).openStream()));
			String line;
			while ((line = in.readLine()) != null) {
				json.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	  public static HashMap sortByValues(HashMap map) { 
	       List list = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue())
	                  .compareTo(((Map.Entry) (o1)).getValue());
	            }
	       });

	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }

}


