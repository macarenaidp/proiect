import org.apache.axis.client.*;

import javax.xml.namespace.*;

import java.util.*;
import java.net.*;


public class WebClient {
	private URL endpoint;
	private Service service;
	private HashMap<String, Call> operationCalls;
	private String serverAddress;
	
	public WebClient (String address) throws MalformedURLException {
		serverAddress = address;
		endpoint = new URL(serverAddress);
		service = new Service();
		operationCalls=new HashMap<String, Call>();
	}
	
	public Object invoke(String operationName, Object[] params) throws Exception {
		Call call;
		
		if(operationCalls.containsKey(operationName)) {
			call=operationCalls.get(operationName);
		}
		else {
			call = (Call)service.createCall();	
			call.setTargetEndpointAddress(endpoint);
			call.setOperationName(new QName(operationName)); // operation name
			operationCalls.put(operationName, call);
		}
		
		return call.invoke(params);
	}

}
