class Pair<E, V> {

	private final E _left;
	private final V _right;

	Pair(E e, V v) {
		_left = e;
		_right = v;
	}
	
	E getLeft() {
		return _left;
	}

	V getRight() {
		return _right;
	}

}
