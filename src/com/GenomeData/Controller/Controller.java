package com.GenomeData.Controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.GenomeData.Database.DatabaseHelper;
import com.GenomeData.Database.DatabaseOperations;
import com.GenomeData.Database.PropertiesInstance;
import com.GenomeData.Model.Probe;

public class Controller {
	private DatabaseOperations dbService;

	public Controller() {

		dbService = new DatabaseOperations();
	}

	public ArrayList<Probe> getResultSingleChromosome(Probe query) {
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
}
