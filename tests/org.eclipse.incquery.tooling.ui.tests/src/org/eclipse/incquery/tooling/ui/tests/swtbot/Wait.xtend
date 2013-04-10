package org.eclipse.incquery.tooling.ui.tests.swtbot

import org.eclipse.swtbot.eclipse.finder.matchers.WithPartName
import org.eclipse.swtbot.eclipse.finder.waits.Conditions
import org.hamcrest.Matcher
import org.hamcrest.Matchers

import static org.eclipse.incquery.tooling.ui.tests.swtbot.SwtBotComponent.*

class Wait extends SwtBotComponent {
	private new(){}
		
	def static forEditor(String editorName){
		bot.waitUntil(Conditions::waitForEditor(stringMatcher(editorName)))
	}
	def static Matcher stringMatcher(String s){
		return WithPartName::withPartName(
			Matchers::containsString(s)
		);
	}
}