package ���߻���;

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
 * ��ü���� ������ �����ϱ� ���� Ŭ����.
 */
public class Game extends JFrame implements Runnable, KeyListener {
	private static final long serialVersionUID = 1L;
	final static public int width = 1000;
	final static public int height = 750;
	final public int MAX_HP = 20;

	Socket socket;
	Engine engine; // ���� ����
	Graphics gc; // ���� �׷���
	GUI gui;
	Control control; // ���� ��Ʈ�ѷ�
	
	int cnt = 0; // �������� ������ ���� ����, ���� ������ �����ϱ� ���� ���ȴ�
	
	Player player; // ����
	Bullet bullet; // �Ѿ˿� ���� ����
	Bullet otherBullet;	
	CopyOnWriteArrayList<ServerFood> food;
	CopyOnWriteArrayList<ServerEnemy> enemy;
	CopyOnWriteArrayList<OtherPlayer> other;
	Vector<ServerPlayer> playerOther;
	
	ObjectOutputStream outputStream;
	ObjectInputStream inputStream;

	boolean pause = false; // �Ͻ�����
	int mode = 0; // ����ȭ�� ����
	int killer = 999;

	//public MainFrame frame = new MainFrame();
	public Game(Socket s) {
		socket = s;
		initGraphic();
		initStream();
		initGame();	
		start(); // �������� ������ �����ϱ� ���� �޼ҵ�. ���ӽ���.
	}
	
	public void initGraphic(){
		setTitle("���߻���!"); // ���߻��� Ÿ��Ʋ
		setSize(width, height); // â�� ũ�� 1920X1080 �� ũ��(FullHD ����)
		setLocation(0, 0); // â�� ��ġ�� ���� ���� ���� ����.
		setResizable(false); // ����� ������ �� ���� ����
		setVisible(true); // �������� ���̰� ����
		setBackground(Color.WHITE); // Background�� �⺻ �÷��� ������� ����
		
		this.addKeyListener(this); // Key Listener�� �߰��Ͽ� ����Ű ������ �޾ƿ� �� �ְ� �Ѵ�.
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
		control = new Control(this); // ��Ʈ�ѷ��� ���
		engine = new Engine(this); // Engine�� ���
		bullet = new Bullet(this); // Bullet �� ���
		otherBullet = new Bullet(this);
		food = new CopyOnWriteArrayList<ServerFood>();
		enemy = new CopyOnWriteArrayList<ServerEnemy>();
		other = new CopyOnWriteArrayList<OtherPlayer>();
	}

	public void start() {
		Thread th = new Thread(this); // �����带 ����
		th.start(); // �������� ������ ���۽�Ų��
	}

	public void run() {
		while (player.hp != 0) { // life�� 0�� �ƴϰԵǸ� ������ ������
			try {
				gui.setPannel();
				control.arrowkey(); // ���� Ű�� ���� ĳ������ �̵��� ����
				repaint(); // ������Ʈ�Լ�(�׸��� �׶��׶� ���α⸮������)
				Thread.sleep(20); // 20�и�������� �ѹ��� ������ ���ư���

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
							dispose(); // ������������ �ݽ��ϴ�
							JOptionPane.showMessageDialog(null, "����� �й��Ͽ����ϴ�.");
						}
						engine.addMagazine(); // �Ѿ� �޼ҵ�.
						cnt++; // ������ ���ư� Ƚ��
					}
				}
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
	}
		
	public void update(Graphics g) { /* ������ ���� �����̹����� �̿��Ͽ� ������ ������ ������ ���۴ϴ�. */
		paint(g);
	}

	public void paint(Graphics g) { /* ���� �̹����� �׸������� �޼��带 �����Ų�� */
		GUI.buffimg = createImage(width, height);
		gc = GUI.buffimg.getGraphics(); // �����̹����� ���� �׷��� ��ä�� ���´�.
		drawimages(g);
	}
	
	public void drawimages(Graphics g) { /* ������ ��ü���� �̹����� �׸��ϴ�. */
		if (mode == 0) {
			gui.drawLoading();			
		}else {
			gui.backgroundDrawImg();
			gui.drawEnemy();
			gui.drawFood();
			gui.drawBullet(bullet);
			gui.drawOther();
			gui.drawBullet(otherBullet);
			gui.UIDrawImg(); // ��ü UI�� �׸��� .
		}
		g.drawImage(GUI.buffimg, 0, 0, this);
	}

	public void keyPressed(KeyEvent e) { /* Ű�� ������. */
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
	
	public void keyReleased(KeyEvent e) { /* Ű�� ������ �� */
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
