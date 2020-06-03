package form;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import alert.alertWindow;
import components.DoubleInputPanel;
import mainWindow.content;

//Class for a form
public class formWindow extends JFrame {
	//This class is used to insert and update products and providers
	
	//CLASS VARIABLES
	private String[][] columnsList;
	private String[][] data = null;
	private String[] tables;
	private String nameForModifyObject;
	private ArrayList<Object> inputList;
	private JPanel panel;
	private String consultedItem;
	private content invokerClass;
	
	//CONSTRUCTORS
	public formWindow(content invokerClass, String consultedItem) {
		
		this.invokerClass = invokerClass;
		this.consultedItem = consultedItem;
		setWindowVariables();
		
		panel = new JPanel(null);
		panel.setBackground(setting.programSettings.getBackgroundContentColor());
		this.add(panel);
		
		setFrameSettings();
		createPanelComponents();
		
	}
	
	public formWindow(content invokerClass, String consultedItem, String nameForModifyObject) {
		
		this.invokerClass = invokerClass;
		this.consultedItem = consultedItem;
		this.nameForModifyObject = nameForModifyObject;
		setWindowVariables();
		
		panel = new JPanel(null);
		this.add(panel);
		setFrameSettings();
		createPanelComponents();
		
	}
	
	//METHODS FOR CONSTRUCT CLASS
	private void setWindowVariables() {
		switch (consultedItem) {
		
		case "PRODUCTS":
			columnsList = new String[][] {
				{"Nombre en español", "text"},
				{"Nombre en inglés", "text"},
				{"Cantidad disponible", "text"},
				{"Cantidad mínima", "text"},
				{"Proveedor", "combo"}
			};
			
			data = new String[][] {
				{"spanish_name", null },
				{"english_name", null},
				{"current_amount", null },
				{"minimum_amount", null },
				{"id_provider", null },
				{"spent_amount", null }
			};
			
			tables = new String[] {"products"};
			
			break;
		case "PROVIDERS":
			columnsList = new String[][] {
				{"Nombre", "text"},
				{"email", "text"},
				{"Teléfono", "text"},
				{"días entre pedidos", "text"}
			};
			
			data = new String[][] {
				{"name", null},
				{"email", null},
				{"phone_number", null},
				{"days_between_orders", null}
			};
			
			tables = new String[] {"providers"};
			
			break;
		}
	}
	
	private void setFrameSettings() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(380, ((40*(columnsList.length+2))+10));
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void createPanelComponents() {
		createTextLabels();
		createInputBoxes();
		createMultipleBoxes();
		createButtons();
	}

	//METHODS
	private void createTextLabels() {

		ArrayList<JLabel> textList = new ArrayList<JLabel>();
		
		for (int i=0; i<columnsList.length; i++) {
			if (columnsList[i].length >= 3) {
				if (columnsList[i][2].equals("multiple")) {
					continue;
				}
			}
			JLabel text = new JLabel(columnsList[i][0]);
			textList.add(text);
		}
		
		for (int i=0; i<textList.size(); i++) {
			
			panel.add(textList.get(i));
			
			int leftDistance = 10, topDistance = ((40*i)+10), width = 150, height = 30;
			textList.get(i).setBounds(leftDistance, topDistance, width, height);
		}
		
	}
	
	private void createInputBoxes() {
		
		inputList = new ArrayList<Object>();
		
		String[] recoveredData = null;
		
		if (nameForModifyObject != null) {
			recoveredData = recoverDataFromDB();
		}
		
		for (int i=0; i<columnsList.length; i++) {
			
			if (columnsList[i].length >= 3) {
				if (columnsList[i][2].equals("multiple")) {
					continue;
				}
			}
			
			if (columnsList[i][1].equals("text")) {
				JTextField textField;
					
				if (recoveredData == null) {
					textField = new JTextField();
				} else {
					textField = new JTextField(recoveredData[i]);
				}
				
				inputList.add(textField);
			} else if (columnsList[i][1].equals("combo")) {
				JComboBox<String> comboBox = new JComboBox<String>();
				
				String selectedData;
					
				if (recoveredData == null) {
					selectedData = "-1";
				} else {
					selectedData = recoveredData[i];
				}
					
				fillComboBox(comboBox, data[i][0], selectedData);
				
				inputList.add(comboBox);
			}
		}
		
		for (int i=0; i<inputList.size(); i++) {
			
			panel.add((Component)inputList.get(i));
			
			int leftDistance = 200, topDistance = ((40*i)+10), width = 150, height = 30;
			((Component)inputList.get(i)).setBounds(leftDistance, topDistance, width, height);
			
		}
		
	}
	
