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
    	med.closeConnection();
    }


    public void testgetUser() {

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
    	User source_user = med.getUser("user2");

    	assertEquals(source_user.getName(), "user2");
    }


    public void testInitConnection() {
    	final String userName1 = "user1";
		final String ip1 = "127.0.0.1";
		final int port1 = 40001;
		final String homedir1 = "/home1";
		final String destdir1 = "/download1";
		final String[] files1 = { "file1.txt" };

		Mediator med1 = new Mediator(userName1, homedir1, destdir1, files1, ip1, port1);
    	new Main(med1);
    	med1.initConnection();

    	DefaultListModel lm = (DefaultListModel)med1.userFrame.getModel();
    	int size = lm.getSize();

    	assertEquals(size, 0);
    	med1.closeConnection();
    }


    public void testCloseConnection() {
    	final String userName1 = "user1";
		final String ip1 = "127.0.0.1";
		final int port1 = 40001;
		final String homedir1 = "/home1";
		final String destdir1 = "/download1";
		final String[] files1 = { "file1.txt" };

		final String userName2 = "user2";
		final String ip2 = "127.0.0.1";
		final int port2 = 40002;
		final String homedir2 = "/home2";
		final String destdir2 = "/download2";
		final String[] files2 = { "file2.txt" };

		Mediator med1 = new Mediator(userName1, homedir1, destdir1, files1, ip1, port1);
    	new Main(med1);
    	med1.initConnection();

    	Mediator med2 = new Mediator(userName2, homedir2, destdir2, files2, ip2, port2);
    	new Main(med2);
    	med2.initConnection();
    	med2.closeConnection();

    	med1.updateUsers();

    	DefaultListModel lm = (DefaultListModel)med1.userFrame.getModel();
    	int size = lm.getSize();

    	assertEquals(size, 0);
    	med2.closeConnection();
    	med1.closeConnection();
    }


    public void testUpdateUsers() {
    	final String userName1 = "user1";
		final String ip1 = "127.0.0.1";
		final int port1 = 40001;
		final String homedir1 = "/home1";
		final String destdir1 = "/download1";
		final String[] files1 = { "file1.txt" };

		final String userName2 = "user2";
		final String ip2 = "127.0.0.1";
		final int port2 = 40002;
		final String homedir2 = "/home2";
		final String destdir2 = "/download2";
		final String[] files2 = { "file2.txt" };

		Mediator med1 = new Mediator(userName1, homedir1, destdir1, files1, ip1, port1);
    	new Main(med1);
    	med1.initConnection();

    	Mediator med2 = new Mediator(userName2, homedir2, destdir2, files2, ip2, port2);
    	new Main(med2);
    	med2.initConnection();

    	med1.updateUsers();

    	DefaultListModel lm = (DefaultListModel)med1.userFrame.getModel();
    	int size = lm.getSize();
    	assertEquals(size, 1);

    	med2.closeConnection();
    	med1.closeConnection();
    }

}