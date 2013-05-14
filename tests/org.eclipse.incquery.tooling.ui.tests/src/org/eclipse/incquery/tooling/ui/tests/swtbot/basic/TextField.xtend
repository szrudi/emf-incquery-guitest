package org.eclipse.incquery.tooling.ui.tests.swtbot.basic

import org.eclipse.incquery.tooling.ui.tests.interfaces.Editable
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText

import static org.eclipse.incquery.tooling.ui.tests.swtbot.basic.SwtBotComponent.*

class TextField extends SwtBotComponent implements Editable {
	val SWTBotText widget;
	
	new(String widgetName){
		this.widget = bot.textWithLabel(widgetName)
	}

	override TextField setText(String text) {
		widget.setText(text)
		return this
	}
	

	override TextField save() {
		// Text field does not need save
		return this
	}
	
	override TextField saveAndClose() {
		// Text field does not need save and can't be closed
		return this
	}
	
}
