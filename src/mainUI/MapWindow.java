package mainUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mainUI.menu.MapPopupMenu;
import system.MapBox;
import system.RPGCreator;
import system.SettingValues;

public class MapWindow extends JPanel implements ChangeListener, MouseListener {
	private static int activeMap;
	private static int defMapNum = 2;
	private static int FONT_SIZE = SettingValues.MAP_TAB_FONT_SIZE;

	private static MapBox mapBox = RPGCreator.getMapBox();

	private JTabbedPane mapTab;

	private LinkedList<String> openList = new LinkedList<String>();

	public MapWindow() {
		super(true);
		setBorder(new LineBorder(Color.WHITE));
		setLayout(new BorderLayout());
		setBackground(Color.DARK_GRAY);
		mapTab = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		mapTab.setFont(new Font(Font.DIALOG, Font.BOLD, FONT_SIZE));
		mapTab.addMouseListener(this);
		add(mapTab, BorderLayout.CENTER);

		for (int i = 0; i < defMapNum; i++) {
			openMap(mapBox.newMap().getName());
		}

		activeMap = mapTab.getSelectedIndex();
	}

	public void openMap(String name) {
		if (!openList.contains(name)) {
			JScrollPane map = new JScrollPane(new MapGrid(mapBox.getInfo(name)));
			mapTab.addTab(name, map);
			openList.add(name);
		}
		mapTab.setSelectedIndex(openList.indexOf(name));
	}

	public void openNewestMap() {
		openMap(mapBox.getLastName());
	}

	public void closeMap(String name) {
		int tabIndex = openList.indexOf(name);
		if (tabIndex == -1)
			return;
		else {
			closeMap(tabIndex);
		}
		reopen();
	}

	public void closeMap(int index) {
		if (index >= mapTab.getTabCount() || index < 0)
			return;
		mapTab.remove(index);
		openList.remove(index);
		reopen();
	}

	public String[] getOpenedName() {
		return openList.toArray(new String[0]);
	}

	public void changeListName(String before, String after) {
		int index = openList.indexOf(before);
		openList.set(index, after);
		reopen();
	}

	public int getActiveMap() {
		return activeMap;
	}

	public void reopen() {
		LinkedList<String> preOpenList = new LinkedList<String>(openList);
		openList.clear();
		mapTab.removeAll();
		int listSize = preOpenList.size();
		for (int i = 0; i < listSize; i++) {
			openMap(preOpenList.poll());
		}
	}

	// Event Listeners
	public void stateChanged(ChangeEvent e) {
		activeMap = mapTab.getSelectedIndex();
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		popupAction(e);
	}

	public void mouseReleased(MouseEvent e) {
		popupAction(e);
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	private void popupAction(MouseEvent e) {
		if (e.isPopupTrigger()) {
			int index = mapTab.indexAtLocation(e.getX(), e.getY());
			if (index != -1) {
				new MapPopupMenu(openList.get(index)).show(mapTab, e.getX(), e.getY());
			}
		}
	}
}
