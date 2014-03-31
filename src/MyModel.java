import javax.swing.JLabel;
 
class MyModel extends javax.swing.table.DefaultTableModel{
 
    /*Object[][] row ={	{"Row 1 Col 1", "Row 1 Col 2", "Row 1 Col3", "5%", "Row 1 Col5"},
                      	{"Row 2 Col 1", "Row 2 Col 2", "Row 2 Col3", "10%", "Row 2 Col5"}
    				};*/
 
    Object[] col = {"Source", "Destination", "File Name", "Progress", "Status"};
 
    public MyModel (){
 
    //Adding columns
    for(Object c: col)
    	this.addColumn(c);
 
    //Adding rows
    /*for(Object[] r: row)
    	this.addRow(r);*/
 
    }
 
    @Override
    public boolean isCellEditable(int row, int column) {
    	return false;
    }
 
}