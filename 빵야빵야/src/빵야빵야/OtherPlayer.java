package 户具户具;

public class OtherPlayer {

	Game game;
	String id;
	int x; 
	int y;
	int state=0;
	OtherPlayer(Game game){
		id = null;
		this.game = game;
		x = -100;
		y = -100;
	}
	
	public void drawing() {
		if(state==0){
			game.gc.drawImage(GUI.egg_enemy, x, y, game);
		}else if(state==1){
			game.gc.drawImage(GUI.nomal_enemy, x, y, game);
		}else if(state ==2){
			game.gc.drawImage(GUI.rare_enemy, x, y, game);
		}else{
			game.gc.drawImage(GUI.unique_enemy, x, y, game);
		}
	}

	public String getID() {
		// TODO Auto-generated method stub
		return id;
	}

	public void setID(String id) {
		this.id = id;
		
	}
	
}
