package ���߻���;

import java.util.Random;
/**
 * 
 * ������ ������ ����ϴ� Ŭ����.
 * ����� �������� ��ü�� �����ϱ� ���� ��ǥ�� �����ϴ� �Լ��� ����ִ�.
 *
 */
public class Engine {
	Game game;
	Engine(Game g){
		game = g;
	}
	
	public int[] GenerateXNY() { /*��ǥ�� �������� �����Ѵ�.*/
		Random rand = new Random();
		int x_rand = (rand.nextInt() % 1920);
		x_rand = Math.abs(x_rand);
		int y_rand = (rand.nextInt() % 1080);
		y_rand = Math.abs(y_rand);// x,y��ǥ�� �������� �޽��ϴ�
		int x_rand2 = (int) Math.pow(game.x - x_rand, 2);
		int y_rand2 = (int) Math.pow(game.y - y_rand, 2);
		double dist = Math.sqrt(x_rand2 + y_rand2); // ĳ���Ϳ� �޾ƿ� ��ǥ�� �Ÿ��� ���Ѵ�.
		int[] res = new int[2];
		if (dist > 100) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
			res[0] = x_rand;
			res[1] = y_rand;
			return res; // �޾ƿ� ��ǥ�� ĳ���Ϳ� 100�Ÿ� �ۿ� �ִٸ�, ��ǥ�� ���Ͻ�ŵ�ϴ�
		} else {
			return GenerateXNY(); // ��ǥ�� ĳ���Ϳ� 100�Ÿ��ȿ��ִٸ�, �ٽ� ��ǥ�� ���� ���Ͻ�ŵ�ϴ�.
		}
	}
		
}


