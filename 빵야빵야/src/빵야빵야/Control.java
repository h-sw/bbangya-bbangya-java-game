package 빵야빵야;

/**
 * 위 클래스는 Game 클래스에서 KeyListener 를 통해 받은 키 값을 통해 키보드 입력을 제어하는 클래스입니다.
 */
public class Control{
	boolean keyUp = false; 			// 위쪽 화살표가 눌러지지않은채로있다.
	boolean keyDown = false;        // 아래쪽 화살표가 눌러지지않은채로있다.
	boolean keyLeft = false;        // 왼쪽 화살표가 눌러지지않은채로있다.
	boolean keyRight = false;       // 오른쪽 화살표가 눌러지지않은채로있다.
	boolean space = false;          // 스페이스 바가 눌러지지않은 채로 있다.
	boolean shift = false;          // 쉬프트
	boolean p = false;              // 일시정지 pause
	boolean et = false;             // 엔터
	boolean r = false;              // 재시작
	private Game game;              //Control 이 갖는 게임 객체
	
	Control(Game g){/*Control의 생성자. 현재 실행중인 Game을 상호참조 해 준다.*/
		game = g;
	}
	public void arrowkey() {/*방향키를 눌러 받은 키 값을 통해 움직임을 제어한다. 방향키를 누를 때 마다 4만큼의 속도를 지니게 한다. 게임 대기화면에서 enter키를 누르면 게임을 시작한다.*/
		if (game.mode == 0) {

		} else {
			if (keyUp == true && game.pause == false) {
				if (game.player.p.y > 0){
					if (space == false) { game.player.p.y -= 4; }
				}
			}
			if (keyDown == true && game.pause == false) {
				if (game.player.p.y < 750) {
					if (space == false) { game.player.p.y += 4; }
				}
			}
			if (keyLeft == true && game.pause == false) {
				if (game.player.p.x > 0) {
					if (space == false) { game.player.p.x -= 4; }
				}
			}
			if (keyRight == true && game.pause == false) {
				if (game.player.p.x < 1000) {
					if (space == false) { game.player.p.x += 4; }
				}
			}
		}
	}
	
}
