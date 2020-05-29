package test;

import login.loginWindow;
import setting.userSettings;
import window.mainWindow;

public class tester {
	
	public static void main(String[] args) {
		
		loginWindow loginWindow = new loginWindow();
		
		Boolean loggedIn = false;
		
		do {
			
			if (userSettings.getUsername() != null) {
				loggedIn = true;
			}
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!loggedIn);
		
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
