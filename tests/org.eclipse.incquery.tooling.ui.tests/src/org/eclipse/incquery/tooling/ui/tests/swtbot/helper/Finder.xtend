package org.eclipse.incquery.tooling.ui.tests.swtbot.helper

import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Button
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Editor
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Menu
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Table
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.TextField
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Tree
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.View
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Window

class Finder {
	
	def text(String s){ new TextField(s) }

	def table(String s){ new Table(s) }

	def tree(){ tree("") }
	def tree(String s){ new Tree(s) }
	def tree(Integer i, View v){ new Tree(i, v.widget) }

	def menu(String s){ new Menu(s) }

	def view(String s){ new View(s) }

	def editor(String s){ new Editor(s) } 

	def window(String s){ new Window(s) }

	def button(String s){ new Button(s) }

	//def toolbarDropDownbutton(String s){ new ToolbarDropDownButton(s) }
	//def toolbarDropDownbutton(String s, View v){ new ToolbarDropDownButton(s,v.widget) }
	//def contextMenu(String s){ s }
}
