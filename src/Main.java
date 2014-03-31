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
import javax.swing.table.TableColumn;

public class Main extends JPanel {

	private DefaultListModel	model1;										// list model
	private DefaultListModel	model2;										// list model
	private JList				list, user;								// lists

	private Mediator med = new Mediator();

	public Main() {
		init();
	}

	public void init() {
		// initialize model fisiere
		model1 = new DefaultListModel();

		// initialize model utilizatori
		model2 = new DefaultListModel();

		// initialize lists, based on the specific model
		list = new JList(model1);
		list.setName("list");

		//user = new JList(new ReverseListModel(model));
		user = new JList(model2);
		user.setName("user");
		

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
		JScrollPane jsp = new JScrollPane(list);
		jsp.setName("scroll");
		middle.add(jsp);

		JScrollPane jsp2 = new JScrollPane(user);
		//jsp2.setPreferredSize(new Dimension(450,110));
		jsp2.setName("scroll2");
		right.add(jsp2);

        JTable table = new JTable();
        table.setName("progress_table");
        table.setModel(new MyModel());//invoking our custom model
        table.setDefaultRenderer(JLabel.class,  new Renderer());// for the rendering of cell
        JScrollPane jp = new JScrollPane(table);
        
        middle.add(jp);

        JLabel statusBar = new JLabel("Status message...");
        statusBar.setHorizontalAlignment(SwingConstants.LEFT);
        bottom.add(statusBar);

		// mediator init
		med.registerList(list);
		med.registeruser(user);
		med.registerTable(table);

		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				JList list = (JList) e.getSource();
				if (!(list.isSelectionEmpty()) && e.getValueIsAdjusting()) {
					String fileName = list.getSelectedValue().toString();
					med.downloadFile(fileName);
				}
			}
		});

		user.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				JList list = (JList) e.getSource();
				if (!(list.isSelectionEmpty())  && e.getValueIsAdjusting()) {
					String userName = list.getSelectedValue().toString();
					med.listFiles(userName);
				}
			}
		});
	}

	public static void buildGUI() {
		JFrame frame = new JFrame("User1"); // title
		frame.setContentPane(new Main()); // content
		frame.setSize(500, 500); // width / height
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit application when window is closed
		frame.setVisible(true); // show it!
	}

	public void addUser(String user) {
		model2.addElement(user);
	}

	public static void main(String[] args) {
		// run on EDT (event dispatch thread), not on main thread!
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				buildGUI();
			}
		});
	}

}


