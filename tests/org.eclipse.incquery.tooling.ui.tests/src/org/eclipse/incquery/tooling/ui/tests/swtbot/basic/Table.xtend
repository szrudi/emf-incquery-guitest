package org.eclipse.incquery.tooling.ui.tests.swtbot.basic

import org.eclipse.incquery.tooling.ui.tests.interfaces.Treelike
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotWorkbenchPart
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem

import static org.eclipse.incquery.tooling.ui.tests.swtbot.basic.SwtBotComponent.*
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException

class Table extends SwtBotComponent implements Treelike {
	val SWTBotTable widget;
	
	new(String widgetName){
		if (widgetName.empty){
			this.widget = bot.table
		} else {
			this.widget = bot.tableWithLabel(widgetName)
		}
	}
/*	new(Integer i, SWTBotWorkbenchPart part){
		this.widget = part.bot.table(i)
	}*/
	override Table choose(String... path) {
		getTableItem(path.get(0))
		return this
	}
	
	override Table activate(String... path) {
		getTableItem(path.get(0)).check
		return this
	}

	def private SWTBotTableItem getTableItem(String name){
		widget.getTableItem(name).select
	}

	override hasItem(String[] path) {
		try {
			getTableItem(path.get(0))
			return true
		} catch (WidgetNotFoundException e) {
			return false
		}
	}
}
