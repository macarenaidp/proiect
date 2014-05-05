import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumn;

	// ---------- swing worker ----------

	public class ProgressWorker extends SwingWorker<Void, Integer> {

		private String fileName;
		private String source;
		private String destination;
		private int index;
		private JTable table;
		private JLabel statusBar;
		public static int value = 0;
		

		public ProgressWorker(JTable table, JLabel statusBar, String file, String source, String dest) {
			this.table = table;
			this.statusBar = statusBar;
			this.fileName = file;
			this.source = source;
			this.destination = dest;
			this.index = ((MyModel)table.getModel()).getRowCount();
		}
		
		public static void upgradeProgress(int v) {
			value = v;
		}

		@Override
		public Void doInBackground() throws Exception {
			CustomProgressBar pb = new CustomProgressBar();
			Random random = new Random();
			int progress = 0;

			Object[] row = {this.source, this.destination, this.fileName, progress, "Receiving..."};
			((MyModel)table.getModel()).addRow(row);

			TableColumn myCol = table.getColumnModel().getColumn(3);
			myCol.setCellRenderer(pb);

			while (progress < 100) {

				try {
					Thread.sleep(random.nextInt(500));
				} catch (InterruptedException ignore) {}

				progress += value;
				//System.out.println("Am primit progress"+progress);

				statusBar.setText("Downloading " + this.fileName + "...");
				((MyModel)table.getModel()).setValueAt(progress, this.index, 3);
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