package org.eclipse.incquery.tooling.ui.tests.interfaces;

public interface Treelike<T> {
	T choose(String... path);
	T activate(String... path);
	boolean hasItem(String... path);
}
