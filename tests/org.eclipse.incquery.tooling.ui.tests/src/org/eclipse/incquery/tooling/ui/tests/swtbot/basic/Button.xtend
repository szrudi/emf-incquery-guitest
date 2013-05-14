package org.eclipse.incquery.tooling.ui.tests.swtbot.basic

import org.eclipse.incquery.tooling.ui.tests.interfaces.Clickable
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton

import static org.eclipse.incquery.tooling.ui.tests.swtbot.basic.SwtBotComponent.*

class Button extends SwtBotComponent implements Clickable {
	val SWTBotButton widget
	
	new(String widgetName){
		this.widget = bot.button(widgetName)
	}

	override Button click() {
		widget.click()
		return this
	} 

	override isInactive() {
		return !widget.enabled
	}
}
