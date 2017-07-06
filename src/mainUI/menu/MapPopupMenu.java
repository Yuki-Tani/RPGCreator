package mainUI.menu;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import mainUI.MapWindow;
import system.MapBox;
import system.RPGCreator;
import system.SettingValues;

public class MapPopupMenu extends JPopupMenu implements ActionListener{
	private static final String[] NAMES = new String[]{"New","Open","Close","Rename","Delete"};
	private static final int[] NONE_SELECTED_DISENABLE_INDEX= {2,3,4};
	private JMenuItem[] item = new JMenuItem[NAMES.length];
	private static final int FONT_SIZE = SettingValues.MENU_FONT_SIZE;
	
	private MapBox mapBox = RPGCreator.getMapBox();
	
	private String selectedName;
	
	public MapPopupMenu(){
		this( "##SYSTEM##None");
	}
	public MapPopupMenu(String mapName){
		super();
		selectedName = mapName;
		for(int i=0;i<NAMES.length;i++){
			item[i] = new JMenuItem(NAMES[i]);
			item[i].addActionListener(this);
			item[i].setActionCommand(NAMES[i]);
			item[i].setFont(new Font(Font.DIALOG,Font.PLAIN,FONT_SIZE));
			add(item[i]);
		}
		if(selectedName.equals("##SYSTEM##None")){
			for (int i : NONE_SELECTED_DISENABLE_INDEX) {
				item[i].setEnabled(false);
			}
		}
	}
	
//Event methods	
	public void actionPerformed(ActionEvent e){
		MapWindow mapWin = RPGCreator.getMainFrame().getMapWin();
		String com = e.getActionCommand();
		String[] maps = mapBox.getMapNames();
		
		if(com.equals("New")){
			new MapMakeDialog(mapBox.getDefName());
		}else if(com.equals("Open")){
			new MapSelectDialog(maps).setInfomationCommand("Open");
		}else if(com.equals("Close")){
			mapWin.closeMap(selectedName);
		}else if(com.equals("Rename")){
			new MapRenameDialog(selectedName);
		}else if(com.equals("Delete")){
			if(deleteDialog(selectedName)){
				mapWin.closeMap(selectedName);
				mapBox.deleteMap(selectedName);
			}	
		}
	}
	private boolean deleteDialog(String name){	
		return JOptionPane.showConfirmDialog(this,"Delete \""+name+"\" completely.",
											 "Delete Map",JOptionPane.OK_CANCEL_OPTION,
											 JOptionPane.WARNING_MESSAGE)
				== JOptionPane.OK_OPTION;
	}

}
