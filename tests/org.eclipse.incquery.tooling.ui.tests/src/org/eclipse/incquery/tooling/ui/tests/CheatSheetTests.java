package org.eclipse.incquery.tooling.ui.tests;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class CheatSheetTests {

	private static SWTWorkbenchBot bot = new SWTWorkbenchBot();
	private static String GETTING_STARTED = "Getting Started with EMF-IncQuery";
	private static String INCQUERY = "EMF-IncQuery";
	private static String CHEATSHEET = "Cheat Sheets...";
	
	@BeforeClass
	public static void beforeClass(){
		bot.viewByTitle("Welcome").close();
		SWTBotPreferences.PLAYBACK_DELAY = 200;
	}
	
	@Test
	public void testIncQCheatsheetsExists() {
		bot.menu("Help").menu(CHEATSHEET).click();
		bot.shell("Cheat Sheet Selection").activate();
		bot.tree().getTreeItem(INCQUERY).expand().select(GETTING_STARTED);
		bot.button("OK").click();
		bot.viewByTitle("Cheat Sheets")
			.bot().treeWithLabel(GETTING_STARTED);
	}
	

    @AfterClass
    public static void sleep() {
    	//bot.sleep(2000);
    }
}
