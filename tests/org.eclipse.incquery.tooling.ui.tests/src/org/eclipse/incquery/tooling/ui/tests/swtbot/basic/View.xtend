package org.eclipse.incquery.tooling.ui.tests.swtbot.basic

import org.eclipse.incquery.tooling.ui.tests.interfaces.Focusable
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView

import static org.eclipse.incquery.tooling.ui.tests.swtbot.basic.SwtBotComponent.*

class View extends SwtBotComponent implements Focusable {
	val SWTBotView widget
	val String title
	
	new(String widgetName){
		this.widget = bot.viewByTitle(widgetName)
		this.title = widgetName
	}
	
	override View focus() {
		widget.setFocus
		return this
	}
	
	override waitUntilCloses(int timeout) {
		throw new UnsupportedOperationException("Auto-generated function stub")
	}
	override View waitUntilCloses(){
		waitUntilCloses(30000);
		return this
	}
	def SWTBotView getWidget(){
		return widget
	}
}
	