package GameController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
/**
 * 
 * 게임의 엔진을 담당하는 클래스.
 * 현재는 랜덤으로 객체를 생성하기 위한 좌표를 생성하는 함수가 담겨있다.
 *
 */
public class ServerEngine {
	public static int[] GenerateXNY() { /*좌표를 랜덤으로 생성한다.*/
		Random rand = new Random();
		int x_rand = (rand.nextInt() % 1024);
		x_rand = Math.abs(x_rand);
		int y_rand = (rand.nextInt() % 768);
		y_rand = Math.abs(y_rand);
		int[] res = new int[2];
		res[0] = x_rand;
		res[1] = y_rand;
		return res;
	}
	
	public static void getTime(){
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
		System.out.print("["+sdf.format(dt).toString()+"] ");
	}
}


