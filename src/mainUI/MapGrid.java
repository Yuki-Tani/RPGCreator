package mainUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import mainUI.menu.MapGridPopupMenu;
import system.MapCellInfo;
import system.MapInfo;
import system.PictureBox;
import system.PictureInfo;
import system.RPGCreator;
import system.SettingValues;
import system.tool.PicAddress;

// Through this class, User can check and operate MapInfo visibly.
public class MapGrid extends JLabel implements MouseListener,MouseMotionListener{
	
	private static final int CELL_WIDTH = SettingValues.CELL_WIDTH;
	private static int MapCount = 0;
	
	private static PictureBox picBox
								= RPGCreator.getPictureBox();
	private MapInfo mapInfo;
	private int cellX,cellY;
	
	private Point operatedCell;
	private PictureInfo operatedPic;
	private LinkedList<Point> rolloverCell;
	
	public MapGrid(MapInfo mapInfo){
		super();
		MapCount ++;
		this.mapInfo = mapInfo;
		cellX = mapInfo.getW();
		cellY = mapInfo.getH();
		operatedCell = null;
		operatedPic  = null;
		rolloverCell = new LinkedList<Point>();
		setPreferredSize(new Dimension(cellX*CELL_WIDTH,cellY*CELL_WIDTH));
		setBorder(new LineBorder(Color.WHITE));
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public Point gridPoint(Point p){
		return gridPoint(p.x,p.y);
	}
	public Point gridPoint(int x, int y){
		return new Point(x/CELL_WIDTH,y/CELL_WIDTH);
	}
	public static String defName(){
		return "No" + getMapCount();
	}
//Paint	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setBackground(Color.BLACK);
		g2.clearRect(0,0,cellX*CELL_WIDTH,cellY*CELL_WIDTH);
		drawGrid(g2);
		for(int i=0;i<cellX;i++){
			for(int j=0;j<cellY;j++){
				drawMapCell(i,j,g2);
			}
		}
		drawFilm(g2);
	}
	private void drawGrid(Graphics2D g2){
		g2.setPaint(Color.DARK_GRAY);
		g2.setStroke(new BasicStroke(1));
		for(int i=0;i<cellX+1;i++){
			g2.draw(new Line2D.Double(CELL_WIDTH*i,0,CELL_WIDTH*i,cellY*CELL_WIDTH));
		}
		for(int i=0;i<cellY+1;i++){
			g2.draw(new Line2D.Double(0,CELL_WIDTH*i,cellX*CELL_WIDTH,CELL_WIDTH*i));
		}
	}
	private void drawMapCell(int x,int y,Graphics2D g2){
		MapCellInfo cell = mapInfo.getMapInfo()[x][y];
		PicAddress[] back = cell.getBackPicAdds();
		PicAddress[] fore = cell.getForePicAdds();
		for(int i=0;i<back.length;i++){
			PictureInfo info = picBox.getPicInfo(back[i].getSerial());
			Point2D.Double p1 = info.getP1(back[i].getIndex());
			Point2D.Double p2 = info.getP2(back[i].getIndex());
			g2.drawImage(info.getPicture(),
						x*CELL_WIDTH, y*CELL_WIDTH,(x+1)*CELL_WIDTH,(y+1)*CELL_WIDTH,
						(int)p1.x,(int)p1.y,(int)p2.x,(int)p2.y,this);
		}
		for(int i=0;i<fore.length;i++){
			PictureInfo info = picBox.getPicInfo(fore[i].getSerial());
			Point2D.Double p1 = info.getP1(fore[i].getIndex());
			Point2D.Double p2 = info.getP2(fore[i].getIndex());
			g2.drawImage(info.getPicture(),
						x*CELL_WIDTH, y*CELL_WIDTH,(x+1)*CELL_WIDTH,(y+1)*CELL_WIDTH, 
						(int)p1.x,(int)p1.y,(int)p2.x,(int)p2.y,this);
		}

	}
	private void drawFilm(Graphics2D g2){

		Rectangle2D.Double cell = new Rectangle2D.Double();
		g2.setPaint(new Color(200,200,200,100));
		int size = rolloverCell.size();
		for(int i =0;i<size;i++){
			Point cellP = rolloverCell.poll();
			int x = cellP.x;
			int y = cellP.y;
			if(0<=x && x<=cellX && 0<=y && y <= cellY){
				cell.setRect(x*CELL_WIDTH,y*CELL_WIDTH,CELL_WIDTH,CELL_WIDTH);
				g2.fill(cell);
			}	
			rolloverCell.offer(cellP);
		}
	}
	
	public static int getMapCount(){
		return MapCount;
	}
//Mouse Event	
	public void mouseClicked(MouseEvent e){
		
	}
	public void mousePressed(MouseEvent e){
		putPicture(e);
		setRollover(e);
		repaint();
		popupAction(e);
	}
	public void mouseReleased(MouseEvent e){
		popupAction(e);
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){
		rolloverCell.clear();
		repaint();
	}
	public void mouseDragged(MouseEvent e){
		putPicture(e);
		setRollover(e);
		repaint();
	}
	public void mouseMoved(MouseEvent e){
		setRollover(e);
		repaint();
	}
	
	private void putPicture(MouseEvent e){
		Point cellP = gridPoint(e.getPoint());
		PictureInfo pic = PaletteWindow.getActivePic();
		if(cellP.equals(operatedCell) && ((pic == null && operatedPic == null)
									|| (pic != null && pic.equals(operatedPic))))
			return;
		System.out.println("click: ("+cellP.x+" , "+cellP.y+") (MG)");
		mapInfo.setMapPic(cellP,pic);
		operatedCell = cellP;
		operatedPic = pic;
	}
	private void setRollover(MouseEvent e){
		Point origin = gridPoint(e.getPoint());
		PictureInfo pic = PaletteWindow.getActivePic();
		int start = 0;
		if(pic == null){
			PicAddress picAdd = mapInfo.getMapCellInfo(origin).getTopPicAdd();
			if(picAdd == null){
				rolloverCell.clear();
				return;
			}else{
				pic = picBox.getPicInfo(picAdd.getSerial());
				start = picAdd.getIndex();
			}	
		}
		int cellNum = pic.getCellNum();
		Point fromO = new Point(0,0);
		rolloverCell.clear();
		for(int i= 0;i<cellNum;i++){
			rolloverCell.offer(new Point(origin.x+fromO.x,origin.y+fromO.y));
			Point dP = pic.getLink((i+start)%cellNum);
			fromO.translate(dP.x, dP.y);	
		}	
	}
	private void popupAction(MouseEvent e) {
		System.out.println("in");
		if (e.isPopupTrigger()) {
				new MapGridPopupMenu(mapInfo).show(this, e.getX(), e.getY());
		}
	}
}
