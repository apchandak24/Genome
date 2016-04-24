package com.GenomeData.View;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumnModel;

import com.GenomeData.Controller.Controller;
import com.GenomeData.Model.Probe;
import com.GenomeData.Utils.Constants;

public class UserInterface {
	private JFrame mainFrame;
	private JPanel queryPanelRange;
	private JPanel queryPanelBase;
	private JPanel resultPanel;
	private Controller mController;
	private JComboBox<String> chrComboBox;
	private JTextField start;
	private JTextField end;
	private ResultTable mResultTable;
	private ArrayList<Probe> resultList = new ArrayList<>();
	private JLabel resultLabel;
	private JTextField startBase1;
	private JTextField startBase2;
	private JComboBox<String> chrComboBox1;
	private JComboBox<String> chrComboBox2;
	private JProgressBar bar;
	long startTime;
	public UserInterface(Controller mController) {
		this.mController = mController;
		createUI();
	}

	private void createUI() {
		mainFrame = new JFrame("Genome");
		mainFrame.setSize(600, 600);
		mainFrame.setLayout(new FlowLayout(FlowLayout.LEFT));

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		queryPanelRange = new JPanel(new FlowLayout(FlowLayout.LEFT));
		queryPanelBase = new JPanel(new FlowLayout(FlowLayout.LEFT));
		resultPanel = new JPanel(new FlowLayout());
		resultLabel = new JLabel();

		createRangeQueryPanel();
		createBaseQueryPanel();
		createResultPanel();

		bar = new JProgressBar();
		bar.setIndeterminate(true);
		bar.setStringPainted(true);
		bar.setString("Fetching data..");
		bar.setVisible(false);

		mainFrame.add(queryPanelRange);
		mainFrame.add(queryPanelBase);
		mainFrame.add(bar);
		mainFrame.add(resultLabel);
		mainFrame.add(resultPanel);
		
		mainFrame.setVisible(true);

	}

	private void createRangeQueryPanel() {
		chrComboBox = new JComboBox<String>(Constants.CHROMOSOME_NAMES);
		JLabel startLabel = new JLabel("Start");
		JLabel endLabel = new JLabel("End");
		start = new JTextField(8);
		end = new JTextField(8);
		JButton getResultRange = new JButton("Get Result");

		getResultRange.setActionCommand("Range");
		getResultRange.addActionListener(new ClickListener());

		queryPanelRange.add(chrComboBox);
		queryPanelRange.add(startLabel);
		queryPanelRange.add(start);
		queryPanelRange.add(endLabel);
		queryPanelRange.add(end);
		queryPanelRange.add(getResultRange);

	}

	private void createBaseQueryPanel() {
		chrComboBox1 = new JComboBox<String>(Constants.CHROMOSOME_NAMES);
		chrComboBox2 = new JComboBox<String>(Constants.CHROMOSOME_NAMES);
		JLabel base1 = new JLabel("Base");
		JLabel base2 = new JLabel("Base");
		startBase1 = new JTextField(8);
		startBase2 = new JTextField(8);
		JButton getResultBase = new JButton("Get Result");
		getResultBase.setActionCommand("Base");
		getResultBase.addActionListener(new ClickListener());

		queryPanelBase.add(chrComboBox1);
		queryPanelBase.add(base1);
		queryPanelBase.add(startBase1);
		queryPanelBase.add(chrComboBox2);
		queryPanelBase.add(base2);
		queryPanelBase.add(startBase2);
		queryPanelBase.add(getResultBase);

	}

	private void createResultPanel() {
		mResultTable = new ResultTable(resultList);
		JTable table = new JTable(mResultTable);
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(3).setPreferredWidth(100);
		columnModel.getColumn(0).setPreferredWidth(10);
		JScrollPane pane = new JScrollPane(table);
		pane.setPreferredSize(new Dimension(580, 450));
		resultPanel.add(pane);

	}

	private class ClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "Range":
				try {
					final String name = (String) chrComboBox.getSelectedItem();
					final long startVal = Long.parseLong(start.getText().trim());
					final long endVal = Long.parseLong(end.getText().trim());
					SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>()
					{
					    @Override
					    protected Void doInBackground()
					    {
					    	startTime = new Date().getTime();
					    	bar.setVisible(true);
					    	resultList = mController.getResultSingleChromosome(new Probe(name, startVal, endVal, 0));
							mResultTable.setProbes(resultList);
							return null;
					    }
					    @Override
					    protected void done()
					    {
					    	long elapse = new Date().getTime()-startTime;
					    	bar.setVisible(false);
					    	mResultTable.fireTableDataChanged();
							resultLabel.setText("Total records fetched: " + resultList.size()+" in "+(elapse/1000.0)+" seconds");
					    }
					};
					worker.execute();
				} catch (NumberFormatException exception) {
					resultLabel.setText("Please enter numeric values");
				}
				break;
			case "Base":
				try {
					String chr1 = (String) chrComboBox1.getSelectedItem();
					String chr2 = (String) chrComboBox2.getSelectedItem();
					long base1 = Long.parseLong(startBase1.getText().trim());
					long base2 = Long.parseLong(startBase2.getText().trim());
					final Probe p1 = new Probe(chr1);
					p1.setStart(base1);
					final Probe p2 = new Probe(chr2);
					p2.setStart(base2);
					
					SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>()
					{
					    @Override
					    protected Void doInBackground()
					    {
					    	startTime = new Date().getTime();
					    	bar.setVisible(true);
					    	resultList  = mController.getResultMultipleChromosome(p1, p2);
							mResultTable.setProbes(resultList);
							return null;
					    }
					 
					    @Override
					    protected void done()
					    {
					    	long elapse = new Date().getTime()-startTime;
					    	bar.setVisible(false);
					    	mResultTable.fireTableDataChanged();
							resultLabel.setText("Total records fetched: " + resultList.size()+" in "+(elapse/1000.0)+" seconds");
					    }
					};
					worker.execute();
				} catch (NumberFormatException exception) {
					resultLabel.setText("Please enter numeric values");
				}
				break;
			default:
				break;
			}
		}

	}

}
