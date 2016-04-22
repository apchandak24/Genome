package com.GenomeData.View;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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
		mainFrame.add(queryPanelRange);
		mainFrame.add(queryPanelBase);
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
		JComboBox<String> chrComboBox1 = new JComboBox<String>(Constants.CHROMOSOME_NAMES);
		JComboBox<String> chrComboBox2 = new JComboBox<String>(Constants.CHROMOSOME_NAMES);
		JLabel base1 = new JLabel("Base");
		JLabel base2 = new JLabel("Base");
		JTextField startBase1 = new JTextField(8);
		JTextField startBase2 = new JTextField(8);
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
					String name = (String) chrComboBox.getSelectedItem();
					long startVal = Long.parseLong(start.getText().trim());
					long endVal = Long.parseLong(end.getText().trim());
					resultList = mController.getResultSingleChromosome(new Probe(name, startVal, endVal, 0));
					mResultTable.setProbes(resultList);
					mResultTable.fireTableDataChanged();
					resultLabel.setText("Total records fetched: "+resultList.size());
				} catch (NumberFormatException exception) {
					resultLabel.setText("Please enter numeric values");
				}
				break;
			case "Base":

				break;
			default:
				break;
			}
		}

	}

}
