package system;

import java.awt.Point;
import java.util.HashMap;

import system.tool.PicAddress;

public class MapInfo {
	
	private String name;
	private int w,h;
	private MapCellInfo[][] map;
	private HashMap<Point,MapCellInfo> trash;
	/* 
	 * 
	 */
	public MapInfo(String name,int w,int h){
		this.name = name;
		this.w = w;
		this.h = h;
		map = new MapCellInfo[w][h];
		trash = new HashMap<Point,MapCellInfo>();
		
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
					map[i][j] = new MapCellInfo();
			}
		}
	}
//set methods
	public void changeName(String newName){
		name = newName;
	}
	public void setMapPic(Point p,PictureInfo pic){
		int x = p.x;
		int y = p.y;
		if(pic == null){//remove
			PicAddress picAdd = getMapCellInfo(x,y).getTopPicAdd();
			if(picAdd == null) return;
			Point link;
			int count = 0;
			while(getMapCellInfo(x,y).contains(picAdd)){
				link = getMapCellInfo(x,y).removePicAdd(picAdd);
				count ++;
				if(link==null) break;
				x += link.x;
				y += link.y;
				picAdd.translate(1);
			}
			picAdd.moveZero();
			while(getMapCellInfo(x,y).contains(picAdd) && count <2){
				link = getMapCellInfo(x,y).removePicAdd(picAdd);
				if(link == null) break;
				x += link.x;
				y += link.y;
				picAdd.translate(1);
			}
		}else{//set
			String serialCode = pic.getSerialCode();
			for(int i=0;i<pic.getCellNum();i++){
				getMapCellInfo(x,y).setPicAdd(pic.getPosition(i),pic.getRank(i),
						new PicAddress(serialCode,i), pic.getLink(i));
				x += pic.getLink(i).x;
				y += pic.getLink(i).y;
			}	
		}
	}
	public void setMapGround(PictureInfo pic){
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				setMapPic(new Point(i,j),pic);
			}
		}
	}
//get methods	
	public MapCellInfo[][] getMapInfo(){
		return map;
	}
	public MapCellInfo getMapCellInfo(Point cellP){
		return getMapCellInfo(cellP.x,cellP.y);
	}
	public MapCellInfo getMapCellInfo(int x,int y){
		if(0 <= x && x < w && 0 <= y && y < h) return map[x][y];
		else{
			Point p = new Point(x,y);
			if(trash.containsKey(p)) return trash.get(p);
			else{
				trash.put(p,new MapCellInfo());
				return trash.get(p);
			}
		}
	}
	public String getName(){
		return name;
	}
	public int getW(){
		return w;
	}
	public int getH(){
		return h;
	}
}
