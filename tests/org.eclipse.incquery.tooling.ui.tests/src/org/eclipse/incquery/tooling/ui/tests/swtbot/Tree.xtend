package org.eclipse.incquery.tooling.ui.tests.swtbot

import org.eclipse.incquery.tooling.ui.tests.interfaces.Treelike
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotWorkbenchPart
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem

import static org.eclipse.incquery.tooling.ui.tests.swtbot.SwtBotComponent.*

class Tree extends SwtBotComponent implements Treelike {
	val SWTBotTree widget;
	
	new(String widgetName){
		if (widgetName.empty){
			this.widget = bot.tree
		} else {
			this.widget = bot.treeWithLabel(widgetName)
		}
	}
	new(Integer i, SWTBotWorkbenchPart part){
		this.widget = part.bot.tree(i)
	}
	override Tree choose(String... path) {
		getTreeItem(path)
		return this
	}
	
	override doubleClick(String... path) {
		getTreeItem(path).doubleClick
		return this
	}
	
	def chooseActionFromContextMenu(String[] path, String... actionPath){
		var mt = getTreeItem(path).contextMenu(actionPath.get(0))
		for (String menuName: actionPath.tail){
			mt = mt.menu(menuName)
		}
		mt.click()
		return this
	}

	def private SWTBotTreeItem getTreeItem(String[] path){
		widget.expandNode(path).select
	}
	
}
