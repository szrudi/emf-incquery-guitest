package org.eclipse.incquery.tooling.ui.tests.interfaces;

public interface Focusable<T> {
	T focus();
	T waitUntilCloses();
	T waitUntilCloses(int timeout);
}
