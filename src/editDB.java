import static utils.Constants.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * @author Karan
 * @see http://stackoverflow.com/questions/15171253
 */
public class EditDB implements ActionListener {

	private String databaseName;
	private JFrame frame;
	private JTable table;
	private JPanel north, south;
	private JButton deleteSelected, save, addRow;
	Vector<Vector<Object>> data;
	Vector<String> columnNames;
	private DefaultTableModel model;

	/**
	 * Work on the GUI. Displays the table and buttons.
	 * Also initializes the state of the program.
	 * @param databaseName
	 * @param quesToAnsMap
	 */
	public EditDB(String databaseName, Map<String, String> quesToAnsMap) {
		this.databaseName = databaseName;
		createAndShowGUI(databaseName, quesToAnsMap);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == addRow) {
				JTextField question = new JTextField();
				JTextField answer = new JTextField();
				Object[] message = {"Question:", question, "Answer:", answer};
				int option = JOptionPane.showConfirmDialog(frame, message, "Add New", 
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.OK_OPTION && question.getText() != null
						&& question.getText().length() > 0) {
					Vector<Object> newQuestion = new Vector<Object>(3, 1);
					newQuestion.add(question.getText());
					newQuestion.add(answer.getText());
					newQuestion.add(false);
					model.addRow(newQuestion);
					table.scrollRectToVisible(table.getCellRect(model.getRowCount() + 1, 
							model.getColumnCount(), false)); // Auto scroll to last row
				}
			} else if (e.getSource() == save) {
				File file = new File(DATABASE + databaseName);
				if (file.exists()) {
					BufferedWriter writer = new BufferedWriter(new FileWriter(file));
					for (Vector<Object> singleRow : data) {
						writer.write(singleRow.get(0) + ":::::" + singleRow.get(1));
						writer.newLine();
						writer.flush();
					}
					writer.close();
					JOptionPane.showMessageDialog(null, "Database saved successfully!");
				} else {
					JOptionPane.showMessageDialog(null, "File not found. Make sure the " +
							"file is in the same folder as this program and has \"" + 
							EXTENSION + "\" extension.");
				}
			} else if (e.getSource() == deleteSelected) {
				// TODO: Delete all selected rows!
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	//********************** BUILD GUI **********************//
	/**
	 * Puts the GUI together, initializes data.
	 * @param databaseName
	 * @param quesToAnsMap
	 */
	private void createAndShowGUI(String databaseName, Map<String, String> quesToAnsMap) {
		initializeFrame(databaseName);
		columnNames = new Vector<String>(3, 0);
		columnNames.add("Question");
		columnNames.add("Answer");
		columnNames.add("Delete?");
		initializeNorth(quesToAnsMap, columnNames);
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
	private void initializeNorth(Map<String, String> quesToAnsMap, Vector<String> columnNames) {
		north = new JPanel(new GridLayout(1, 1));
		model = new DefaultTableModel(convertMapToVector(quesToAnsMap), columnNames) {
			private static final long serialVersionUID = 1L;
			@Override
	        public Class<?> getColumnClass(int col) {
	            return getValueAt(0, col).getClass();
	        }
		};
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

	
	//Converts a Map to a 2D Vector and returns it.
	public Vector<Vector<Object>> convertMapToVector(Map<String, String> quesToAnsMap) {
		data = new Vector<Vector<Object>>(quesToAnsMap.size(), 1);
		int i = 0;
		for (String question : quesToAnsMap.keySet()) {
			Vector<Object> singleQuestion = new Vector<Object>(3, 1);
			singleQuestion.add(question);
			singleQuestion.add(quesToAnsMap.get(question));
			singleQuestion.add(false);
			data.add(singleQuestion);
			i++;
		}
		return data;
	}

}
