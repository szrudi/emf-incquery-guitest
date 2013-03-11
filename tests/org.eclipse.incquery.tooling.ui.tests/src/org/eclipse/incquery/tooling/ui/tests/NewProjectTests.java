package org.eclipse.incquery.tooling.ui.tests;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.junit.Test;

public class NewProjectTests extends AbstractSwtBotTester{
	protected static String PROJECTNAME = "proba123";

	@Test
	public void test() {
		createIncQueryProject(PROJECTNAME);
		SWTBotView projectExplorer = bot.viewByTitle("Project Explorer");
		projectExplorer.bot().tree().getTreeItem(PROJECTNAME);
	}
}
