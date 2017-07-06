package mainUI.menu;

import javax.swing.JOptionPane;

import mainUI.MapWindow;
import mainUI.util.ChoiceDialog;
import mainUI.util.DialogInfomation;
import mainUI.util.DialogListener;
import system.MapBox;
import system.RPGCreator;

public class MapSelectDialog extends ChoiceDialog implements DialogListener{
	private MapBox mapBox = RPGCreator.getMapBox();
	private MapWindow mapWin = RPGCreator.getMainFrame().getMapWin();
	
	public DialogListener listener;
	
	public MapSelectDialog(String[] mapNames){
		super("Map Select");
		addDialogListener(this);
		
		addBlank();
		for (String name : mapNames) {
			addCheckLabel(name);
		}
		addBlank();
		
		optimizeSize();
		setVisible(true);
	}
	public void setDialogListener(DialogListener listener){
		this.listener = listener;
	}
//Event method
	public void fromDialog(DialogInfomation d){
		if(d.getInfomationCommand().equals("MapRename")){
			new MapRenameDialog(d.getString(0));
		}else if(d.getInfomationCommand().equals("Open")){
				mapWin.openMap(d.getString(0));
		}else if(d.getInfomationCommand().equals("Close")){
				mapWin.closeMap(d.getString(0));
	    }else if(d.getInfomationCommand().equals("Delete")){
			if(deleteDialog(d.getString(0))){
				mapWin.closeMap(d.getString(0));
				mapBox.deleteMap(d.getString(0));
			}	
		}
		dispose();
	}
	
	private boolean deleteDialog(String name){	
		return JOptionPane.showConfirmDialog(this,"Delete \""+name+"\" completely.",
											 "Delete Map",JOptionPane.OK_CANCEL_OPTION,
											 JOptionPane.WARNING_MESSAGE)
				== JOptionPane.OK_OPTION;
	}
}
