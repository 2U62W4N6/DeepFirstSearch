package graph;

import java.util.Comparator;

public class SortId<V extends Vertex> implements Comparator<V>{

	@Override
	public int compare(V o1, V o2) {
		if (o1.getId() < o2.getId()) {
			return -1;
		} else if (o1.getId() > o2.getId()) {
			return 1;
		} else {
			return 0;
		}
	}
}
