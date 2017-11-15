package ���߻���;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
/**
 * ����ġ�� �ױ� ���� ������ ������ �޾ƿ��� Ŭ����
 * ���Ӵ� Food(game) ������ �ϳ��� �ҷ�����  �迭���� ������ �߰����ش�.
 * �ٸ� ���ӿ��� �����Ǵ� ���� �����ϱ� ����.
 */
class Food {
	int position[] = new int[2]; 											//������ �ʱ� ��ǥ
	Game game;																//���� �迭�� ���� �ִ� ����
	ArrayList arr; 															//�ڱ��ڽ��� �迭�� ����
	static Image foodimg = new ImageIcon("Img/food.png").getImage(); 		//������ �̹����� �ҷ��´�.
	
	Food(Game g){
		arr = new ArrayList();
		game = g;
	}
	
	Food(int x, int y) {
		position[0] = x;
		position[1] = y;
	}
	
	public void DrawImg() {	/*�迭�� ����� ������ ȭ�鿡 �׷��ش�.*/
		for (int i = 0; i < arr.size(); ++i) {
			Food pa = (Food) (arr.get(i));
			game.gc.drawImage(foodimg, pa.position[0], pa.position[1], game);
		}
	}
	
	
	public void move() {/*������ ��ġ�� �������ְ�, �ѹ� �����Ǹ� �ٲ��� �ʴ´�.*/
		for (int i = 0; i < arr.size(); ++i) {
			Food food = (Food) (arr.get(i)); // �����ʸ� �߰��Ѵ�

			int dis1 = (int) Math.pow((game.player.p.x + 25) - (food.position[0] + 25), 2);
			int dis2 = (int) Math.pow((game.player.p.y + 25) - (food.position[1] + 25), 2);

			double dist = Math.sqrt(dis1 + dis2); // ĳ���Ϳ��� �Ÿ��� ���ϴ� �˰���

			if (dist < 25) { // �Ÿ��� 25���Ϸ� �پ��ԵǸ� ������ ������� ����ġ�� �þ��.
				if (game.mode == 1) {
					game.player.score += 10;
					game.player.point++;
					game.player.total += 10;
					game.player.rank += 10;
					game.player.exp.push(10);

				}
				arr.remove(i);
			}

		}
		if ((game.cnt) % 60 == 0) {
			int[] r = game.engine.GenerateXNY(); // �������� ��ǥ�� �޾ƿ´�
			Food food = new Food(r[0], r[1]);    // �޾ƿ� ��ǥ�� ������ �߰���Ų��.
			arr.add(food);                       // cnt/60�� �ð��� ���������� �ϳ��� ������ ȭ�鿡 �߰��Ѵ�
		}
	}
}

