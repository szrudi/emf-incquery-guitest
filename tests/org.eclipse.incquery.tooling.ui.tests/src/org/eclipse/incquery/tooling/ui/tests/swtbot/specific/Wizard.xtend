package org.eclipse.incquery.tooling.ui.tests.swtbot.specific

import org.eclipse.incquery.tooling.ui.tests.interfaces.Focusable
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.SwtBotComponent

import static org.eclipse.incquery.tooling.ui.tests.swtbot.basic.SwtBotComponent.*
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Window

class Wizard extends SwtBotComponent implements Focusable {
	var Window window
	
	new(String... path){
		bot.menu("File").menu("New").menu("Other...").click
		window = new Window("New")
		window.focus
		bot.tree().expandNode(path).select
		bot.button("Next >").click
	}

	override Wizard focus() {
		window.focus
		return this
	}
	
	override Wizard waitUntilCloses() {
		window.waitUntilCloses
		return this
	}
	
	override Wizard waitUntilCloses(int timeout) {
		window.waitUntilCloses(timeout)
		return this
	}
	
}
