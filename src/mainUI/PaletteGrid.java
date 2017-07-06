package mainUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import system.PictureInfo;

public class PaletteGrid extends JLabel implements MouseListener{
	private static final int CELL_WIDTH = 75,
							 GRID_Y = 8,
							 GRID_X = 2;
	
	private LinkedList<PictureInfo> pics;
	private int activeNum;
	
	public PaletteGrid(){
		super();
		setPreferredSize(new Dimension(GRID_X*CELL_WIDTH,GRID_Y*CELL_WIDTH));
		setBorder(new LineBorder(Color.WHITE));
		addMouseListener(this);
		pics = new LinkedList<PictureInfo>();
		setActiveNum(-1);
	}
	public PaletteGrid(LinkedList<PictureInfo> pics){
		this();
		this.pics = pics;
	}
	
	public void setActiveNum(int num){
		activeNum = num;
		tellActivePic();
		repaint();
	}
	public void setPictureInfo(PictureInfo pic){
		pics.add(pic);
		tellActivePic();
		repaint();
	}
	public int getActiveNum(){
		return activeNum;
	}
	
	public void tellActivePic(){
		if(activeNum < 0 || pics.size() <= activeNum){
			PaletteWindow.setEraser();
		}else{
			PaletteWindow.setActivePic(pics.get(activeNum));
		}
	}

	
//Paint
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setBackground(Color.BLACK);
		g2.clearRect(0,0,GRID_X*CELL_WIDTH,GRID_Y*CELL_WIDTH);
		
		if(pics!=null){
			for (int i=0; i<pics.size();i++) {
				Point2D.Double p = pics.get(i).getSquareSize(CELL_WIDTH);
				g2.drawImage(pics.get(i).getPicture(), (i/GRID_Y)*CELL_WIDTH, (i%GRID_Y)*CELL_WIDTH,
																			(int)p.x, (int)p.y, this);
			}
		}
		//draw grid
		g2.setPaint(Color.DARK_GRAY);
		g2.setStroke(new BasicStroke(1));
		for(int i=0;i<GRID_X+1;i++){
			g2.draw(new Line2D.Double(CELL_WIDTH*i,0,CELL_WIDTH*i,GRID_Y*CELL_WIDTH));
		}
		for(int i=0;i<GRID_Y+1;i++){
			g2.draw(new Line2D.Double(0,CELL_WIDTH*i,GRID_X*CELL_WIDTH,CELL_WIDTH*i));
		}
		if(0 <= activeNum  && activeNum < GRID_X*GRID_Y){
			g2.draw(new Rectangle2D.Double((activeNum/GRID_Y)*CELL_WIDTH+1,
								(activeNum%GRID_Y)*CELL_WIDTH+1,CELL_WIDTH-2,CELL_WIDTH-2));
			g2.setPaint(Color.WHITE);
			g2.draw(new Rectangle2D.Double((activeNum/GRID_Y)*CELL_WIDTH,
								(activeNum%GRID_Y)*CELL_WIDTH,CELL_WIDTH,CELL_WIDTH));
		}
	}
	
//Event Listeners
	public void mouseClicked(MouseEvent e){}
	public void mousePressed(MouseEvent e){
		Point point = e.getPoint();
		int x = point.x;
		int y = point.y;
		if(x<0||x>GRID_X*CELL_WIDTH||y<0||y>GRID_Y*CELL_WIDTH) return;
		int num = (x/CELL_WIDTH)*GRID_Y + (y/CELL_WIDTH);
		System.out.println("Palette No."+num+" is clicked");
		setActiveNum(num);
		return;
	}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
}
