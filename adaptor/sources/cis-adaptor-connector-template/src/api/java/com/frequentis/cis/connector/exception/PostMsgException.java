/**
 * This class was created for the Common Information Space Adapter! 
 */
package com.frequentis.cis.connector.exception;

/**
 * @author TObritzh
 * @since 24.06.2015
 * @version 1.0
 * 
 * This is the PostMsgException that indicates that during the post of a message to the CIS
 * something happens and the post was not successful.
 *
 */
public class PostMsgException extends Exception {

	private static final long serialVersionUID = -7891257908520708888L;

	/**
	 * 
	 */
	public PostMsgException() {

	}

	/**
	 * @param message
	 */
	public PostMsgException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public PostMsgException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PostMsgException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public PostMsgException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
