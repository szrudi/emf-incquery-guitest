package org.eclipse.incquery.tooling.ui.tests.swtbot.basic

import org.eclipse.incquery.tooling.ui.tests.interfaces.Treelike
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu

import static org.eclipse.incquery.tooling.ui.tests.swtbot.basic.SwtBotComponent.*
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException

class Menu extends SwtBotComponent implements Treelike {
	val SWTBotMenu widget;
	
	new(String widgetName){
		this.widget = bot.menu(widgetName)
	}
	
	override Menu choose(String... path) {
		var mt = widget
		for (String menuName: path){
			mt = mt.menu(menuName)
		}
		mt.click()
		return this
	}
	

	override Menu activate(String... path) {
		choose(path)
		return this
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
