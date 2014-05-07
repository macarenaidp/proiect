import java.awt.Component;
 
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class CustomProgressBar extends JProgressBar implements TableCellRenderer {

	/**
	 * Creates a JProgressBar with the range 0,100.
	 * @return 
	 */
	public CustomProgressBar(){
		super(0, 100);
		setStringPainted(true);
	}

	public Component getTableCellRendererComponent(
										JTable table,
										Object value,
										boolean isSelected,
										boolean hasFocus,
										int row,
										int column) {
 
		//value is a percentage e.g. 95%
		int p = (Integer) value;
		setValue(p);
		return this;
	}
}