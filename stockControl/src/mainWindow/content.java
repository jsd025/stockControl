package mainWindow;

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import alert.alertWindow;
import form.ConfirmOrderReceivedWindow;
import listeners.tableChangesListener;

public class content extends JPanel{

	private static final long serialVersionUID = 1L;
	
	Dimension panelDimension;
	JTextField textFieldSearch;
	JTable table;
	String consultedItem, stringOfItemName;
	JButton buttonNewOrModify, buttonDelete, buttonConfirmOrderReceived;
	
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
			tableHeader = new String[] {"Plato", "Productos", "Cantidad"};
			
			columns = new String[] {"pl.name", "pr.spanish_name", "ppr.amount"};
			tables = new String[] {"plates pl", "products_plates_relationship ppr", "products pr"};
			
			break;
			
		case "ORDERS":
			stringOfItemName = "pedido";
			tableHeader = new String[] {"Id", "Proveedor", "Fecha", "Productos", "Cantidad pedida", "Cantidad recibida"};
			
			columns = new String[] {"orders.id_order", "providers.name", "orders.date", "products.spanish_name", "products_orders_relationship.ordered_amount", "products_orders_relationship.received_amount"};
			tables = new String[] {"orders", "providers", "products_orders_relationship", "products"};
			
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
		
		this.setBackground(setting.programSettings.getBackgroundContentColor());
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
		
