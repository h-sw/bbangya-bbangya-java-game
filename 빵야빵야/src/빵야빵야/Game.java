package ���߻���;

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
 * ��ü���� ������ �����ϱ� ���� Ŭ����.
 *
 */
public class Game extends JFrame implements Runnable, KeyListener {

	final static public int width = 1000;
	final static public int height = 750;

	ArrayList<ServerFood> food = new ArrayList<ServerFood>(); // ����
	ArrayList<ServerEnemy> enemy = new ArrayList<ServerEnemy>(); // ��ֹ�
	Engine engine; // ���� ����
	Bullet bullet; // �Ѿ˿� ���� ����
	Bullet otherBullet;
	int x = 400, y = 300; // ĳ������ ���� ��ġ, �׸��� �������� ��ǥ�� �޾ƿ��� ���� ����
	int cnt = 0; // �������� ������ ���� ����, ���� ������ �����ϱ� ���� ���ȴ�
	Player player; // ����
	OtherPlayer other;
	int mode = 0; // ����ȭ�� ����
	int dmappear = 0; // ��ֹ� �����ֱ⺯��
	int selectmode = 1; // ���̵� ���� ����
	boolean pause = false; // �Ͻ�����
	Control control; // ���� ��Ʈ�ѷ�
	Graphics gc; // ���� �׷���
	Socket socket;
	Vector<ServerPlayer> playerOther;
	ObjectOutputStream outputStream;
	ObjectInputStream inputStream;
	String id;
	int killer = 999;

	//public MainFrame frame = new MainFrame();
	public Game(Socket s) {
		setTitle("���߻���!"); // ���߻��� Ÿ��Ʋ
		setSize(width, height); // â�� ũ�� 1920X1080 �� ũ��(FullHD ����)
		setLocation(0, 0); // â�� ��ġ�� ���� ���� ���� ����.
		setResizable(false); // ����� ������ �� ���� ����
		setVisible(true); // �������� ���̰� ����
		setBackground(Color.WHITE); // Background�� �⺻ �÷��� ������� ����
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
		control = new Control(this); // ��Ʈ�ѷ��� ���
		engine = new Engine(this); // Engine�� ���
		bullet = new Bullet(this); // Bullet �� ���
		otherBullet = new Bullet(this);
		other = new OtherPlayer(this, socket);
		start(); // �������� ������ �����ϱ� ���� �޼ҵ�. ���ӽ���.
		this.addKeyListener(this); // Key Listener�� �߰��Ͽ� ����Ű ������ �޾ƿ� �� �ְ� �Ѵ�.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});
	}

	public void start() {
		Thread th = new Thread(this); // �����带 ����
		th.start(); // �������� ������ ���۽�Ų��
	}

