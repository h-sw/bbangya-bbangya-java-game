package GameController;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class ServerBullet implements Serializable{
	protected static final long serialVersionUID = 1L;
	public int x, y, direction; // x,y는 총알의 시작 좌표. direction은 총알의 방향 {1:왼쪽   2:오른쪽   3:위   4:아래} 
	ArrayList<ServerBullet> arr = new ArrayList<ServerBullet>(); // 자기자신에 대한 배열을 가져 계속해서 생성되는 총알의 정보를 담는다.
	Image weapon = new ImageIcon("Img/bullet.png").getImage(); // 총알의 이미지를 로딩한다.

	public ServerBullet(int x, int y, int a){ // Bullet 생성자.
		this.x = x;
		this.y = y;	
		this.direction = a;
	}
	
	void create(int x,int y){ // 총알을 생성해 배열에 저장한다.
		for(int i=1;i<=4;i++){
			ServerBullet bl = new ServerBullet(x,y,i);
			arr.add(bl);
	//		this.sendBullet(bl);
		}	
	}
	
	public void move(){ // 총알을 4방향으로 이동하도록 한다.
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
