package 빵야빵야;

import java.util.Random;

class Enemy { // 세균맨의클래스
	int position[] = new int[2]; // 세균맨의 위치를 랜덤으로 받아오기 위한 변수
	private int velocity; // 세균맨의 이동속도를 조절하기 위한 변수
	private int dst[] = new int[2]; // 세균맨의 목적지를 정하기 위한 변수

	Enemy(int x, int y) {
		position[0] = x;
		position[1] = y;
		dst = GenerateXNY(); // 목적지를 랜덤으로 받아온다
		Random rand = new Random();
		velocity = (rand.nextInt() % 3) + 2; // 2와 4사이의 속도를 받아온다
	}

	int[] resetDst() { // 좌표를 다시 정해서 리턴시키는 메서드
		return GenerateXNY();
	}

	int[] GenerateXNY() { // 좌표를 랜덤으로 리턴시키는 메서드
		Random rand = new Random();
		int x_rand = (rand.nextInt() % 800);
		x_rand = Math.abs(x_rand);
		int y_rand = (rand.nextInt() % 600);
		y_rand = Math.abs(y_rand);
		int[] res = new int[2];
		res[0] = x_rand;
		res[1] = y_rand;
		return res;
	}

	public void move() { // 세균맨의 움직임을 통제하기 위한 메서드
		if (position[0] >= dst[0] && position[0] >= 0) {
			position[0] = position[0] - velocity;
		}
		if (position[0] <= dst[0] && position[0] <= 800) {
			position[0] = position[0] + velocity;
		}

		if (position[1] >= dst[1] && position[1] >= 0) {
			position[1] = position[1] - velocity;
		}
		if (position[1] <= dst[1] && position[1] <= 600) {
			position[1] = position[1] + velocity;
		} // 세균맨이 화면 밖으로 못벗어나는 범위 내에서 돌아다니게 만든다

		if (position[0] > 800) {
			dst = resetDst();
		}
		if (position[1] > 600) {
			dst = resetDst();
		}

		if (position[0] < 0) {
			dst = resetDst();
		}
		if (position[1] < 0) {
			dst = resetDst();
		} // 세균맨이 만에하나 화면밖으로 넘어갔을 경우에 화면반대편으로 나오게하고, 목적지를 재설정한다
		if (Math.abs(position[0] - dst[0]) <= velocity * 2) {
			dst = resetDst();
		}
		if (Math.abs(position[1] - dst[1]) <= velocity * 2) {
			dst = resetDst();
		}
		// 세균맨이 목적지 근처에 도착하였을 경우 목적지를 재설정한다
		if (position[0] > 800 || position[0] < velocity) {
			dst = resetDst();
		}
		if (position[1] > 600 || position[1] < velocity) {
			dst = resetDst();
		} // 세균맨이 화면밖으로 넘어갈 경우 목적지를 재설정한다
	}
}
