package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import alert.alertWindow;

//Class that manages queries for the database
public class dbio {
	//All methods and variables are static
	
	//CLASS VARIABLES
	protected static dbConnection objectDBConnection;
	
	//PRIVATE METHODS
	private static Connection establishConnection() {
																																																			
		if (objectDBConnection == null) {
			objectDBConnection = new dbConnection();
		} else {
			objectDBConnection.incrementConnectionUsedBy();
		}
		
		return objectDBConnection.getConnection();
		
	}
	
	private static void finishConnection() {
		
		objectDBConnection.decrementConnectionUsedBy();
		
		objectDBConnection.closeConnection();
		
	}
	
	//PUBLIC METHODS
	public static ResultSet select(String[] columns, String[] tables, String[][] inputWhere) {
		
		String query;
		
		query = "SELECT ";
		
		for (int i=0; i<columns.length; i++) {
			query += columns[i];
			if ((columns.length-1) != i) {
				query += ", ";
			}
		}
		
		query += " FROM ";
		
		for (int i=0; i<tables.length; i++) {
			query += tables[i];
			if ((tables.length-1) != i) {
				query += ", ";
			}
		}
		
		if (inputWhere != null) {
			Boolean isORActive = false;
			query += " WHERE ";
			
			for (int i=0; i<inputWhere.length; i++) {
				if (inputWhere[i].length == 3) {
					if (inputWhere[i][2] != null) {
						if (inputWhere[i][2].equals("OR") && !isORActive) {
							inputWhere[i][0] = "(" + inputWhere[i][0];
							isORActive = true;
						} else if (!inputWhere[i][2].equals("OR") && isORActive) {
							inputWhere[i][1] += ")";
							isORActive = false;
						}
					}
				}
				
				query += inputWhere[i][0] + " LIKE " + inputWhere[i][1];
				if (inputWhere.length-1 != i) {
					inputWhere[i][2].toUpperCase();
					query += " " + inputWhere[i][2] + " ";
				}
			}
		}
		
		query += " ORDER BY " + columns[0];
		
		System.out.println(query);
		
		Connection connection = establishConnection();
		
		ResultSet rs;
		
		try {
			Statement s = connection.createStatement();
			rs = s.executeQuery(query);
		} catch (SQLException e) {
			new alertWindow("Error 302: No se ha podido seleccionar este item de la base de datos", "Aceptar");
			//e.printStackTrace();
			rs = null;
		}
		
		finishConnection();
		
		return rs;
	}

	public static int update(String[][] data, String[] tables, String[][] inputWhere) {
		
		int result;
		
		String query;
		
		query = "UPDATE ";
		
		for (int i=0; i<tables.length; i++) {
			query += tables[i];
			if ( i != (tables.length-1)) {
				query += ", ";
			}
		}
		
		query +=" SET ";
		
		for (int i=0; i<data.length; i++) {
			query += data[i][0] + " = " + data[i][1];
			
			if ( i != (data.length-1)) {
				query += ", ";
			}
		}
		
		if (inputWhere != null) {
			query += " WHERE ";
			
			for (int i=0; i<inputWhere.length; i++) {
				
				inputWhere[i][1].toUpperCase();
				
				query += "UPPER(" + inputWhere[i][0] + ") = " + inputWhere[i][1];
					
				if (i != inputWhere.length-1) {
						query += " AND ";
				}
					
			}
		}
		
		System.out.println(query);
		
		Connection connection = establishConnection();
		
		try {
			Statement s = connection.createStatement();
			result = s.executeUpdate(query);
		} catch (SQLException e) {
			new alertWindow("Error 303: Error al actualizar la base de datos", "Aceptar");
			//e.printStackTrace();
			result = -1;
		}
		
		finishConnection();
		
		return result;
		
	}
	
	public static int insert(String table, String[][] data) {
		
		System.out.println("Insertando");
		
		int result;
		
		String query;
		
		query = "INSERT INTO ";
		
		query += table + " (";
		
		for (int i=0; i<data.length; i++) {
			query += data[i][0];
			
			if (i != data.length-1) {
				query += ", ";
			}
		}
		
		query += ") VALUES (";
		
		for (int i=0; i<data.length; i++) {
			query += data[i][1];
			
			if (i != data.length-1) {
				query += ", ";
			}
		}
		
		query += ")";
		
		System.out.println(query);
		
		Connection connection = establishConnection();
		try {
			Statement s = connection.createStatement();
			result = s.executeUpdate(query);
		} catch (SQLException e) {
			new alertWindow("Error 304: No se ha podido a�adir a la base de datos", "Aceptar");
			//e.printStackTrace();
			result = -1;
		}
		
		finishConnection();
		
		return result;
		
	}
	
	public static Boolean delete(String[] table, String[][] data) {
		
		Boolean success;
		
		String query;
		
		query = "DELETE FROM ";
		
		for (int i=0; i<table.length; i++) {
		
			query += table[i];
			
			if (i != table.length-1) {
				query += ", ";
			}
		}
		
		query += " WHERE ";
		
		for (int i=0; i<data.length; i++) {
			query += data[i][0] + " = '" + data[i][1] + "'";
			
			if (data.length-1 != i) {
				data[i][2].toUpperCase();
				query += " " + data[i][2] + " ";
			}
		}
		
		System.out.println(query);
		
		Connection connection = establishConnection();

		try {
			Statement s = connection.createStatement();
			s.executeUpdate(query);
			success = true;
		} catch (SQLException e) {
			new alertWindow("Error 305: Fallo al borrar de la base de datos", "Aceptar");
			//e.printStackTrace();
			success = false;
		}
		
		finishConnection();
		
		return success;
		
	}
}
