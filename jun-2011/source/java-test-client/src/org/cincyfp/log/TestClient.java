package org.cincyfp.log;

import org.apache.log4j.Logger;

public class TestClient {
	static Logger logger = Logger.getLogger(TestClient.class); 

	public static void main(String[] args) {

		try {
			logger.info("Entering application.");
			logger.trace("testing trace");
			logger.debug("testing debug");
			logger.error("testing error");
			for (int i = 0; i < 10000; i++) {
				Thread.sleep(2000);
				logger.info("message" + i);
			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

	}
}
