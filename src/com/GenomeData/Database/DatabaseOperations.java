package com.GenomeData.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.GenomeData.Model.Probe;

public class DatabaseOperations {

	private static final String PROBE_TABLE_NAME = "probe";
	private static final String NAME_INDEX = "name_index";
	private static final String START_INDEX = "start_index";
	private static final String FILE_PATH = "Probes/probes.txt";

	/**
	 * Create the probe table if it is not present in database. This can be
	 * avoided if table is already created in data base before the application
	 * starts.
	 * 
	 * @param dbConnection
	 */
	public boolean createProbeTable(Connection dbConnection){
		Statement stmt = null;
		try {
			stmt = dbConnection.createStatement();
			String query = "CREATE TABLE IF NOT EXISTS " + PROBE_TABLE_NAME + " ("
					+ " id BIGINT NOT NULL AUTO_INCREMENT , " + " name VARCHAR(255), "
					+ " start BIGINT, end BIGINT, value DOUBLE, " + " PRIMARY KEY ( id ) " + " )";
			stmt.executeUpdate(query);

			createNameIndex(dbConnection);
			createStartIndex(dbConnection);
			return true;
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
		return false;

	}

	/**
	 * Create index on the table by chromosome name
	 * 
	 * @param dbConnection
	 */

	private void createNameIndex(Connection dbConnection) {
		Statement stmt = null;
		if (checkIfNameIndexExists(dbConnection)) {
			try {
				stmt = dbConnection.createStatement();
				String query = "CREATE Index " + NAME_INDEX + " ON " + PROBE_TABLE_NAME + " (name) USING HASH";
				stmt.executeUpdate(query);
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
		}
	}
	
	/**
	 * Create index on the table by start value
	 * 
	 * @param dbConnection
	 */

	private void createStartIndex(Connection dbConnection) {
		Statement stmt = null;
		if (checkIfStartIndexExists(dbConnection)) {
			try {
				stmt = dbConnection.createStatement();
				String query = "CREATE Index " + START_INDEX + " ON " + PROBE_TABLE_NAME + " (start) USING BTREE";
				stmt.executeUpdate(query);
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
		}
	}

	/**
	 * Check if name index already exists
	 * 
	 * @param dbConnection
	 * @return
	 */

	private boolean checkIfNameIndexExists(Connection dbConnection) {
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
	/**
	 * Check if start index already exists
	 * 
	 * @param dbConnection
	 * @return
	 */

	private boolean checkIfStartIndexExists(Connection dbConnection) {
		PreparedStatement stmt = null;
		try {

			String query = "SELECT COUNT(1) IndexIsThere FROM INFORMATION_SCHEMA.STATISTICS WHERE "
					+ "table_schema=DATABASE() AND table_name Like ? AND index_name Like ? ";
			stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, PROBE_TABLE_NAME);
			stmt.setString(2, START_INDEX);
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

	/**
	 * Get the list of probe for a single chromosome between specified start and end values 
	 * 
	 * @param dbConnection
	 * @return ArrayList<Probe>
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
	/**
	 * Get the list of probe for a single chromosome between for values greater than given base value
	 * 
	 * @param dbConnection
	 * @return ArrayList<Probe>
	 */
	public ArrayList<Probe> getProbeListStart(Connection dbConnection, Probe probe) {
		ArrayList<Probe> probes = new ArrayList<Probe>();
		PreparedStatement stmt = null;
		try {

			String query = "SELECT * from " + PROBE_TABLE_NAME
					+ " where ( name Like ? ) AND ( start >= ?)";
			stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, probe.getName());
			stmt.setLong(2, probe.getStart());
			System.out.println(stmt.toString());
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
	/**
	 * Get the list of probe for a single chromosome between for values less than given base value
	 * 
	 * @param dbConnection
	 * @return ArrayList<Probe>
	 */
	public ArrayList<Probe> getProbeListEnd(Connection dbConnection, Probe probe) {
		ArrayList<Probe> probes = new ArrayList<Probe>();
		PreparedStatement stmt = null;
		try {

			String query = "SELECT * from " + PROBE_TABLE_NAME
					+ " where ( name Like ? ) AND ( start <= ?)";
			stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, probe.getName());
			stmt.setLong(2, probe.getStart());
			System.out.println(stmt.toString());
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
	
	/**
	 * Get the list of probe for a single chromosome
	 * 
	 * @param dbConnection
	 * @return ArrayList<Probe>
	 */
	public ArrayList<Probe> getProbeListByName(Connection dbConnection, Probe probe) {
		ArrayList<Probe> probes = new ArrayList<Probe>();
		PreparedStatement stmt = null;
		try {

			String query = "SELECT * from " + PROBE_TABLE_NAME
					+ " where ( name Like ? )";
			stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, probe.getName());
			System.out.println(stmt.toString());
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
	
	
	/**
	 * Load the probe.txt data from location Probes/probes.txt
	 * @param dbConnection
	 */
	
	public void loadFileData(Connection dbConnection){
		
		String esquel = " LOAD DATA LOCAL INFILE '" + FILE_PATH +
                "' INTO TABLE probe IGNORE 1 lines(name,start,end,value) " ;
		Statement stmtq = null;
		try {
			stmtq = dbConnection.createStatement();
			stmtq.executeUpdate(esquel);
		}catch (SQLException exception) {
			exception.printStackTrace();
			System.out.println(exception.getMessage());
		} finally {
			if (stmtq != null)
				try {
					stmtq.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

		
	}

}