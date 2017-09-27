package ���߻���;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

class Game extends Frame implements Runnable, KeyListener {
	boolean keyUp = false; // ���� ȭ��ǥ�� ������������ä���ִ�.
	boolean keyDown = false;// �Ʒ��� ȭ��ǥ�� ������������ä���ִ�.
	boolean keyLeft = false;// ���� ȭ��ǥ�� ������������ä���ִ�.
	boolean keyRight = false;// ������ ȭ��ǥ�� ������������ä���ִ�.
	boolean space = false; // �����̽� �ٰ� ������������ ä�� �ִ�.
	boolean shift = false; // ����Ʈ
	boolean p = false; // �Ͻ����� p
	boolean et = false; // ����
	boolean r = false; // �����

	ArrayList dirtyman = new ArrayList(); // ���ո��� ������Ʈ�� ������ �������� ���� �迭
	ArrayList foodarray = new ArrayList(); // �����ʸ� �����������ϱ����� �迭
	ArrayList nodiearray = new ArrayList(); // ���� ������Ʈ�� ������ �������� ���� �迭
	Enemy en; // ���ո� Ŭ������ ������
	Food pa; // ������ Ŭ������ ������
	Nodie nd;// ���� Ŭ������ ������
	Exp s = new Exp(10);
	Image dbImage; // ����� �̹���
	Image stdimg = new ImageIcon("egg.png").getImage();
	Image nodieimg = new ImageIcon("����.png").getImage();
	Image background = new ImageIcon("���.png").getImage();
	Image enemy = new ImageIcon("���ո�����.png").getImage();
	Image foodimg = new ImageIcon("food.png").getImage();
	Image level1 = new ImageIcon("�ʱ�.png").getImage();
	Image level2 = new ImageIcon("�߱�.png").getImage();
	Image level3 = new ImageIcon("���.png").getImage();

	Image nomal_insect = new ImageIcon("nomal_insect.png").getImage();
	Image rare_insect = new ImageIcon("rare_insect.png").getImage();
	Image unique_insect = new ImageIcon("unique_insect.png").getImage();
	Image regend_insect = new ImageIcon("regend_insect.png").getImage();

	Image nomal_bird = new ImageIcon("nomal_bird.png").getImage();
	Image rare_bird = new ImageIcon("rare_bird.png").getImage();
	Image unique_bird = new ImageIcon("unique_bird.png").getImage();
	Image regend_bird = new ImageIcon("regend_bird.png").getImage();

	Image nomal_fish = new ImageIcon("nomal_fish.png").getImage();
	Image rare_fish = new ImageIcon("rare_fish.png").getImage();
	Image unique_fish = new ImageIcon("unique_fish.png").getImage();
	Image regend_fish = new ImageIcon("regend_fish.png").getImage();

	Image nomal_reptile = new ImageIcon("nomal_reptile.png").getImage();
	Image rare_reptile = new ImageIcon("rare_reptile.png").getImage();
	Image unique_reptile = new ImageIcon("unique_reptile.png").getImage();
	Image regend_reptile = new ImageIcon("regend_reptile.png").getImage();
	
	Image foodimg2 = new ImageIcon("food1.png").getImage();
	Image foodimg3 = new ImageIcon("food2.png").getImage();

	Image buffimg = null; // ������۸��� ����ϱ����� �����̹����� �����Ѵ�
	Graphics gc; // ������Ʈ���� �׸��� ���� �׷��� ���� �����Ѵ�
	int x = 400, y = 300; // ĳ������ ���� ��ġ, �׸��� �������� ��ǥ�� �޾ƿ��� ���� ����
	int cnt = 0; // �������� ������ ���� ����, ���� ������ �����ϱ� ���� ���ȴ�
	int life = 0; // ���
	int point = 0; // �������� ���� ����
	int score = 0; // ��ȭ��
	int total = 0; // ��������
	int rank = 0; // ���� ȯ�� ����
	int rev; // ��ȭ ���� Ȯ�� ����
	int boost = 40; // �ν�Ʈ ������
	int nodienum = 0; // ���������� ����
	int nodietime = 0; // �����ð�
	int mode = 0; // ����ȭ�� ����
	int dmappear = 0; // ���ո� �����ֱ⺯��
	int selectmode = 1;// ���̵� ���� ����
	boolean pause = false;// �Ͻ�����
	boolean nodie = false;// ����

