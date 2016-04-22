package com.GenomeData.Main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.GenomeData.Database.DatabaseHelper;
import com.GenomeData.Database.DatabaseService;
import com.GenomeData.Database.PropertiesInstance;
import com.GenomeData.Model.Probe;

public class MainActivity {
	private static DatabaseService dbService;

	public static void main(String[] args) {
		// 2819469
		dbService = new DatabaseService();

//		if (createDatabase()) {
//			System.out.println("loading file data into database....");
//			populateDatabase();
//			System.out.println("Data inserted successfully");
//		}
		System.out.println("Executing query.... ");
		Probe query = new Probe("chr2", 0, 249228390, 0);
			
		ArrayList<Probe> result = getResultSingleChromosome(query);
			
		System.out.println("obtained result size-- "+result.size());
		

	}

	private static ArrayList<Probe> getResultSingleChromosome(Probe query) {
		Connection dbConnection = DatabaseHelper.getConnection(PropertiesInstance.getInstance().getProperties());
		ArrayList<Probe> probes = new ArrayList<Probe>();
		try {
			if (dbConnection != null) {
				probes = dbService.getProbeList(dbConnection, query);

			}
		} finally {
			if (dbConnection != null)
				try {
					dbConnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return probes;

	}
	private static ArrayList<Probe> getResultMultipleChromosome(Probe p1,Probe p2) {
		Connection dbConnection = DatabaseHelper.getConnection(PropertiesInstance.getInstance().getProperties());
		ArrayList<Probe> probes = new ArrayList<Probe>();
		try {
				
			
			
			
			
		} finally {
			if (dbConnection != null)
				try {
					dbConnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return probes;

	}
	
	

	private static boolean createDatabase() {
		Connection dbConnection = DatabaseHelper.getConnection(PropertiesInstance.getInstance().getProperties());
		try {
			return dbService.createProbeTable(dbConnection);
		} finally {
			if (dbConnection != null)
				try {
					dbConnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	private static void populateDatabase() {
		Connection dbConnection = DatabaseHelper.getConnection(PropertiesInstance.getInstance().getProperties());
		try {
			if (dbConnection != null) {
				dbService.loadFileData(dbConnection);
			}
		} finally {
			if (dbConnection != null)
				try {
					dbConnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

	}

}
