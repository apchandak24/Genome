package com.GenomeData.Main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import com.GenomeData.Controller.Controller;
import com.GenomeData.Database.DatabaseHelper;
import com.GenomeData.Database.DatabaseOperations;
import com.GenomeData.Database.PropertiesInstance;
import com.GenomeData.Model.Probe;
import com.GenomeData.Utils.Constants;
import com.GenomeData.View.UserInterface;

public class MainActivity {
	private static DatabaseOperations dbService;
	
	public static void main(String[] args) {
		// 2819469
		dbService = new DatabaseOperations();
		Controller mController = new Controller();
		UserInterface ui = new UserInterface(mController);
//
//		if (createDatabase()) {
//			System.out.println("loading file data into database....");
//			populateDatabase();
//			System.out.println("Data inserted successfully");
//		}
//		System.out.println("Executing query.... ");
//		// Probe query = new Probe("chr2", 0, 249228390, 0);
//		//
//		// ArrayList<Probe> result = getResultSingleChromosome(query);
//		//
//		// System.out.println("obtained result size-- " + result.size());
//		Probe p1 = new Probe("chr1", 62924, 0, 0);
//		Probe p2 = new Probe("chr10", 0, 0, 0);
//		System.out.println(getResultMultipleChromosome(p1, p2).size());

	}

	private static ArrayList<Probe> getResultMultipleChromosome(Probe p1, Probe p2) {
		Connection dbConnection = DatabaseHelper.getConnection(PropertiesInstance.getInstance().getProperties());
		ArrayList<Probe> probes = new ArrayList<Probe>();
		try {
			String [] chrName = Constants.CHROMOSOME_NAMES;
			int sIndex = Arrays.asList(chrName).indexOf(p1.getName());
			int eIndex = Arrays.asList(chrName).indexOf(p2.getName());
			if (sIndex <= eIndex) {
				probes.addAll(dbService.getProbeListStart(dbConnection, p1));
				for (int i = sIndex + 1; i < eIndex; i++) {
					Probe p = new Probe(chrName[i]);
					probes.addAll(dbService.getProbeListByName(dbConnection, p));
				}
				probes.addAll(dbService.getProbeListEnd(dbConnection, p2));
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
