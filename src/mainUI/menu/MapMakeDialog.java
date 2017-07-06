package mainUI.menu;

import mainUI.util.DialogInfomation;
import mainUI.util.DialogListener;
import mainUI.util.InputDialog;
import system.RPGCreator;

public class MapMakeDialog extends InputDialog implements DialogListener{
	
	public MapMakeDialog(){this("map name");}
	public MapMakeDialog(String initial){
		super("New Map");
		addDialogListener(this);
		addBlank();
		addTextInput("Name :",initial);
		addSizeInput("Size :",RPGCreator.getMapBox().getDefMapW(),
								RPGCreator.getMapBox().getDefMapH());
		addBlank();
		addButtons();
		optimizeSize();
		setVisible(true);
	}
//Listener
	public void fromDialog(DialogInfomation d){
			RPGCreator.getMapBox().newMap(d.getString(0),d.getInt(1),d.getInt(2));
			RPGCreator.getMainFrame().getMapWin().openNewestMap();
	}
}
