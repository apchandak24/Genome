package com.GenomeData.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;

import com.GenomeData.Model.Probe;

public class DatabaseService {

	private static final String PROBE_TABLE_NAME = "probe";
	private static final String NAME_INDEX = "name_index";

	/**
	 * Create the probe table if it is not present in database. This can be
	 * avoided if table is already created in data base before the application
	 * starts.
	 * 
	 * @param dbConnection
	 */
	private void createProbeTable(Connection dbConnection) throws SQLException {
		Statement stmt = null;
		try {
			stmt = dbConnection.createStatement();
			String query = "CREATE TABLE IF NOT EXISTS " + PROBE_TABLE_NAME + " ("
					+ " id BIGINT NOT NULL AUTO_INCREMENT , " + " name VARCHAR(255), "
					+ " start BIGINT, end BIGINT, value DOUBLE, " + " PRIMARY KEY ( id ) " + " )";
			stmt.executeUpdate(query);

			createIndex(dbConnection);
		} catch (SQLException exception) {
			exception.printStackTrace();
			System.out.println(exception.getMessage());
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

	}
	/**
	 * Create index on the table by chromosome name
	 * @param dbConnection
	 */

	private void createIndex(Connection dbConnection) {
		Statement stmt = null;
		if (checkIfIndexExists(dbConnection)) {
			try {
				stmt = dbConnection.createStatement();
				String query = "CREATE Index " + NAME_INDEX + " ON " + PROBE_TABLE_NAME + " (name) USING HASH";
				stmt.executeUpdate(query);
			} catch (SQLException exception) {
				exception.printStackTrace();
				System.out.println(exception.getMessage());
			} finally {
				if (stmt != null)
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
		}
	}

	private boolean checkIfIndexExists(Connection dbConnection) {
		PreparedStatement stmt = null;
		try {

			String query = "SELECT COUNT(1) IndexIsThere FROM INFORMATION_SCHEMA.STATISTICS WHERE "
					+ "table_schema=DATABASE() AND table_name Like ? AND index_name Like ? ";
			stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, PROBE_TABLE_NAME);
			stmt.setString(2, NAME_INDEX);
			ResultSet result = stmt.executeQuery();
			int cnt = 0;
			
				while (result.next()) {
					cnt = result.getInt("IndexIsThere");
				}
			return cnt == 0 ? false : true;

		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return false;
	}

	public boolean insertData(LinkedList<Probe> probes, Connection dbConnection) {
		PreparedStatement stmt = null;
		try {
			createProbeTable(dbConnection);

			String query = "INSERT INTO " + PROBE_TABLE_NAME + " (name, start, end, value) VALUES" + "(?,?,?,?)";
			stmt = dbConnection.prepareStatement(query);
			int cnt = 0;
			for (Probe probe : probes) {
				stmt.setString(1, probe.getName());
				stmt.setLong(2, probe.getStart());
				stmt.setLong(3, probe.getEnd());
				stmt.setDouble(4, probe.getValue());
				stmt.addBatch();
				cnt++;
				if ((cnt % 1000) == 0 || cnt == probes.size()) {
					System.out.println("added " + cnt / 1000 + " batch");
					stmt.executeBatch();
				}
			}
			return true;
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return false;
	}

	/**
	 * Get the list of symbols entered by user from database
	 * 
	 * @param dbConnection
	 * @return ArrayList<Symbol>
	 */
	public ArrayList<Probe> getProbeList(Connection dbConnection, Probe probe) {
		ArrayList<Probe> probes = new ArrayList<Probe>();
		PreparedStatement stmt = null;
		try {

			String query = "SELECT * from " + PROBE_TABLE_NAME
					+ " where ( name Like ? ) AND (( start <= ? AND end >= ? ) OR "
					+ "( start >= ? AND end <= ? ) OR ( start <= ? AND end >= ? ))";
			stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, probe.getName());
			stmt.setLong(2, probe.getStart());
			stmt.setLong(3, probe.getStart());
			stmt.setLong(4, probe.getStart());
			stmt.setLong(5, probe.getEnd());
			stmt.setLong(6, probe.getEnd());
			stmt.setLong(7, probe.getEnd());
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				Probe p = new Probe(result.getString("name"), result.getLong("start"), result.getLong("end"),
						result.getDouble("value"));
				probes.add(p);
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return probes;
	}

}
