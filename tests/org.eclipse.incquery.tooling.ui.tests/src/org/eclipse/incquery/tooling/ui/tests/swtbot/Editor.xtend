package org.eclipse.incquery.tooling.ui.tests.swtbot

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor
import org.hamcrest.Matcher
import org.eclipse.swtbot.eclipse.finder.waits.Conditions
import org.hamcrest.Matchers
import org.eclipse.swtbot.eclipse.finder.matchers.WithPartName
import org.eclipse.incquery.tooling.ui.tests.interfaces.Editable
import org.eclipse.incquery.tooling.ui.tests.interfaces.Focusable

import static org.eclipse.incquery.tooling.ui.tests.swtbot.SwtBotComponent.*

class Editor extends SwtBotComponent implements Editable, Focusable {
	val SWTBotEclipseEditor widget
	val String title
	
	new(String widgetName){
		this.widget = bot.editorByTitle(widgetName).toTextEditor()
		this.title = widgetName
	}

	override Editor setText(String text) {
		widget.setText(text)
		return this
	}
	override Editor saveAndClose(){
		widget.saveAndClose
		return this
	}
	override Editor save(){
		widget.save
		return this
	}

	override Editor focus() {
		widget.setFocus
		return this
	}	
	override Editor waitUntilCloses(int timeout) {
		val Matcher withPartName = WithPartName::withPartName(Matchers::containsString(title));		
		bot.waitUntil(Conditions::waitForEditor(withPartName))
		return this
	}
	override Editor waitUntilCloses() {
		waitUntilCloses(30000)
	}
	
}
