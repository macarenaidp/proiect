import java.util.List;

import javax.swing.*;

public class Mediator {
	
	private JList				list, user;	// lists
	private JTextField			tName;			// name field
	private JButton				bAdd;			// add button
	private JButton				bRemove;		// remove button
	
	private StateManager stateManager = new StateManager(this);
	
	// ---------- registration methods ----------
	
	public void registerList(JList list) {
		this.list = list;
	}
	
	public void registeruser(JList user) {
		this.user = user;
	}
	
	public void registerName(JTextField tName) {
		this.tName = tName;
	}
	
	public void registerAdd(JButton bAdd) {
		this.bAdd = bAdd;
	}
	
	public void registerRemove(JButton bRemove) {
		this.bRemove = bRemove;
	}
	
	// ---------- list methods ----------

	public void downloadFile(String fileName) {
		
	}
	
	public void addList() {
		final String text = tName.getText();
		if (text.isEmpty()) {
			JOptionPane.showMessageDialog(
					null, "Name is empty!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//bAdd.setEnabled(false);
		
		new ListWorker(text).execute();
	}
	
	public void removeList() {
		int index = list.getSelectedIndex();
		if (index == -1) {
			final String text = tName.getText();
			if (text.isEmpty()) {
			JOptionPane.showMessageDialog(
					null, "No item selected!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
			}
			else
			{
				((DefaultListModel)list.getModel()).removeElement(text);
			}
		}
		else
			((DefaultListModel)list.getModel()).remove(index);
	}
	
	// ---------- methods called on events ----------
	
	public void add() {
		stateManager.add();
	}
	
	public void remove() {
		stateManager.remove();
	}
	
	public void list() {
		stateManager.setListState();
	}
	
	public void message() {
		stateManager.setMessageState();
	}
	
	// ---------- swing worker ----------
	
	private class ListWorker extends SwingWorker<Void, Integer> {
		
		private String text;
		
		public ListWorker(String text) {
			this.text = text;
		}

		@Override
		protected Void doInBackground() throws Exception {
			int i = 3;
			while (i-- > 0)
				try {
					Thread.sleep(1000);
					publish(i);
				} catch (InterruptedException e) {}
			
			return null;
		}
		
		@Override
		protected void process(List<Integer> list) {
			for (int i : list)
				System.out.println("Processed: " + i);
		}
		
		@Override
		public void done() {
			((DefaultListModel)list.getModel()).addElement(text);
			//bAdd.setEnabled(true);
		}
		
	}
}
