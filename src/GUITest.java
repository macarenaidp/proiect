import javax.swing.DefaultListModel;

import junit.framework.TestCase;
 
public class GUITest extends TestCase {

    public void testAddNewUserCount() {
    	final String userName = "user1";
		final String ip = "127.0.0.1";
		final int port = 40001;
		final String homedir = "/home";
		final String destdir = "/download";
		final String[] files = { "file.txt" };

		Mediator med = new Mediator(userName, homedir, destdir, files, ip, port);
    	new Main(med);

    	String[] user_files = { "user_file.txt" };
    	med.addUser(user_files, "user2", "127.0.0.1", 40002);

    	DefaultListModel lm = (DefaultListModel)med.userFrame.getModel();
    	int size = lm.getSize();

    	assertEquals(size, 1);
    }

    public void testAddNewUserName() {

    	final String userName = "user1";
		final String ip = "127.0.0.1";
		final int port = 40001;
		final String homedir = "/home";
		final String destdir = "/download";
		final String[] files = { "file.txt" };

		Mediator med = new Mediator(userName, homedir, destdir, files, ip, port);
    	new Main(med);

    	String[] user_files = { "user_file.txt" };
    	med.addUser(user_files, "user2", "127.0.0.1", 40002);

    	DefaultListModel lm = (DefaultListModel)med.userFrame.getModel();
    	String user_name = (String)lm.getElementAt(0);

    	assertEquals(user_name, "user2");
    }

}