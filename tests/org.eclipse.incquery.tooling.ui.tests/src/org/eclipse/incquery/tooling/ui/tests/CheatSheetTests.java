package org.eclipse.incquery.tooling.ui.tests;

import org.junit.Test;

public class CheatSheetTests extends AbstractIncQueryTester {

	private static String GETTING_STARTED = "Getting Started with EMF-IncQuery";
	private static String INCQUERY = "EMF-IncQuery";
	private static String CHEATSHEET = "Cheat Sheets...";
	
	@Test
	public void testIncQCheatsheetsExists() {
		bot.menu("Help").menu(CHEATSHEET).click();
		bot.tree().getTreeItem(INCQUERY).expand().select(GETTING_STARTED);
		bot.button("OK").click();
		bot.viewByTitle("Cheat Sheets")
			.bot().treeWithLabel(GETTING_STARTED);
	}
}
