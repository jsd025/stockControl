package window;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class contentTakeOrders extends JPanel {
	
	Dimension panelDimension;
	
	contentTakeOrders(Dimension panelDimension) {
		
		this.panelDimension = panelDimension;
		
		this.add(new JLabel("Texto prueba tomar comandas"));
	}
	
}
