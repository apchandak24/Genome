package com.GenomeData.View;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.GenomeData.Model.Probe;

public class ResultTable extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String[] columns = { "Chromosome", "Start","End","Value" };
	private  ArrayList<Probe> probes=new ArrayList<>();

	public void setProbes(ArrayList<Probe> probes) {
		this.probes = probes;
	}
	public ResultTable(ArrayList<Probe> list) {
		this.probes= list;
	}
	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public int getRowCount() {
		return probes.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		 switch(column) {
         case 0: return probes.get(row).getName();
         case 1: return probes.get(row).getStart();
         case 2: return probes.get(row).getEnd();
         case 3: return probes.get(row).getValue();
     }
		return null;
	}
	
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

}
