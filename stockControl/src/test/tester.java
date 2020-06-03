package test;

import login.loginWindow;
import mainWindow.mainWindow;
import setting.userSettings;

public class tester {
	
	public static void main(String[] args) {
		
		loginWindow loginWindow = new loginWindow();
		
		Boolean loggedIn = false;
		
		do {
			
			loginWindow.refresh();
			
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
