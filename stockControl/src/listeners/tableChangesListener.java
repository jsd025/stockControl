package listeners;

import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import window.content;

public class tableChangesListener implements ListSelectionListener {

	content invokerClass;
	
	public tableChangesListener(content invokerClass) {
		
		this.invokerClass = invokerClass;
		
	}
	
	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (!event.getValueIsAdjusting()) {
			
			if (invokerClass.getTable().getSelectedRow() != -1) {
				invokerClass.changeButtonNewOrModifyText(true);
				invokerClass.changeButtonDeleteVisibility(true);
			} else {
				invokerClass.changeButtonNewOrModifyText(false);
				invokerClass.changeButtonDeleteVisibility(false);
			}
			
		}
		
	}

}
