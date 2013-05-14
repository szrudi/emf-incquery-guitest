package org.eclipse.incquery.tooling.ui.tests.swtbot.helper

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException
import org.junit.BeforeClass
import org.eclipse.incquery.tooling.ui.tests.swtbot.specific.Wizard
import org.eclipse.incquery.tooling.ui.tests.swtbot.specific.QueryExplorer
import org.eclipse.incquery.tooling.ui.tests.swtbot.specific.ProjectExplorer

class UiTestBot {
	val Finder finder
	val TestHelper helper
		
	new(){
		finder = new Finder
		helper = new TestHelper(finder)
	}

	def find(){ finder }

	def projectExplorer(){ new ProjectExplorer() }
	def queryExplorer(){ new QueryExplorer() }
	def wizard(String... s){ new Wizard(s) }	

	def help(){ helper }

	@BeforeClass
	def void beforeClass(){
		var SWTBotView view
		
		try {
			val SWTWorkbenchBot bot = new SWTWorkbenchBot()
			view = bot.activeView();	
		} catch (WidgetNotFoundException e){
			println("No active view")			
		}
        if(view != null && view.getTitle().equals("Welcome")) {
        	view.close();
        }		
	}

	/*
	@AfterClass
    def void resetWorkbench() {
        try {
            val IWorkbench workbench = PlatformUI::getWorkbench();
            val IWorkbenchWindow workbenchWindow =
                    workbench.getActiveWorkbenchWindow();
            val IWorkbenchPage page = workbenchWindow.getActivePage();
            val org.eclipse.swt.widgets.Shell activeShell = Display::getCurrent().getActiveShell();
            if (activeShell != workbenchWindow.getShell()) {
                activeShell.close();
            }
            
            page.closeAllEditors(false);
            val String defaultPerspectiveId =
                    workbench.getPerspectiveRegistry().getDefaultPerspective();
            workbench.showPerspective(defaultPerspectiveId, workbenchWindow);
            page.resetPerspective();
        } catch (WorkbenchException e) {
            throw new RuntimeException(e);
        }
    }
	*/
}
