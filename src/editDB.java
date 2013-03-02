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
import javax.swing.table.TableModel;


/**
 * @author Karan
 *
 */
public class EditDB implements ActionListener {

	private JFrame frame;
	private JTable table;
	private JPanel north, south;
	private JButton deleteSelected, save, addRow;
	private EditTableModel model;

	/**
	 * Work on the GUI. Displays the table and buttons.
	 * Also initializes the state of the program.
	 * @param databaseName
	 * @param quesToAnsMap
	 */
	public EditDB(String databaseName, Map<String, String> quesToAnsMap) {
		createAndShowGUI(databaseName, quesToAnsMap);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == addRow) {
				
			} else if (e.getSource() == save) {
				
			} else if (e.getSource() == deleteSelected) {
				
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	//********************** BUILD GUI **********************//
	/**
	 * Puts the GUI together, initializes data.
	 * @param databaseName
	 * @param quesToAnsMap
	 */
	protected void createAndShowGUI(String databaseName, Map<String, String> quesToAnsMap) {
		initializeFrame(databaseName);
		initializeNorth(quesToAnsMap, COLUMN_NAMES);
		initializeSouth();
		frame.add(north, BorderLayout.NORTH);
		frame.add(south, BorderLayout.SOUTH);
		frame.setVisible(true);
	}
	
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
	private void initializeNorth(Map<String, String> quesToAnsMap, String[] columnNames) {
		north = new JPanel(new GridLayout(1, 1));

		model = new EditTableModel(quesToAnsMap);
		table = new JTable(model);
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
