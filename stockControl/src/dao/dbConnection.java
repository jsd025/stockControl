package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {
	
	Connection connection;
	
	int connectionUsedBy;
	
	Boolean closingConnection;
	
	dbConnection() {
		
		System.out.println("Conexion abierta");
		
		connectionUsedBy = 1;
		closingConnection = false;
		
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			connection = DriverManager.getConnection("jdbc:mysql://db4free.net:3306/stockcontrol?useSSL=false","pcprogramuser","WF?n(@ob~{8rX(1n");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	Runnable tryingToCloseConnection = new Runnable() {
	    public void run() {
	    	
	    	closingConnection = true;
	    	
	    	try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {

				//Mensaje de error: Su dispositivo ha cerrado el hilo que iba a cerrar la conexión con la base de datos. Riesgo: Alto.
				
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

	protected synchronized void incrementConnectionUsedBy() {
		this.connectionUsedBy++;
	}
	
	protected synchronized void decrementConnectionUsedBy() {
		this.connectionUsedBy--;
	}
	
	protected synchronized void closeConnection() {
		new Thread(tryingToCloseConnection).start();
	}
	
}
