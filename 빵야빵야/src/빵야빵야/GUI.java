package ���߻���;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

/**
 * 
 * ���� ���࿡ ���Ǵ� UI�� ��Ÿ �̹������� �ҷ����� Ŭ����.
 *
 */
public class GUI {

	static Image dbImage; // ����� �̹���
    static Image stdimg3 = new ImageIcon("Img/egg.png").getImage();
	static Image background = new ImageIcon("Img/���.jpg").getImage();
	static Image level1 = new ImageIcon("Img/�ʱ�.png").getImage();

	static Image enemy = Toolkit.getDefaultToolkit().createImage("Img/enemy.gif"); // ��ֹ��� �̹��� ������
	static Image nomal = Toolkit.getDefaultToolkit().createImage("Img/normal.gif");
	static Image rare = Toolkit.getDefaultToolkit().createImage("Img/rare.gif");
	static Image unique = Toolkit.getDefaultToolkit().createImage("Img/unique.gif");
	static Image regend = Toolkit.getDefaultToolkit().createImage("Img/regend.gif");
	static Image buffimg = null; // ������۸��� ����ϱ����� �����̹����� �����Ѵ�
	static Image stdimg = Toolkit.getDefaultToolkit().createImage("Img/move_egg.gif");
	static Image egg = Toolkit.getDefaultToolkit().createImage("Img/move_egg.gif");
	static Image title = new ImageIcon("Img/title.png").getImage();
	static Image food = new ImageIcon("Img/food.png").getImage();
}
