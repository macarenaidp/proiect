import javax.swing.DefaultListModel;

import junit.framework.TestCase;
 
public class GUITest extends TestCase {

    public void testAddNewUserCount() {
    	Main m = new Main();
    	Mediator med = m.med;

    	String[] files = {"file1"};
    	med.addUser(files, "UserName", "127.0.0.1", 7777);

    	DefaultListModel lm = (DefaultListModel)med.userFrame.getModel();
    	int size = lm.getSize();

    	assertEquals(size, 1);
    }

    public void testAddNewUserName() {
    	Main m = new Main();
    	Mediator med = m.med;

    	String[] files = {"file1"};
    	med.addUser(files, "UserName", "127.0.0.1", 7777);

    	DefaultListModel lm = (DefaultListModel)med.userFrame.getModel();
    	String u = (String)lm.getElementAt(0);

    	assertEquals(u, "UserName");
    }

}