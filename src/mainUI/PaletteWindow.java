package mainUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import system.PictureBox;
import system.PictureInfo;
import system.RPGCreator;
import system.SettingValues;

public class PaletteWindow extends JPanel implements ChangeListener{

	private static final int FONT_SIZE = SettingValues.PALETTE_TAB_FONT_SIZE;
	
	private static PictureBox  picBox = RPGCreator.getPictureBox();
	private static PictureInfo activePic;
	
	private LinkedList<PaletteGrid> grids;
	private JTabbedPane palTab;
	
	public PaletteWindow() {
		super();
		setLayout(new BorderLayout());
		activePic = null;
		grids = new LinkedList<PaletteGrid>();
		palTab = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.WRAP_TAB_LAYOUT);
		palTab.addChangeListener(this);
		palTab.setFont(new Font(Font.DIALOG,Font.BOLD,FONT_SIZE));

		// add Tabs
		grids.add(new PaletteGrid(picBox.getPicInfoAll()));
		grids.add(new PaletteGrid(picBox.getPicInfoAll()));
		grids.add(new PaletteGrid(picBox.getPicInfoAll()));
		grids.add(new PaletteGrid(picBox.getPicInfoAll()));
		grids.add(new PaletteGrid(picBox.getPicInfoAll()));
		grids.add(new PaletteGrid(picBox.getPicInfoAll()));
		
		palTab.add("Home",grids.get(0));
		palTab.add("Basic",grids.get(1));
		palTab.add("Bulding",grids.get(2));
		palTab.add("Item",grids.get(3));
		palTab.add("Character",grids.get(4));
		palTab.add("Exp-1",grids.get(5));
		
		add(palTab);
	}
	
	public static void setActivePic(PictureInfo pic){
		activePic = pic;
		System.out.println("PAL: No."+pic.getSerialCode()+" (PW)");
	}
	public static void setEraser(){
		activePic = null;
		System.out.println("PAL: Eraser(PW)");
	}
	public static PictureInfo getActivePic(){
		return activePic;
	}
	
//Event Listener	
	public void stateChanged(ChangeEvent e){
		
	}
}
