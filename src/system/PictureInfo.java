package system;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

//Pictures are used as "PictureInfo".
//Picture blocks are specified by system.tool.PicAddress(serial,index)
//
public class PictureInfo{
	public static final int NORTH = 0,
							 EAST  = 1,
							 SOUTH = 2,
							 WEST  = 3;
	public static final int BACK = SettingValues.BACK,
			 				 FORE = SettingValues.FORE;
	// serialCode of the picture
	private String serialCode;
	// picture's image
	private BufferedImage picture;
	private double width,height; // image size
	// cell dimensions
	private Point dim;
	private double cellW,cellH; // cell widths (cellW == cellH)
	private int cellNum;		// how many cells(<dimX*dimY)
	// cell coordinates
	private Point[] vertexes;
	// pictures' overlap 
	// "position"  BACK of FORE (from MainCharacter)
	// "rank"      overlap in each position (DOWN 0 ... 9 UP)
	private Integer[] position,
					rank;
	// false(1) -> MainChara cannot move in this direction  
	private boolean[][] passages;
	
	public PictureInfo(String serialCode, BufferedImage picture,Point dim,Point[] vertexes,
							Integer[] position,Integer[] rank,boolean[][] passages){
		this.serialCode = serialCode;
		this.picture = picture;
		width = picture.getWidth();
		height = picture.getHeight();
		
		this.dim = dim;
		this.vertexes = vertexes;
		cellNum = vertexes.length;
		
		cellW = width/dim.x;
		cellH = height/dim.y;
		
		this.position = position;
		this.rank = rank;
		this.passages = passages;
	}
	
//get methods	
	public BufferedImage getPicture(){
		return picture;
	}
	public Point2D.Double getP1(int cellIndex){
		if(cellIndex>=cellNum) return null;
		int cellX = vertexes[cellIndex].x;
		int cellY = vertexes[cellIndex].y;
		return new Point2D.Double(cellW*cellX,cellH*cellY);
	}
	public Point2D.Double getP2(int cellIndex){
		if(cellIndex>=cellNum || cellIndex<0) return null;
		int cellX = vertexes[cellIndex].x;
		int cellY = vertexes[cellIndex].y;
		return new Point2D.Double(cellW*(cellX+1),cellH*(cellY+1));
	}
	public Point2D.Double getSquareSize(double side){
		double cellSide;
		if(dim.x>dim.y){
			cellSide = side/dim.x;
		}else{
			cellSide = side/dim.y;
		}
		return new Point2D.Double(cellSide*dim.x,cellSide*dim.y);
	}
	public Point getVertex(int cellIndex){
		if(cellIndex>=cellNum || cellIndex<0) return null;
		return vertexes[cellIndex];
	}
	public Point getDimension(){
		return dim;
	}
	public int getCellNum(){
		return cellNum;
	}
	public String getSerialCode(){
		return serialCode;
	}
	public boolean getPassage(int cellIndex,int direction){
		if(cellIndex>=cellNum || cellIndex<0 || direction>3 || direction<0) return true;
		return passages[cellIndex][direction];
	}
	public int getPosition(int cellIndex){
		return position[cellIndex];
	}
	public int getRank(int cellIndex){
		return rank[cellIndex];
	}
	public Point getLink(int cellIndex){
		Point curVer = vertexes[cellIndex];
		if(cellIndex<0) return null;
		else if(cellIndex < cellNum){
			Point nextVer = vertexes[(cellIndex+1)%cellNum];
			return new Point(nextVer.x-curVer.x,nextVer.y-curVer.y);
		}else{
			return null;
		}
			
	}
	
//set methods	
	private int vertexToIndex(Point vertex){
		int index = 0;
		while(index<cellNum && !vertexes[index].equals(vertex)) index ++;
		if(index == cellNum) return -1;
		return index;
	}
}
