package listeners;

import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mainWindow.content;

public class tableChangesListener implements ListSelectionListener {

	content invokerClass;
	
	public tableChangesListener(content invokerClass) {
		
		this.invokerClass = invokerClass;
		
	}
	
	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (!event.getValueIsAdjusting()) {
			
			if (invokerClass.getTable().getSelectedRow() != -1 && invokerClass.getTable().getSelectedRow() != 0) {
				invokerClass.changeButtonNewOrModifyText(true);
				invokerClass.changeButtonDeleteVisibility(true);
				if (invokerClass.getConsultedItem().toUpperCase().equals("ORDERS")) {
					invokerClass.changeButtonConfirmOrderReceivedVisibility(true);
				}
			} else {
				invokerClass.changeButtonNewOrModifyText(false);
				invokerClass.changeButtonDeleteVisibility(false);
				if (invokerClass.getConsultedItem().toUpperCase().equals("ORDERS")) {
					invokerClass.changeButtonConfirmOrderReceivedVisibility(false);
				}
			}
			
		}
		
	}

}
