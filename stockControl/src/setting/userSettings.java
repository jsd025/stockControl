package setting;

import alert.alertWindow;

public class userSettings {
	
	static private String[][] userPermissions = new String[][] {
		{"Comandero", null},
		{"Productos", null},
		{"Proveedores", null},
		{"Platos", null},
		{"Registro de comandas", null},
		{"Administrar permisos de usuarios", null}
	};
	static private String language;
	static private String username = null;
	
	static public String[][] getUserPermissions() {
		return userPermissions;
	}
	
	static public void setUserPermissions(String permissions) {
		
		if (permissions.length() != userPermissions.length) {
			alertWindow internalError = new alertWindow("Ha ocurrado un error interno. Por favor, reinicie la aplicación", "Aceptar"); 
		}
		
		for (int i=0; i<userPermissions.length; i++) {			
			if (permissions.charAt(i) == '1') {
				userPermissions[i][1] = "true";
			} else {
				userPermissions[i][1] = "false";
			}
		}
	}
	
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
	
}
