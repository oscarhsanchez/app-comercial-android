package gpovallas.ws;

import gpovallas.obj.Error;


public class WsResponse {
	
	public static String RESULT_OK = "OK";

	public String result;
	private String responseString;
	public Error error;
	
	public WsResponse() {
	}
	
	public WsResponse(String responseString) {
		super();
		this.responseString = responseString;
	}

	
	public String getResponseString() {
		return responseString;
	}
	
	public void setResponseString(String responseString) {
		this.responseString = responseString;
	}
	


	public boolean failed() {
		if (result != null && result.equals(WsResponse.RESULT_OK) && error == null)
			return false;
		else
			return true;
	}

}
