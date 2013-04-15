package org.eclipse.incquery.tooling.ui.tests.swtbot

import org.eclipse.incquery.tooling.ui.tests.interfaces.Clickable
import org.eclipse.incquery.tooling.ui.tests.interfaces.Treelike
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotWorkbenchPart
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton

import static org.eclipse.incquery.tooling.ui.tests.swtbot.SwtBotComponent.*

class ToolbarDropDownbutton extends SwtBotComponent implements Clickable, Treelike {
	val SWTBotToolbarDropDownButton widget
	
	new(String widgetName){
		this.widget = bot.toolbarDropDownButton(widgetName)
	}

	new(String widgetName, SWTBotWorkbenchPart part){
		this.widget = part.toolbarDropDownButton(widgetName)
	}

	override ToolbarDropDownbutton click() {
		widget.click()
		return this
	} 
	

	override ToolbarDropDownbutton choose(String... path) {
		if (path.size > 1){
			throw new UnsupportedOperationException(
				"ToolbarDropDownbutton only supports first level elements")
		}
		widget.menuItem(path.get(0)).click()
		return this
	}
	
	override ToolbarDropDownbutton doubleClick(String... path) {
		choose(path)
		return this
	}
}
