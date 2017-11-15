package GameController;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import Server.SocketManager;

public class ServerPlayer implements Serializable{
	protected static final long serialVersionUID = 1L;
	String id = null;										//플레이어 아이디					
	private int x;
	private int y;
	private int state;
	private boolean is_bullet = false;				//총알 발사 여부
	private boolean is_die = false;                  //죽은 여부
	private boolean is_eat = false;
	private boolean is_hit = false;
	
//	private SocketManager mySocketManager;

	public ServerPlayer(SocketManager manager){
//		mySocketManager = manager;
//		mySocketManager.setPlayer(this);
	}
	
	public String getID(){
		return id;
	}
	
	public void setXY(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getState(){
		return state;
	}
	
	public void setState(int n){
		state = n;
	}

	public void setIsBullet(boolean b) {
		this.is_bullet = b;
	}
	
	public boolean getIsBullet(){
		return is_bullet;
	}
	
	public boolean isDie(){
		return is_die;
	}

	public void setID(String id) {
		this.id = id;
	}
	
	public void setIsEat(boolean b){
		is_eat = b;
	}
	
	public boolean getIsEat(){
		return is_eat;
	}

	public void setIsHit(boolean b) {
		is_hit = b;
		
	}
	
	public boolean getIsHit(){
		return is_hit;
	}
}
