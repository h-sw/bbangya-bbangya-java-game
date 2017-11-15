package GameController;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class ServerBullet implements Serializable{
	protected static final long serialVersionUID = 1L;
	public int x, y, direction; // x,y�� �Ѿ��� ���� ��ǥ. direction�� �Ѿ��� ���� {1:����   2:������   3:��   4:�Ʒ�} 
	ArrayList<ServerBullet> arr = new ArrayList<ServerBullet>(); // �ڱ��ڽſ� ���� �迭�� ���� ����ؼ� �����Ǵ� �Ѿ��� ������ ��´�.
	Image weapon = new ImageIcon("Img/bullet.png").getImage(); // �Ѿ��� �̹����� �ε��Ѵ�.

	public ServerBullet(int x, int y, int a){ // Bullet ������.
		this.x = x;
		this.y = y;	
		this.direction = a;
	}
	
	void create(int x,int y){ // �Ѿ��� ������ �迭�� �����Ѵ�.
		for(int i=1;i<=4;i++){
			ServerBullet bl = new ServerBullet(x,y,i);
			arr.add(bl);
	//		this.sendBullet(bl);
		}	
	}
	
	public void move(){ // �Ѿ��� 4�������� �̵��ϵ��� �Ѵ�.
		for(int i=0;i<arr.size();i++){
			ServerBullet bl = (ServerBullet)(arr.get(i));
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
}
