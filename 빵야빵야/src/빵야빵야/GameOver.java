package 빵야빵야;


import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

class GameOver extends Frame implements ActionListener {

	Label score = new Label();
	Label rank = new Label();
	Button output = new Button("최고랭킹 확인");
	Button restart = new Button("재시작");
	String ini = "";
	String x; // 다른 메소드에서 정수형 변수 p를 txt파일로 저장하기 위한 스트링 변수

	GameOver(int p) {
		x = Integer.toString(p); // 정수형 p값을 스트링으로 변환
		setLocation(250, 80);
		setResizable(false);
		setBackground(Color.white);
		add(score);
		add(output);
		add(rank);
		add(restart);
		ini = "스코어  " + p;
		score.setText(ini);
		score.setFont(getFont());

		setLayout(new GridLayout(4, 1));
		output.addActionListener(this);
		restart.addActionListener(this);
		setSize(300, 200);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
		try {
			BufferedWriter in = new BufferedWriter(new FileWriter("out.txt", true)); // out.txt
																						// 파일
																						// 생성

			in.write(x); // 획득한 점수들 txt파일에 저장
			in.newLine();
			in.close();

		} catch (IOException e1) {
			System.err.println(e1); // 에러가 있다면 메시지 출력
			System.exit(1);
		}
		if (e.getActionCommand().equals("최고랭킹 확인")) {
			ArrayList<Integer> aList = new ArrayList(); // 점수들 넣기 위한 배열리스트
			String l;
			String str;
			int i;
			try {
				BufferedReader out = new BufferedReader(new FileReader("out.txt")); // 텍스트
																					// 파일
																					// 읽기
				while ((l = out.readLine()) != null) { // 텍스트 파일에 공백이 없다면
					i = Integer.parseInt(l); // 스트링값을 정수형 변수로 바꾼 뒤
					aList.add(i); // 배열리스트에 추가
				}
				out.close();

			} catch (IOException e1) {
				System.err.println(e1); // 에러가 있다면 메시지 출력
				System.exit(1);
			}
			Collections.sort(aList); // 점수들을 정렬시켜 제일 최고점수를 찾는다.
			for (int a : aList) { // 배열리스트 안에 있는 정수형 변수들을
				str = Integer.toString(a); // 스트링으로 변환시켜서
				rank.setText("최고 기록 : " + str); // 최고 점수를 출력한다.
			}

		}

		if (e.getActionCommand().equals("재시작")) {

			Game rg = new Game();
			dispose();
			setVisible(false);
		}

	}

}