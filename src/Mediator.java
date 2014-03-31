import java.util.List;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumn;

public class Mediator {
	
	private JList				list, user;	// lists
	private JTable table;
	private List<User> users;
	
	// ---------- registration methods ----------
	
	public void registerList(JList list) {
		this.list = list;
	}
	
	public void registeruser(JList user) {
		this.user = user;
	}
	
	public void registerTable(JTable t) {
		this.table = t;
	}
	
	public void addUser(String[] files, String name) {
		User new_user = new User(files, name);
		((DefaultListModel)this.user.getModel()).addElement(name);
		this.users.add(new_user);
	}

	public void listFiles(String userName) {
		
		DefaultListModel model = (DefaultListModel)this.list.getModel();
		model.removeAllElements();

		User u = null;
		for (User uu: this.users)
			if(uu.getName() == userName)
				u = uu;
		
		for (String f: u.getFiles())
			model.addElement(f);
		
	}
	
	// ---------- list methods ----------

	public void downloadFile(String fileName) {
		new ProgressWorker(fileName).execute();
	}
	
	
	
	// ---------- swing worker ----------
	
	private class ProgressWorker extends SwingWorker<Void, Integer> {
		
		private String text;
		private int index;
		
		public ProgressWorker(String text) {
			this.text = text;
			this.index = ((MyModel)table.getModel()).getRowCount();
		}

		@Override
		protected Void doInBackground() throws Exception {
			CustomProgressBar pb = new CustomProgressBar();
			String x = "0%";
			Object[][] row ={	{"Row 1 Col 1", "Row 1 Col 2", this.text, x, "Receiving..."}};

			for(Object[] r: row)
		    	((MyModel)table.getModel()).addRow(r);

			TableColumn myCol = table.getColumnModel().getColumn(3);
			myCol.setCellRenderer(pb);

			Random random = new Random();
			int progress = 0;

			while (progress < 100) {
            	//Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {}

                progress += 10;
                x = progress + "%";

                ((MyModel)table.getModel()).setValueAt(x, this.index, 3);
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
		}
		
	}
}
