package window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import listeners.tableChangesListener;

public class content extends JPanel{

	private static final long serialVersionUID = 1L;
	
	Dimension panelDimension;
	
	JTextField textFieldSearch;
	
	JTable table;
	
	String consultedItem, stringOfItemName;
	
	JButton buttonNewOrModify, buttonDelete;
	
	//SQL
	String[] columns;
	String[] tables;
	
	//JTabel
	String[] tableHeader;
	
	content(Dimension panelDimension, String consultedItem) {
		
		this.setLayout(null);
		
		this.consultedItem = consultedItem.trim().toUpperCase();
		
		switch (this.consultedItem) {
		case "PRODUCTS":
			stringOfItemName = "producto";
			tableHeader = new String[] {"Nombre", "Name", "Cantidad disponible"};
			
			columns = new String[] {"pr.spanish_name", "pr.english_name", "pr.current_amount"};
			tables = new String[] {"products pr"};
			
			break;
			
		case "PROVIDERS":
			stringOfItemName = "proveedor";
			tableHeader = new String[] {"Nombre", "Email", "Teléfono"};
			
			columns = new String[] {"pr.name", "pr.email", "pr.phone_number"};
			tables = new String[] {"providers pr"};
			
			break;
			
		case "PLATES":
			stringOfItemName = "plato";
			tableHeader = new String[] {"Plato", "Productos"};
			
			columns = new String[] {"pl.name", "pr.spanish_name"};
			tables = new String[] {"plates pl", "products_plates_relationship ppr", "products pr"};
			
			break;
			
		case "ORDERS":
			stringOfItemName = "pedido";
			tableHeader = new String[] {"Proveedor", "Fecha", "Productos"};
			
			columns = new String[] {"prov.name", "or.date", "prod.spanish_name"};
			tables = new String[] {"orders or", "providers prov", "products_orders_relationship por", "products prod"};
			
			break;
		}
		
		
		
		//System.out.println("Dimension contenido: \nAncho: "+panelDimension.getWidth()+"\nAlto: "+panelDimension.getHeight()+"\n");
		
		//Que ocurre si el tamaño el panel es menor que 400 en ancho o en alto: El que sea menor a 400 se establece en 400 antes de comenzar.
		if (panelDimension.getWidth()>= 400 && panelDimension.getHeight()>=400) {
			this.panelDimension = panelDimension;
		} else {
			int dimensionWidth = (int)panelDimension.getWidth();
			int dimensionHeight = (int)panelDimension.getHeight();
			
			if (dimensionWidth < 400) {
				dimensionWidth = 400;
			}
			
			if (dimensionHeight < 400) {
				dimensionHeight = 400;
			}
			
			this.panelDimension = new Dimension(dimensionWidth, dimensionHeight);
		}
		
		this.setMinimumSize(new Dimension(400, 400));
		
		//this.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//System.out.println("Estableciendo tamaño de contenido:\nAncho: "+panelDimension.getWidth()+"\nAlto: "+panelDimension.getHeight()+"\n");
		
		this.setBounds(0, 0, (int)panelDimension.getWidth(), (int)panelDimension.getHeight());
		
		this.setAutoscrolls(true);
		
		createComponents();
		
	}

	private void createComponents() {
		
		addButtonNewOrModify();
		
		addTextFieldSearch();
		
		addButtonSearch();
		
		addTable();
		
		addButtonDelete();
		
	}

