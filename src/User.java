
public class User {

	private String[] files;
	private String name;

	public User(String[] files, String name) {
		this.files = files;
		this.name = name;
	}

	public String[] getFiles() {
		return this.files;
	}

	public String getName() {
		return this.name;
	}
}
