package org.eclipse.incquery.tooling.ui.tests.swtbot.basic

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot

abstract class SwtBotComponent {
	protected static val SWTWorkbenchBot bot = new SWTWorkbenchBot()
/*	protected var T widget
	
	def T getWidget(){
		return widget
	}
*/
}
