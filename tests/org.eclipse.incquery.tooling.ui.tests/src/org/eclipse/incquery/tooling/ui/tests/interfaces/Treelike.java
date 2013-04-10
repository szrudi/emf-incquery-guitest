package org.eclipse.incquery.tooling.ui.tests.interfaces;

public interface Treelike<T> {
	T choose(String... path);
	T doubleClick(String... path);
}
