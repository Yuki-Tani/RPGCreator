package system;

import java.io.File;
import mainUI.MainFrame;


public class RPGCreator{
	
	private static PictureBox pictureBox;
	private static MapBox mapBox;
	private static MainFrame main;

// ver0.1 時点　ver1.1に向けて　変更版にバグあり
// PictureAddress で管理しているが、オブジェクト比較でget等しているためgetできず
// 管理形式を変えるかそれ以外の処置が必須
	
	public static void main(String arg[]){
		//String path = RPGCreater.class.getResource("res").toString();
		File picFol = new File("src/system/res/pictures/");//jar変換時書き換え
		System.out.println("pictures existance: " + picFol.isDirectory());
		
		pictureBox = new PictureBox(picFol);
		mapBox = new MapBox();
		main = new MainFrame("RPG-Creator");

	}
	
	public static PictureBox getPictureBox(){
		return pictureBox;
	}
	public static MapBox getMapBox(){
		return mapBox;
	}
	public static MainFrame getMainFrame(){
		return main;
	}
	
}