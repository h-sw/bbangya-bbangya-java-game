package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import GameController.ServerEngine;
import GameController.ServerPlayer;
import common.RequestPacket;
import common.RequestPacket.SYNC_TYPE;

public class SocketManager extends Thread implements Runnable{
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Game game;
	private Socket socket;
	private ServerPlayer player;

	private boolean isConnected = true;

	public SocketManager(Socket socket, ServerManager server) {
		this.socket = socket;
		game = server.getGame();
		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("초기화 실패.");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (isConnected) {
			try {
				socket.setTcpNoDelay(true);
				Object obj = inputStream.readObject();
				RequestPacket requestPacket = (RequestPacket) obj;

				if (requestPacket.getMethodName().equals("sendXY")) {
					Object[] args = requestPacket.getArgs();
					game.updatePlayer(this, (String) args[0], (int) args[1], (int) args[2], (int) args[3],
							(boolean) args[4], (int) args[5], (boolean) args[6]);
					sendInfo();
				}
			} catch (Exception e) {
				game.removePlayer(this);
				ServerEngine.getTime();
				System.out.println(socket.getInetAddress() + "접속 종료! 접속 인원: " + game.getPlayer().size());
				ServerEngine.getTime();
				System.out.println(e.getMessage());
				// e.printStackTrace();
				isConnected = false;
			}
		}
	}

	public void sendInfo() {
		try {
			RequestPacket packet = new RequestPacket();
			packet.setClassName("Socket");
			packet.setMethodName("sendPlayer");
			packet.setArgs(new Object[] { game.getPlayer(), game.getEnemy(), game.getFood() });
			packet.setSyncType(SYNC_TYPE.SYNCHRONOUS);
			outputStream.writeObject(packet);
			outputStream.flush();
			outputStream.reset();
			player.setIsEat(false);
			player.setIsHit(false);
			outputStream.reset();
		} catch (Exception e) {

		}
	}

	public void setPlayer(ServerPlayer p) {
		this.player = p;
	}

	public ServerPlayer getPlayer() {
		return this.player;
	}

	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}

}
