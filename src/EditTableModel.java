import java.util.Map;

import javax.swing.table.AbstractTableModel;

import static utils.Constants.*;

public class EditTableModel extends AbstractTableModel {
	
	private Object[][] data;
	
	// Set the data array based on passed map
	public EditTableModel(Map<String, String> quesToAnsMap) {
		this.data = getData(quesToAnsMap);
		fireTableDataChanged();
	}
	
	// Return the number of columns
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	// Return number of rows in model
	public int getRowCount() {
		return data.length;
	}
	
	// Return name of column col
	public String getColumnName(int col) {
		return COLUMN_NAMES[col];
	}
	
	// Return object at specified row and col
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	// Return the type of data in col
	public Class getColumnClass(int col) {
		return getValueAt(0, col).getClass();
	}

	// Returns true if col is editable
	public boolean isCellEditable(int row, int col) {
		if (col < 3) {
			return true;
		} else {
			return false;
		}
	}

	// Sets value at cell(row, col) to be new value
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
	
	//Converts a Map to a 2D array and returns it.
	public Object[][] getData(Map<String, String> quesToAnsMap) {
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
}
