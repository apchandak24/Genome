package com.GenomeData.Main;

import java.util.logging.Logger;

import com.GenomeData.Controller.Controller;

public class MainActivity {
	private final static Logger LOGGER = Logger.getLogger(MainActivity.class.getName()); 
	public static void main(String[] args) {
		// 2819469
		Controller mController = new Controller();
		if (mController.createDatabase()) {
			LOGGER.info("loading file data into database....");
			mController.populateDatabase();
			LOGGER.info("Data inserted successfully");
			System.out.println("Data base created successfully.\n To perform query operations on database run the MainAtivityUI program.");
		}

	}
}
