package com.GenomeData.Controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import com.GenomeData.Database.DatabaseHelper;
import com.GenomeData.Database.DatabaseOperations;
import com.GenomeData.Database.PropertiesInstance;
import com.GenomeData.Model.Probe;
import com.GenomeData.Utils.Constants;

public class Controller {
	private DatabaseOperations dbService;

	public Controller() {

		dbService = new DatabaseOperations();
	}

	/**
	 * Get the list of values for single chromosome between specified range
	 * 
	 * @param query
	 * @return
	 */
	public ArrayList<Probe> getResultSingleChromosome(Probe query) {
		Connection dbConnection = DatabaseHelper.getConnection(PropertiesInstance.getInstance().getProperties());
		ArrayList<Probe> probes = new ArrayList<Probe>();
		try {
			if (query.getEnd() > query.getStart()) {
				if (dbConnection != null) {
					probes = dbService.getProbeList(dbConnection, query);
				}
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

	/**
	 * Get the list of values for multiple chromosomes
	 * 
	 * @param query
	 * @return
	 */
	public ArrayList<Probe> getResultMultipleChromosome(Probe p1, Probe p2) {
		Connection dbConnection = DatabaseHelper.getConnection(PropertiesInstance.getInstance().getProperties());
		ArrayList<Probe> probes = new ArrayList<Probe>();
		try {
			String[] chrName = Constants.CHROMOSOME_NAMES;
			int sIndex = Arrays.asList(chrName).indexOf(p1.getName());
			int eIndex = Arrays.asList(chrName).indexOf(p2.getName());
			ArrayList<Probe> queryProbes = new ArrayList<>();
			if (sIndex <= eIndex) {
				for (int i = sIndex + 1; i < eIndex; i++) {
					Probe p = new Probe(chrName[i]);
					queryProbes.add(p);
				}
				if (sIndex == eIndex)
					p2 = null;

				probes.addAll(dbService.getMultipleProbeListByNames(dbConnection, queryProbes, p1, p2));
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

	/**
	 * Create table probe
	 * 
	 * @return
	 */
	public boolean createDatabase() {
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

	/**
	 * Load probes.txt into database
	 */
	public void populateDatabase() {
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
