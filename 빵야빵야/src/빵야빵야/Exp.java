package 户具户具;

public class Exp {
	private int[] data;
	private int top;
	private int dataSize;

	public Exp(int dataSize) {
		this.dataSize = dataSize;
		this.top = -1;
		this.data = new int[dataSize];
	}
	
	public void clear() {
		this.top = -1;
	}

	public void push(int value) {
		this.data[this.top++] = value;

	}

	public boolean isFull() {
		if (top == dataSize - 1) {
			return true;
		} else {
			return false;
		}
	}

	public int getTop() {
		return (this.top);
	}
}
