package Server;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import GameController.ServerBullet;
import GameController.ServerEngine;
import GameController.ServerPlayer;
import Generator.ServerGenerator;
import Generator.ServerEnemy;
import Generator.ServerFood;

public class Game implements Serializable{
	private Vector<ServerPlayer> player = null;
	private ArrayList<ServerEnemy>enemy = null;
	private ArrayList<ServerFood>food = null;
	private ServerGenerator generator;
	
	public static int MaxPlayer = 2;
	
	public Game(ServerManager server){
		ServerEngine.getTime();
		System.out.println("initializing....");
		player = new Vector<ServerPlayer>();
		enemy = new ArrayList<ServerEnemy>();
		food = new ArrayList<ServerFood>();
	}
	
	public void addPlayer(SocketManager manager){
		if(player.size()<=MaxPlayer){
			ServerPlayer p = new ServerPlayer(manager);
			player.add(p);
			int res[] = ServerEngine.GenerateXNY();
			p.setXY(res[0], res[1]);
			p.setState(0);
			p.setIsBullet(false);
			manager.setPlayer(p);
		}
	}
	
	public void updatePlayer(SocketManager manager, String id, int x,  int y, int state, boolean isBullet, int kill){
		synchronized (this) {
			for(ServerPlayer p : player){
				if(manager.getPlayer().equals(p)){
					if(p.getID()==null){
						p.setID(id);
					}
					p.setXY(x, y);
					p.setState(state);
					p.setIsBullet(isBullet);
					
					if(kill!=999){
						ServerEngine.getTime();
						System.out.println("hit Enemy!");
						removeEnemy(999);
					}
				}
			}
		}
	}
	
	public void removeEnemy(int index){
		enemy.remove(index);
	}
	
	public void removeBulletInfo(){
		for(ServerPlayer p : player){
			p.setIsBullet(false);
		}
	}
	
	public void removePlayer(String id){
		synchronized (this) {
			for(int i=0;i<player.size();i++){
				ServerPlayer p = player.get(i);
				if(p.getID().equals(id)){
					p.setXY(-150, -150);
					player.remove(i);
					break;
				}
			}
		}
	}
	
	public void findPlayer(String id) {
		for(ServerPlayer p : player){
			if(p.getID().equals(id)){
				p.setXY(-100, -100);
				
			}
		}
		removePlayer(id);
	}
	
	
	public void isEat(){
		synchronized (this) {
			for(ServerFood f : food) {
				synchronized (this) {
					for (ServerPlayer p : player) {
						int dis1 = (int) Math.pow((p.getX() + 25) - (f.getX() + 25), 2);
						int dis2 = (int) Math.pow((p.getY() + 25) - (f.getY() + 25), 2);
						double dist = Math.sqrt(dis1 + dis2); // 캐릭터와의 거리를 구하는 알고리즘

						if (dist < 25) { // 거리가 25이하로 줄어들게되면 음식이 사라지고 경험치가 늘어난다.
							p.setIsEat(true);
							food.remove(f);
							return;
						}
					}
				}
			}
		}
	}
	
	
	public void isHit(){
		synchronized (this) {
			for(ServerEnemy e : enemy) {
				synchronized (this) {
					for (ServerPlayer p : player) {
						int dis1 = (int) Math.pow((p.getX() + 10) - (e.getX() + 10), 2);
						int dis2 = (int) Math.pow((p.getY() + 15) - (e.getY()+ 15), 2);
						double dist = Math.sqrt(dis1 + dis2); //장애물과 자기자신의 거리를 계산한다.
						if (dist < 11) {
								p.setIsHit(true);
						}
					}
				}
			}
		}
	}
	
	/*
	public boolean isDie(){
		for(ServerBullet b: this.arr){
			int dis1 = (int) Math.pow((x + 10) - (b.x + 10), 2);
			int dis2 = (int) Math.pow((y + 15) - (b.y + 15), 2);
			double dist = Math.sqrt(dis1 + dis2); //장애물과 자기자신의 거리를 계산한다.
			if(dist<11){
				return true;
			}
		}
		return false;
	}
	*/
	
	public ArrayList<ServerEnemy> getEnemy(){
		return enemy;
	}
	
	public Vector<ServerPlayer> getPlayer(){
		return player;
	}

	public void initGenerator() {
		generator = new ServerGenerator(this);
		generator.start();
	}

	public ArrayList<ServerFood> getFood() {
		return food;
	}
}
