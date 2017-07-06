package mainUI;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import mainUI.menu.MainMenuBar;

public class MainFrame extends JFrame{
//instance variable
	public static final int MAP_SIZE_X = 100,
							MAP_SIZE_Y = 100;
	
	private JPanel[] panel = new JPanel[3];
	private MapWindow mapWin;
	private PaletteWindow palWin;
	private MainMenuBar menu;

//constructor
	public MainFrame(){ this(""); }

	public MainFrame(String name){
		super(name);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(0,0,1800,1000);
		
		palWin = new PaletteWindow();
		mapWin = new MapWindow();
		
		panel[0] = mapWin;
		panel[1] = palWin;
		panel[2] = new JPanel();
		
		menu = new MainMenuBar();
		
	    add(panel[0],BorderLayout.CENTER);
	    add(panel[1],BorderLayout.WEST);
	    add(panel[2],BorderLayout.SOUTH);
	    
	    for(JPanel p :panel){
	    	p.setBorder(new LineBorder(Color.black,1,false));
	    }
	    
	    setJMenuBar(menu);
	    
		setVisible(true);
	}
//method
	public MapWindow getMapWin(){
		return mapWin;
	}
	public PaletteWindow getPaletteWin(){
		return palWin;
	}
	
}