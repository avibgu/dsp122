package common.controller;

import org.junit.Test;

public class EC2ControllerTest {

	private EC2Controller mEc2Controller = EC2Controller.getInstance();;

	@Test
	public void startTheManagerTest() throws Exception {
		mEc2Controller.startTheManager(2);
	}
	
	@Test
	public void startWorkersTest() throws Exception {
		mEc2Controller.startWorkers(2, 2);
	}
	
	@Test
	public void stopWorkersTest() throws Exception {
		mEc2Controller.stopWorkers();
	}
}
