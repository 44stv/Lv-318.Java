package main.java.tasks;

public class FriendPairs {

	public long countOfPairs(int n) {
		return c(n, 2)+1;
	}

	private long factorial(int n) {

		long valueToReturn = 1;

		if (n > 0) {
			for (int i = 1; i <= n; i++) {
				valueToReturn = valueToReturn * i;
			}
		}

		return valueToReturn;

	}

	private long c(int n, int k) {
		return factorial(n) / (factorial(k) * factorial(n - k));
	}

}