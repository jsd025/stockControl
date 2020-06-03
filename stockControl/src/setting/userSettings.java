package setting;

import alert.alertWindow;

//Class for static store of users variables
public class userSettings {
	
	//CLASS VARIABLES
	static private String[][] userPermissions = new String[][] {
		{"Comandero", null},
		{"Productos", null},
		{"Proveedores", null},
		{"Platos", null},
		{"Pedidos", null},
		{"Administrar permisos de usuarios", null}
	};
	static private String language;	//Language is not used yet.
	static private String username = null;
	
	//GETTERS & SETTERS
	static public String[][] getUserPermissions() {
		return userPermissions;
	}
	
	static public void setUserPermissions(String permissions) {
		
		if (permissions.length() != userPermissions.length) {
			new alertWindow("Error 403: Ha ocurrido un error interno. Por favor, reinicie la aplicación", "Aceptar"); 
		}
		
		for (int i=0; i<userPermissions.length; i++) {			
			if (permissions.charAt(i) == '1') {
				userPermissions[i][1] = "true";
			} else {
				userPermissions[i][1] = "false";
			}
		}
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		userSettings.username = username;
	}
	
	//METHODS
	public Boolean changeUserPermission(String key, Boolean value) {
		
		Boolean response = false;
		
		for (int i=0; i<userPermissions.length; i++) {
			if (userPermissions[i][0].equals(key)) {
				userPermissions[i][1] = "\"" + value + "\"";
				response = true;
			}
		}
		
		return response;
	}
}
