package Generator;

import GameController.ServerEngine;
import Server.Game;

public class ServerGenerator extends Thread {
	private Game game;
	int cnt = 0;
	int dmappear = 700;
	int count = 0;

	public ServerGenerator(Game game) {
		this.game = game;
	}

	public void StartGenerator() {
		if (game != null) {

		} else {
			System.out.println("������ ������� �ʾҽ��ϴ�.");
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(20);
				cnt++;
				genEnemy();
				genFood();
				game.isEat();
				game.isHit();
				moveEnemy();
				if(game.getPlayer().size()!=game.MaxPlayer){
					game.getEnemy().clear();
					while(game.getPlayer().size()==game.MaxPlayer){
						
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void genEnemy() {

		if (count < 10 && (cnt) % dmappear == 0) {
			count++;
			int[] r = ServerEngine.GenerateXNY(); // ��ǥ�� �������� �޾ƿ´�.
			ServerEnemy en = new ServerEnemy(r[0], r[1]); // �޾ƿ� ��ǥ�� ��ֹ��� ����
															// �߰��Ѵ�.
			game.getEnemy().add(en);
		}
	}

	public void moveEnemy() {
		for (ServerEnemy e : game.getEnemy()) {
			e.move();
		}
	}
	
	public void genFood(){
		if ((cnt) % 90 == 0) {
			int[] r = ServerEngine.GenerateXNY(); // ��ǥ�� �������� �޾ƿ´�.
			ServerFood food = new ServerFood(r[0], r[1]); // �޾ƿ� ��ǥ�� ��ֹ��� ���� �߰��Ѵ�.
			game.getFood().add(food);
		}
	}
}
