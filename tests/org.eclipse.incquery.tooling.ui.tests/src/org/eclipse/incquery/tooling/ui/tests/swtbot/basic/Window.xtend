package org.eclipse.incquery.tooling.ui.tests.swtbot.basic

import org.eclipse.incquery.tooling.ui.tests.interfaces.Focusable
import org.eclipse.swt.SWTException
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell

import static org.eclipse.incquery.tooling.ui.tests.swtbot.basic.SwtBotComponent.*
import static org.eclipse.swtbot.swt.finder.waits.Conditions.*

class Window extends SwtBotComponent implements Focusable {
	val SWTBotShell widget
	
	new(String widgetName){
		this.widget = bot.shell(widgetName)
	}
	
	override Window focus() {
		widget.activate
		return this
	}
	 
	override Window waitUntilCloses(int timeout){
		try {
			bot.waitUntil(shellCloses(widget), timeout);	
		} catch (SWTException e){
			println(e.message)
		}
		return this
	}
	override Window waitUntilCloses(){
		return waitUntilCloses(30000);
	}
	def getWidget() {
		return widget
	}
}
	