	private void createMultipleBoxes() {
		
		Boolean multipleBoxesNeeded = false;
		
		for (int i=0; i<columnsList.length; i++) {
			for (int j=0; j<columnsList[i].length; j++) {
				if (columnsList[i][j].equals("multiple")) {
					multipleBoxesNeeded = true;
				}
			}
		}
		
		if (multipleBoxesNeeded) {
			try {
				String[] columns = new String[] {"pr.spanish_name"};
				String[] tables = new String[] {"products pr"};
				
				//Get all products
				ResultSet resultSetAllProducts = dao.dbio.select(columns, tables, null);
				ArrayList<String> dataFromResultSetAllProducts = new ArrayList<String>();
				
				int counterResultSetAllProductsLength = 0;
				
				while (resultSetAllProducts.next()) {
					counterResultSetAllProductsLength++;
				}
					
				String[] arrayAllProducts = new String[counterResultSetAllProductsLength];
					
				resultSetAllProducts.first();
				
				for (int i=0; i<arrayAllProducts.length; i++) {
					arrayAllProducts[i] = resultSetAllProducts.getString(1);
					resultSetAllProducts.next();
				}
	
				//Get name of selected product
				columns = new String[] {"pr.spanish_name", "ppr.amount"};
				tables = new String[] {"products pr", "products_plates_relationship ppr", "plates p"};
				String[][] inputWhere = new String[][] {{"pr.id_product", "ppr.id_product", "AND"}, {"ppr.id_plate", "p.id_plate"}};
				
				ResultSet resultSetProductsSelected = dao.dbio.select(columns, tables, inputWhere);
				
				int counterResultSetProductsSelectedLength = 0;
				
				while (resultSetProductsSelected.next()) {
					counterResultSetProductsSelectedLength++;
				}
					
				resultSetProductsSelected.first();
				
				for (int i=0; i<counterResultSetProductsSelectedLength; i++) {
					DoubleInputPanel multipleBox = new DoubleInputPanel(arrayAllProducts, resultSetProductsSelected.getString(1), resultSetProductsSelected.getString(2), new Dimension(400, (50+(i*40))));
					this.add(multipleBox);
				}
			} catch (SQLException e) {
				new alertWindow("Error 316: No se han podido recuperar los datos del producto de la base de datos", "Aceptar");
				//e.printStackTrace();
			}
		}
	}
	
