import java.awt.Frame;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;

public class Mediator {

	@SuppressWarnings("rawtypes")
	public JList fileFrame;
	@SuppressWarnings("rawtypes")
	public JList userFrame;
	public JTable progressTable;
	private JLabel statusBar;

	private List<User> peers;

	private String userName;
	private String homeDir;
	private String downloadDir;
	private String[] files;
	private String ip;
	private int port;

	private Server server;
	WebClient webClient;

	public Mediator(String userName, String homeDir, String downloadDir, String[] files, String ip, int port) {
		this.userName = userName;
		this.peers = new ArrayList<User>();
		this.homeDir = homeDir;
		this.downloadDir = downloadDir;
		this.files = files;
		this.ip = ip;
		this.port = port;

		try {
			this.webClient = new WebClient("http://localhost:8080/axis/WebServer.jws");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		this.server = new Server(this.homeDir, this.ip, this.port);
	}

	// ---------- web server connection methods ----------

	public void closeConnection() {
		Object[] params = new Object[] { this.userName };

		try {
			this.webClient.invoke("unregisterUser", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void initConnection() {
		String my_ip = this.server.IP;
		int my_port = this.server.PORT;

		Object[] params = new Object[] { this.userName, my_ip, my_port, this.files }; // operation parameters

		try {
			this.webClient.invoke("registerUser", params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.updateUsers();
	}


	@SuppressWarnings("rawtypes")
	public void updateUsers() {
		this.statusBar.setText("Updating peer list ...");

		((DefaultListModel)this.userFrame.getModel()).clear();
		((DefaultListModel)this.fileFrame.getModel()).clear();
		this.peers = new ArrayList<User>();
		Object[] params = new Object[] { this.userName };

		try {
			String active_users = (String)this.webClient.invoke("requestUsers", params);
			String[] users = active_users.split("-");
			String[] data;
			String[] files;

			for (String user : users) {
				if (user.isEmpty())
					continue;

				data = user.split(" ");
				files = data[3].split(";");
				this.addUser(files, data[0], data[1], Integer.parseInt(data[2]));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		this.statusBar.setText("Peer list up to date");
	}

	// ---------- getter methods ----------

	public Server getServer() {
		return this.server;
	}

	public String getUserName() {
		return this.userName;
	}

	// ---------- setter methods ----------

	public void setFileList(@SuppressWarnings("rawtypes") JList file_frame) {
		this.fileFrame = file_frame;
	}

	public void setUserList(@SuppressWarnings("rawtypes") JList user_frame) {
		this.userFrame = user_frame;
	}

	public void setStatusBar(JLabel status) {
		this.statusBar = status;
	}

	public void setTable(JTable table) {
		this.progressTable = table;
	}

	// ---------- user methods ----------

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addUser(String[] files, String name, String ip, int port) {
		User new_user = new User(files, name, ip, port);
		((DefaultListModel)this.userFrame.getModel()).addElement(name);
		this.peers.add(new_user);
	}


	public User getUser(String name) {
		User u = null;

		for (int i = 0; i < this.peers.size(); i ++) { 
			u = this.peers.get(i);
			if (u.getName().equals(name))
				return u;
		}

		return null;
	}

	// ---------- file methods ----------

	@SuppressWarnings("unchecked")
	public void listFiles(String userName) {

		@SuppressWarnings("rawtypes")
		DefaultListModel model = (DefaultListModel)this.fileFrame.getModel();
		model.removeAllElements();

		User selected = null;
		for (User u: this.peers)
			if(u.getName() == userName)
				selected = u;

		for (String file: selected.getFiles())
			model.addElement(file);
	}


	public void downloadFile(String fileName, String source) {
		this.statusBar.setText("Downloading " + fileName + "...");
		User source_user = this.getUser(source);

		if (source_user == null) {
			return;
		}

		new ProgressWorker(this.progressTable, this.statusBar, fileName, source, this.userName).execute();
		new Client(source_user, fileName, this.downloadDir);
	}


	// ---------- GUI methods ----------

	public void buildGUI() {
		JFrame frame = new JFrame(this.userName); // title
		frame.setContentPane(new Main(this)); // content
		frame.setSize(500, 500); // width / height
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit application when window is closed
		frame.setVisible(true); // show it!

		final Mediator self = this;

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt){
                self.closeConnection();
            }
        });

		Thread update_users = new Thread(new Runnable() {
			public void run() {
				while(true) {
					try {
						Thread.sleep(10000); // every 10 seconds
						self.updateUsers();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		update_users.start();
	}
}
