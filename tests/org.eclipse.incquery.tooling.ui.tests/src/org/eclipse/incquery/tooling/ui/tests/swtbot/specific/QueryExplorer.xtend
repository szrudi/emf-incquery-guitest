package org.eclipse.incquery.tooling.ui.tests.swtbot.specific

import org.eclipse.incquery.tooling.ui.tests.interfaces.Focusable
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.SwtBotComponent
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.View
import org.eclipse.incquery.tooling.ui.tests.swtbot.helper.Finder
import org.jnario.lib.TimeoutError

class QueryExplorer extends SwtBotComponent implements Focusable {
	var View view
	
	new() {
		try {
			view = new View("Query Explorer")	
		} catch (TimeoutError e){
			open()
			view = new View("Query Explorer")
		}
		
	}

	def greenButton(){ new GreenButton(view) }
	def PatternRegistry patternRegistry(){ return new PatternRegistry(view) }
	def MiddleTree middleTree(){ return new MiddleTree(view) }
	
	def private open(){
		val find = new Finder()
		find.menu("Window").choose("Show View", "Other...")
		val shell = find.window("Show View").focus
		find.tree.choose("EMF-IncQuery","Query Explorer")
		find.button("OK").click
		shell.waitUntilCloses
		find.view("Query Explorer")
		
	} 

	override focus() {
		view.focus
		return this
	}
	
	override waitUntilCloses() {
		view.waitUntilCloses
		return this
	}
	
	override waitUntilCloses(int timeout) {
		view.waitUntilCloses(timeout)
		return this
	}	
}
	