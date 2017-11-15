package 빵야빵야;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

/**
 * 
 * 게임 실행에 사용되는 UI나 기타 이미지들을 불러오는 클래스.
 *
 */
public class GUI {

	static Image dbImage; // 빈공간 이미지
    static Image stdimg3 = new ImageIcon("Img/egg.png").getImage();
	static Image background = new ImageIcon("Img/배경.jpg").getImage();
	static Image level1 = new ImageIcon("Img/초급.png").getImage();

	static Image enemy = Toolkit.getDefaultToolkit().createImage("Img/enemy.gif"); // 장애물의 이미지 정보를
	static Image nomal = Toolkit.getDefaultToolkit().createImage("Img/normal.gif");
	static Image rare = Toolkit.getDefaultToolkit().createImage("Img/rare.gif");
	static Image unique = Toolkit.getDefaultToolkit().createImage("Img/unique.gif");
	static Image regend = Toolkit.getDefaultToolkit().createImage("Img/regend.gif");
	static Image buffimg = null; // 더블버퍼링을 사용하기위한 버퍼이미지를 정의한다
	static Image stdimg = Toolkit.getDefaultToolkit().createImage("Img/move_egg.gif");
	static Image egg = Toolkit.getDefaultToolkit().createImage("Img/move_egg.gif");
	static Image title = new ImageIcon("Img/title.png").getImage();
	static Image food = new ImageIcon("Img/food.png").getImage();
}
