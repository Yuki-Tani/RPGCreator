package mainUI.menu;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import mainUI.MapWindow;
import mainUI.PaletteWindow;
import system.MapBox;
import system.MapInfo;
import system.PictureInfo;
import system.RPGCreator;
import system.SettingValues;

public class MapGridPopupMenu extends JPopupMenu implements ActionListener {
	private static final String[] NAMES = new String[] { "Set Ground" };
	private static final int[] NONE_SELECTED_DISENABLE_INDEX = { 0 };
	private JMenuItem[] item = new JMenuItem[NAMES.length];
	private static final int FONT_SIZE = SettingValues.MENU_FONT_SIZE;

	private MapBox mapBox = RPGCreator.getMapBox();

	private MapInfo mapInfo;

	public MapGridPopupMenu() {
		this(null);
	}

	public MapGridPopupMenu(MapInfo mapInfo) {
		super();
		this.mapInfo = mapInfo;
		for (int i = 0; i < NAMES.length; i++) {
			item[i] = new JMenuItem(NAMES[i]);
			item[i].addActionListener(this);
			item[i].setActionCommand(NAMES[i]);
			item[i].setFont(new Font(Font.DIALOG, Font.PLAIN, FONT_SIZE));
			add(item[i]);
		}
		if (mapInfo == null) {
			for (int i : NONE_SELECTED_DISENABLE_INDEX) {
				item[i].setEnabled(false);
			}
		}
	}

	// Event methods
	public void actionPerformed(ActionEvent e) {
		MapWindow mapWin = RPGCreator.getMainFrame().getMapWin();
		String com = e.getActionCommand();

		if (com.equals("Set Ground")) {
			PictureInfo pic = PaletteWindow.getActivePic();
			mapInfo.setMapGround(pic);
			mapWin.repaint();
		}
	}

}
