package com.frequentis.cis.connector.generator;

public interface Generator {
	
	public void generateAndForwardToCore(Object msg);
}
