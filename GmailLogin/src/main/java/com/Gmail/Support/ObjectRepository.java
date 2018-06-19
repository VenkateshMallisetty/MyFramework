package com.Gmail.Support;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.Gmail.accelerators.TestEngine;

public class ObjectRepository
{
	static DocumentBuilderFactory docBuilderFactory = null;
	   static DocumentBuilder docBuilder = null;
	   static  Document doc = null;
	   public static Map<String,String> identification;
	   public static Map<String,String> locations;
	   
	    public static void storeIdentification() throws Throwable
	    {  
	    	identification=new HashMap<String,String>();
	    	locations=new HashMap<String,String>();
	    	docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.parse (new File(TestEngine.configProps.getProperty("ObjectRepository")));
			
			 NodeList listOfObject = doc.getElementsByTagName("object");
			 for(int s=0; s<listOfObject.getLength() ; s++){
            
            try{
                 	
               
		         Node firstOjectNode = listOfObject.item(s);
		         if(firstOjectNode.getNodeType() == Node.ELEMENT_NODE){


		             Element firstObjectElement = (Element)firstOjectNode;

		             //-------
		             NodeList firstNameList = firstObjectElement.getElementsByTagName("name");
		             Element firstNameElement = (Element)firstNameList.item(0);
		             NodeList textFNList = firstNameElement.getChildNodes();
		             String name=((Node)textFNList.item(0)).getNodeValue().trim();
		             
		             NodeList lastNameList = firstObjectElement.getElementsByTagName("identifyBy");
	                 Element lastNameElement = (Element)lastNameList.item(0);
	                 NodeList textLNList = lastNameElement.getChildNodes();
	                 String identifyby=((Node)textLNList.item(0)).getNodeValue().trim();
	                 
	                 
		             NodeList lastValueList = firstObjectElement.getElementsByTagName("value");
	                 Element lastValueElement = (Element)lastValueList.item(0);
	                 NodeList textLNList1 = lastValueElement.getChildNodes();
	                 String value=((Node)textLNList1.item(0)).getNodeValue().trim();
	                 
	                
	                 identification.put(name, identifyby);
	                 locations.put(name, value);
		         }
             }
		         catch(NullPointerException e)
		         {
		        	 
		          continue;
	              
		         }
			 }
	    	
	    
	    	
	    }
//	    public static void storeValue() throws Throwable
//	    {  
//	    	
//	    	locations=new HashMap<String, String>();
//	    	docBuilderFactory = DocumentBuilderFactory.newInstance();
//			docBuilder = docBuilderFactory.newDocumentBuilder();
//			doc = docBuilder.parse (new File(TestEngine.configProps.getProperty("ObjectRepository")));
//			
//			 NodeList listOfObject = doc.getElementsByTagName("object");
//			
//			 for(int s=0; s<listOfObject.getLength() ; s++){
//            
//                try{ 
//		         Node firstOjectNode = listOfObject.item(s);
//		         if(firstOjectNode.getNodeType() == Node.ELEMENT_NODE){
//
//
//		             Element firstObjectElement = (Element)firstOjectNode;
//
//		             //-------
//		             NodeList firstNameList = firstObjectElement.getElementsByTagName("name");
//		             Element firstNameElement = (Element)firstNameList.item(0);
//
//		             NodeList textFNList = firstNameElement.getChildNodes();
//		             String name=((Node)textFNList.item(0)).getNodeValue().trim();
//		             
//		             NodeList lastNameList = firstObjectElement.getElementsByTagName("value");
//	                 Element lastNameElement = (Element)lastNameList.item(0);
//	                 NodeList textLNList = lastNameElement.getChildNodes();
//	                 String value=((Node)textLNList.item(0)).getNodeValue().trim();
//	                 
//	                                
//	                 
//	                 locations.put(name, value);
//		              }
//                }
//		         catch(NullPointerException e)
//		         {
//		        	  continue;
//		       
//		         }
//			 }
//			 
//		         
//		   
//	    	
//	    	
//	    	
//	    }
	    public static String getIdentify(String objectName) throws Throwable
		{
	    	 
	    	return identification.get(objectName);
	    	
		}
	    public static String getValue(String objectName) throws Throwable
		{
	    	
	    
	    	return locations.get(objectName);
	    	
		}
	   

}
