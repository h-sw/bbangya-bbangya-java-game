package ���߻���;


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

/**
 * 
 * ������ ���� �� �� Ȱ���� �ҷ����� Ŭ����.
 * ���� �̱��� �� DB�� �ڷᰡ ����� ����.
 *
 */

class GameOver extends Frame implements ActionListener {

	Label score = new Label();
	Label rank = new Label();
	Button output = new Button("�ְ�ŷ Ȯ��");
	Button restart = new Button("�����");
	String ini = "";
	String x;

	GameOver(int p) {
		x = Integer.toString(p);
		setLocation(250, 80);
		setResizable(false);
		setBackground(Color.white);
		add(score);
		add(output);
		add(rank);
		add(restart);
		ini = "���ھ�  " + p;
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
			BufferedWriter in = new BufferedWriter(new FileWriter("out.txt", true));
			in.write(x);
			in.newLine();
			in.close();

		} catch (IOException e1) {
			System.err.println(e1);
			System.exit(1);
		}
		if (e.getActionCommand().equals("�ְ�ŷ Ȯ��")) {
			ArrayList<Integer> aList = new ArrayList();
			String l;
			String str;
			int i;
			try {
				BufferedReader out = new BufferedReader(new FileReader("out.txt"));

				while ((l = out.readLine()) != null) { 
					i = Integer.parseInt(l);
					aList.add(i);
				}
				out.close();

			} catch (IOException e1) {
				System.err.println(e1);
				System.exit(1);
			}
			Collections.sort(aList);
			for (int a : aList) {
				str = Integer.toString(a);
				rank.setText("�ְ� ��� : " + str);
			}

		}

		if (e.getActionCommand().equals("�����")) {

		
			dispose();
			setVisible(false);
		}

	}

}