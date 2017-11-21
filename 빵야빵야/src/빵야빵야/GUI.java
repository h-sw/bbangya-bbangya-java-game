package ���߻���;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import GameController.ServerEnemy;
import GameController.ServerFood;

/**
 * 
 * ���� ���࿡ ���Ǵ� UI�� ��Ÿ �̹������� �ҷ����� Ŭ����.
 *
 */
public class GUI {
	private Game game;
	GUI(Game g){
		game = g;
	}

	static Image dbImage; // ����� �̹���
    static Image stdimg3 = new ImageIcon("Img/egg.png").getImage();
	static Image background = new ImageIcon("Img/���.jpg").getImage();
	static Image level1 = new ImageIcon("Img/�ʱ�.png").getImage();

	
	static Image enemy1 = Toolkit.getDefaultToolkit().createImage("Img/enemy.gif");
	static Image enemy2 = Toolkit.getDefaultToolkit().createImage("Img/enemy2.gif");
	
	static Image nomal = Toolkit.getDefaultToolkit().createImage("Img/normal2.gif");
	static Image rare = Toolkit.getDefaultToolkit().createImage("Img/rare2.gif");
	static Image unique = Toolkit.getDefaultToolkit().createImage("Img/unique2.gif");

	static Image nomal_enemy = Toolkit.getDefaultToolkit().createImage("Img/normal.gif");
	static Image rare_enemy = Toolkit.getDefaultToolkit().createImage("Img/rare.gif");
	static Image unique_enemy = Toolkit.getDefaultToolkit().createImage("Img/unique.gif");

	static Image buffimg = null; // ������۸��� ����ϱ����� �����̹����� �����Ѵ�
	static Image stdimg = Toolkit.getDefaultToolkit().createImage("Img/move_egg.gif");

	static Image egg_enemy = Toolkit.getDefaultToolkit().createImage("Img/move_egg2.gif");
	static Image weapon = new ImageIcon("Img/bullet1.png").getImage();
	static Image title = new ImageIcon("Img/title.png").getImage();
	static Image food = new ImageIcon("Img/food.png").getImage();
	static Image info = new ImageIcon("Img/info.png").getImage();

	public void backgroundDrawImg() {
		game.gc.drawImage(GUI.background, 0, 0, game); // ������ ����̹��������� 0,0�� ��ġ��Ų��
	}
	
	public void setPannel() { /* ��带 �����ϴ� �Լ� */
		if (game.mode == 0) {
			if (game.control.et == true) {
				game.mode=1;
			}
		}
		if (game.control.p == true) { // p�� ������ ������ ����ϴ�
			game.pause = !game.pause;
			game.control.p = false;
		}
	}
	
	public void drawEXP(){
	    Graphics2D g2d = (Graphics2D)game.gc;
	    int width = 30;
	    
		g2d.setColor(Color.GRAY);
		g2d.setStroke(new BasicStroke(width));
		game.gc.drawLine(0, 743, 1024 ,743);
	        
	    g2d.setColor(Color.ORANGE);
	    g2d.setStroke(new BasicStroke(width));
		game.gc.drawLine(0, 743, 102*(game.player.score/5), 743);
		
		g2d.setColor(Color.WHITE);
		game.gc.drawString("EXP", 20, 730);
	}
	
	public void drawHP(){
		Graphics2D g2d = (Graphics2D)game.gc;
		int width = 30;
		
		width = 30;
		g2d.setColor(Color.GRAY);
		g2d.setStroke(new BasicStroke(width));
		game.gc.drawLine(0, 47, 300 ,47);
		
		g2d.setColor(Color.RED);
		g2d.setStroke(new BasicStroke(width));
		game.gc.drawLine(0, 47, game.player.hp*10 ,47);
		
		g2d.setColor(Color.WHITE);
		game.gc.drawString("HP", 20, 54);
	}
	
