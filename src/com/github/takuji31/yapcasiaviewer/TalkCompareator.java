package com.github.takuji31.yapcasiaviewer;

import java.util.Comparator;

public class TalkCompareator implements Comparator<Talk> {

	@Override
	public int compare(Talk lhs, Talk rhs) {
		return (int) (lhs.startOn.getTime() - rhs.startOn.getTime());
	}

}
