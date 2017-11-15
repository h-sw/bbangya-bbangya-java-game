package 빵야빵야;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import GameController.ServerPlayer;
import Generator.ServerEnemy;
import Generator.ServerFood;
import common.RequestPacket;
import common.RequestPacket.SYNC_TYPE;

/**
 * 
 * 전체적인 게임을 실행하기 위한 클래스.
 *
 */
public class Game extends JFrame implements Runnable, KeyListener {

	final static public int width = 1000;
	final static public int height = 750;

	ArrayList<ServerFood> food = new ArrayList<ServerFood>(); // 음식
	ArrayList<ServerEnemy> enemy = new ArrayList<ServerEnemy>(); // 장애물
	Engine engine; // 게임 엔진
	Bullet bullet; // 총알에 대한 정보
	Bullet otherBullet;
	int x = 400, y = 300; // 캐릭터의 시작 위치, 그리고 앞으로의 좌표를 받아오기 위한 변수
	int cnt = 0; // 쓰레드의 루프를 세는 변수, 각종 변수를 통제하기 위해 사용된다
	Player player; // 유저
	OtherPlayer other;
	int mode = 0; // 메인화면 변수
	int dmappear = 0; // 장애물 생성주기변수
	int selectmode = 1; // 난이도 선택 변수
	boolean pause = false; // 일시정지
	Control control; // 게임 컨트롤러
	Graphics gc; // 게임 그래픽
	Socket socket;
	Vector<ServerPlayer> playerOther;
	ObjectOutputStream outputStream;
	ObjectInputStream inputStream;
	String id;
	int killer = 999;

