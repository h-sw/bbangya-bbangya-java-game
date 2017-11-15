package 빵야빵야;
/**
 * Stack의 형태로 경험치를 구현하는 클래스.
 */
public class Exp {
	private int[] data; // Stack 배열 선언.
	private int top; // Stack의 초점
	private int dataSize; // Stack의 크기.

	public Exp(int dataSize) {
		this.dataSize = dataSize;
		this.top = -1;
		this.data = new int[dataSize];
	}
	
	public void clear() {/*배열을 초기화 시킨다.*/
		this.top = -1;
	}

	public void push(int value) {/*Stack을 하나 추가한다. 경험치가 늘어난다.*/
		this.data[++this.top] = value;

	}

	public boolean isFull() { /*경험치가 꽉찼는지 확인.*/
		if (top == dataSize - 1) {
			return true;
		} else {
			return false;
		}
	}

	public int getTop() { /*현재 경험치가 얼마나 쌓였는지 불러온다.*/
		return (this.top);
	}
}