	public void run() {
		while (player.hp != 0) { // life�� 0�� �ƴϰԵǸ� ������ ������
			try {
				setPannel();
				control.arrowkey(); // ���� Ű�� ���� ĳ������ �̵��� ����
				repaint(); // ������Ʈ�Լ�(�׸��� �׶��׶� ���α⸮������)
				Thread.sleep(20); // 20�и�������� �ѹ��� ������ ���ư���

				if (pause == false) {
					if (other == null) {

					}
					if (mode != 0) {
						if (control.r == true) {
							reset(); // r�� ������ ���µ˴ϴ�.
						}
						killer = bullet.killEnemy(enemy);
						player.sendXY();
//						enemy.moveImg(); // ��ֹ� �߰�, �����̰� ��
//						food.move(); // ���� �߰��޼ҵ�
						bullet.move();
						otherBullet.move();
						if(otherBullet.isDie()){
							player.hp --;
						}
						if(player.hp == 0){
							JOptionPane.showMessageDialog(null, "����� �й��Ͽ����ϴ�.");
							dispose(); // ������������ �ݽ��ϴ�
							break;
						}
						boostcontrol(); // �Ѿ� �޼ҵ�.
						cnt++; // ������ ���ư� Ƚ��
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
	public void update(Graphics g) { /* ������ ���� �����̹����� �̿��Ͽ� ������ ������ ������ ���۴ϴ�. */
		paint(g);
	}

	public void paint(Graphics g) { /* ���� �̹����� �׸������� �޼��带 �����Ų�� */
		GUI.buffimg = createImage(width, height); // �����̹����� �׸��� (������۸��� ����Ͽ� ȭ����
													// �������� ���ش�)
		gc = GUI.buffimg.getGraphics(); // �����̹����� ���� �׷��� ��ä�� ���´�.
		drawimages(g);
	}

	public void drawimages(Graphics g) { /* ������ ��ü���� �̹����� �׸��ϴ�. */

		if (mode == 0) { // ���� ���̵��� ���� �̹����� ��Ÿ���ϴ�.

			setBackground(Color.WHITE);
			gc.setFont(new Font("�ü�", Font.ROMAN_BASELINE, 100));
			String s = "���߻���";
			gc.drawString(s, (width - s.length() * 100) / 2, 230);
			gc.setFont(new Font("Default", Font.PLAIN, 20));
			gc.drawLine(300, 300, 724, 300);
			s = "Press Enter";
			gc.drawString(s, (width - s.length() * 10) / 2, 330);
			s = "My ID: "+player.id;
			gc.drawString(s, (width - s.length() * 10) / 2, 360);
			s = "����Ű�� �����ø� ������";
			gc.drawString(s, (width - s.length() * 20) / 2, 390);
			s = "���ӵ˴ϴ�.";
			gc.drawString(s, (width - s.length() * 20) / 2, 410);
			gc.drawLine(300, 430, 724, 430);
			s = "�̵� : ����Ű";
			gc.drawString(s, (width - s.length() * 20) / 2, 475);
			s = "�Ѿ˹߻� : Space";
			gc.drawString(s, (width - s.length() * 15) / 2, 510);
			// gc.drawString("Press Enter", , 250);
			if (selectmode == 1) {
				gc.drawImage(GUI.level1, 0, 10, this);
			}
			g.drawImage(GUI.buffimg, 0, 0, this);
		}
		if (mode != 0) {
			backgroundDrawImg(); // ����� �ҷ��´�.
//			enemy.drawing(); // ��ֹ��� �׸���.
			drawEnemy();
			drawFood(); // ������ �׸���.
			bullet.DrawImg(); // �Ѿ��� �׸���.
			other.drawing();
			otherBullet.DrawImg();
			UIDrawImg(); // ��ü UI�� �׸��� .
			g.drawImage(GUI.buffimg, 0, 0, this); // �����̹����� �׸���. 0,0���� ��ǥ��
													// ���缭������ũ�⿡ �������
		}

	}

	public void backgroundDrawImg() {
		gc.drawImage(GUI.background, 0, 0, this); // ������ ����̹��������� 0,0�� ��ġ��Ų��
	}

	public void UIDrawImg() { /* UI�� �׷��ִ� �Լ� */
		gc.setFont(new Font("Default", Font.ROMAN_BASELINE, 20)); // ��Ʈ�Ǹ���ũ�⸦���Ѵ�
		gc.drawString("��ƾ�ð�:" + Integer.toString(cnt / 50), 10, 55); // �����ӵ���1/40��ŭ�ð����귯��
		gc.drawString("���� ������ :" + Integer.toString(player.point), 10, 80); // �����������ǰ�������Ÿ����
		gc.drawString("���� HP:" + Integer.toString(player.hp), 10, 155);
		gc.drawString("��ȭ ����ġ: " + Integer.toString(player.score) + "%", 400, 728);
		gc.drawString("�� ����: " + Integer.toString(player.rank + cnt / 50), 10, 105);
		String s = "�̵� : ����Ű           �Ѿ˹߻�:Space ";
		gc.drawString(s, (width) / 2, 55);

		if (mode == 1 || mode == 2) {
			gc.drawString("�Ѿ� ���� :" + Integer.toString(player.boost), 10, 130);
		}

		if (pause == true) {
			gc.drawString("PAUSE", 340, 250); // �Ͻ����� ���¿��� PAUSE�� ��Ÿ���ϴ�
		}
		player.MovestdImage(GUI.stdimg, player.p.x, player.p.y, GUI.stdimg.getWidth(this), GUI.stdimg.getHeight(this));
	}

	public void reset() { /* ���� ���� �� �ʱ�ȭ �Լ� */
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

	public void setPannel() { /* ��带 �����ϴ� �Լ� */
		if (mode == 0) {
			if (control.et == true) {
				mode = selectmode; // ���͸� ������ �ش��带 �����Ų �� ������ ���۵˴ϴ�
			}
			if (mode == 1) {
				dmappear = 700; // ��ֹ� ���� �ֱ�
			}
		}
		if (control.p == true) { // p�� ������ ������ ����ϴ�
			pause = !pause;
			control.p = false;
		}
	}

	public void boostcontrol() { /* �Ѿ��� �����ϱ� ���� �޼ҵ� */
		if (control.space == false) {
			if (player.boost < 40) {
				if (cnt % 100 == 0) {
					player.boost++; // cnt%100 ���� �Ѿ� ����
				}
			}
		} else {
			control.space = false;
		}
	}

	private boolean isReady = false;

	public void keyPressed(KeyEvent e) { /* Ű�� ������. */
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

	public void keyTyped(KeyEvent e) { // ŰŸ�� �޼��� , �̼ҽ��������������� keylistener��
										// ��ӹ����� �ڵ����λ���°Ŷ� ����
	}

	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}
}
