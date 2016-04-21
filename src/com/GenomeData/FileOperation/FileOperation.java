package com.GenomeData.FileOperation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import com.GenomeData.Model.Probe;

public class FileOperation {

	private BufferedReader mBufferedReader;
	private static final String FILE_PATH = "Probes/probe_small.txt";

	public FileOperation() {

	}

	public void loadData() {
		try {
			mBufferedReader = new BufferedReader(new FileReader(FILE_PATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public LinkedList<Probe> parseData() {
		String line;
		LinkedList<Probe> list = new LinkedList<Probe>();
		int cnt = 0;
		try {
			while ((line = mBufferedReader.readLine()) != null) {
				String values[] = line.split("\t");
				if (cnt != 0) {
					if (values.length >= 4) {
						Probe probe = new Probe(values[0], Long.parseLong(values[1].trim()),
								Long.parseLong(values[2].trim()), Double.parseDouble(values[3]));
						list.add(probe);
					}
				}else{
					cnt++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
