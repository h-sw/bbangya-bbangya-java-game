package 빵야빵야;

import java.awt.Image;
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
	private static final long serialVersionUID = 1L;
	Point p;
	int point; 							// 아이템을 먹은 갯수
	int score;							// 경험치 양
	int total;							// 점수
	int rank; 							// 점수
	int boost;							// 부스트 게이지
	int state = 0;
	int hp = 1;
	boolean isBullet = false;
	public boolean die = false;
	boolean win = false;
	boolean isHit = false;
	public boolean isAdult = false;
	
	Exp exp;
	Game game;
	Socket socket;
	ObjectOutputStream outputStream;
	ObjectInputStream inputStream;
	String id = makeID();
	
	
	int [] initPlayer(){
		Random rand = new Random();
		int x_rand = (rand.nextInt() % 1000);
		x_rand = Math.abs(x_rand);
		int y_rand = (rand.nextInt() % 750);
		y_rand = Math.abs(y_rand);
		int[] res = new int[2];
		res[0] = x_rand;
		res[1] = y_rand;
		return res;
	}
	
	Player(Game g, Socket s){
		game = g; 
		init();
		int point[] = initPlayer();
		p = new Point(point[0],point[1]);
		exp = new Exp(5);
		socket = s;
		outputStream = game.getOutputStream();
	}
	
	public void init(){
		hp = 1; 
		point = 0; 
		score = 0; 
		total = 0; 
		rank = 0; 
		boost = 3;
	}
	
	public void MovestdImage(Image dbImage, int x, int y, int width, int height) {/*자기 자신의 이미지를 그려주는 함수*/
		// 첫번째 진화.
		if (total == 50 && exp.isFull()) {
			isAdult = true;
			game.engine.addHP();
			dbImage = GUI.nomal;
			GUI.stdimg = dbImage;
			score = 0; // 진화율 초기화
			exp.clear(); // 스택 초기화
			state= 1;
		}
		// 두번째 진화
		if (total == 100 && exp.isFull()) {
			game.engine.addHP();
			dbImage = GUI.rare;
			GUI.stdimg = dbImage;
			score = 0;
			exp.clear();
			state = 2;
		}
		// 세번째 진화
		if (total == 150 && exp.isFull()) {
			game.engine.addHP();
			dbImage = GUI.unique;
			GUI.stdimg = dbImage;
			score = 0;
			exp.clear();
			state = 3;
		}
		
		if (total > 150 && exp.isFull()) {
			game.engine.addHP();
			score = 0;
			exp.clear();
		}
		game.gui.drawPlayer(width,height);

	}
	public void sendXY(){
		try {		
//			System.out.println("write packet");
			RequestPacket packet = new RequestPacket();
			packet.setClassName("Player");
			packet.setMethodName("sendXY");
			packet.setSyncType(SYNC_TYPE.SYNCHRONOUS);
			packet.setArgs(new Object[] { id,p.x, p.y,state,isBullet,game.killer,die});
//			System.out.println("sending..");
			isBullet = false;
			outputStream.writeObject(packet);
			outputStream.flush();
			game.engine.Reciever();
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
