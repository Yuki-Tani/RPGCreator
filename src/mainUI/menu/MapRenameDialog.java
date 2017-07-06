package mainUI.menu;

import mainUI.util.DialogInfomation;
import mainUI.util.DialogListener;
import mainUI.util.InputDialog;
import system.RPGCreator;

public class MapRenameDialog extends InputDialog implements DialogListener {
	private String beforeName ;

	public MapRenameDialog(String currentName){
		super("Map Rename");
		addDialogListener(this);
		beforeName = currentName;
		
		addBlank();
		addTextInput("new name", currentName);
		addBlank();
		addButtons();
		
		optimizeSize();
		setVisible(true);
	}
	public void fromDialog(DialogInfomation d) {
		RPGCreator.getMapBox().changeMapName(beforeName, d.getString(0));
		RPGCreator.getMainFrame().getMapWin().changeListName(beforeName, d.getString(0));
		dispose();
	}

}
