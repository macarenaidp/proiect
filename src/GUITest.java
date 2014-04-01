import java.util.ArrayList;
import javax.swing.DefaultListModel;

import junit.framework.TestCase;
 
public class GUITest extends TestCase {

    public void testAddNewUserCount() {
    	Main m = new Main("TestUser");
    	Mediator med = m.med;

    	String[] files = {"file1"};
    	med.addUser(files, "UserName");

    	DefaultListModel lm = (DefaultListModel)m.user_list.getModel();
    	int size = lm.getSize();

    	assertEquals(size, 1);
    }

    public void testAddNewUserName() {
    	Main m = new Main("TestUser");
    	Mediator med = m.med;

    	String[] files = {"file1"};
    	med.addUser(files, "UserName");

    	DefaultListModel lm = (DefaultListModel)m.user_list.getModel();
    	String u = (String)lm.getElementAt(0);

    	assertEquals(u, "UserName");
    }

}