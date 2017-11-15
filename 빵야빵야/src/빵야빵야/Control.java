package ���߻���;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;



/**
 * �� Ŭ������ Game Ŭ�������� KeyListener �� ���� ���� Ű ���� ���� Ű���� �Է��� �����ϴ� Ŭ�����Դϴ�.
 */
public class Control{
	boolean keyUp = false; 			// ���� ȭ��ǥ�� ������������ä���ִ�.
	boolean keyDown = false;        // �Ʒ��� ȭ��ǥ�� ������������ä���ִ�.
	boolean keyLeft = false;        // ���� ȭ��ǥ�� ������������ä���ִ�.
	boolean keyRight = false;       // ������ ȭ��ǥ�� ������������ä���ִ�.
	boolean space = false;          // �����̽� �ٰ� ������������ ä�� �ִ�.
	boolean shift = false;          // ����Ʈ
	boolean p = false;              // �Ͻ����� pause
	boolean et = false;             // ����
	boolean r = false;              // �����
	private Game game;              //Control �� ���� ���� ��ü
	
	Control(Game g){/*Control�� ������. ���� �������� Game�� ��ȣ���� �� �ش�.*/
		game = g;
	}
	public void arrowkey() {/*����Ű�� ���� ���� Ű ���� ���� �������� �����Ѵ�. ����Ű�� ���� �� ���� 4��ŭ�� �ӵ��� ���ϰ� �Ѵ�. ���� ���ȭ�鿡�� enterŰ�� ������ ������ �����Ѵ�.*/
		if (game.mode == 0) {
			if (keyLeft == true) {
				game.selectmode--;
				if (game.selectmode == 0) { game.selectmode = 3; }
				keyLeft = false;
			}
			if (keyRight == true) {
				game.selectmode++;
				if (game.selectmode == 4) { game.selectmode = 1; }
				keyRight = false;
			}
		} else {
			if (keyUp == true && game.pause == false) {
				if (game.y > 0){
					if (space == false) { game.player.p.y -= 4; }
				}
			}
			if (keyDown == true && game.pause == false) {
				if (game.y < 768) {
					if (space == false) { game.player.p.y += 4; }
				}
			}
			if (keyLeft == true && game.pause == false) {
				if (game.x > 0) {
					if (space == false) { game.player.p.x -= 4; }
				}
			}
			if (keyRight == true && game.pause == false) {
				if (game.x < 1024) {
					if (space == false) { game.player.p.x += 4; }
				}
			}
			if (et == true) { game.mode = game.selectmode; }
		}
	}
	
}
