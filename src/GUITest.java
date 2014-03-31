import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import junit.framework.TestCase;
 
public class GUITest extends TestCase {
    private List<Integer> emptyList;
    private List<Integer> filledList;
 
    @Override
    protected void setUp() {
    	emptyList = new ArrayList<Integer>();
 
    	filledList = new ArrayList<Integer>();
    	filledList.add(1);
    	filledList.add(2);
    	filledList.add(3);
    }

    public void testNewUserSize() {
    	Main m = new Main();
    	Mediator med = m.med;

    	String[] files = {"file1"};
    	med.addUser(files, "UserName");

    	DefaultListModel lm = (DefaultListModel)m.user.getModel();
    	int size = lm.getSize();

    	assertEquals(size, 1);
    }

    public void testNewUserName() {
    	Main m = new Main();
    	Mediator med = m.med;

    	String[] files = {"file1"};
    	med.addUser(files, "UserName");

    	DefaultListModel lm = (DefaultListModel)m.user.getModel();
    	String u = (String)lm.getElementAt(0);

    	assertEquals(u, "UserName");
    }

}