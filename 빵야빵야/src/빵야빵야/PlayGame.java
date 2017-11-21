package 빵야빵야;

import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

public class PlayGame {
	/**
	 * 게임을 실행하는 클래스.
	 * @throws IOException 
	 */

	public static void main(String[] ar) throws IOException {
		try {
			Socket socket = new Socket("localhost", 5553);

			if (socket.isConnected()) {
				socket.setTcpNoDelay(true);
				new Game(socket);

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "서버가 실행되지 않았습니다.");
		}
	}
}
