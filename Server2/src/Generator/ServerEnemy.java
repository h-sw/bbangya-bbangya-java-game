package Generator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import GameController.ServerEngine;

public class ServerEnemy implements Serializable{
	protected static final long serialVersionUID = 1L;
	int position[] = new int[2]; // 장애물의 위치를 랜덤으로 받아오기 위한 변수
	private int velocity; // 장애물의 이동속도를 조절하기 위한 변수
	private int dst[] = new int[2]; // 이동경로를 저장하기 위한 변수
	
	int[] resetDst() { /* 좌표를 다시 계산해 리턴시킨다. */
		return ServerEngine.GenerateXNY();
	}

	ServerEnemy(int x, int y) {/* 장애물의 생성자 */
		position[0] = x;
		position[1] = y;
		dst = ServerEngine.GenerateXNY(); // 목적지를 랜덤으로 받아온다
		Random rand = new Random();
		velocity = (rand.nextInt() % 3) + 2; // 2와 4사이의 속도를 받아온다
	}
	
	public void move() { /* 장애물을 움직인다. */
		if (position[0] >= dst[0] && position[0] >= 0) {
			position[0] = position[0] - velocity;
		}
		if (position[0] <= dst[0] && position[0] <= 1024) {
			position[0] = position[0] + velocity;
		}

		if (position[1] >= dst[1] && position[1] >= 0) {
			position[1] = position[1] - velocity;
		}
		if (position[1] <= dst[1] && position[1] <= 768) {
			position[1] = position[1] + velocity;
		} // 장애물이 화면 밖으로 빠져나가지 못하게 한다.

		if (position[0] > 1024) {
			dst = resetDst();
		}
		if (position[1] > 768) {
			dst = resetDst();
		}

		if (position[0] < 0) {
			dst = resetDst();
		}
		if (position[1] < 0) {
			dst = resetDst();
		} // 장애물이 만에하나 화면밖으로 넘어갔을 경우에 화면반대편으로 나오게하고, 목적지를 재설정한다
		if (Math.abs(position[0] - dst[0]) <= velocity * 2) {
			dst = resetDst();
		}
		if (Math.abs(position[1] - dst[1]) <= velocity * 2) {
			dst = resetDst();
		}
		// 장애물이 목적지 근처에 도착하였을 경우 목적지를 재설정한다
		if (position[0] > 1024 || position[0] < velocity) {
			dst = resetDst();
		}
		if (position[1] > 768 || position[1] < velocity) {
			dst = resetDst();
		} // 장애물이 화면밖으로 넘어갈 경우 목적지를 재설정한다
	}

	public int getX() {
		// TODO Auto-generated method stub
		return position[0];
	}
	public int getY() {
		// TODO Auto-generated method stub
		return position[1];
	}
}
