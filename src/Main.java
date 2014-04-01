import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Main extends JPanel {

	private DefaultListModel	file_model;		// list model
	private DefaultListModel	user_model;		// list model
	public JList				file_list, user_list;	// lists
	private JLabel statusBar;
	private String userName;
	
	public Mediator med = new Mediator();

	public Main(String userName) {
		this.userName = userName;
		init();
	}

	public void init() {
		// initialize models
		file_model = new DefaultListModel();
		user_model = new DefaultListModel();

		// initialize lists, based on the specific model
		file_list = new JList(file_model);
		file_list.setName("list");

		user_list = new JList(user_model);
		user_list.setName("user");

		// main panel
		JPanel top = new JPanel(new FlowLayout());

		JPanel middle = new JPanel(new GridLayout(2,0));
		middle.setName("middle");

		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEADING));
		bottom.setName("bottom");

		JPanel right = new JPanel(new GridLayout(1,0));
		right.setPreferredSize(new Dimension(100,getHeight()));
		right.setName("right");

		this.setLayout(new BorderLayout(10,10));
		this.add(top, BorderLayout.NORTH);
		this.add(middle, BorderLayout.CENTER);
		this.add(right,BorderLayout.LINE_END);
		this.add(bottom, BorderLayout.PAGE_END);

		// middle panel: the two lists (scrollable)
		JScrollPane jsp = new JScrollPane(file_list);
		jsp.setName("scroll");
		middle.add(jsp);

		JScrollPane jsp2 = new JScrollPane(user_list);
		jsp2.setName("scroll2");
		right.add(jsp2);

        JTable table = new JTable();
        table.setName("progress_table");
        table.setModel(new MyModel());//invoking our custom model
        table.setDefaultRenderer(JLabel.class,  new Renderer());// for the rendering of cell
        JScrollPane jp = new JScrollPane(table);

        middle.add(jp);

        this.statusBar = new JLabel("");
        statusBar.setHorizontalAlignment(SwingConstants.LEFT);
        bottom.add(statusBar);

		// mediator init
		med.registerList(file_list);
		med.registerUser(user_list);
		med.registerTable(table);
		med.registerStatusBar(statusBar);

		// Fake user for testing
		/*
		String[] files = {"file1", "file2"};
		med.addUser(files, "name");
		*/

		// Selection listener for the file list
		file_list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				JList list = (JList) e.getSource();

				if (!(list.isSelectionEmpty()) && e.getValueIsAdjusting()) {
					String fileName = list.getSelectedValue().toString();
					String sourceUser = user_list.getSelectedValue().toString();
					med.downloadFile(fileName, sourceUser, userName);
				}
			}
		});

		// Selection listener for the user list
		user_list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				JList list = (JList) e.getSource();
				if (!(list.isSelectionEmpty())  && e.getValueIsAdjusting()) {
					String userName = list.getSelectedValue().toString();
					med.listFiles(userName);
				}
			}
		});
	}

	public static void buildGUI(String userName) {
		JFrame frame = new JFrame(userName); // title
		frame.setContentPane(new Main(userName)); // content
		frame.setSize(500, 500); // width / height
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit application when window is closed
		frame.setVisible(true); // show it!
	}

	public static void main(String[] args) {
		// run on EDT (event dispatch thread), not on main thread!
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				buildGUI("User1");
			}
		});
	}

}


