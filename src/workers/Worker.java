package workers;


public interface Worker {
	public void initialize();
	public void waitForTasksThread();
}
