package org.eclipse.incquery.tooling.ui.tests.swtbot.basic

import org.eclipse.incquery.tooling.ui.tests.interfaces.Clickable
import org.eclipse.incquery.tooling.ui.tests.interfaces.Treelike
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotWorkbenchPart
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton

import static org.eclipse.incquery.tooling.ui.tests.swtbot.basic.SwtBotComponent.*
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException

class ToolbarDropDownButton extends SwtBotComponent implements Clickable, Treelike {
	val SWTBotToolbarDropDownButton widget
	
	new(String widgetName){ 
		this.widget = bot.toolbarDropDownButton(widgetName)
	}

	new(String widgetName, SWTBotWorkbenchPart part){
		this.widget = part.toolbarDropDownButton(widgetName)
	}

	override ToolbarDropDownButton click() {
		widget.click()
		return this
	} 
	

	override ToolbarDropDownButton choose(String... path) {
		if (path.size > 1){
			throw new UnsupportedOperationException(
				"ToolbarDropDownbutton only supports first level elements")
		}
		widget.menuItem(path.get(0)).click()
		return this
	}
	
	override ToolbarDropDownButton activate(String... path) {
		choose(path)
		return this
	}
	override isInactive() {
		return !widget.enabled
		//SWTBotAssert::assertNotEnabled(widget)
		//return this
	}
		
	override hasItem(String[] path) {
		try {
			choose(path)
			return true
		} catch (WidgetNotFoundException e) {
			return false
		}
	}

}
