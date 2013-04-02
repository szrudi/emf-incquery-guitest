package org.eclipse.incquery.tooling.ui.tests

import org.eclipse.incquery.tooling.ui.tests.swtbot.Button
import org.eclipse.incquery.tooling.ui.tests.swtbot.Editor
import org.eclipse.incquery.tooling.ui.tests.swtbot.Menu
import org.eclipse.incquery.tooling.ui.tests.swtbot.Shell
import org.eclipse.incquery.tooling.ui.tests.swtbot.Text
import org.eclipse.incquery.tooling.ui.tests.swtbot.Tree
import org.eclipse.incquery.tooling.ui.tests.swtbot.View
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException
import org.eclipse.swtbot.swt.finder.utils.SWTUtils
import org.junit.BeforeClass

class UiTestHelper {
	
	private val SWTWorkbenchBot bot = new SWTWorkbenchBot() 
	
	@BeforeClass
	def void beforeClass(){
		var SWTBotView view
		
		try {
			view = bot.activeView();	
		} catch (WidgetNotFoundException e){
			println("No active view")			
		}
        if(view != null && view.getTitle().equals("Welcome")) {
        	view.close();
        }
		
	}
	
	def text(String s){ new Text(s) }
	def tree(String s){ new Tree(s) }
	def tree(){ tree("") }
	def Menu menu(String s){ new Menu(s); }
	//def contextMenu(String s){ s }
	def button(String s){ new Button(s) }
	//def toolbarDropDownbutton(String s){ s }
	def shell(String s){ new Shell(s) }
	def view(String s){ new View(s) }
	def editor(String s){ new Editor(s) }
	
	
	def captureScreenshot(String filename){
		SWTUtils::captureScreenshot(filename + "-window.png")
	}	
}






