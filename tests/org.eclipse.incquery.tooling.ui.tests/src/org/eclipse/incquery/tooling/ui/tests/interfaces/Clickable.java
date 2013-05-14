package org.eclipse.incquery.tooling.ui.tests.interfaces;

public interface Clickable<T> {
	T click();
	boolean isInactive();
}
