package org.eclipse.incquery.tooling.ui.tests.swtbot.specific

import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Tree
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.View
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.SwtBotComponent
import org.eclipse.incquery.tooling.ui.tests.interfaces.Treelike
import org.eclipse.incquery.tooling.ui.tests.interfaces.Focusable

class ProjectExplorer extends SwtBotComponent implements Treelike, Focusable  {
	Tree t;
	View v;
	
	new(){
		v = new View("Project Explorer")
		v.focus
		t = new Tree("");
	}
	def open(String... path){
		t.activate(path)
	}
		
	override hasItem(String[] path) {
		t.hasItem(path)
	}

	override activate(String[] path) {
		t.activate(path)
	}
	
	override choose(String[] path) {
		t.choose(path)
	}
	
	override focus() {
		v.focus
		return this
	}
	
	override waitUntilCloses() {
		v.waitUntilCloses
		return this
	}
	
	override waitUntilCloses(int timeout) {
		v.waitUntilCloses(timeout)
		return this
	}	
	
}
	