	private void addButtonNewOrModify() {

		buttonNewOrModify = new JButton();
		changeButtonNewOrModifyText(false);
		
		buttonNewOrModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonNewOrModifyOnClick();
			}
		});
		this.add(buttonNewOrModify);
		
		//Ancho: Empieza en 5% de panel y acaba en 25%.
		buttonNewOrModify.setBounds(((int)panelDimension.getWidth()/20), 10, ((int)panelDimension.getWidth()/5), 30);
		
	}
	
	private void buttonNewOrModifyOnClick() {
		
		if (table.getSelectedRow() == -1) {
			if (consultedItem.toUpperCase().equals("PRODUCTS") || consultedItem.toUpperCase().equals("PROVIDERS")) {
				new form.formWindow(this, consultedItem);
			} else {
				new form.adaptableFormWindow(consultedItem);
			}
		} else {
			if (consultedItem.toUpperCase().equals("PRODUCTS") || consultedItem.toUpperCase().equals("PROVIDERS")) {
				new form.formWindow(this, consultedItem, table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
			} else {
				//new form.adaptableFormWindow(consultedItem, table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
			}
		}
		
	}
	
	private void addTextFieldSearch() {
		textFieldSearch = new JTextField("Ingrese el filtro del "+ stringOfItemName +" que desea buscar...");
		
		this.add(textFieldSearch);
		
		//Ancho: Empieza en 30% de panel y acaba en 80%
		textFieldSearch.setBounds(((int)(panelDimension.getWidth()*0.3)), 10, ((int)panelDimension.getWidth()/2), 30);
	}
	
	private void addButtonSearch() {
		
		JButton buttonSearch = new JButton();
		
		buttonSearch.setText("Buscar");
		
		buttonSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String inputText = textFieldSearch.getText();
				inputText.trim();
				
				String[][] whereCondition = new String[columns.length][3];
				
				if (inputText == "") {
					whereCondition = null;
				} else {
					for (int i=0; i<columns.length; i++) {
						whereCondition[i][0] = columns[i];
						whereCondition[i][1] = "'%" + inputText + "%'";
						whereCondition[i][2] = "OR";
					}
				}				
				
				searchForTable(whereCondition);
				
			}
		});
		
		this.add(buttonSearch);
		
		//Ancho: Empieza en 85% y acaba en 95%.
		buttonSearch.setBounds(((int)(panelDimension.getWidth()*0.85)), 10, ((int)panelDimension.getWidth()/10), 30);
		
	}
	
	private void addTable() {
		
		table = new JTable();
		
		searchForTable(null);
		
		table.setAutoscrolls(true);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.add(table);
		
		table.setBounds(((int)panelDimension.getWidth()/20), 50, ((int)(panelDimension.getWidth()*0.9)), ((int)panelDimension.getHeight()-110));
		
		tableChangesListener tableChangesListener = new tableChangesListener(this);
		table.getSelectionModel().addListSelectionListener(tableChangesListener);
	}
	
	private void addButtonDelete() {
		buttonDelete = new JButton("Borrar " + stringOfItemName);
		
		buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.out.println(table.getSelectedRow());
				String[][] inputWhere = {{columns[0], table.getModel().getValueAt(table.getSelectedRow(), 0).toString()}};
				
				dao.dbio.delete(tables, inputWhere);
				
				refreshTable();
			}
		});
		
		this.add(buttonDelete);
		
		buttonDelete.setBounds(((int)(panelDimension.getWidth()*0.65)), ((int)panelDimension.getHeight()-40), ((int)(panelDimension.getWidth()*0.3)), 30);
		
		changeButtonDeleteVisibility(false);
	}
	
	public void changeButtonDeleteVisibility(Boolean visibility) {
		buttonDelete.setVisible(visibility);
	}

	private void searchForTable(String[][] inputWhere) {
		
		if (consultedItem.equals("PLATES")) {
			
			String[][] platesWhere = {{"pr.id_product", "ppr.id_product", "AND"}, {"pl.id_plate", "ppr.id_plate"}};
			
			if (inputWhere != null) {
			
				String[][] platesBuffer = new String[inputWhere.length+platesWhere.length][3];
				
				 for (int i=0; i<inputWhere.length; i++) {
					 for (int j=0; j<inputWhere[i].length; j++) {
						 platesBuffer[i][j] = inputWhere[i][j];						 
					 }
				 }
				 
				 if (platesBuffer[inputWhere.length-1][2] == null) {
					 platesBuffer[inputWhere.length-1][2] = "AND";
				 }
				 
				 for (int i=0; i<platesWhere.length; i++) {
					 for (int j=0; j<platesWhere[i].length; j++) {
						 platesBuffer[platesBuffer.length-platesWhere.length+i][j] = platesWhere[i][j];
					 }
				 }
				 
				 inputWhere = platesBuffer;				 
				 
			} else {
				inputWhere = platesWhere;
			}
			
		}
		
		try {
			for (int i=table.getModel().getRowCount(); i>0; i--) {
				((DefaultTableModel) table.getModel()).removeRow(i-1);
			}
		} catch (NullPointerException e) {
			System.out.println("No había datos que borrar en la tabla");
		}
		
		ResultSet resultSetProducts = null;
		
		resultSetProducts = dao.dbio.select(columns, tables, inputWhere);
		
		int resultSetSize = 0;
		
		try {
			
			while (resultSetProducts.next()) {
				resultSetSize++;
			}
			
			Object[][] tableData = new Object[resultSetSize][tableHeader.length];
			
			resultSetProducts.first();
			
			//Bucle para cada resultado en la consulta
			for (int i=0; i<resultSetSize; i++) {
				
				for (int j=0; j<(tableHeader.length); j++) {
					
					tableData[i][j] = resultSetProducts.getObject(j+1);
					
				}
				
				resultSetProducts.next();
				
			}
			
			table.setModel(new DefaultTableModel(tableData, tableHeader) {
			    @Override
				public boolean isCellEditable(int row, int column) {
			    	return false;//This causes all table cells to be not editable
			    }
			});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			//Imprimir mensaje de error: No se han podido leer los datos recibidos de la base de datos.
		} catch (NullPointerException e) {
			e.printStackTrace();
			
			//Imprimir mensaje de error: Error al comunicarse con la base de datos.
			
		}
		
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
		
	}
	
	public void changeButtonNewOrModifyText(Boolean isForModify) {
		if (isForModify) {
			buttonNewOrModify.setText("Modificar " + stringOfItemName + " seleccionado");
		} else {
			buttonNewOrModify.setText("Registrar nuevo " + stringOfItemName);
		}
	}
	
	public void refreshTable() {
		
		searchForTable(null);
		
	}
	
	public void refreshContent() {
		
		
		
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}
	
}