		if (consultedItem.toUpperCase().equals("ORDERS")) {
			addButtonConfirmOrderReceived();
		}
	}

	private void addButtonNewOrModify() {

		buttonNewOrModify = new JButton();
		changeButtonNewOrModifyText(false);
		buttonNewOrModify.setBackground(setting.programSettings.getButtonColor());
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
		
		if (table.getSelectedRow() == -1 || table.getSelectedRow() == 0) {
			if (consultedItem.toUpperCase().equals("PRODUCTS") || consultedItem.toUpperCase().equals("PROVIDERS")) {
				new form.formWindow(this, consultedItem);
			} else {
				new form.adaptableFormWindow(this, consultedItem);
			}
		} else {
			if (consultedItem.toUpperCase().equals("PRODUCTS") || consultedItem.toUpperCase().equals("PROVIDERS")) {
				new form.formWindow(this, consultedItem, table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
			} else {
				new form.adaptableFormWindow(this, consultedItem, table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
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
		buttonSearch.setBackground(setting.programSettings.getButtonColor());
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
						if (i<columns.length-1) {
							whereCondition[i][2] = "OR";
						} else {
							whereCondition[i][2] = "AND";
						}
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
		table.setBackground(setting.programSettings.getBackgroundTableColor());
		
		this.add(table);
		
		table.setBounds(((int)panelDimension.getWidth()/20), 50, ((int)(panelDimension.getWidth()*0.9)), ((int)panelDimension.getHeight()-110));
		
		tableChangesListener tableChangesListener = new tableChangesListener(this);
		table.getSelectionModel().addListSelectionListener(tableChangesListener);
	}
	
	private void addButtonDelete() {
		buttonDelete = new JButton("Borrar " + stringOfItemName);
		buttonDelete.setBackground(setting.programSettings.getButtonCancelColor());
		buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String queryTable[] = {tables[0]};
				String[][] inputWhere = {{columns[0], table.getModel().getValueAt(table.getSelectedRow(), 0).toString()}};
				dao.dbio.delete(queryTable, inputWhere);
				refreshTable();
			}
		});
		
		this.add(buttonDelete);
		
		buttonDelete.setBounds(((int)(panelDimension.getWidth()*0.55)), ((int)panelDimension.getHeight()-40), ((int)(panelDimension.getWidth()*0.4)), 30);
		
		changeButtonDeleteVisibility(false);
	}
	
	public void changeButtonDeleteVisibility(Boolean visibility) {
		buttonDelete.setVisible(visibility);
	}
	
	private void addButtonConfirmOrderReceived() {
		buttonConfirmOrderReceived = new JButton("Confirmar pedido recibido");
		buttonConfirmOrderReceived.setBackground(new Color(91, 245, 122));
		buttonConfirmOrderReceived.setBounds(((int)(panelDimension.getWidth()*0.05)), ((int)panelDimension.getHeight()-40), ((int)(panelDimension.getWidth()*0.4)), 30);
		buttonConfirmOrderReceived.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openConfirmOrderReceivedWindow();
			}
		});
		changeButtonConfirmOrderReceivedVisibility(false);
		this.add(buttonConfirmOrderReceived);
	}

	private void searchForTable(String[][] inputWhere) {
				
		switch (consultedItem) {
			case "PLATES":
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
			break;
			case "ORDERS":
				//columns = new String[] {"prov.name", "or.date", "prod.spanish_name"};
				//tables = new String[] {"orders or", "providers prov", "products_orders_relationship por", "products prod"};
				String[][] ordersWhere = {{"products.id_product", "products_orders_relationship.id_product", "AND"}, {"products_orders_relationship.id_order", "orders.id_order", "AND"}, {"orders.id_provider", "providers.id_provider"}};
				
				if (inputWhere != null) {
				
					String[][] ordersBuffer = new String[inputWhere.length+ordersWhere.length][3];
					
					 for (int i=0; i<inputWhere.length; i++) {
						 for (int j=0; j<inputWhere[i].length; j++) {
							 ordersBuffer[i][j] = inputWhere[i][j];						 
						 }
					 }
					 
					 if (ordersBuffer[inputWhere.length-1][2] == null) {
						 ordersBuffer[inputWhere.length-1][2] = "AND";
					 }
					 
					 for (int i=0; i<ordersWhere.length; i++) {
						 for (int j=0; j<ordersWhere[i].length; j++) {
							 ordersBuffer[ordersBuffer.length-ordersWhere.length+i][j] = ordersWhere[i][j];
						 }
					 }
					 
					 inputWhere = ordersBuffer;				 
					 
				} else {
					inputWhere = ordersWhere;
				}
			break;
		}
		
		try {
			for (int i=table.getModel().getRowCount(); i>0; i--) {
				((DefaultTableModel) table.getModel()).removeRow(i-1);
			}
		} catch (NullPointerException e) {
			System.out.println("No había datos que borrar en la tabla");
		}
		
		ResultSet resultSetProducts = dao.dbio.select(columns, tables, inputWhere);
		
		int resultSetSize = 0;
		
		try {
			
			while (resultSetProducts.next()) {
				resultSetSize++;
			}
			
			Object[][] tableData = new Object[resultSetSize+1][tableHeader.length];
			//Set title of columns:
			tableData[0] = tableHeader;
			
			resultSetProducts.first();
			
			//Bucle para cada resultado en la consulta
			for (int i=1; i<=resultSetSize; i++) {
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
			
			table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			    @Override
			    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
			    {
			        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			        c.setBackground(row == 0 ? setting.programSettings.getBackgroundTableHeaderColor(): setting.programSettings.getBackgroundTableColor());			        
			        return c;
			    }
			});
		} catch (SQLException e) {
			e.printStackTrace();
			new alertWindow("Error 303: No se han podido leer los datos de la base de datos.", "Aceptar");
		} catch (NullPointerException e) {
			e.printStackTrace();
			new alertWindow("Error 304: Error al conectarse con la base de datos.", "Aceptar");
			
		}
		
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
		
	}
	
	private void openConfirmOrderReceivedWindow() {
		String providerName = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
		String date = table.getModel().getValueAt(table.getSelectedRow(), 1).toString();
		String productName = table.getModel().getValueAt(table.getSelectedRow(), 2).toString();
		
		new ConfirmOrderReceivedWindow(this, table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
	}
	
	public void changeButtonConfirmOrderReceivedVisibility(Boolean visibility) {
		buttonConfirmOrderReceived.setVisible(visibility);
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
	
	/*
	public void refreshContent() {	

	}
	*/

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public String getConsultedItem() {
		return consultedItem;
	}
	
}
