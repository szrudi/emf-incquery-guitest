package org.eclipse.incquery.tooling.ui.tests;

import static org.junit.Assert.assertNotNull;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.BeforeClass;
import org.junit.Test;

public class CheatSheetTests {

	private static SWTWorkbenchBot bot = new SWTWorkbenchBot();
	private static String GETTING_STARTED = "Getting Started with EMF-IncQuery";
	private static String INCQUERY = "EMF-IncQuery";
	private static String CHEATSHEET = "Cheat Sheets...";

	@BeforeClass
	public static void beforeClass(){
		bot.viewByTitle("Welcome").close();
	}
	
	@Test
	public void testIncQCheatsheetsExists() {
		// Arrange
		bot.menu("Help").menu(CHEATSHEET).click();

        // Act
		SWTBotTree tree = bot.tree();
		SWTBotTreeItem item = tree.getTreeItem(INCQUERY);
		bot.button("Cancel").click();
		
        // Assert
		assertNotNull(item);
	}
	
	@Test
	public void testIncQCheatsheeetsOpen(){
		// Arrange
		bot.menu("Help").menu(CHEATSHEET).click();
		bot.shell("Cheat Sheet Selection").activate();
		bot.tree().getTreeItem(INCQUERY).expand().select(GETTING_STARTED);
		bot.button("OK").click();
		
		// Act
		SWTBotView cheatsheet = bot.viewByTitle("Cheat Sheets");
		
		// Assert
		assertNotNull(cheatsheet);
	}
	
	@Test
	public void testIncQCheatsheeetsShows(){
		// Arrange
		bot.menu("Help").menu(CHEATSHEET).click();
		bot.tree().getTreeItem(INCQUERY).expand().select(GETTING_STARTED);
		bot.shell("Cheat Sheet Selection").activate();
		bot.button("OK").click();
		
		// Act
		SWTBotTree tree = bot.treeWithLabel(GETTING_STARTED);
		
		// Assert
		assertNotNull(tree);
	}
}
