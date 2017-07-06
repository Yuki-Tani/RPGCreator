package mainUI.menu;

import java.awt.Font;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MainMenuBar extends JMenuBar{
	
	private static final int FONT_SIZE = 18;
	
	private JMenu[] menus;
	
	public MainMenuBar(){
		super();
		menus = new JMenu[6];
		
		menus[0] = new FileMenu();
		menus[1] = new EditMenu();
		menus[2] = new WindowMenu();
		menus[3] = new MapMenu();
		menus[4] = new ActionMenu();
		menus[5] = new StoryMenu();
		
		
		for (JMenu menu : menus) {
			menu.setFont(new Font(Font.DIALOG,Font.PLAIN,FONT_SIZE));
			add(menu);
		}
	}
}
