import javax.swing.JLabel;
 
class MyModel extends javax.swing.table.DefaultTableModel{
 
    Object[][] row ={	{"Row 1 Col 1", "Row 1 Col 2", "Row 1 Col3", new CustomProgressBar(), "Row 1 Col5"},
                      	{"Row 2 Col 1", "Row 2 Col 2", "Row 2 Col3", new CustomProgressBar(), "Row 2 Col5"}
    				};
 
    Object[] col = {"Source", "Destination", "File Name", "Progress", "Status"};
 
    public MyModel (){
 
    //Adding columns
    for(Object c: col)
    	this.addColumn(c);
 
    //Adding rows
    for(Object[] r: row)
    	this.addRow(r);
 
    }
 
    @Override
 
    public Class getColumnClass(int columnIndex) {
        if(columnIndex == 0 || columnIndex == 3)return getValueAt(0, columnIndex).getClass();
 
        else return super.getColumnClass(columnIndex);
 
    }
 
}