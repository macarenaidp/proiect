import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WebServer {

	private static HashMap<String, String> registered_users = new HashMap<String, String>();

	public void unregisterUser(String name) {
		registered_users.remove(name);
	}


	public void registerUser(String name, String ip, int port, String[] files) {
		String f = new String();

		for (int i = 0 ; i < files.length; i ++)
			f = f + ";" + files[i];

		String new_user = name + " " + ip + " " + port + " " + f;
		registered_users.put(name, new_user);
    }

	public String requestUsers(String name) {
		String result = new String();

		for (Map.Entry<String, String> entry : registered_users.entrySet()) {
			if (entry.getKey().toString().equals(name))
				continue;

			result = result + "-" + (String)entry.getValue();
		}

		return result;
	}
}
