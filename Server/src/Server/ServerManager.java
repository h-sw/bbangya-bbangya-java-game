package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import GameController.ServerEngine;


public class ServerManager{
	private boolean isRunning = true;
	private ServerSocket serverSocket;
	
	private Game game;
	
	public ServerManager(){		
		game = new Game(this,2);
		
		try{
			serverSocket = new ServerSocket(5553);
			ServerEngine.getTime();
			System.out.println("Server Started..");
					
			while(isRunning){
				Socket socket = serverSocket.accept();
				if(socket.isConnected()){
					ServerEngine.getTime();
					System.out.println(socket.getInetAddress()+"����..");
					SocketManager manager = new SocketManager(socket, this);
					game.addPlayer(manager);
					ServerEngine.getTime();
					System.out.println("���� �ο�: "+game.getPlayer().size());
					manager.start();
					if(game.isGenerator()==false&&game.getPlayer().size()>=2){
						ServerEngine.getTime();
						System.out.println("start Generator");
						game.initGenerator();
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
			isRunning = false;
		}
	}
	
	public boolean isConnected(){
		return isRunning;
	}
	
	public Game getGame(){
		return game;
	}
}
