
public class User {

	private String[] files;
	private String name;
	private String homedir;
	private String ip;
	private int port;

	public User(String[] files, String name, String ip, int port) {
		this.files = files;
		this.name = name;
		this.ip = ip;
		this.port = port;
	}

	public String[] getFiles() {
		return this.files;
	}

	public String getName() {
		return this.name;
	}

	public String getHomedir() {
		return this.homedir;
	}

	public String getIP() {
		return this.ip;
	}

	public int getPort() {
		return this.port;
	}

}

