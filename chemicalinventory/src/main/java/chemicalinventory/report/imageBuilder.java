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
package chemicalinventory.report;

import java.io.File;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import chemaxon.struc.Molecule;
import chemaxon.util.MolHandler;
import chemicalinventory.context.Attributes;


/**
 * @author Dann Vestergaard
 */
public class imageBuilder {
	
	private String tempFolder = "";
	private String compound_id = "";
	private String path = "";
	
	/**
	 * Create the image to insert into the compound report ....
	 * @param compound_id
	 * @return
	 */
	public boolean exportPIC(String compound_id)
	{
		String molecule = "";
		
		try{
			//Connection from the pool
			Context init = new InitialContext();
			if(init == null ) 
				throw new Exception("No Context");
			
			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup(Attributes.DATA_SOURCE);
			if(ds != null) 
			{
				Connection con = ds.getConnection();
				if(con != null)  
				{	
					Statement stmt = con.createStatement();
					
		
					String query = "SELECT s.cd_smiles" +
							" FROM compound c LEFT JOIN structures s" +
							" ON c.cd_id = s.cd_id" +
							" WHERE c.id = "+compound_id;
					
					ResultSet rs = stmt.executeQuery(query);
					
					if(rs.next())
					{
						molecule = rs.getString("s.cd_smiles");
					}
					else
					{
						con.close();
						return false;
					}
				}
				con.close();
			}
			
		}//end of try
		catch (Exception e)
		{
			return false;
		}
		
		try {
			File ft = File.createTempFile("mol-image", ".png", (new File(Attributes.IMAGE_REPORT)));
			
			RandomAccessFile ranfile = new RandomAccessFile(ft, "rw");
			
			MolHandler molhandler = new MolHandler(molecule);
			
			Molecule molec = molhandler.getMolecule();
			
			if(molec.getDim()<2)
			{
				molec.clean(2, "O1");
			}
			
			byte[] png = molec.toBinFormat("png");
			
			ranfile.write(png);
			ranfile.close();
			path = ft.getPath();
			path = path.substring(path.indexOf("mol-image"));
			
			return true;
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @return Returns the compound_id.
	 */
	public String getCompound_id() {
		return compound_id;
	}
	/**
	 * @param compound_id The compound_id to set.
	 */
	public void setCompound_id(String compound_id) {
		this.compound_id = compound_id;
	}
	/**
	 * @return Returns the path.
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return Returns the tempFolder.
	 */
	public String getTempFolder() {
		return tempFolder;
	}
	/**
	 * @param tempFolder The tempFolder to set.
	 */
	public void setTempFolder(String tempFolder) {
		this.tempFolder = tempFolder;
	}
}
