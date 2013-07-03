package chemicalinventory.compound.export;

import java.io.File;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;

import javax.xml.transform.dom.DOMSource;
  
import javax.xml.transform.stream.StreamResult; 

import java.io.*;


import chemicalinventory.context.Attributes;

public class Compound_export implements Serializable {
	
	/**
	 * Default serialversion id.
	 */
	private static final long serialVersionUID = 1L;

	private int compound_id;
	
	public int createExportData(int compound_id) {
		
		/*
		 * First check if an export xml file is allready crated.
		 */
		
		/*try {
			//Create instance of DocumentBuilderFactory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			//Get the DocumentBuilder
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			//Create blank DOM Document
			Document doc = docBuilder.newDocument();

			//create the root element
			Element root = doc.createElement("compound");
			//all it to the xml tree
			doc.appendChild(root);

			//create a comment
			Comment comment = doc.createComment("This is comment");
			//add in the root element
			root.appendChild(comment);

			//create child element
			Element childElement = doc.createElement("compound_id");
			//Add the atribute to the child
			childElement.setAttribute("compound_id","1");
			root.appendChild(childElement);


			TransformerFactory tranFactory = TransformerFactory.newInstance(); 
			Transformer aTransformer = tranFactory.newTransformer(); 

			Source src = new DOMSource(doc); 
			Result dest = new StreamResult(System.out); 
			aTransformer.transform(src, dest); 	

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("************ERROR ERROR************");
			e.printStackTrace();
		}*/
	
		return 1000;
	}
	
	public Document createEmptyExportXML() {
		
		try {
			//Create instance of DocumentBuilderFactory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			//Get the DocumentBuilder
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			//Create blank DOM Document
			Document doc = docBuilder.newDocument();

			//create the root element
			Element root = doc.createElement("CI-Compounds");
			//all it to the xml tree
			doc.appendChild(root);

			//create a comment
			Comment comment = doc.createComment("This file holds data for a number of compounds to export from a runnig CI System.");
			//add in the root element
			root.appendChild(comment);
			
			doc.getDocumentElement().normalize();
			
			/*
			 * Save the created xml instance to a file.
			 */
			System.out.println("normal app: "+Attributes.APPLICATION);
			System.out.println("qualified app: "+Attributes.QUALIFIED_APPLICATIOM);
									
			File xmlFile = new File(Attributes.QUALIFIED_APPLICATIOM+"/xml/compound_export.xml");
		//	if(xmlFile.exists())
			//	xmlFile.delete();		
			
			//DOMSource source = new DOMSource(doc);
			
			
			
			//docBuilder. parse(xmlFile);
			
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(xmlFile);
			transformer.transform(source, result);
						
			return doc;
			
		} catch (TransformerConfigurationException tce) {
			 // Error generated by the parser
			   System.out.println ("\n** Transformer Factory error");
			   System.out.println("   " + tce.getMessage() );

			   // Use the contained exception, if any
			   Throwable x = tce;
			   if (tce.getException() != null)
			       x = tce.getException();
			   x.printStackTrace();
			   
			   return null;

		} catch (TransformerException te) {
			   // Error generated by the parser
			   System.out.println ("\n** Transformation error");
			   System.out.println("   " + te.getMessage() );

			   // Use the contained exception, if any
			   Throwable x = te;
			   if (te.getException() != null)
			       x = te.getException();
			   x.printStackTrace();
			   
			   return null;

		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
		
	}

}