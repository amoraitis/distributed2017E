package model;
public class MyThread implements Runnable {

	/* Define a string variable with name input*/
	String input;

	/* Define the constructor of the class that assigns the string*/
	public MyThread(String inp){
		this.input = inp;
	}

	public void run() {
		for (int i = 0; i < 1000; i++) {
			System.err.println(i + ":\t" + input);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
	}
}
