package org.eclipse.incquery.tooling.ui.tests.swtbot

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree
import org.eclipse.incquery.tooling.ui.tests.interfaces.Treelike

class Tree extends SwtBotComponent implements Treelike {
	val SWTBotTree widget;
	
	new(String widgetName){
		if (widgetName.empty){
			this.widget = bot.tree()
		} else {
			this.widget = bot.treeWithLabel(widgetName)
		}
	}
	override Tree choose(String... path) {
		widget.expandNode(path).select()
		return this
	}
}
