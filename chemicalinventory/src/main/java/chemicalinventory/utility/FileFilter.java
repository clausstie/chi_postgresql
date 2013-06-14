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

import java.io.*;
import java.io.FilenameFilter;

/**
 *An implementation of the FilenameFilter, used to make sure
 *that only filenames wich is of a spcific type is used.
 **/

public class FileFilter implements FilenameFilter {

	private String extension = "";
	
    public FileFilter(String extension)
    {
    	this.extension = extension;
		
		this.extension = "."+ this.extension;
    }
    
    /**
     * @param dir File.
     * @param name String.
     * @return Boolean.
     */    
    public boolean accept(File dir, String name) 
    {
        return (name.endsWith(this.extension));
    }
}
