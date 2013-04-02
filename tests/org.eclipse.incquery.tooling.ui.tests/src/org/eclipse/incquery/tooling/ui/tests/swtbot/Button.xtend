package org.eclipse.incquery.tooling.ui.tests.swtbot

import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton
import org.eclipse.incquery.tooling.ui.tests.interfaces.Clickable

class Button extends SwtBotComponent implements Clickable {
	val SWTBotButton widget
	
	new(String widgetName){
		this.widget = bot.button(widgetName)
	}

	override Button click() {
		widget.click()
		return this
	} 
	
}
