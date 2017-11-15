package Generator;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class ServerFood implements Serializable{
	protected static final long serialVersionUID = 1L;
	int position[] = new int[2];
	
	ServerFood(int x, int y) {
		position[0] = x;
		position[1] = y;
	}
	
	public void move() {/*음식의 위치를 지정해주고, 한번 지정되면 바꾸지 않는다.*/

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
