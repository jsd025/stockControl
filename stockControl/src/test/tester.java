package test;

import window.mainWindow;

public class tester {
	
	public static void main(String[] args) {
		
		mainWindow window = new mainWindow();
		
		while (true) {
			window.refresh();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
