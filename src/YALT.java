import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import static utils.Constants.*;

/**
 * @author Karan Goel
 *
 */
public class YALT implements ActionListener {

	private String fileName; // File name selected by user
	private String answerToQues; // Holds the answer to the random question shown
	private Map<String, String> quesToAnsMap; // Map of questions to answers in a file
	private ArrayList<String> questionList; // List of all questions in a file

	public static void main(String[] args) {
		new YALT(); // Create and run the GUI
	}

	private JFrame frame;
	private JPanel north;
	private JPanel center;
	private JPanel south;
	private JButton nextQuestion;
	private JButton showAnswer;
	private JTextArea question;
	private JTextArea answer;
	private JComboBox fileSelector;
	private JButton go;
	private JLabel numberOfQuestions;

	private JMenuBar menubar;
	private JMenu menu, databaseMenu;
	private JMenuItem exit, edit, delete;

	/**
	 * Work on the GUI. Displays all buttons, labels etc etc.
	 * Also sets up the fileSelector with list of files in the
	 * database folder.
	 */
	public YALT() {
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	// Create and show GUI
            	initializeFrame();		
        		setFrameMenu();
        		initializeNorth();
        		initializeCenter();		
        		initializeSouth();
        		addEverything();
        		frame.setVisible(true);
            }
        });
	}


	/**
	 * The meat of the program, catches all actions
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO: Add new database
		try {
			if(e.getSource() == fileSelector) { 
				// If file is selected
				fileName = DATABASE + (String) fileSelector.getSelectedItem();
				go.setEnabled(true); // File name is changed, set "Go" to enabled.
				databaseMenu.setEnabled(false); // File changed, hence disable manage database menu
			} else if(e.getSource() == go) { 
				// When the "Go" button is pressed
				goActions();
			} else if (e.getSource() == nextQuestion) { 
				// Show next random question
				answer.setText("Answer: ");
				answerToQues = showRandomQuestion(questionList);
			} else if(e.getSource() == showAnswer) { 
				// User requests the answer
				answer.setText("Answer: " + answerToQues);
			} else if(e.getSource() == exit) {
				// Exit the program
				System.exit(1);
			} else if(e.getSource() == edit) {
				new EditDB((String) fileSelector.getSelectedItem(), quesToAnsMap);
			} else if(e.getSource() == delete) {
				// Popup confirm, delete
				int n = JOptionPane.showConfirmDialog(frame,
						"Are you sure you want to delete the database " 
						+ (String) fileSelector.getSelectedItem() + "?",
						"Confirm Deletion", JOptionPane.YES_NO_OPTION, 
						JOptionPane.ERROR_MESSAGE);
				checkAndDelete(n, fileName);
			}
		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(null, "File not found. Make sure the " +
					"file is in the same folder as this program and has \"" + 
					EXTENSION + "\" extension.");
		}
	}

	/**
	 * Do stuff when "Go" is clicked
	 * @throws FileNotFoundException
	 */
	private void goActions() throws FileNotFoundException {
		go.setEnabled(false); // Disable "Go" until filename is changed.
		question.setText("Question: ");
		answer.setText("Answer: ");
		Scanner scan = new Scanner(new File(fileName));
		int count = 0; // Store the number of questions
		try {
			// Map all questions and answers
			quesToAnsMap = new HashMap<String, String>(); // Re-initialize map
			while(scan.hasNextLine()) {
				String line = scan.nextLine();
				if(line.contains(":::::")) {
					String[] qna = line.split(":::::");
					quesToAnsMap.put(qna[0], qna[1]);
					count++;
				} else {
					// file is in wrong format
					JOptionPane.showMessageDialog(null, "Invalid file content on line " 
							+ count + 1 + ". Separate questions with " +
							"answers using \":::::\" (five colons).");
					return;
				}
			}
		} finally {
			// Make sure scanner is closed.
			scan.close();
		}
		// Display total number of questions
		numberOfQuestions.setText(count + " questions");
		// Display a random question, update the answerToQues to it's answer
		questionList = new ArrayList<String>();
		questionList = mapToArrayList(quesToAnsMap);
		answerToQues = showRandomQuestion(questionList);
		databaseMenu.setEnabled(true);
	}

	/**
	 * Returns an array of .EXTENSION files in the DB folder
	 */
	private String[] getFileNames() {
		File dir = new File(DATABASE);
		String[] files = dir.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith("." + EXTENSION);
			}
		});
		return files;
	}

	/**
	 * Converts the given Map to an ArrayList storing only the questions. 
	 */
	public ArrayList<String> mapToArrayList(Map<String, String> map) {
		ArrayList<String> listOfKeys = new ArrayList<String>();
		Set<String> keySet = map.keySet();
		// Add all keys into the listOfKeys
		for(String q : keySet) {
			listOfKeys.add(q);
		}
		return listOfKeys;
	}

	/**
	 * Prints a random question, and returns it's answer
	 * @param list
	 * @return answer to question shown
	 */
	public String showRandomQuestion(ArrayList<String> list) {
		Random rand = new Random();
		String randomQ = list.get(rand.nextInt(list.size()));
		question.setText(randomQ);
		return quesToAnsMap.get(randomQ);
	}

	/**
	 * Deletes the passed file if n is yes.
	 * @param n
	 * @throws FileNotFoundException 
	 */
	private void checkAndDelete(int n, String fileName) {
		if (n == JOptionPane.YES_OPTION) {
			File file = new File(fileName);
			if(file.delete()) {
				JOptionPane.showMessageDialog(null, "Deletion successful!");
				// After deletion, refresh the state of the program
				// TODO: Better alternative??
				frame.dispose();
				new YALT();
			} else {
				JOptionPane.showMessageDialog(null, "Deletion failed!");
			}
		}
	}


	//********************** BUILD GUI **********************//
	/**
	 * Initializes the state of frame.
	 */
	private void initializeFrame() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(550, 500));
		frame.setLocation(new Point(400, 300));
		frame.setTitle("Yet Another Learning Tool");
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());
	}

	/**
	 * Sets the properties to passed TextArea to make it loop like a label
	 * with word wrapping
	 */
	private JTextArea textAreaProperties(JTextArea textArea) {
		textArea.setEditable(false);
		textArea.setCursor(null);  
		textArea.setOpaque(false);
		textArea.setMargin(new Insets(30, 15, 20, 15));
		textArea.setFocusable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		return textArea;
	}

	/**
	 * Initializes the state of north portion of frame.
	 */
	private void initializeNorth() {
		north = new JPanel(new GridLayout(1, 4));
		String[] files = getFileNames(); // Get all file names in DATABASE folder
		fileSelector = new JComboBox(files);
		fileName = DATABASE + files[0]; // Set the filename to be the first file in array
		fileSelector.addActionListener(this);
		go = new JButton("Go");
		numberOfQuestions = new JLabel();
		numberOfQuestions.setHorizontalAlignment(JLabel.CENTER);
		north.add(fileSelector);
		north.add(go);
		go.addActionListener(this);
		north.add(numberOfQuestions);	
	}

	/**
	 * Initializes the state of center portion of frame.
	 */
	private void initializeCenter() {
		center = new JPanel(new GridLayout(2, 1));
		question = new JTextArea("Question");
		question = textAreaProperties(question);
		answer = new JTextArea("Answer");
		answer = textAreaProperties(answer);
		center.add(question);
		center.add(answer);
		question.setFont(new Font("Serif", Font.BOLD, 15));
		answer.setFont(new Font("Serif", Font.BOLD, 15));
	}

	/**
	 * Initializes the state of south portion of frame.
	 */
	private void initializeSouth() {
		south = new JPanel(new GridLayout(2, 2));
		showAnswer = new JButton("Show Answer");
		showAnswer.addActionListener(this);
		nextQuestion = new JButton("Next Question");
		south.add(showAnswer);
		south.add(nextQuestion);
		nextQuestion.addActionListener(this);
		south.add(new JLabel("Created by Karan Goel."));
		south.add(new JLabel("http://www.goel.im | @TheKaranGoel"));

		nextQuestion.setIcon(new ImageIcon("arrow.png"));
		showAnswer.setIcon(new ImageIcon("check.png"));
	}

	/**
	 * Initializes the menu bar of frame.
	 */
	private void setFrameMenu() {
		// Initialize the menubar
		menubar = new JMenuBar();

		// Initialize a menu
		menu = new JMenu("File");
		menubar.add(menu);
		exit = new JMenuItem("Exit");
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK)); // Alt + F4
		exit.addActionListener(this);
		menu.add(exit);

		databaseMenu = new JMenu("Manage Database");
		databaseMenu.setToolTipText("Select a database from list below, and press \"Go\"");
		databaseMenu.setEnabled(false); // hidden until "Go" is clicked
		menubar.add(databaseMenu);

		edit = new JMenuItem("Edit");
		edit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK)); // Alt + E
		edit.addActionListener(this);
		databaseMenu.add(edit);

		delete = new JMenuItem("Delete");
		delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK)); // Alt + D
		delete.addActionListener(this);	
		databaseMenu.add(delete);

		frame.setJMenuBar(menubar);
	}

	/**
	 * Combine all panels into the frame.
	 */
	private void addEverything() {
		frame.add(north, BorderLayout.NORTH);
		frame.add(center, BorderLayout.CENTER);
		frame.add(south, BorderLayout.SOUTH);
	}
	//********************** BUILD GUI **********************//

}