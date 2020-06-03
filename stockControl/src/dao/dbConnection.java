package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import alert.alertWindow;

//Class to manage connections to the database
public class dbConnection {
	
	//CLASS VARIABLES
	private Connection connection;
	private int connectionUsedBy;
	private Boolean closingConnection;
	
	//CONSTRUCTORS
	dbConnection() {
		
		System.out.println("Conexion abierta");
		
		connectionUsedBy = 1;
		closingConnection = false;
		
		//try to connect to the database in the constructor
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			connection = DriverManager.getConnection("jdbc:mysql://db4free.net:3306/stockcontrol?useSSL=false","pcprogramuser","WF?n(@ob~{8rX(1n");
		} catch (SQLException e) {
			new alertWindow("Error 301: No se ha podido conectar con el servidor. Inténtelo de nuevo más tarde", "Aceptar");
			e.printStackTrace();
		}
		
	}
	
	//CREATING A RUNNABLE
	Runnable tryingToCloseConnection = new Runnable() {
	    public void run() {
	    	//This runnable tries to close the connection to the database. It only succeeds when the connection takes a second without being used
	    	closingConnection = true;
	    	
	    	try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				new alertWindow("Error 601: Su dispositivo ha interrumpido el cierre de la conexión con la base de datos", "Aceptar");
				//Error de riesgo alto. La conexión puede quedar abierta.
				e1.printStackTrace();
			}
	    	
	    	if (connectionUsedBy <= 0) {
				try {
					dbio.objectDBConnection = null;
					connection.close();
					System.out.println("Conexion cerrada");
					try {
						super.finalize();
						this.finalize();
					} catch (Throwable e) {
						e.printStackTrace();
					}
				} catch (SQLException e) {
					e.printStackTrace();
					closingConnection = false;
				}
	    	} else {
	    		closingConnection = false;
	    	}
	   }
	};
	
	//METHODS
	protected synchronized void incrementConnectionUsedBy() {
		this.connectionUsedBy++;
	}
	
	protected synchronized void decrementConnectionUsedBy() {
		this.connectionUsedBy--;
	}
	
	//GETTERS & SETTERS
	protected synchronized void closeConnection() {
		new Thread(tryingToCloseConnection).start();
	}
	
	protected Connection getConnection() {
		return connection;
	}

	protected synchronized void setConnection(Connection connection) {
		this.connection = connection;
	}

	protected int getConnectionUsedBy() {
		return connectionUsedBy;
	}

	protected synchronized void setConnectionUsedBy(int connectionUsedBy) {
		this.connectionUsedBy = connectionUsedBy;
	}
	
}
