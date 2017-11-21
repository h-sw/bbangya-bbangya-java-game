package 빵야빵야;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import GameController.ServerEnemy;
import GameController.ServerFood;
import GameController.ServerPlayer;
/**
 * 전체적인 게임을 실행하기 위한 클래스.
 */
public class Game extends JFrame implements Runnable, KeyListener {
	private static final long serialVersionUID = 1L;
	final static public int width = 1000;
	final static public int height = 750;
	final public int MAX_HP = 20;

	Socket socket;
	Engine engine; // 게임 엔진
	Graphics gc; // 게임 그래픽
	GUI gui;
	Control control; // 게임 컨트롤러
	
	int cnt = 0; // 쓰레드의 루프를 세는 변수, 각종 변수를 통제하기 위해 사용된다
	
	Player player; // 유저
	Bullet bullet; // 총알에 대한 정보
	Bullet otherBullet;	
	CopyOnWriteArrayList<ServerFood> food;
	CopyOnWriteArrayList<ServerEnemy> enemy;
	CopyOnWriteArrayList<OtherPlayer> other;
	Vector<ServerPlayer> playerOther;
	
	ObjectOutputStream outputStream;
	ObjectInputStream inputStream;

	boolean pause = false; // 일시정지
	int mode = 0; // 메인화면 변수
	int killer = 999;

	//public MainFrame frame = new MainFrame();
	public Game(Socket s) {
		socket = s;
		initGraphic();
		initStream();
		initGame();	
		start(); // 쓰레드의 루프를 시작하기 위한 메소드. 게임시작.
	}
	
	public void initGraphic(){
		setTitle("빵야빵야!"); // 빵야빵야 타이틀
		setSize(width, height); // 창의 크기 1920X1080 의 크기(FullHD 기준)
		setLocation(0, 0); // 창의 위치를 조절 왼쪽 위에 고정.
		setResizable(false); // 사이즈를 조절할 수 없게 만듬
		setVisible(true); // 프레임을 보이게 만듬
		setBackground(Color.WHITE); // Background의 기본 컬러를 흰색으로 설정
		
		this.addKeyListener(this); // Key Listener를 추가하여 방향키 정보를 받아올 수 있게 한다.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				player.p.x = -100; player.p.y = -100;
				player.sendXY();
				super.windowClosing(e);
				System.exit(0);
			}
		});
	}
	
	public void initStream(){
		try {
			inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initGame(){
		gui = new GUI(this);
		player = new Player(this, socket);		
		control = new Control(this); // 컨트롤러를 사용
		engine = new Engine(this); // Engine을 사용
		bullet = new Bullet(this); // Bullet 을 사용
		otherBullet = new Bullet(this);
		food = new CopyOnWriteArrayList<ServerFood>();
		enemy = new CopyOnWriteArrayList<ServerEnemy>();
		other = new CopyOnWriteArrayList<OtherPlayer>();
	}

	public void start() {
		Thread th = new Thread(this); // 쓰레드를 정의
		th.start(); // 쓰레드의 루프를 시작시킨다
	}

	public void run() {
		while (player.hp != 0) { // life가 0이 아니게되면 루프가 끝난다
			try {
				gui.setPannel();
				control.arrowkey(); // 받은 키에 따른 캐릭터의 이동을 통제
				repaint(); // 리페인트함수(그림을 그때그때 새로기리기위함)
				Thread.sleep(20); // 20밀리세컨드당 한번의 루프가 돌아간다

				if (pause == false) {
					if (mode != 0) {
						killer = bullet.killEnemy(enemy);
						player.sendXY();
						engine.moveObject();
						if(otherBullet.isDie()){
							player.hp --;
						}
						if(player.hp <= 0){
							System.out.println("I'm die.");
							player.die = true;
							player.p.x = -100; player.p.y = -100;
							player.sendXY();
							dispose(); // 게임프레임은 닫습니다
							JOptionPane.showMessageDialog(null, "당신은 패배하였습니다.");
						}
						engine.addMagazine(); // 총알 메소드.
						cnt++; // 루프가 돌아간 횟수
					}
				}
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
	}
		
	public void update(Graphics g) { /* 프레임 내의 버퍼이미지를 이용하여 깜빡임 현상을 완전히 없앱니다. */
		paint(g);
	}

	public void paint(Graphics g) { /* 각종 이미지를 그리기위한 메서드를 실행시킨다 */
		GUI.buffimg = createImage(width, height);
		gc = GUI.buffimg.getGraphics(); // 버퍼이미지에 대한 그래픽 객채를 얻어온다.
		drawimages(g);
	}
	
	public void drawimages(Graphics g) { /* 게임의 전체적인 이미지를 그립니다. */
		if (mode == 0) {
			gui.drawLoading();			
		}else {
			gui.backgroundDrawImg();
			gui.drawEnemy();
			gui.drawFood();
			gui.drawBullet(bullet);
			gui.drawOther();
			gui.drawBullet(otherBullet);
			gui.UIDrawImg(); // 전체 UI를 그린다 .
		}
		g.drawImage(GUI.buffimg, 0, 0, this);
	}

	public void keyPressed(KeyEvent e) { /* 키를 누르면. */
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			control.keyLeft = true;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			control.keyRight = true;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			control.keyUp = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			control.keyDown = true;
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			control.space = true;
		} else if (e.getKeyCode() == KeyEvent.VK_P) {
			control.p = true;
		} else if (e.getKeyCode() == KeyEvent.VK_R) {
			control.r = true;
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			control.et = true;
		}
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
			if (player.isAdult) {
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

	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
