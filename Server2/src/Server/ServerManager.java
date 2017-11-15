package Server;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import GameController.ServerEngine;
import GameController.ServerPlayer;

public class ServerManager implements Serializable{
	private boolean isRunning = true;
	private ServerSocket serverSocket;
	
	private Game game;
	
	public ServerManager(){
		game = new Game(this);
		
		try{
			serverSocket = new ServerSocket(5553);
			ServerEngine.getTime();
			System.out.println("Server Started..");
					
			while(isRunning){
				Socket socket = serverSocket.accept();
				if(socket.isConnected()){
					ServerEngine.getTime();
					System.out.println(socket.getInetAddress()+"立加..");
					SocketManager manager = new SocketManager(socket, this);
					game.addPlayer(manager);
					ServerEngine.getTime();
					System.out.println("立加 牢盔: "+game.getPlayer().size());
					manager.start();
					if(game.getPlayer().size()>=2){
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
