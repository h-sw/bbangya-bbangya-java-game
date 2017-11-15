package 빵야빵야;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
/**
 * 경험치를 쌓기 위한 음식의 정보를 받아오는 클래스
 * 게임당 Food(game) 생성자 하나를 불러오고  배열에다 음식을 추가해준다.
 * 다른 게임에서 교란되는 것을 방지하기 위함.
 */
class Food {
	int position[] = new int[2]; 											//음식의 초기 좌표
	Game game;																//음식 배열이 속해 있는 게임
	ArrayList arr; 															//자기자신을 배열로 가짐
	static Image foodimg = new ImageIcon("Img/food.png").getImage(); 		//음식의 이미지를 불러온다.
	
	Food(Game g){
		arr = new ArrayList();
		game = g;
	}
	
	Food(int x, int y) {
		position[0] = x;
		position[1] = y;
	}
	
	public void DrawImg() {	/*배열에 저장된 음식을 화면에 그려준다.*/
		for (int i = 0; i < arr.size(); ++i) {
			Food pa = (Food) (arr.get(i));
			game.gc.drawImage(foodimg, pa.position[0], pa.position[1], game);
		}
	}
	
	
	public void move() {/*음식의 위치를 지정해주고, 한번 지정되면 바꾸지 않는다.*/
		for (int i = 0; i < arr.size(); ++i) {
			Food food = (Food) (arr.get(i)); // 경험초를 추가한다

			int dis1 = (int) Math.pow((game.player.p.x + 25) - (food.position[0] + 25), 2);
			int dis2 = (int) Math.pow((game.player.p.y + 25) - (food.position[1] + 25), 2);

			double dist = Math.sqrt(dis1 + dis2); // 캐릭터와의 거리를 구하는 알고리즘

			if (dist < 25) { // 거리가 25이하로 줄어들게되면 음식이 사라지고 경험치가 늘어난다.
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
			int[] r = game.engine.GenerateXNY(); // 랜덤으로 좌표를 받아온다
			Food food = new Food(r[0], r[1]);    // 받아온 좌표에 음식을 추가시킨다.
			arr.add(food);                       // cnt/60의 시간이 지날때마다 하나의 음식을 화면에 추가한다
		}
	}
}

