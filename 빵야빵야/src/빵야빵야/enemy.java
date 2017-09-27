package ���߻���;

import java.util.Random;

class Enemy { // ���ո���Ŭ����
	int position[] = new int[2]; // ���ո��� ��ġ�� �������� �޾ƿ��� ���� ����
	private int velocity; // ���ո��� �̵��ӵ��� �����ϱ� ���� ����
	private int dst[] = new int[2]; // ���ո��� �������� ���ϱ� ���� ����

	Enemy(int x, int y) {
		position[0] = x;
		position[1] = y;
		dst = GenerateXNY(); // �������� �������� �޾ƿ´�
		Random rand = new Random();
		velocity = (rand.nextInt() % 3) + 2; // 2�� 4������ �ӵ��� �޾ƿ´�
	}

	int[] resetDst() { // ��ǥ�� �ٽ� ���ؼ� ���Ͻ�Ű�� �޼���
		return GenerateXNY();
	}

	int[] GenerateXNY() { // ��ǥ�� �������� ���Ͻ�Ű�� �޼���
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

	public void move() { // ���ո��� �������� �����ϱ� ���� �޼���
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
		} // ���ո��� ȭ�� ������ ������� ���� ������ ���ƴٴϰ� �����

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
		} // ���ո��� �����ϳ� ȭ������� �Ѿ�� ��쿡 ȭ��ݴ������� �������ϰ�, �������� �缳���Ѵ�
		if (Math.abs(position[0] - dst[0]) <= velocity * 2) {
			dst = resetDst();
		}
		if (Math.abs(position[1] - dst[1]) <= velocity * 2) {
			dst = resetDst();
		}
		// ���ո��� ������ ��ó�� �����Ͽ��� ��� �������� �缳���Ѵ�
		if (position[0] > 800 || position[0] < velocity) {
			dst = resetDst();
		}
		if (position[1] > 600 || position[1] < velocity) {
			dst = resetDst();
		} // ���ո��� ȭ������� �Ѿ ��� �������� �缳���Ѵ�
	}
}
