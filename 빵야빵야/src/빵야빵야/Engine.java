package 빵야빵야;

import java.util.Random;
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
	
	public int[] GenerateXNY() { /*좌표를 랜덤으로 생성한다.*/
		Random rand = new Random();
		int x_rand = (rand.nextInt() % 1920);
		x_rand = Math.abs(x_rand);
		int y_rand = (rand.nextInt() % 1080);
		y_rand = Math.abs(y_rand);// x,y자표를 랜덤으로 받습니다
		int x_rand2 = (int) Math.pow(game.x - x_rand, 2);
		int y_rand2 = (int) Math.pow(game.y - y_rand, 2);
		double dist = Math.sqrt(x_rand2 + y_rand2); // 캐릭터와 받아온 좌표의 거리를 구한다.
		int[] res = new int[2];
		if (dist > 100) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
			res[0] = x_rand;
			res[1] = y_rand;
			return res; // 받아온 좌표가 캐릭터와 100거리 밖에 있다면, 좌표를 리턴시킵니다
		} else {
			return GenerateXNY(); // 좌표가 캐릭터와 100거리안에있다면, 다시 좌표를 정해 리턴시킵니다.
		}
	}
		
}