	public Game() {

		setTitle("���߻���!");
		setSize(800, 600);
		start(); // �������� ������ �����ϱ� ���� �޼ҵ�
		setLocation(250, 80);
		setResizable(false); // ����� ������ �� ���� ����
		setVisible(true); // �������� ���̰� ����
		this.addKeyListener(this); // Ű�����ʸ� �߰��Ͽ� ����Ű ������ �޾ƿ� �� �ְ� �Ѵ�.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});}
//	}

	public void start() {
		Thread th = new Thread(this); // �����带 ����
		th.start(); // �������� ������ ���۽�Ų��
	}

	public void run() {
		while (life == 0) { // life�� 0�� �ƴϰԵǸ� ������ ������
			try {
				modeandpause(); // ��� ����
				arrowkey(); // ���� Ű�� ���� ĳ������ �̵��� ����
				repaint(); // ������Ʈ�Լ�(�׸��� �׶��׶� ���α⸮������)
				Thread.sleep(20); // 20�и�������� �ѹ��� ������ ���ư���
				if (pause == false) {
					if (mode != 0) {
						if (r == true) {
							reset();// r�� ������ ���µ˴ϴ�.
						}
						enemymove(); // ���ո��� �߰�, �����̰� ��
						foodmove(); // �������� �߰��޼ҵ�
						nodiemove();// ���������� �߰��޼ҵ�
						boostcontrol(); // �ν�Ʈ ���޼ҵ�
						cnt++; // ������ ���ư� Ƚ��
					}
				}
			} catch (Exception e) {
			}
		}
	}

	public void update(Graphics g) {
		// ������ ���� �����̹����� �̿��Ͽ� ������ ������ ������ ���۴ϴ�.
		paint(g);
	}

	public void paint(Graphics g) { // ���� �̹����� �׸������� �޼��带 �����Ų��
		buffimg = createImage(800, 600); // �����̹����� �׸��� (������۸��� ����Ͽ� ȭ���� ��������
											// ���ش�)
		gc = buffimg.getGraphics(); // �����̹����� ���� �׷��� ��ä�� ���´�.
		drawimages(g);
	}

	public void drawimages(Graphics g) {

		if (mode == 0) { // ���� ���̵��� ���� �̹����� ��Ÿ���ϴ�.
			setBackground(Color.BLACK);
			if (selectmode == 1) {
				gc.drawImage(level1, 0, 10, this);
			} else if (selectmode == 2) {
				gc.drawImage(level2, 0, 10, this);
			} else if (selectmode == 3) {
				gc.drawImage(level3, 0, 10, this);
			}
			g.drawImage(buffimg, 0, 0, this);
		}
		if (mode != 0) {
			backgroundDrawImg(); // ����� �׸��� �׸���
			enemydrawing(); // ���ո��� �׸��� �׸���
			foodDrawImg(); // �������� �׸��� �׸���
			nodieDrawImg(); // ������������ �׸���
			UIDrawImg(); // ��ü UI �׸��� �׸���
			g.drawImage(buffimg, 0, 0, this); // �����̹����� �׸���. 0,0���� ��ǥ�� ���缭������ũ�⿡
												// �������
		}

	}

	public void backgroundDrawImg() {
		gc.drawImage(background, 0, 0, this); // ������ ����̹��������� 0,0�� ��ġ��Ų��
	}

	public void UIDrawImg() {
		gc.setFont(new Font("Default", Font.ROMAN_BASELINE, 20)); // ��Ʈ�Ǹ���ũ�⸦���Ѵ�
		gc.drawString("��ƾ�ð�:" + Integer.toString(cnt / 50), 380, 50); // �����ӵ���1/40��ŭ�ð����귯��
		gc.drawString("���� ������ :" + Integer.toString(point), 500, 50); // �����������ǰ�������Ÿ����
		gc.drawString("��ȭ ����ġ: " + Integer.toString(score) + "%", 620, 80);
		gc.drawString("�� ����: " + Integer.toString(rank + cnt / 50), 650, 110);

		if (mode == 1 || mode == 2) {
			gc.drawString("���� ������:" + Integer.toString(nodienum), 20, 50); // ������
																			// ����������
																			// ����
			gc.drawString("�����ð� :" + Integer.toString(nodietime), 650, 50); // �����ð�
			gc.drawString("�ν��� ������ :" + Integer.toString(boost), 190, 50); // �ν��͸�
																			// ��
																			// ��
																			// �ִ�
																			// ������
		}

		if (pause == true) {
			gc.drawString("PAUSE", 340, 250); // �Ͻ����� ���¿��� PAUSE�� ��Ÿ���ϴ�
		}
		MovestdImage(stdimg, x, y, 50, 50); // ĳ������ �̹����� ��ǥ�� ���� �׸���( ���������ư��鼭
											// ��Ӵٽñ׸��Ƿ� �����̴°�ó�� ����)ũ��� 50.50

	}

	public void MovestdImage(Image dbImage, int x, int y, int width, int height) {

		if (total == 100 && s.isFull()) {
			rev = (int) (Math.random() * 100) + 1; // ���� ��ȭ
			if (1 <= rev && rev <= 40) { // 40% ����
				dbImage = nomal_insect;
				stdimg = dbImage;
			} else if (41 <= rev && rev <= 60) { // 20% ����
				dbImage = nomal_bird;
				stdimg = dbImage;
			} else if (61 <= rev && rev <= 80) { // 20% ���
				dbImage = nomal_fish;
				stdimg = dbImage;
			} else if (81 <= rev && rev <= 100) { // 20% �����
				dbImage = nomal_reptile;
				stdimg = dbImage;
			}
			score = 0; // ��ȭ�� �ʱ�ȭ
			s.clear(); // ���� �ʱ�ȭ

		}

		// �ι�° ��ȭ
		if (total == 200 && s.isFull()) {
			if (dbImage == nomal_insect) {
				dbImage = rare_insect;
				stdimg = dbImage;
			} else if (dbImage == nomal_bird) {
				dbImage = rare_bird;
				stdimg = dbImage;
			} else if (dbImage == nomal_fish) {
				dbImage = rare_fish;
				stdimg = dbImage;
			} else if (dbImage == nomal_reptile) {
				dbImage = rare_reptile;
				stdimg = dbImage;
			}
			score = 0;
			s.clear();

		}

		// ����° ��ȭ
		if (total == 300 && s.isFull()) {
			if (dbImage == rare_insect) {
				dbImage = unique_insect;
				stdimg = dbImage;
			} else if (dbImage == rare_bird) {
				dbImage = unique_bird;
				stdimg = dbImage;
			} else if (dbImage == rare_fish) {
				dbImage = unique_fish;
				stdimg = dbImage;
			} else if (dbImage == rare_reptile) {
				dbImage = unique_reptile;
				stdimg = dbImage;
			}
			score = 0;
			s.clear();

		}
		if (total == 400 && s.isFull()) {
			if (dbImage == unique_insect) {
				dbImage = regend_insect;
				stdimg = dbImage;
			} else if (dbImage == unique_bird) {
				dbImage = regend_bird;
				stdimg = dbImage;
			} else if (dbImage == unique_fish) {
				dbImage = regend_fish;
				stdimg = dbImage;
			} else if (dbImage == unique_reptile) {
				dbImage = regend_reptile;
				stdimg = dbImage;
			}
			score = 0;
			s.clear();

		}
		if (total > 400 && s.isFull()) {
			score = 0;
			s.clear();

		}
		gc.setClip(x, y, width, height); // ĳ�������̹����� ��ǥ�� ũ�⸦ �޾ƿ´�
		gc.drawImage(stdimg, x, y, this); // ĳ���͸� ��ǥ�� ���� ��Ҹ� �ٲپ� �׸���.
	}

	public void foodmove() {
		for (int i = 0; i < foodarray.size(); ++i) {
			pa = (Food) (foodarray.get(i)); // �����ʸ� �߰��Ѵ�

			int dis1 = (int) Math.pow((x + 25) - (pa.position[0] + 25), 2);
			int dis2 = (int) Math.pow((y + 25) - (pa.position[1] + 25), 2);

			double dist = Math.sqrt(dis1 + dis2); // ĳ���Ϳ� �������� �Ÿ��� ���ϴ� �˰���

			if (dist < 25) { // �Ÿ��� 25���Ϸ� �پ��ԵǸ� �����ʰ� ������� ������ ������ �ȴ�.
				if (mode == 1) {
					score += 10;
					point++;
					total += 10;
					rank += 10;
					s.push(10);

				} else if (mode == 2) {
					score += 10;
					point++;
					total += 10;
					rank += 20;
					s.push(10);

				} else if (mode == 3) {
					score += 10;
					point++;
					total += 10;
					rank += 30;
					s.push(10);
				}
				foodarray.remove(i);
			}

		}
		if ((cnt) % 60 == 0) {
			int[] r = GenerateXNY(); // �������� ��ǥ�� �޾ƿ´�
			pa = new Food(r[0], r[1]); // �޾ƿ� ��ǥ�� �����ʸ� �߰���Ų��.
			foodarray.add(pa); // cnt/60�� �ð��� ���������� �ϳ��� �����ʸ� ȭ�鿡 �߰��Ѵ�
		}
	}

	public void foodDrawImg() {
//		Random rand = new Random();
		
		for (int i = 0; i < foodarray.size(); ++i) {
			pa = (Food) (foodarray.get(i));
			gc.drawImage(foodimg, pa.position[0], pa.position[1], this);
		} // �߰��� �������� ����ŭ �������� �̹����� �߰��Ѵ�
	}

	public void nodiemove() {
		if (mode == 1) {
			for (int i = 0; i < nodiearray.size(); ++i) {
				nd = (Nodie) (nodiearray.get(i)); // ������������ �߰��Ѵ�

				int dis1 = (int) Math.pow((x + 10) - (nd.position[0] + 10), 2);
				int dis2 = (int) Math.pow((y + 15) - (nd.position[1] + 15), 2);
				double dist = Math.sqrt(dis1 + dis2); // ĳ���Ϳ� ��ź�� �Ÿ��� ���ϴ� �˰���

				if (dist < 25) {
					nodienum++;
					nodiearray.remove(i); // ���������ۿ� ��������� ������������ ������� ����������
											// �þ�ϴ�
				}
			}
			if ((cnt) % 800 == 0) {
				int[] r = GenerateXNY(); // �������� ��ǥ�� �޾ƿ´�
				nd = new Nodie(r[0], r[1]); // �޾ƿ� ��ǥ�� ������������ �߰���Ų��.
				nodiearray.add(nd); // cnt/800�� �ð��� ���������� �ϳ��� ���� �������� ȭ�鿡 �߰��Ѵ�
			}
		}
	}

	public void nodieDrawImg() {
		for (int i = 0; i < nodiearray.size(); ++i) {
			nd = (Nodie) (nodiearray.get(i));
			gc.drawImage(nodieimg, nd.position[0], nd.position[1], this);
		} // �߰��� ���������� ����ŭ ������ �̹����� �߰��Ѵ�
	}

	public void nodiemode(int a) {
		nodietime = a;
		nodie = true; // ������ Ȱ��ȭ��ŵ�ϴ�,
		nodienum--; // ������尡 ������ ���������� �پ��ϴ�.
	}

	public void nodiemode() {
		shift = false;
		if (nodietime > 0) {
			nodietime--; // ����Ÿ���� �����پ��ϴ�
			if (nodietime <= 0) {
				nodie = false; // �����ð��� �ٵǸ� ������带 �����մϴ�
			}
		}
	}

	public void enemydrawing() {
		for (int i = 0; i < dirtyman.size(); ++i) {
			en = (Enemy) (dirtyman.get(i));
			gc.drawImage(enemy, en.position[0], en.position[1], this);
		} // �߰��� ���ո��� ����ŭ ���ƴٴϴ� ���ո��� �׸��� �߰��Ѵ�.
	}

	public void enemymove() {
		nodiemode();
		for (int i = 0; i < dirtyman.size(); ++i) {
			en = (Enemy) (dirtyman.get(i)); // 4 ���ո��� �߰���Ų��.
			en.move(); // ���ո��� �������� �����ϴ� �޼��带 �ҷ��´�
			int dis1 = (int) Math.pow((x + 10) - (en.position[0] + 10), 2);
			int dis2 = (int) Math.pow((y + 15) - (en.position[1] + 15), 2);
			double dist = Math.sqrt(dis1 + dis2); // ���ոǰ� �л��� �Ÿ��� ��� �˰���
			Random rand = new Random();
			if (dist < 11) {
				if (nodie == false) {
					life--;
					GameOver gg = new GameOver(rank + cnt / 50 + point); // �Ÿ����پ�����ӿ���
					dispose(); // ������������ �ݽ��ϴ�
				}
			}
		}
		if ((cnt) % dmappear == 0) {
			int[] r = GenerateXNY(); // ��ǥ�� �������� �޾ƿɴϴ�
			en = new Enemy(r[0], r[1]); // �޾ƿ� ��ǥ�� ���̵� ���� ������ �ð��� ����������
										// ���ո����߰���ŵ�ϴ�
			dirtyman.add(en);
		}
	}

	public int[] GenerateXNY() { // ��ǥ�� �������� �ҷ����� �޽��
		Random rand = new Random();
		int x_rand = (rand.nextInt() % 800);
		x_rand = Math.abs(x_rand);
		int y_rand = (rand.nextInt() % 600);
		y_rand = Math.abs(y_rand);// x,y��ǥ�� �������� �޽��ϴ�
		int x_rand2 = (int) Math.pow(x - x_rand, 2);
		int y_rand2 = (int) Math.pow(y - y_rand, 2);
		double dist = Math.sqrt(x_rand2 + y_rand2); // ĳ���Ϳ� �޾ƿ� ��ǥ�� �Ÿ��� ���մϴ�
		int[] res = new int[2];
		if (dist > 100) {
			res[0] = x_rand;
			res[1] = y_rand;
			return res; // �޾ƿ� ��ǥ�� ĳ���Ϳ� 100�Ÿ� �ۿ� �ִٸ�, ��ǥ�� ���Ͻ�ŵ�ϴ�
		} else {
			return GenerateXNY(); // ��ǥ�� ĳ���Ϳ� 100�Ÿ��ȿ��ִٸ�, �ٽ� ��ǥ�� ���� ���Ͻ�ŵ�ϴ�.
		}
	}

	public void reset() { // ������ ��� �ʱ���·� �����ϴ�.
		dirtyman.removeAll(dirtyman);
		foodarray.removeAll(foodarray);
		nodiearray.removeAll(nodiearray);
		point = 0;
		mode = 0;
		cnt = 0;
		s.clear();
		x = 400;
		y = 300;
	}

	public void modeandpause() {
		if (mode == 0) {
			if (et == true) {
				mode = selectmode; // ���͸� ������ �ش��带 �����Ų �� ������ ���۵˴ϴ�
			}
			if (mode == 1) { // ���̵��� ������
				nodienum = 3;// ���������� ����
				dmappear = 150;// ���ո� ���� �ֱ�
			} else if (mode == 2) {
				nodienum = 2;
				dmappear = 80;
			} else if (mode == 3) {
				nodienum = 0;
				dmappear = 20; // ��޿����� ���ո��� ���� �ֱⰡ ª���ϴ�
			}
		}
		if (p == true) { // p�� ������ ������ ����ϴ�
			pause = !pause;
			p = false;
		}
	}

	public void boostcontrol() {
		if (space == false) { // �ν��� �������� �����ϱ� ���� if��
			if (boost < 40) {
				if (cnt % 20 == 0) {
					boost++; // �ν��Ͱ������� space�� ������������ õõ�� �����˴ϴ�
				}
			}
		} else {
			if (boost > 0) {
				boost--; // space�� ������ �ν��Ͱ������� �پ��ϴ�
			} else {
				space = false;
			}
		}
	}

	public void arrowkey() { // ĳ������ �̵��ӵ��� ����Ű�� ���� �̵������� �����ϰ�, ĳ���͸� ȭ��
								// �����θ��������������մϴ�. �׸��� �ν��� ���� �����մϴ�
		if (mode == 0) {
			if (keyLeft == true) {
				selectmode--;
				if (selectmode == 0) {
					selectmode = 3;
				}
				keyLeft = false;
			}
			if (keyRight == true) {
				selectmode++;
				if (selectmode == 4) {
					selectmode = 1;
				}
				keyRight = false;
			}
		} else {
			if (keyUp == true && pause == false) {
				if (y > 0) {
					if (space == false) {
						y -= 8;
					} else {
						y -= 15;
					}
				}
			}
			if (keyDown == true && pause == false) {
				if (y < 570) {
					if (space == false) {
						y += 8;
					} else {
						y += 15;
					}
				}
			}
			if (keyLeft == true && pause == false) {
				if (x > 0) {
					if (space == false) {
						x -= 8;
					} else {
						x -= 15;
					}
				}
			}
			if (keyRight == true && pause == false) {
				if (x < 780) {
					if (space == false) {
						x += 8;
					} else {
						x += 15;
					}
				}
			}
			if (shift == true) {
				nodiemode();
			}
			if (et == true) {
				mode = selectmode;
			}
		}
	}

	public void keyPressed(KeyEvent e) { // ����Ű�� �������� �����ٴ� ��ȣ�� �޾ƿ´�( Ű�Է���
											// true�θ����)

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			int a=0;
			while(a<3){
				a++;
			}
			keyLeft = true;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			keyRight = true;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			keyUp = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			keyDown = true;
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {

			if (mode != 3) {
				space = true;
			} // ��޸�忡���� �ν��Ͱ� �����������Ƿ�
		} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			if (nodienum > 0) {
				nodiemode(200);
			} // ������������ 200���ͽ��۽�ŵ�ϴ�.
			shift = true;
		} else if (e.getKeyCode() == KeyEvent.VK_P) {
			p = true;
		} else if (e.getKeyCode() == KeyEvent.VK_R) {
			r = true;
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			et = true;
		}
	}

	private void sleep(int i) {
		// TODO Auto-generated method stub
		
	}
	public void keyReleased(KeyEvent e) { // Ű�� �������� Ű�Է��� false�� �����

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			keyLeft = false;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			keyRight = false;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			keyUp = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			keyDown = false;
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			space = false;
		} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shift = false;
		} else if (e.getKeyCode() == KeyEvent.VK_P) {
			p = false;
		} else if (e.getKeyCode() == KeyEvent.VK_R) {
			r = false;
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			et = false;
		}
	}

	public void keyTyped(KeyEvent e) { // ŰŸ�� �޼��� , �̼ҽ��������������� keylistener��
										// ��ӹ����� �ڵ����λ���°Ŷ� ����
	}

}