	public void drawMagazine(){
		Graphics2D g2d = (Graphics2D)game.gc;
		int width;
		width = 30;
		g2d.setColor(Color.GRAY);
		g2d.setStroke(new BasicStroke(width));
		game.gc.drawLine(724, 47, 1000 ,47);
		
		width = 30;
		g2d.setColor(Color.BLUE);
		g2d.setStroke(new BasicStroke(width));
		game.gc.drawLine(724, 47, 724+(5*game.player.boost) ,47);
		
		g2d.setColor(Color.WHITE);
		game.gc.drawString("Bullet", 744, 54);
	}
	
	public void UIDrawImg() { /* UI�� �׷��ִ� �Լ� */	
		game.gc.setFont(new Font("Default", Font.ROMAN_BASELINE, 20)); // ��Ʈ�Ǹ���ũ�⸦���Ѵ�
		drawHP();
		drawEXP();
		drawMagazine();	
		if (game.pause == true) {
			game.gc.drawImage(GUI.info, (Game.width-info.getWidth(game))/2, (Game.height-info.getHeight(game))/2, game);
		}	
		game.player.MovestdImage(GUI.stdimg, game.player.p.x, game.player.p.y, GUI.stdimg.getWidth(game), GUI.stdimg.getHeight(game));
	}
	
	public void drawLoading(){
		game.setBackground(Color.WHITE);	
		game.gc.setFont(new Font("�ü�", Font.ROMAN_BASELINE, 100));
		String s = "���߻���";
		game.gc.drawString(s, (Game.width - s.length() * 100) / 2, 230);
		game.gc.setFont(new Font("Default", Font.PLAIN, 20));
		game.gc.drawLine(300, 300, 724, 300);
		s = "Press Enter";
		game.gc.drawString(s, (Game.width - s.length() * 10) / 2, 330);
		s = "My ID: "+game.player.id;
		game.gc.drawString(s, (Game.width - s.length() * 10) / 2, 360);
		s = "����Ű�� �����ø� ������";
		game.gc.drawString(s, (Game.width - s.length() * 20) / 2, 390);
		s = "���ӵ˴ϴ�.";
		game.gc.drawString(s, (Game.width - s.length() * 20) / 2, 410);
		game.gc.drawLine(300, 430, 724, 430);
		s = "�̵� : ����Ű";
		game.gc.drawString(s, (Game.width - s.length() * 20) / 2, 475);
		s = "�Ѿ˹߻� : Space";
		game.gc.drawString(s, (Game.width - s.length() * 15) / 2, 510);
	}
	
	public void drawEnemy(){
		for(int i=0;i<game.enemy.size();i++){
			ServerEnemy en = (ServerEnemy) (game.enemy.get(i));
			if(en.getKind()==1){
				game.gc.drawImage(GUI.enemy1, en.getX(), en.getY(), game);
			}else{
				game.gc.drawImage(GUI.enemy2, en.getX(), en.getY(), game);
			}
			
		}
	}
	
	public void drawFood(){
		for(int i=0;i<game.food.size();i++){
			ServerFood f = (ServerFood) (game.food.get(i));
			game.gc.drawImage(GUI.food, f.getX(), f.getY(), game);
		}
	}
	
	public void drawPlayer(int width, int height){
		game.gc.setClip(game.player.p.x, game.player.p.y, width, height);      // ĳ�������̹����� ��ǥ�� ũ�⸦ �޾ƿ´�
		game.gc.drawImage(GUI.stdimg, game.player.p.x, game.player.p.y, game); // ĳ���͸� ��ǥ�� ���� ��Ҹ� �ٲپ� �׸���.
		game.gc.drawString("Me", game.player.p.x-10, game.player.p.y-50);
	}
	
	public void drawOther(){
		for(OtherPlayer o : game.other){
			o.drawing();
		}
	}
	
	public void drawBullet(Bullet b){ // �Ѿ��� �׸��� �׷��ִ� �Լ�
		for(int i=0;i<b.arr.size();i++){
			Bullet bl = (Bullet) (b.arr.get(i));
			game.gc.drawImage(GUI.weapon, bl.x,bl.y,game);
		}
	}
}
