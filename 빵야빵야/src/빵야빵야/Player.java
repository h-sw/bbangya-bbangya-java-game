package ���߻���;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Random;

import common.RequestPacket;
import common.RequestPacket.SYNC_TYPE;

/**
 * 
 * �÷��̾��� ������ ��� �ִ� Ŭ����.
 *
 */
public class Player implements Serializable{
	Point p;
	int point; 							// �������� ���� ����
	int score;							// ����ġ ��
	int total;							// ����
	int rank; 							// ����
	int boost;							// �ν�Ʈ ������
	int state = 0;
	int hp = 1;
	boolean isBullet = false;

	Exp exp;
	Game game;
	Socket socket;
	ObjectOutputStream outputStream;
	ObjectInputStream inputStream;
	String id = makeID();
	public boolean isAdult = true;
	
	int [] initPlayer(){
		Random rand = new Random();
		int x_rand = (rand.nextInt() % 750);
		x_rand = Math.abs(x_rand);
		int y_rand = (rand.nextInt() % 500);
		y_rand = Math.abs(y_rand);
		int[] res = new int[2];
		res[0] = x_rand;
		res[1] = y_rand;
		return res;
	}
	
	Player(Game g, Socket s){
		game = g; hp = 1; point = 0; score = 0; total = 0; rank = 0; boost = 5;
		int point[] = initPlayer();
		p = new Point(point[0],point[1]);
		exp = new Exp(10);
		socket = s;
		outputStream = game.getOutputStream();
	}
	
	public void MovestdImage(Image dbImage, int x, int y, int width, int height) {/*�ڱ� �ڽ��� �̹����� �׷��ִ� �Լ�*/
		// ù��° ��ȭ.
		if (total == 100 && exp.isFull()) {
			isAdult = true;
			hp += 2;
			dbImage = GUI.nomal;
			GUI.stdimg = dbImage;
			score = 0; // ��ȭ�� �ʱ�ȭ
			exp.clear(); // ���� �ʱ�ȭ
			state= 1;
		}
		// �ι�° ��ȭ
		if (total == 200 && exp.isFull()) {
			hp +=2;
			dbImage = GUI.rare;
			GUI.stdimg = dbImage;
			score = 0;
			exp.clear();
			state = 2;
		}
		// ����° ��ȭ
		if (total == 300 && exp.isFull()) {
			hp +=2;
			dbImage = GUI.unique;
			GUI.stdimg = dbImage;
			score = 0;
			exp.clear();
			state = 3;
		}
		// �׹�° ��ȭ
		if (total == 400 && exp.isFull()) {
			hp +=2;
			dbImage = GUI.regend;
			GUI.stdimg = dbImage;
			score = 0;
			exp.clear();
			state = 4;
		}
		if (total > 400 && exp.isFull()) {
			hp ++;
			score = 0;
			exp.clear();
		}
		game.gc.setClip(p.x, p.y, width, height);      // ĳ�������̹����� ��ǥ�� ũ�⸦ �޾ƿ´�
		game.gc.drawImage(GUI.stdimg, p.x, p.y, game); // ĳ���͸� ��ǥ�� ���� ��Ҹ� �ٲپ� �׸���.
		game.gc.drawString("Me", p.x-10, p.y-50);
	}
	public void sendXY(){
		try {		
//			System.out.println("write packet");
			RequestPacket packet = new RequestPacket();
			packet.setClassName("Player");
			packet.setMethodName("sendXY");
			packet.setSyncType(SYNC_TYPE.SYNCHRONOUS);
			packet.setArgs(new Object[] { id,p.x, p.y,state,isBullet,game.killer});
//			System.out.println("sending..");
			isBullet = false;
			outputStream.writeObject(packet);
			outputStream.flush();
			game.Reciever();
		} catch (IOException e) {

			e.printStackTrace();
		}	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
	}
	
	public String makeID(){
		Random rand = new Random();
		int id = Math.abs(rand.nextInt()%10000);
		System.out.println("ID:"+id);
		return String.valueOf(id);	
	}
	
	public void DrawImg() {	/*�迭�� ����� ������ ȭ�鿡 �׷��ش�.*/
		game.gc.drawImage(GUI.stdimg, p.x, p.y, game);
	}


}
