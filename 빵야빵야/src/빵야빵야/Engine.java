package 빵야빵야;

import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import GameController.ServerEnemy;
import GameController.ServerFood;
import GameController.ServerPlayer;
import common.RequestPacket;

/**
 * 
 * 게임의 엔진을 담당하는 클래스.
 * 현재는 랜덤으로 객체를 생성하기 위한 좌표를 생성하는 함수가 담겨있다.
 *
 */
public class Engine {
	Game game;
	Engine(Game g){
		game = g;
	}
	
	public void plusScore(){
		game.player.score += 10;
		game.player.point++;
		game.player.total += 10;
		game.player.rank += 10;
		game.player.exp.push(10);
	}
	
	public void addHP(){
		game.player.hp ++;
		game.player.hp ++;
	}
	
	void Reciever(){
		Object obj;
		try {
			obj = game.inputStream.readObject();
			RequestPacket requestPacket = (RequestPacket) obj;
			Object[] arg = requestPacket.getArgs();
			
			game.enemy = (CopyOnWriteArrayList<ServerEnemy>)arg[1];
			game.food = (CopyOnWriteArrayList<ServerFood>)arg[2];
			
			if (requestPacket.getMethodName().equals("sendPlayer")) {
				game.playerOther = (Vector<ServerPlayer>) arg[0];
				for (ServerPlayer p : game.playerOther) {
					if (game.player.id.equals(p.getID())) {
						if(p.getIsEat()){
							game.engine.plusScore();
						}
						if(p.getIsHit()){
							System.out.println("is hit!");
							game.player.isHit = true;
							game.player.hp--;
						}
					} else {
						boolean isNew = true;
						for(int i=0;i<game.other.size();i++){
							OtherPlayer o = game.other.get(i);
							if(o.getID()!=null&&o.getID().equals(p.getID())){
								
								isNew = false;
								o.x = p.getX();
								o.y = p.getY();
								o.state = p.getState();
								if (p.getIsBullet() == true) {
									System.out.println("other is shooting!");
									game.otherBullet.create(o.x, o.y);
								}
								if(p.isDie()){
									o.x = -100;
									o.y = -100;
								}
							}
						}
						if(isNew){
							System.out.println("is New");
							OtherPlayer o = new OtherPlayer(game);
							o.setID(p.getID());
							game.other.add(o);
							o.x = p.getX();
							o.y = p.getY();
							o.state = p.getState();
							if (p.getIsBullet() == true) {
								System.out.println("other is shooting!");
								game.otherBullet.create(o.x, o.y);
							}
							if(p.isDie()){
								o.x = -100;
								o.y = -100;
							}
						}

					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void moveObject(){
		game.bullet.move();
		game.otherBullet.move();
	}
	
	public void addMagazine() { /* 총알을 충전하기 위한 메소드 */
		if (game.control.space == false) {
			if (game.player.boost < 40) {
				if (game.cnt % 200 == 0) {
					game.player.boost++; // cnt%100 마다 총알 충전
				}
			}
		} else {
			game.control.space = false;
		}
	}
}


