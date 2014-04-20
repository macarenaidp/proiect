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

		Client client = new Client(source_user, fileName, "/home/camelia/Desktop/idp/");
		new ProgressWorker(fileName, source, dest).execute();
	}



	// ---------- swing worker ----------

	private class ProgressWorker extends SwingWorker<Void, Integer> {

		private String fileName;
		private String source;
		private String destination;
		private int index;

		public ProgressWorker(String file, String source, String dest) {
			this.fileName = file;
			this.source = source;
			this.destination = dest;
			this.index = ((MyModel)table.getModel()).getRowCount();
		}

		@Override
		protected Void doInBackground() throws Exception {
			CustomProgressBar pb = new CustomProgressBar();
			Random random = new Random();
			int progress = 0;
			String progress_str = "0%";

			Object[] row = {this.source, this.destination, this.fileName, progress_str, "Receiving..."};
			((MyModel)table.getModel()).addRow(row);

			TableColumn myCol = table.getColumnModel().getColumn(3);
			myCol.setCellRenderer(pb);

			while (progress < 100) {

				try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {}

                progress += 10;
                progress_str = progress + "%";

                statusBar.setText("Downloading " + this.fileName + "...");
                ((MyModel)table.getModel()).setValueAt(progress_str, this.index, 3);
            }

            return null;
		}

		@Override
		protected void process(List<Integer> list) {
			for (int i : list)
				System.out.println("Processed: " + i);
		}

		@Override
		public void done() {
			((MyModel)table.getModel()).setValueAt("Completed", this.index, 4);
			statusBar.setText("Finished downloading " + this.fileName);
		}

	}
}
