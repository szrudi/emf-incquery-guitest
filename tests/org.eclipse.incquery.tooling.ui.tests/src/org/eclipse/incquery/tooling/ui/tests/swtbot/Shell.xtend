package org.eclipse.incquery.tooling.ui.tests.swtbot

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell
import org.eclipse.swt.SWTException
import org.eclipse.incquery.tooling.ui.tests.interfaces.Focusable

import static org.eclipse.incquery.tooling.ui.tests.swtbot.SwtBotComponent.*

import static extension org.eclipse.swtbot.swt.finder.waits.Conditions.*

class Shell extends SwtBotComponent implements Focusable {
	val SWTBotShell widget;
	
	new(String widgetName){
		this.widget = bot.shell(widgetName);
	}
	
	override Shell focus() {
		widget.activate
		return this
	}
	 
	override Shell waitUntilCloses(int timeout){
		try {
			bot.waitUntil(shellCloses(widget), timeout);	
		} catch (SWTException e){
			println(e.message)
		}
		return this
	}
	override Shell waitUntilCloses(){
		return waitUntilCloses(30000);
	}
}
	