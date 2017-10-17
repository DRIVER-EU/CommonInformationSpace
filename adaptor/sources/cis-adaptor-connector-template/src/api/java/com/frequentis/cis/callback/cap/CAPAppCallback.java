package com.frequentis.cis.callback.cap;

import com.google.publicalerts.cap.Alert;

public interface CAPAppCallback {
	
	public void capReceived(Alert capMsg);
	
	public void capReceived(String capMsg);
	

}
