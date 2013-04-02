package org.eclipse.incquery.tooling.ui.tests.swtbot

import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu
import org.eclipse.incquery.tooling.ui.tests.interfaces.Treelike

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
	
}
