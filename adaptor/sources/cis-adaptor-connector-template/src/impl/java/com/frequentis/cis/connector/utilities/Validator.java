package com.frequentis.cis.connector.utilities;

import java.io.InvalidObjectException;

import com.google.publicalerts.cap.Alert;

/**
 * The Validator class provides general method for validating values.
 * 
 * @author TObritzh
 *
 */
public class Validator {

	/**
	 * Default Constructor
	 */
	public Validator() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * This method validates if the given Object is a valid Google CAP Object.
	 *  
	 * @param msg The Coogle CAP Object
	 * @return the valid Google CAP Object
	 * @throws InvalidObjectException If the Object is no valid Google CAP object
	 */
	public static Alert checkIfAlertObject(Object msg) throws InvalidObjectException {
		Alert alert = null;
		if (msg instanceof Alert) {
			alert = (Alert)msg;
		} else {
			throw new InvalidObjectException("Given Object is no Alert Object!");
		}
		return alert;
	}
}
