package ���߻���;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import Generator.ServerEnemy;



/**
 * �ڽ��� ĳ���Ͱ� ���� �� Space�� ������ 4�������� ���ư��� �Ѿ˿� ���� ������ ����ִ� Ŭ�����̴�.
 * 
 * */
public class Bullet {
	public int x, y, direction; // x,y�� �Ѿ��� ���� ��ǥ. direction�� �Ѿ��� ���� {1:����   2:������   3:��   4:�Ʒ�} 
	ArrayList<Bullet> arr = new ArrayList<Bullet>(); // �ڱ��ڽſ� ���� �迭�� ���� ����ؼ� �����Ǵ� �Ѿ��� ������ ��´�.
	Game game; // ���ӿ� ���� ������ ����
	Image weapon = new ImageIcon("Img/bullet.png").getImage(); // �Ѿ��� �̹����� �ε��Ѵ�.
	
	public Bullet(Game g){// Bullet ������. ���� ��  �ϳ��� Ŭ������ �����ϰ� �������� �迭�� ����Ǵ� ���.
		game = g;
	}
	
	public Bullet(int x, int y, int a){ // Bullet ������.
		this.x = x;
		this.y = y;	
		this.direction = a;
	}
	
	void create(int x,int y){ // �Ѿ��� ������ �迭�� �����Ѵ�.
		for(int i=1;i<=4;i++){
			Bullet bl = new Bullet(x,y,i);
			arr.add(bl);
	//		this.sendBullet(bl);
		}	
	}
	
	public void move(){ // �Ѿ��� 4�������� �̵��ϵ��� �Ѵ�.
		for(int i=0;i<arr.size();i++){
			Bullet bl = (Bullet)(arr.get(i));
			if(bl.direction == 1){
				bl.y -= 6;
			}else if(bl.direction == 2){
				bl.y += 6;
			}else if(bl.direction == 3){
				bl.x -= 6;
			}else if(bl.direction == 4){
				bl.x += 6;
			}
		}
	}
	
	public void DrawImg(){ // �Ѿ��� �׸��� �׷��ִ� �Լ�
		for(int i=0;i<arr.size();i++){
			Bullet bl = (Bullet) (arr.get(i));
			game.gc.drawImage(weapon, bl.x,bl.y,game);
		}
	}
	
	public boolean isDie(){
		for(Bullet b: this.arr){
			int dis1 = (int) Math.pow((game.player.p.x + 10) - (b.x + 10), 2);
			int dis2 = (int) Math.pow((game.player.p.y + 15) - (b.y + 15), 2);
			double dist = Math.sqrt(dis1 + dis2); //��ֹ��� �ڱ��ڽ��� �Ÿ��� ����Ѵ�.
			if(dist<11){
				return true;
			}
		}
		return false;
	}
	
	public int killEnemy(ArrayList<ServerEnemy> enemy){
		for(Bullet b: this.arr){
			for(int i = 0;i<enemy.size();i++){
				ServerEnemy e = enemy.get(i);
				int dis1 = (int) Math.pow((b.x + 10) - (e.getX() + 10), 2);
				int dis2 = (int) Math.pow((b.y + 15) - (e.getY() + 15), 2);
				double dist = Math.sqrt(dis1 + dis2); //��ֹ��� �ڱ��ڽ��� �Ÿ��� ����Ѵ�.
				if(dist<11){
					System.out.println("Enemy kill");
					return i;
				}
			}
		}
		return 999;
	}	
}
