package com.GenomeData.Main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.GenomeData.Database.DatabaseHelper;
import com.GenomeData.Database.DatabaseService;
import com.GenomeData.Database.PropertiesInstance;
import com.GenomeData.FileOperation.FileOperation;
import com.GenomeData.Model.Probe;

public class MainActivity {
	private static DatabaseService dbService;
	public static void main(String[] args) {
		//2819469 
		dbService = new DatabaseService();
		
		//LinkedList<Probe> probes =  readFileData();
		
		//populateDatabase(probes);
		Probe query = new Probe("chr1", 0, 1000000, 0);
		ArrayList<Probe> result = getResult(query);
		System.out.println(result.size());
		
	}
	private static ArrayList<Probe> getResult(Probe query){
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
	private static void populateDatabase(LinkedList<Probe> probes) {
		Connection dbConnection = DatabaseHelper.getConnection(PropertiesInstance.getInstance().getProperties());
		try {
			if (dbConnection != null) {
				dbService.insertData(probes, dbConnection);
				System.out.println("Data inserted successfully");
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
	private static LinkedList<Probe> readFileData(){
		FileOperation mfOperation = new FileOperation();
		mfOperation.loadData();
		LinkedList<Probe> probes = mfOperation.parseData();
		System.out.println("File read, total records "+probes.size());
		return probes;

	}

}
