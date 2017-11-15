package 빵야빵야;

import java.net.Socket;

import javax.swing.JOptionPane;

public class PlayGame {
	/**
	 * 게임을 실행하는 클래스.
	 */

	public static void main(String[] ar) {
		Socket s;
		Game rg1;

		try {
			s = new Socket("localhost", 5553);

			if (s.isConnected()) {
				rg1 = new Game(s);

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "서버가 실행되지 않았습니다.");
		}
	}
}
