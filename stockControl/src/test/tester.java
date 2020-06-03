package test;

import login.loginWindow;
import mainWindow.mainWindow;
import setting.userSettings;

//Class with main window
public class tester {
	
	public static void main(String[] args) {
		
		loginWindow loginWindow = new loginWindow();
		Boolean loggedIn = false;
		
		//Wait for user login, and the open the "mainWindow".
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
