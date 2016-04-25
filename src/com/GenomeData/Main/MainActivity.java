package com.GenomeData.Main;

import java.util.Date;
import java.util.logging.Logger;

import com.GenomeData.Controller.Controller;
/**
 * Main class to create and populate the database
 * @author ankita
 *
 */
public class MainActivity {
	private final static Logger LOGGER = Logger.getLogger(MainActivity.class.getName()); 
	public static void main(String[] args) {
		Controller mController = new Controller();
		if (mController.createDatabase()) {
			long startTime = new Date().getTime();
			LOGGER.info("loading file data into database....");
			mController.populateDatabase();
			LOGGER.info("Data inserted successfully in "+(new Date().getTime() - startTime)/1000 +" seconds ");
			System.out.println("Data base created successfully.\nTo perform query operations on database run the MainActivityUI program.");
		}

	}
}
