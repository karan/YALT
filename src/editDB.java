import static utils.Constants.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;


/**
 * @author Karan
 *
 */
public class editDB implements ActionListener {

	private JFrame frame;
	private JTable table;

	public editDB(String databaseName, Map<String, String> quesToAnsMap) {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(new Dimension(550, 500));
		frame.setLocation(new Point(450, 300));
		frame.setTitle("Edit Database" + databaseName);
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());

		String[] columnNames = {"Question", "Answer"};
		String[][] data = new String[quesToAnsMap.keySet().size()][2];

		int i = 0;
		for (String question : quesToAnsMap.keySet()) {
			data[i][0] = question;
			data[i][1] = quesToAnsMap.get(question);
			i++;
		}

		table = new JTable(data, columnNames);
		table.setFillsViewportHeight(true);
		table.setRowHeight(25);
		table.setRowMargin(5);
		table.getTableHeader().setReorderingAllowed(false);
		
		JScrollPane scrollPane = new JScrollPane(table);

		frame.add(scrollPane);

		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Adding, removing rows
	}

}
