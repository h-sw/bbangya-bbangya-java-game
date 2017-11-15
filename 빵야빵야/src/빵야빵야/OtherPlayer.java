package 户具户具;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.Socket;

public class OtherPlayer {

	Game game;
	int x; 
	int y;
	int state=0;
	OtherPlayer(Game game, Socket socket){
		this.game = game;
		x = -100;
		y = -100;
	}
	
	public void drawing() {
		if(state==0){
			game.gc.drawImage(GUI.egg, x, y, game);
		}else if(state==1){
			game.gc.drawImage(GUI.nomal, x, y, game);
		}else if(state ==2){
			game.gc.drawImage(GUI.rare, x, y, game);
		}else if(state==3){
			game.gc.drawImage(GUI.unique, x, y, game);
		}else{
			game.gc.drawImage(GUI.regend, x, y, game);
		}
			
	}
	
}