	//public MainFrame frame = new MainFrame();
	public Game(Socket s) {
		setTitle("빵야빵야!"); // 빵야빵야 타이틀
		setSize(width, height); // 창의 크기 1920X1080 의 크기(FullHD 기준)
		setLocation(0, 0); // 창의 위치를 조절 왼쪽 위에 고정.
		setResizable(false); // 사이즈를 조절할 수 없게 만듬
		setVisible(true); // 프레임을 보이게 만듬
		setBackground(Color.WHITE); // Background의 기본 컬러를 흰색으로 설정
		//frame.MainFrame();
		socket = s;

		try {
			inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		player = new Player(this, socket);
		
		other = null;
		control = new Control(this); // 컨트롤러를 사용
		engine = new Engine(this); // Engine을 사용
		bullet = new Bullet(this); // Bullet 을 사용
		otherBullet = new Bullet(this);
		other = new OtherPlayer(this, socket);
		start(); // 쓰레드의 루프를 시작하기 위한 메소드. 게임시작.
		this.addKeyListener(this); // Key Listener를 추가하여 방향키 정보를 받아올 수 있게 한다.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});
	}

	public void start() {
		Thread th = new Thread(this); // 쓰레드를 정의
		th.start(); // 쓰레드의 루프를 시작시킨다
	}

	public void run() {
		while (player.hp != 0) { // life가 0이 아니게되면 루프가 끝난다
			try {
				setPannel();
				control.arrowkey(); // 받은 키에 따른 캐릭터의 이동을 통제
				repaint(); // 리페인트함수(그림을 그때그때 새로기리기위함)
				Thread.sleep(20); // 20밀리세컨드당 한번의 루프가 돌아간다

				if (pause == false) {
					if (other == null) {

					}
					if (mode != 0) {
						if (control.r == true) {
							reset(); // r을 누를시 리셋됩니다.
						}
						killer = bullet.killEnemy(enemy);
						player.sendXY();
//						enemy.moveImg(); // 장애물 추가, 움직이게 함
//						food.move(); // 음식 추가메소드
						bullet.move();
						otherBullet.move();
						if(otherBullet.isDie()){
							player.hp --;
						}
						if(player.hp == 0){
							JOptionPane.showMessageDialog(null, "당신은 패배하였습니다.");
							dispose(); // 게임프레임은 닫습니다
							break;
						}
						boostcontrol(); // 총알 메소드.
						cnt++; // 루프가 돌아간 횟수
					}
				}
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
	}
	void Reciever(){
		Object obj;
		try {
			obj = inputStream.readObject();
			RequestPacket requestPacket = (RequestPacket) obj;
			Object[] arg = requestPacket.getArgs();
			
			enemy = (ArrayList<ServerEnemy>)arg[1];
			food = (ArrayList<ServerFood>)arg[2];
			
			if (requestPacket.getMethodName().equals("sendPlayer")) {
				playerOther = (Vector<ServerPlayer>) arg[0];
				for (ServerPlayer p : playerOther) {
					if (player.id.equals(p.getID())) {
						if(p.getIsEat()){
							player.score += 10;
							player.point++;
							player.total += 10;
							player.rank += 10;
							player.exp.push(10);
						}
						if(p.getIsHit()){
							System.out.println("is hit!");
							player.hp--;
						}
					} else {
						other.x = p.getX();
						other.y = p.getY();
						other.state = p.getState();
						if (p.getIsBullet() == true) {
							System.out.println("other is shooting!");
							otherBullet.create(other.x, other.y);
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void drawEnemy(){
		for(int i=0;i<enemy.size();i++){
			ServerEnemy en = (ServerEnemy) (enemy.get(i));
			gc.drawImage(GUI.enemy, en.getX(), en.getY(), this);
		}
	}
	
	public void drawFood(){
		for(int i=0;i<food.size();i++){
			ServerFood f = (ServerFood) (food.get(i));
			gc.drawImage(GUI.food, f.getX(), f.getY(), this);
		}
	}
	public void update(Graphics g) { /* 프레임 내의 버퍼이미지를 이용하여 깜빡임 현상을 완전히 없앱니다. */
		paint(g);
	}

	public void paint(Graphics g) { /* 각종 이미지를 그리기위한 메서드를 실행시킨다 */
		GUI.buffimg = createImage(width, height); // 버퍼이미지를 그린다 (떠블버퍼링을 사용하여 화면의
													// 깜빡임을 없앤다)
		gc = GUI.buffimg.getGraphics(); // 버퍼이미지에 대한 그래픽 객채를 얻어온다.
		drawimages(g);
	}

	public void drawimages(Graphics g) { /* 게임의 전체적인 이미지를 그립니다. */

		if (mode == 0) { // 게임 난이도에 따라서 이미지를 나타냅니다.

			setBackground(Color.WHITE);
			gc.setFont(new Font("궁서", Font.ROMAN_BASELINE, 100));
			String s = "빵야빵야";
			gc.drawString(s, (width - s.length() * 100) / 2, 230);
			gc.setFont(new Font("Default", Font.PLAIN, 20));
			gc.drawLine(300, 300, 724, 300);
			s = "Press Enter";
			gc.drawString(s, (width - s.length() * 10) / 2, 330);
			s = "My ID: "+player.id;
			gc.drawString(s, (width - s.length() * 10) / 2, 360);
			s = "엔터키를 누르시면 서버에";
			gc.drawString(s, (width - s.length() * 20) / 2, 390);
			s = "접속됩니다.";
			gc.drawString(s, (width - s.length() * 20) / 2, 410);
			gc.drawLine(300, 430, 724, 430);
			s = "이동 : 방향키";
			gc.drawString(s, (width - s.length() * 20) / 2, 475);
			s = "총알발사 : Space";
			gc.drawString(s, (width - s.length() * 15) / 2, 510);
			// gc.drawString("Press Enter", , 250);
			if (selectmode == 1) {
				gc.drawImage(GUI.level1, 0, 10, this);
			}
			g.drawImage(GUI.buffimg, 0, 0, this);
		}
		if (mode != 0) {
			backgroundDrawImg(); // 배경을 불러온다.
//			enemy.drawing(); // 장애물을 그린다.
			drawEnemy();
			drawFood(); // 음식을 그린다.
			bullet.DrawImg(); // 총알을 그린다.
			other.drawing();
			otherBullet.DrawImg();
			UIDrawImg(); // 전체 UI를 그린다 .
			g.drawImage(GUI.buffimg, 0, 0, this); // 버퍼이미지를 그린다. 0,0으로 좌표를
													// 맞춰서프레임크기에 딱맞춘다
		}

	}

	public void backgroundDrawImg() {
		gc.drawImage(GUI.background, 0, 0, this); // 가져온 배경이미지파일을 0,0에 위치시킨다
	}

	public void UIDrawImg() { /* UI를 그려주는 함수 */
		gc.setFont(new Font("Default", Font.ROMAN_BASELINE, 20)); // 폰트의모양과크기를정한다
		gc.drawString("버틴시간:" + Integer.toString(cnt / 50), 10, 55); // 루프속도의1/40만큼시간이흘러감
		gc.drawString("먹은 아이템 :" + Integer.toString(player.point), 10, 80); // 먹은아이템의갯수를나타낸다
		gc.drawString("남은 HP:" + Integer.toString(player.hp), 10, 155);
		gc.drawString("진화 경험치: " + Integer.toString(player.score) + "%", 400, 728);
		gc.drawString("총 점수: " + Integer.toString(player.rank + cnt / 50), 10, 105);
		String s = "이동 : 방향키           총알발사:Space ";
		gc.drawString(s, (width) / 2, 55);

		if (mode == 1 || mode == 2) {
			gc.drawString("총알 갯수 :" + Integer.toString(player.boost), 10, 130);
		}

		if (pause == true) {
			gc.drawString("PAUSE", 340, 250); // 일시정지 상태에서 PAUSE를 나타냅니다
		}
		player.MovestdImage(GUI.stdimg, player.p.x, player.p.y, GUI.stdimg.getWidth(this), GUI.stdimg.getHeight(this));
	}

	public void reset() { /* 게임 시작 전 초기화 함수 */
//		enemy.arr.removeAll(enemy.arr);
//		food.arr.removeAll(food.arr);
		bullet.arr.removeAll(bullet.arr);
		player.point = 0;
		mode = 0;
		cnt = 0;
		player.exp.clear();
		x = 400;
		y = 300;
	}

	public void setPannel() { /* 모드를 설정하는 함수 */
		if (mode == 0) {
			if (control.et == true) {
				mode = selectmode; // 엔터를 누르면 해당모드를 적용시킨 후 게임이 시작됩니다
			}
			if (mode == 1) {
				dmappear = 700; // 장애물 생성 주기
			}
		}
		if (control.p == true) { // p를 누르면 게임이 멈춥니다
			pause = !pause;
			control.p = false;
		}
	}

	public void boostcontrol() { /* 총알을 충전하기 위한 메소드 */
		if (control.space == false) {
			if (player.boost < 40) {
				if (cnt % 100 == 0) {
					player.boost++; // cnt%100 마다 총알 충전
				}
			}
		} else {
			control.space = false;
		}
	}

	private boolean isReady = false;

	public void keyPressed(KeyEvent e) { /* 키를 누르면. */
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			int a = 0;
			while (a < 3) {
				a++;
			}
			control.keyLeft = true;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			control.keyRight = true;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			control.keyUp = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			control.keyDown = true;
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			isReady = true;
			control.space = true;
		} else if (e.getKeyCode() == KeyEvent.VK_P) {
			control.p = true;
		} else if (e.getKeyCode() == KeyEvent.VK_R) {
			control.r = true;
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			control.et = true;
		}
	}

	private void sleep(int i) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent e) { /* 키를 떼었을 때 */
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			control.keyLeft = false;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			control.keyRight = false;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			control.keyUp = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			control.keyDown = false;
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (isReady && player.isAdult) {
				if (player.boost > 0) {
					player.isBullet = true;
					bullet.create(player.p.x, player.p.y);
					player.boost--; 
					control.space = false;
				} else {
					control.space = false;
				}
			}

		} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			control.shift = false;
		} else if (e.getKeyCode() == KeyEvent.VK_P) {
			control.p = false;
		} else if (e.getKeyCode() == KeyEvent.VK_R) {
			control.r = false;
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			control.et = false;
		}
	}

	public void keyTyped(KeyEvent e) { // 키타입 메서드 , 이소스에서쓰진않지만 keylistener를
										// 상속받으면 자동으로생기는거라 놔둠
	}

	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}
}
