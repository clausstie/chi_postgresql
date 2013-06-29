/*
 *   Description: Application used for managing a chemical storage solution.
 *                This application handles users, compounds, containers,
 *                suppliers, locations, labelprinting and everything else
 *       	      neded to manage a chemical storage, based on the java technology.
 *	    	      In addition it includes a sample module. This module, is used
 *      	      to create samples, store results etc.
 *
 *   Copyright:   Copyright Dann Vestergaard and Claus Stie Kallesoe 2004-2009.
 *				  All rights reserved.
 *
 *   overLIB:     overLIB 3.51  -- Copyright Erik Bosrup 1998-2002. All rights reserved.
 *
 *   This file is part of chemicalinventory.
 *
 *   chemicalinventory is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   any later version.
 *
 *   chemicalinventory is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Foobar; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 */
package chemicalinventory.utility;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import chemicalinventory.context.Attributes;

/**
 * @author Dann Vestergaard
 */
public class FileOperations {

	public FileOperations()
	{
	}
	
	/**
	 * Get the list of files in the image folder
	 * @return
	 */
	public Vector listImages()
	{
		File file = new File(Attributes.IMAGE);
		String[] list;
		Vector files = new Vector();
		
		//get the files for the different types
	    FilenameFilter filter = new FileFilter("gif");
	    list = file.list(filter);
	    addArrayElementsToVector(list, files);	
	    list = null; 
	    
	    FilenameFilter filter1 = new FileFilter("jpeg");
	    list = file.list(filter1);
	    addArrayElementsToVector(list, files);	
	    list = null; 
	    
	    FilenameFilter filter2 = new FileFilter("jpg");
	    list = file.list(filter2);
	    addArrayElementsToVector(list, files);	
	    list = null; 
	    
	    FilenameFilter filter3 = new FileFilter("png");
	    list = file.list(filter3);
	    addArrayElementsToVector(list, files);	
	    list = null; 
	   	   
	    return files;
	}

	
	
	/**
	 * Get the list of files in the disabled image folder.
	 * @return
	 */
	public Vector listImages_disabled()
	{	    
		File file = new File(Attributes.IMAGE_DISABLED);	    
		String[] list;
		Vector files = new Vector();
		
		//get the files for the different types
	    FilenameFilter filter = new FileFilter("gif");
	    list = file.list(filter);
	    addArrayElementsToVector(list, files);	
	    list = null; 
	    
	    FilenameFilter filter1 = new FileFilter("jpeg");
	    list = file.list(filter1);
	    addArrayElementsToVector(list, files);	
	    list = null; 
	    
	    FilenameFilter filter2 = new FileFilter("jpg");
	    list = file.list(filter2);
	    addArrayElementsToVector(list, files);	
	    list = null; 
	    
	    FilenameFilter filter3 = new FileFilter("png");
	    list = file.list(filter3);
	    addArrayElementsToVector(list, files);	
	    list = null; 
	   	   
	    return files;
	}
	
	/**
	 * Add the element of an array to the end of a vector.
	 * @param arr
	 * @param vec
	 */
	private void addArrayElementsToVector(String[] arr, Vector vec)
	{
		if(arr.length >= 1)
		{
			for(int i = 0; i<arr.length; i++)
				vec.add(arr[i]);
		}
	}
}
