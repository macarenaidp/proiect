import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumn;

public class Mediator {

	public JList file_list, user_list;
	public JTable table;
	private List<User> users;
	private JLabel statusBar;

	public Mediator() {
		this.users = new ArrayList<User>();
	}

	// ---------- registration methods ----------

	public void registerList(JList file_list) {
		this.file_list = file_list;
	}

	public void registerUser(JList user_list) {
		this.user_list = user_list;
	}

	public void registerStatusBar(JLabel status) {
		this.statusBar = status;
	}

	public void registerTable(JTable t) {
		this.table = t;
	}


	public void addUser(String[] files, String name, String ip, int port) {
		User new_user = new User(files, name, ip, port);
		((DefaultListModel)this.user_list.getModel()).addElement(name);
		this.users.add(new_user);
	}

	public User getUser(String name) {
		User u = null;

		for (int i = 0; i < this.users.size(); i ++) { 
			u = this.users.get(i);
			if (u.getName() == name)
				return u;
		}

		return null;
	}

	public void listFiles(String userName) {

		DefaultListModel model = (DefaultListModel)this.file_list.getModel();
		model.removeAllElements();

		User selected = null;
		for (User u: this.users)
			if(u.getName() == userName)
				selected = u;

		for (String file: selected.getFiles())
			model.addElement(file);
	}

	public void downloadFile(String fileName, String source, String dest) {
		this.statusBar.setText("Downloading " + fileName + "...");
		User source_user = this.getUser(source);

		if (source_user == null) {
			return;
		}
		new ProgressWorker(this.table, this.statusBar, fileName, source, dest).execute();
		Client client = new Client(source_user, fileName, "C:\\idp\\Client\\");
		
		
	}
}
