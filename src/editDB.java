import static utils.Constants.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;


/**
 * @author Karan
 *
 */
public class editDB implements ActionListener {

	private JFrame frame;
	private JTable table;
	private JPanel north, south;
	private JButton deleteSelected, save, addRow;
	private DefaultTableModel model;

	/**
	 * Work on the GUI. Displays the table and buttons.
	 * Also initializes the state of the program.
	 * @param databaseName
	 * @param quesToAnsMap
	 */
	public editDB(String databaseName, Map<String, String> quesToAnsMap) {
		String[] columnNames = {"Question", "Answer", "Delete"};
		Object[][] data = getData(quesToAnsMap);
		initializeFrame(databaseName);
		initializeNorth(data, columnNames);
		initializeSouth();

		frame.add(north, BorderLayout.NORTH);
		frame.add(south, BorderLayout.SOUTH);

		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addRow) {
			model.insertRow(table.getRowCount(), new Object[]{"", "", false});
		}
	}

	/**
	 * Converts a Map to a 2D array and returns it.
	 * @param quesToAnsMap
	 * @return
	 */
	private Object[][] getData(Map<String, String> quesToAnsMap) {
		Object[][] data = new Object[quesToAnsMap.keySet().size()][3];
		int i = 0;
		for (String question : quesToAnsMap.keySet()) {
			data[i][0] = question;
			data[i][1] = quesToAnsMap.get(question);
			data[i][2] = new Boolean(false);
			i++;
		}
		return data;
	}
	
	//********************** BUILD GUI **********************//
	/**
	 * Initializes the state of frame.
	 */
	private void initializeFrame(String databaseName) {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(new Dimension(550, 500));
		frame.setLocation(new Point(450, 300));
		frame.setTitle("Edit Database" + databaseName);
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());
	}
	
	/**
	 * Initializes the state of north portion of frame.
	 */
	private void initializeNorth(Object[][] data, String[] columnNames) {
		north = new JPanel(new GridLayout(1, 1));

		model = new DefaultTableModel(data, columnNames);
		table = new JTable(model) {			
			@Override
			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				case 1:
					return String.class;
				default:
					return Boolean.class;
				}
			}
		};
		table.setFillsViewportHeight(true);
		table.setRowHeight(25);
		table.setRowMargin(5);
		table.getTableHeader().setReorderingAllowed(false);
		JScrollPane scrollPane = new JScrollPane(table);
		north.add(scrollPane);
	}

	/**
	 * Initializes the state of south portion of frame.
	 */
	private void initializeSouth() {
		south = new JPanel(new GridLayout(1, 3));
		deleteSelected = new JButton("Deleted Selected Rows");
		deleteSelected.addActionListener(this);
		addRow = new JButton("Add Row");
		addRow.addActionListener(this);
		save = new JButton("Save Database");
		save.addActionListener(this);
		south.add(deleteSelected);
		south.add(addRow);
		south.add(save);
	}
	//********************** BUILD GUI **********************//

}
