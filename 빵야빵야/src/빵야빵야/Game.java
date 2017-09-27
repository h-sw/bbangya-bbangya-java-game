package 빵야빵야;

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
	boolean keyUp = false; // 위쪽 화살표가 눌러지지않은채로있다.
	boolean keyDown = false;// 아래쪽 화살표가 눌러지지않은채로있다.
	boolean keyLeft = false;// 왼쪽 화살표가 눌러지지않은채로있다.
	boolean keyRight = false;// 오른쪽 화살표가 눌러지지않은채로있다.
	boolean space = false; // 스페이스 바가 눌러지지않은 채로 있다.
	boolean shift = false; // 쉬프트
	boolean p = false; // 일시정지 p
	boolean et = false; // 엔터
	boolean r = false; // 재시작

	ArrayList dirtyman = new ArrayList(); // 세균맨의 오브젝트를 여러개 저장히기 위한 배열
	ArrayList foodarray = new ArrayList(); // 경험초를 여러개저장하기위한 배열
	ArrayList nodiearray = new ArrayList(); // 무적 오브젝트를 여러개 저장히기 위한 배열
	Enemy en; // 세균맨 클래스의 접근자
	Food pa; // 경험초 클래스의 접근자
	Nodie nd;// 무적 클래스의 접근자
	Exp s = new Exp(10);
	Image dbImage; // 빈공간 이미지
	Image stdimg = new ImageIcon("egg.png").getImage();
	Image nodieimg = new ImageIcon("무적.png").getImage();
	Image background = new ImageIcon("배경.png").getImage();
	Image enemy = new ImageIcon("세균맨투명.png").getImage();
	Image foodimg = new ImageIcon("food.png").getImage();
	Image level1 = new ImageIcon("초급.png").getImage();
	Image level2 = new ImageIcon("중급.png").getImage();
	Image level3 = new ImageIcon("고급.png").getImage();

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

	Image buffimg = null; // 더블버퍼링을 사용하기위한 버퍼이미지를 정의한다
	Graphics gc; // 오브젝트들을 그리기 위한 그래픽 툴을 정의한다
	int x = 400, y = 300; // 캐릭터의 시작 위치, 그리고 앞으로의 좌표를 받아오기 위한 변수
	int cnt = 0; // 쓰레드의 루프를 세는 변수, 각종 변수를 통제하기 위해 사용된다
	int life = 0; // 목숨
	int point = 0; // 아이템을 먹은 갯수
	int score = 0; // 진화율
	int total = 0; // 먹이점수
	int rank = 0; // 먹이 환산 점수
	int rev; // 진화 랜덤 확률 변수
	int boost = 40; // 부스트 게이지
	int nodienum = 0; // 무적아이템 갯수
	int nodietime = 0; // 무적시간
	int mode = 0; // 메인화면 변수
	int dmappear = 0; // 세균맨 생성주기변수
	int selectmode = 1;// 난이도 선택 변수
	boolean pause = false;// 일시정지
	boolean nodie = false;// 무적

	public Game() {

		setTitle("빵야빵야!");
		setSize(800, 600);
		start(); // 쓰레드의 루프를 시작하기 위한 메소드
		setLocation(250, 80);
		setResizable(false); // 사이즈를 조절할 수 없게 만듬
		setVisible(true); // 프레임을 보이게 만듬
		this.addKeyListener(this); // 키리스너를 추가하여 방향키 정보를 받아올 수 있게 한다.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});}
//	}

	public void start() {
		Thread th = new Thread(this); // 쓰레드를 정의
		th.start(); // 쓰레드의 루프를 시작시킨다
	}

	public void run() {
		while (life == 0) { // life가 0이 아니게되면 루프가 끝난다
			try {
				modeandpause(); // 모드 설정
				arrowkey(); // 받은 키에 따른 캐릭터의 이동을 통제
				repaint(); // 리페인트함수(그림을 그때그때 새로기리기위함)
				Thread.sleep(20); // 20밀리세컨드당 한번의 루프가 돌아간다
				if (pause == false) {
					if (mode != 0) {
						if (r == true) {
							reset();// r을 누를시 리셋됩니다.
						}
						enemymove(); // 세균맨을 추가, 움직이게 함
						foodmove(); // 경험초의 추가메소드
						nodiemove();// 무적아이템 추가메소드
						boostcontrol(); // 부스트 사용메소드
						cnt++; // 루프가 돌아간 횟수
					}
				}
			} catch (Exception e) {
			}
		}
	}

	public void update(Graphics g) {
		// 프레임 내의 버퍼이미지를 이용하여 깜빡임 현상을 완전히 없앱니다.
		paint(g);
	}

	public void paint(Graphics g) { // 각종 이미지를 그리기위한 메서드를 실행시킨다
		buffimg = createImage(800, 600); // 버퍼이미지를 그린다 (떠블버퍼링을 사용하여 화면의 깜빡임을
											// 없앤다)
		gc = buffimg.getGraphics(); // 버퍼이미지에 대한 그래픽 객채를 얻어온다.
		drawimages(g);
	}

	public void drawimages(Graphics g) {

		if (mode == 0) { // 게임 난이도에 따라서 이미지를 나타냅니다.
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
			backgroundDrawImg(); // 배경의 그림을 그린다
			enemydrawing(); // 세균맨의 그림을 그린다
			foodDrawImg(); // 경험초의 그림을 그린다
			nodieDrawImg(); // 무적아이템을 그린다
			UIDrawImg(); // 전체 UI 그림을 그린다
			g.drawImage(buffimg, 0, 0, this); // 버퍼이미지를 그린다. 0,0으로 좌표를 맞춰서프레임크기에
												// 딱맞춘다
		}

	}

	public void backgroundDrawImg() {
		gc.drawImage(background, 0, 0, this); // 가져온 배경이미지파일을 0,0에 위치시킨다
	}

	public void UIDrawImg() {
		gc.setFont(new Font("Default", Font.ROMAN_BASELINE, 20)); // 폰트의모양과크기를정한다
		gc.drawString("버틴시간:" + Integer.toString(cnt / 50), 380, 50); // 루프속도의1/40만큼시간이흘러감
		gc.drawString("먹은 아이템 :" + Integer.toString(point), 500, 50); // 먹은아이템의갯수를나타낸다
		gc.drawString("진화 경험치: " + Integer.toString(score) + "%", 620, 80);
		gc.drawString("총 점수: " + Integer.toString(rank + cnt / 50), 650, 110);

		if (mode == 1 || mode == 2) {
			gc.drawString("무적 아이템:" + Integer.toString(nodienum), 20, 50); // 소지한
																			// 무적아이템
																			// 갯수
			gc.drawString("무적시간 :" + Integer.toString(nodietime), 650, 50); // 무적시간
			gc.drawString("부스터 게이지 :" + Integer.toString(boost), 190, 50); // 부스터를
																			// 쓸
																			// 수
																			// 있는
																			// 게이지
		}

		if (pause == true) {
			gc.drawString("PAUSE", 340, 250); // 일시정지 상태에서 PAUSE를 나타냅니다
		}
		MovestdImage(stdimg, x, y, 50, 50); // 캐릭터의 이미지를 좌표에 따라 그린다( 루프가돌아가면서
											// 계속다시그리므로 움직이는것처럼 보임)크기는 50.50

	}

	public void MovestdImage(Image dbImage, int x, int y, int width, int height) {

		if (total == 100 && s.isFull()) {
			rev = (int) (Math.random() * 100) + 1; // 랜덤 진화
			if (1 <= rev && rev <= 40) { // 40% 곤충
				dbImage = nomal_insect;
				stdimg = dbImage;
			} else if (41 <= rev && rev <= 60) { // 20% 조류
				dbImage = nomal_bird;
				stdimg = dbImage;
			} else if (61 <= rev && rev <= 80) { // 20% 어류
				dbImage = nomal_fish;
				stdimg = dbImage;
			} else if (81 <= rev && rev <= 100) { // 20% 파충류
				dbImage = nomal_reptile;
				stdimg = dbImage;
			}
			score = 0; // 진화율 초기화
			s.clear(); // 스택 초기화

		}

		// 두번째 진화
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

		// 세번째 진화
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
		gc.setClip(x, y, width, height); // 캐릭터의이미지의 좌표와 크기를 받아온다
		gc.drawImage(stdimg, x, y, this); // 캐릭터를 좌표에 따라 장소를 바꾸어 그린다.
	}

	public void foodmove() {
		for (int i = 0; i < foodarray.size(); ++i) {
			pa = (Food) (foodarray.get(i)); // 경험초를 추가한다

			int dis1 = (int) Math.pow((x + 25) - (pa.position[0] + 25), 2);
			int dis2 = (int) Math.pow((y + 25) - (pa.position[1] + 25), 2);

			double dist = Math.sqrt(dis1 + dis2); // 캐릭터와 경험초의 거리를 구하는 알고리즘

			if (dist < 25) { // 거리가 25이하로 줄어들게되면 경험초가 사라지고 점수가 오르게 된다.
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
			int[] r = GenerateXNY(); // 랜덤으로 좌표를 받아온다
			pa = new Food(r[0], r[1]); // 받아온 좌표에 경험초를 추가시킨다.
			foodarray.add(pa); // cnt/60의 시간이 지날때마다 하나의 경험초를 화면에 추가한다
		}
	}

	public void foodDrawImg() {
//		Random rand = new Random();
		
		for (int i = 0; i < foodarray.size(); ++i) {
			pa = (Food) (foodarray.get(i));
			gc.drawImage(foodimg, pa.position[0], pa.position[1], this);
		} // 추가된 경험초의 수만큼 경험초의 이미지를 추가한다
	}

	public void nodiemove() {
		if (mode == 1) {
			for (int i = 0; i < nodiearray.size(); ++i) {
				nd = (Nodie) (nodiearray.get(i)); // 무적아이템을 추가한다

				int dis1 = (int) Math.pow((x + 10) - (nd.position[0] + 10), 2);
				int dis2 = (int) Math.pow((y + 15) - (nd.position[1] + 15), 2);
				double dist = Math.sqrt(dis1 + dis2); // 캐릭터와 폭탄의 거리를 구하는 알고리즘

				if (dist < 25) {
					nodienum++;
					nodiearray.remove(i); // 무적아이템에 가까워지면 무적아이템이 사라지고 소지갯수가
											// 늘어납니다
				}
			}
			if ((cnt) % 800 == 0) {
				int[] r = GenerateXNY(); // 랜덤으로 좌표를 받아온다
				nd = new Nodie(r[0], r[1]); // 받아온 좌표에 무적아이템을 추가시킨다.
				nodiearray.add(nd); // cnt/800의 시간이 지날때마다 하나의 무적 아이템을 화면에 추가한다
			}
		}
	}

	public void nodieDrawImg() {
		for (int i = 0; i < nodiearray.size(); ++i) {
			nd = (Nodie) (nodiearray.get(i));
			gc.drawImage(nodieimg, nd.position[0], nd.position[1], this);
		} // 추가된 무적아이템 수만큼 아이템 이미지를 추가한다
	}

	public void nodiemode(int a) {
		nodietime = a;
		nodie = true; // 무적를 활성화시킵니다,
		nodienum--; // 무적모드가 켜지면 소지갯수가 줄어듭니다.
	}

	public void nodiemode() {
		shift = false;
		if (nodietime > 0) {
			nodietime--; // 무적타임이 점점줄어듭니다
			if (nodietime <= 0) {
				nodie = false; // 무적시간이 다되면 무적모드를 종료합니다
			}
		}
	}

	public void enemydrawing() {
		for (int i = 0; i < dirtyman.size(); ++i) {
			en = (Enemy) (dirtyman.get(i));
			gc.drawImage(enemy, en.position[0], en.position[1], this);
		} // 추가된 세균맨의 수만큼 돌아다니는 세균맨의 그림을 추가한다.
	}

	public void enemymove() {
		nodiemode();
		for (int i = 0; i < dirtyman.size(); ++i) {
			en = (Enemy) (dirtyman.get(i)); // 4 세균맨을 추가시킨다.
			en.move(); // 세균맨의 움직임을 통제하는 메서드를 불러온다
			int dis1 = (int) Math.pow((x + 10) - (en.position[0] + 10), 2);
			int dis2 = (int) Math.pow((y + 15) - (en.position[1] + 15), 2);
			double dist = Math.sqrt(dis1 + dis2); // 세균맨과 학생의 거리를 재는 알고리즘
			Random rand = new Random();
			if (dist < 11) {
				if (nodie == false) {
					life--;
					GameOver gg = new GameOver(rank + cnt / 50 + point); // 거리가줄어들면게임오버
					dispose(); // 게임프레임은 닫습니다
				}
			}
		}
		if ((cnt) % dmappear == 0) {
			int[] r = GenerateXNY(); // 좌표를 랜덤으로 받아옵니다
			en = new Enemy(r[0], r[1]); // 받아온 좌표에 난이도 별로 지정된 시간이 지날때마다
										// 세균맨을추가시킵니다
			dirtyman.add(en);
		}
	}

	public int[] GenerateXNY() { // 좌표를 랜덤으로 불러오는 메써드
		Random rand = new Random();
		int x_rand = (rand.nextInt() % 800);
		x_rand = Math.abs(x_rand);
		int y_rand = (rand.nextInt() % 600);
		y_rand = Math.abs(y_rand);// x,y자표를 랜덤으로 받습니다
		int x_rand2 = (int) Math.pow(x - x_rand, 2);
		int y_rand2 = (int) Math.pow(y - y_rand, 2);
		double dist = Math.sqrt(x_rand2 + y_rand2); // 캐릭터와 받아온 좌표의 거리를 구합니다
		int[] res = new int[2];
		if (dist > 100) {
			res[0] = x_rand;
			res[1] = y_rand;
			return res; // 받아온 좌표가 캐릭터와 100거리 밖에 있다면, 좌표를 리턴시킵니다
		} else {
			return GenerateXNY(); // 좌표가 캐릭터와 100거리안에있다면, 다시 좌표를 정해 리턴시킵니다.
		}
	}

	public void reset() { // 게임을 모두 초기상태로 돌립니다.
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
				mode = selectmode; // 엔터를 누르면 해당모드를 적용시킨 후 게임이 시작됩니다
			}
			if (mode == 1) { // 난이도별 설정값
				nodienum = 3;// 무적아이템 갯수
				dmappear = 150;// 세균맨 생성 주기
			} else if (mode == 2) {
				nodienum = 2;
				dmappear = 80;
			} else if (mode == 3) {
				nodienum = 0;
				dmappear = 20; // 고급에서는 세균맨의 생성 주기가 짧습니다
			}
		}
		if (p == true) { // p를 누르면 게임이 멈춥니다
			pause = !pause;
			p = false;
		}
	}

	public void boostcontrol() {
		if (space == false) { // 부스터 게이지를 통제하기 위한 if문
			if (boost < 40) {
				if (cnt % 20 == 0) {
					boost++; // 부스터게이지가 space를 떼고있을때는 천천히 충전됩니다
				}
			}
		} else {
			if (boost > 0) {
				boost--; // space를 누르면 부스터게이지가 줄어듭니다
			} else {
				space = false;
			}
		}
	}

	public void arrowkey() { // 캐릭터의 이동속도와 방향키에 따른 이동방향을 결정하고, 캐릭터를 화면
								// 밖으로못빠져나가가게합니다. 그리고 부스터 또한 통제합니다
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

	public void keyPressed(KeyEvent e) { // 방향키를 눌렀을때 눌렀다는 신호를 받아온다( 키입력을
											// true로만든다)

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
			} // 고급모드에서는 부스터가 사용되지않으므로
		} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			if (nodienum > 0) {
				nodiemode(200);
			} // 무적게이지를 200부터시작시킵니다.
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
	public void keyReleased(KeyEvent e) { // 키를 떼었을때 키입력을 false로 만든다

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

	public void keyTyped(KeyEvent e) { // 키타입 메서드 , 이소스에서쓰진않지만 keylistener를
										// 상속받으면 자동으로생기는거라 놔둠
	}

}
