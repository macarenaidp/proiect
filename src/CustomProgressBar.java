import java.awt.Component;
 
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
 
public class CustomProgressBar extends JProgressBar implements TableCellRenderer {
 
  /**
   * Creates a JProgressBar with the range 0,100.
 * @return 
   */
  public void ProgressCellRenderer(){
    setValue(0);
    setString("5%");
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
    final String sValue = value.toString();
    int index = sValue.indexOf('%');
    if (index != -1) {
      int p = 0;
      try{
        p = Integer.parseInt(sValue.substring(0, index));
      }
      catch(NumberFormatException e){
      }
      setValue(p);
      setString(sValue);
    }
    return this;
  }
}