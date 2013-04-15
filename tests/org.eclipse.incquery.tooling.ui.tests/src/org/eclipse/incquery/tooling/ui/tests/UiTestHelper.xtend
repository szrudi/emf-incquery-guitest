package org.eclipse.incquery.tooling.ui.tests

import java.util.ArrayList
import org.eclipse.incquery.tooling.ui.tests.swtbot.Button
import org.eclipse.incquery.tooling.ui.tests.swtbot.Editor
import org.eclipse.incquery.tooling.ui.tests.swtbot.Menu
import org.eclipse.incquery.tooling.ui.tests.swtbot.Shell
import org.eclipse.incquery.tooling.ui.tests.swtbot.TextField
import org.eclipse.incquery.tooling.ui.tests.swtbot.ToolbarDropDownbutton
import org.eclipse.incquery.tooling.ui.tests.swtbot.Tree
import org.eclipse.incquery.tooling.ui.tests.swtbot.View
import org.eclipse.incquery.tooling.ui.tests.swtbot.Wait
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotWorkbenchPart
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException
import org.eclipse.swtbot.swt.finder.utils.FileUtils
import org.eclipse.swtbot.swt.finder.utils.SWTUtils
import org.junit.BeforeClass
import org.osgi.framework.Bundle

import static extension org.eclipse.incquery.tooling.ui.tests.UiTestHelper.*

class UiTestHelper {
	static var screenshotCounter = 0;
		
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
	
	def text(String s){ new TextField(s) }
	def tree(){ tree("") }
	def tree(String s){ new Tree(s) }
	def tree(Integer i, View v){ new Tree(i, v.widget) }
	def Menu menu(String s){ new Menu(s); }
	//def contextMenu(String s){ s }
	def button(String s){ new Button(s) }
	def toolbarDropDownbutton(String s){ new ToolbarDropDownbutton(s) }
	def toolbarDropDownbutton(String s, View v){ new ToolbarDropDownbutton(s,v.widget) }
	def shell(String s){ new Shell(s) }
	def view(String s){ new View(s) }
	def editor(String s){ new Editor(s) }
	
	
	def captureScreenshot(String filename){
		screenshotCounter = screenshotCounter + 1
		SWTUtils::captureScreenshot(screenshotCounter + "-" + filename)
	}
	
	def captureScreenshot(String filename, Shell s){
		screenshotCounter = screenshotCounter + 1
		SWTUtils::captureScreenshot(screenshotCounter + "-" + filename, s.widget.widget)
	}
	def createFolderInPath(String folderName, String... path){
		"File".menu.choose("New","Folder")
		val s = "New Folder".shell.focus
		tree.choose(path)
		"Folder name:".text.setText(folderName)
		"Finish".button.click
		s.waitUntilCloses(40000)
	}
	
	def addNewFileToFolder(String fileName, String... folderPath){
		"File".menu.choose("New","File")
		val s = "New File".shell.focus
		tree.choose(folderPath)
		"File name:".text.setText(fileName)
		"Finish".button.click
		s.waitUntilCloses		
	}
	
	def addFileFromBundleToFolder(String fileName, String bundleFilePath, String... path){
		val Bundle bundle = Activator::getContext().getBundle()
		val String contents = FileUtils::read(bundle.getEntry(bundleFilePath));
		addNewFileToFolder(fileName+".txt",path)//.subList(0, path.size-1).toArray)
		(fileName+".txt").editor.setText(contents).saveAndClose
		"Project Explorer".view.focus
		
		var s = new ArrayList<String>();
		s.addAll(path)
		s.add(fileName+".txt")
		tree.choose(s.join(",").split(",")) // ehh...

		"File".menu.choose("Rename...")
		"New name:".text.setText(fileName)
		"OK".button.click
	}
	def setupProjectDependency(String projectName, String manifest) {
		"Project Explorer".view.focus
		val String[] pl = newArrayList(projectName,"META-INF","MANIFEST.MF")
		val String[] al = newArrayList("Open With","Text Editor")
		tree.chooseActionFromContextMenu(pl,al);	
		Wait::forEditor("MANIFEST.MF");
		"MANIFEST.MF".editor.setText(manifest).saveAndClose	
	}
}