	private void createButtons() {
		
		JButton buttonCancel = new JButton("Cancelar");
		//Button cancel only close this form.
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeFormWindow();
			}
		});
		panel.add(buttonCancel);
		
		buttonCancel.setBounds(10, ((40*columnsList.length)+10), 150, 30);
		
		
		JButton buttonAccept = new JButton("Aceptar");
		
		buttonAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (checkTextFieldContent()) {
					
					String[] textFieldValues = getTextFieldValues();
					String[] valuesForUpdate = new String[data.length];
					
					for (int i=0; i<textFieldValues.length; i++) {
						valuesForUpdate[i] = textFieldValues[i];
					}
					
					if (consultedItem.equals("PRODUCTS")) {
						//Spent amount (this column of the table is not used yet):
						valuesForUpdate[5] = "0";
					}
					
					updateDataValues(valuesForUpdate);
					
					if (nameForModifyObject == null) {	//only true if is new product/provider
						
						String table = consultedItem.toLowerCase();
						String[][] insertData = new String[data.length][2];
						for (int i=0; i<insertData.length; i++) {
							insertData[i][0] = data[i][0];
							if (!data[i][0].equals("spent_amount")) {
								if (data[i][1] == null) {
									insertData[i][1] = data[i][1];
								} else {
									insertData[i][1] = "'" + data[i][1] + "'";
								}
							} else {
								insertData[i][1] = "0";
							}
						}
						dao.dbio.insert(table, insertData);
						
					} else {	//This is only for update a product/provider
						
						String[][] updateData;
						
						if (data[data.length-1][0].equals("spent_amount")) {
							updateData = new String[data.length-1][2];
						} else {
							updateData = new String[data.length-1][2];
						}
						
						for (int i=0; i<updateData.length; i++) {
							updateData[i][0] = data[i][0];
							updateData[i][1] = "'" + data[i][1] + "'";
						}
						
						String[] updateTables = {consultedItem.toLowerCase()};
						String[][] inputWhere = {{data[0][0], "'" + nameForModifyObject + "'"}};
						
						dao.dbio.update(updateData, updateTables, inputWhere);
						
					}
					
					invokerClass.refreshTable();
					
					closeFormWindow();
					
				}
			}
		});
		panel.add(buttonAccept);
		
		buttonAccept.setBounds(200, ((40*columnsList.length)+10), 150, 30);
		
	}
	
	private Boolean checkTextFieldContent() {

		Boolean checked = true;
		
		for (int i=0; i<inputList.size(); i++) {
			try {
				JTextField textField = (JTextField) inputList.get(i);
				
				if (!consultedItem.equals("PROVIDERS") || i!=3) {
					if (textField.getText().trim().equals("")) {
						
						textField.setBorder(BorderFactory.createLineBorder(Color.red));
						
						checked = false;
						
					} else {
						
						if ((consultedItem.equals("PRODUCTS") && (i == 2 || i == 3)) && !isNumeric(textField.getText())) {
							textField.setBorder(BorderFactory.createLineBorder(Color.red));
							checked = false;
						} else {
							textField.setBorder(BorderFactory.createLineBorder(Color.green));
						}
	
					}
				} else {
					
					if ( !textField.getText().trim().equals("") && !isNumeric(textField.getText())) {
						textField.setBorder(BorderFactory.createLineBorder(Color.red));
						checked = false;
					} else {
						textField.setBorder(BorderFactory.createLineBorder(Color.green));
					}
					
				}
			} catch (ClassCastException e) {
				//This is executed when trying to verify a comboBox. The content of a comboBox will always be fine. 
				//Just skip it.
			}
			
		}
		return checked;
	}
	
	private Boolean isNumeric (String text) {
		try {
			Integer.parseInt(text);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private String[] getTextFieldValues() {
		
		String[] textFieldValues = new String[inputList.size()];
	
		for (int i=0; i<inputList.size(); i++) {
			
			String readedText;
			
			try {
				readedText = ((JTextField) inputList.get(i)).getText();
			} catch (ClassCastException e) {
				readedText = ((JComboBox<String>) inputList.get(i)).getSelectedItem().toString();
			}
			
			//Control if day between orders is empty:
			if (readedText.trim().equals("")) {
				readedText = null;
			}
			
			textFieldValues[i] = readedText;
		}
		
		return textFieldValues;
	}
	
	private void updateDataValues(String[] dataValues) {
		
		for (int i=0; i<data.length; i++) {
			try {
				if (columnsList[i][1].equals("text")) {
					data[i][1] = dataValues[i];
				} else if (data[i][0].equals("id_provider")) {
					
					String[] columns = {"id_provider"};
					String[] tables = {"providers"};
					String[][] inputWhere = {{"name", "'" + dataValues[i] + "'"}};
					
					ResultSet resultSet = dao.dbio.select(columns, tables, inputWhere);
					try {
						resultSet.first();					
						data[i][1] = Integer.toString(resultSet.getInt("id_provider"));
					} catch (SQLException e) {
						new alertWindow("Error 317: No se ha podido obtener el id del proveedor seleccionado", "Aceptar");
						//e.printStackTrace();
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				//Index out of range. Common mistake when modifying a product: columnsList is shorter in that case it dates.
				//This exception is caused for "spent_amount" columns
				//Just skip it.
			}
		}
		
	}
	
	private String[] recoverDataFromDB() {
		String[] columns = new String[data.length];
		
		for (int i=0; i<data.length; i++) {
			columns[i] = data[i][0];
		}
		
		String[][] inputWhere = {{columns[0], "'" + nameForModifyObject.toUpperCase() + "'"}};
		ResultSet recoveredResultSet = dao.dbio.select(columns, tables, inputWhere);
		String[] recoveredArray = new String[data.length];
		
		try {
			recoveredResultSet.first();
			
			for (int i=0; i<data.length; i++) {
				
				if (!(consultedItem.equals("PROVIDERS") && i == 3)) {
					recoveredArray[i] = recoveredResultSet.getObject(data[i][0]).toString();
				} else {
					recoveredArray[i] = null;
				}
				
			}
		} catch (SQLException e) {
			new alertWindow("Error 318: No se han podido recuperar los datos de este registro", "Aceptar");
			//e.printStackTrace();
			recoveredArray = null;
		}
		
		return recoveredArray;
		
	}
	
	private void fillComboBox(JComboBox<String> comboBox, String column, String selectedData) {
		
		String[] columns, tables;
		String[][] inputWhere;
		
		switch (column) {
		case "id_provider": 
			
			columns = new String[] {"id_provider", "name"};
			tables = new String[] {"providers"};
			
			ResultSet resultSet = dao.dbio.select(columns, tables, null);
			
			try {
				while (resultSet.next()) {
					int idProvider = resultSet.getInt("id_provider");
					String name = resultSet.getString("name");
					
					comboBox.addItem(name);
					
					if (idProvider == Integer.parseInt(selectedData)) {
						comboBox.setSelectedItem(name);
					}
					
				}
			} catch (SQLException e) {
				new alertWindow("Error 319: Fallo en recuperar todos los proveedores", "Aceptar");
				//e.printStackTrace();
			}		
			
			break;
		}
		
	}
	
	private void closeFormWindow() {
		this.dispose();
	}
	
}
