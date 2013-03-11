package org.eclipse.incquery.tooling.ui.tests;

import org.junit.Test;

public class NewProjectTests extends AbstractIncQueryTester{
	protected static String PROJECTNAME = "proba123";

	@Test
	public void test() {
		createIncQueryProject(PROJECTNAME);
		getTreeItemFromProject(PROJECTNAME);
	}
}
