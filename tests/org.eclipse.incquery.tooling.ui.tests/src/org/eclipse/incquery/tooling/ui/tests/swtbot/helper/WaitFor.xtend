package org.eclipse.incquery.tooling.ui.tests.swtbot.helper

import org.eclipse.swtbot.eclipse.finder.matchers.WithPartName
import org.eclipse.swtbot.eclipse.finder.waits.Conditions
import org.hamcrest.Matcher
import org.hamcrest.Matchers

import static org.eclipse.incquery.tooling.ui.tests.swtbot.helper.WaitFor.*
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.SwtBotComponent
import org.eclipse.incquery.tooling.ui.tests.interfaces.Focusable

class WaitFor extends SwtBotComponent {
		
	def editor(String editorName){
		bot.waitUntil(Conditions::waitForEditor(stringMatcher(editorName)))
	}
	def element(Focusable f){
		f.waitUntilCloses
	}
	def static Matcher stringMatcher(String s){
		return WithPartName::withPartName(
			Matchers::containsString(s)
		);
	}
}