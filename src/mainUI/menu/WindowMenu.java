package mainUI.menu;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import mainUI.MapWindow;
import system.MapBox;
import system.RPGCreator;
import system.SettingValues;

public class WindowMenu extends JMenu implements ActionListener{
	private static final String MENU_NAME = "Window";
	private static final String[] NAMES = new String[]{"New","Open","Close","Rename","Delete"};
	private static final int FONT_SIZE = SettingValues.MENU_FONT_SIZE;
	
	private JMenuItem[] item = new JMenuItem[NAMES.length];
	
	private MapBox mapBox = RPGCreator.getMapBox();
	
	public WindowMenu(){
		super(MENU_NAME+" ");
		
		for(int i=0;i<NAMES.length;i++){
			item[i] = new JMenuItem(NAMES[i]);
			item[i].addActionListener(this);
			item[i].setActionCommand(NAMES[i]);
			item[i].setFont(new Font(Font.DIALOG,Font.PLAIN,FONT_SIZE));
			add(item[i]);
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
			new MapSelectDialog(mapWin.getOpenedName()).setInfomationCommand("Close");
		}else if(com.equals("Rename")){
			new MapSelectDialog(maps).setInfomationCommand("MapRename");
		}else if(com.equals("Delete")){
			new MapSelectDialog(maps).setInfomationCommand("Delete");
		}
	}
}