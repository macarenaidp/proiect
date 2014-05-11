import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class Main extends JPanel {

	public Main(Mediator med) {
		init(med);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void init(final Mediator med) {
		// initialize models
		DefaultListModel fileFrameModel = new DefaultListModel();
		DefaultListModel userFrameModel = new DefaultListModel();

		// initialize lists, based on the specific model
		final JList fileFrame = new JList(fileFrameModel);
		final JList userFrame = new JList(userFrameModel);

		// main panel
		JPanel filePanel = new JPanel(new FlowLayout());
		JPanel progressPanel = new JPanel(new GridLayout(2,0));
		JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

		JPanel userPanel = new JPanel(new GridLayout(1,0));
		userPanel.setPreferredSize(new Dimension(100,getHeight()));

		this.setLayout(new BorderLayout(10,10));
		this.add(filePanel, BorderLayout.NORTH);
		this.add(progressPanel, BorderLayout.CENTER);
		this.add(userPanel,BorderLayout.LINE_END);
		this.add(statusPanel, BorderLayout.PAGE_END);

		// middle panel: the two lists (scrollable)
		JScrollPane jsp = new JScrollPane(fileFrame);
		progressPanel.add(jsp);

		JScrollPane jsp2 = new JScrollPane(userFrame);
		userPanel.add(jsp2);

		JTable table = new JTable();
		table.setModel(new MyModel());//invoking our custom model
		table.setDefaultRenderer(JLabel.class,  new Renderer());// for the rendering of cell
		JScrollPane jp = new JScrollPane(table);

		progressPanel.add(jp);

		JLabel statusBar = new JLabel("");
		statusBar.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusBar);

		// mediator init
		med.setFileList(fileFrame);
		med.setUserList(userFrame);
		med.setTable(table);
		med.setStatusBar(statusBar);

		med.initConnection();

		// Selection listener for the file list
		fileFrame.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				JList list = (JList) e.getSource();

				if (!(list.isSelectionEmpty()) && e.getValueIsAdjusting()) {
					String fileName = list.getSelectedValue().toString();
					String sourceUser = userFrame.getSelectedValue().toString();
					med.downloadFile(fileName, sourceUser);
				}
			}
		});

		// Selection listener for the user list
		userFrame.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				JList list = (JList) e.getSource();
				if (!(list.isSelectionEmpty())  && e.getValueIsAdjusting()) {
					String userName = list.getSelectedValue().toString();
					med.listFiles(userName);
				}
			}
		});
	}


	public static void main(String[] args) {
		final String userName = args[0];
		final String ip = args[1];
		final int port = Integer.parseInt(args[2]);
		final String homedir = args[3];
		final String destdir = args[4];
		final String[] files = args[5].split(" ");

		/*final String userName = "user2";
		final String ip = "127.0.0.1";
		final int port = 40002;
		final String homedir = "/home/camelia/Projects/Poli/4/IDP/test/user2/home/";
		final String destdir = "/home/camelia/Projects/Poli/4/IDP/test/user2/download/";
		final String[] files = { "ceva.txt", "altceva.txt" };*/

		final Mediator med = new Mediator(userName, homedir, destdir, files, ip, port);

		// run on EDT (event dispatch thread), not on main thread!
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				med.buildGUI();
			}
		});

		med.getServer().run();
	}
}