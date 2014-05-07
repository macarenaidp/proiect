@SuppressWarnings("serial")
class MyModel extends javax.swing.table.DefaultTableModel{

    Object[] col = {"Source", "Destination", "File Name", "Progress", "Status"};

    public MyModel () {

    	//Adding columns
    	for(Object c: col)
    		this.addColumn(c);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
    	return false;
    }

}