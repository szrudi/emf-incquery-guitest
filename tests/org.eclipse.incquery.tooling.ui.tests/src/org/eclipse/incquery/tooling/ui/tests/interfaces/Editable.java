package org.eclipse.incquery.tooling.ui.tests.interfaces;

public interface Editable<T> {
	T setText(String text);
	T saveAndClose();
	T save();
}
