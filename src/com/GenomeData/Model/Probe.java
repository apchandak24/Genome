package com.GenomeData.Model;
/**
 * Model class to save Probe result
 * @author ankita
 *
 */
public class Probe {
	
	private String name;
	private long start;
	private long end;
	private double value;
	public Probe(String name){
		this.name = name;
	}
	
	public Probe(String name,long start,long end,double value){
		this.name = name;
		this.start = start;
		this.end = end;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
	

}
