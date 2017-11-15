package 빵야빵야;

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
 * 플레이어의 정보를 담고 있는 클래스.
 *
 */
public class Player implements Serializable{
	Point p;
	int point; 							// 아이템을 먹은 갯수
	int score;							// 경험치 양
	int total;							// 점수
	int rank; 							// 점수
	int boost;							// 부스트 게이지
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
	
	public void MovestdImage(Image dbImage, int x, int y, int width, int height) {/*자기 자신의 이미지를 그려주는 함수*/
		// 첫번째 진화.
		if (total == 100 && exp.isFull()) {
			isAdult = true;
			hp += 2;
			dbImage = GUI.nomal;
			GUI.stdimg = dbImage;
			score = 0; // 진화율 초기화
			exp.clear(); // 스택 초기화
			state= 1;
		}
		// 두번째 진화
		if (total == 200 && exp.isFull()) {
			hp +=2;
			dbImage = GUI.rare;
			GUI.stdimg = dbImage;
			score = 0;
			exp.clear();
			state = 2;
		}
		// 세번째 진화
		if (total == 300 && exp.isFull()) {
			hp +=2;
			dbImage = GUI.unique;
			GUI.stdimg = dbImage;
			score = 0;
			exp.clear();
			state = 3;
		}
		// 네번째 진화
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
		game.gc.setClip(p.x, p.y, width, height);      // 캐릭터의이미지의 좌표와 크기를 받아온다
		game.gc.drawImage(GUI.stdimg, p.x, p.y, game); // 캐릭터를 좌표에 따라 장소를 바꾸어 그린다.
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
	
	public void DrawImg() {	/*배열에 저장된 음식을 화면에 그려준다.*/
		game.gc.drawImage(GUI.stdimg, p.x, p.y, game);
	}


}
