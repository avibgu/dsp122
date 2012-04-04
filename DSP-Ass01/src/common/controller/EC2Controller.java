package common.controller;

public class EC2Controller {

	private EC2Controller() {
	}

	private static class EC2ControllerHolder {
		public static final EC2Controller instance = new EC2Controller();
	}

	public static EC2Controller getInstance() {
		return EC2ControllerHolder.instance;
	}

	public void startTheManager(int numOfURLsPerWorker) {
		// TODO Checks if a Manager node is active on the EC2 cloud. If it is
		// not, the application will start the manager node.

	}
}
