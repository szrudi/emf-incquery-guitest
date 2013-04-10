package org.eclipse.incquery.tooling.ui.tests

import java.util.ArrayList
import org.eclipse.incquery.tooling.ui.tests.swtbot.Button
import org.eclipse.incquery.tooling.ui.tests.swtbot.Editor
import org.eclipse.incquery.tooling.ui.tests.swtbot.Menu
import org.eclipse.incquery.tooling.ui.tests.swtbot.Shell
import org.eclipse.incquery.tooling.ui.tests.swtbot.TextField
import org.eclipse.incquery.tooling.ui.tests.swtbot.Tree
import org.eclipse.incquery.tooling.ui.tests.swtbot.View
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException
import org.eclipse.swtbot.swt.finder.utils.FileUtils
import org.eclipse.swtbot.swt.finder.utils.SWTUtils
import org.junit.BeforeClass
import org.osgi.framework.Bundle
import java.util.List

class UiTestHelper {
	
	private val SWTWorkbenchBot ui = new SWTWorkbenchBot()
	
	@BeforeClass
	def void beforeClass(){
		var SWTBotView view
		
		try {
			view = ui.activeView();	
		} catch (WidgetNotFoundException e){
			println("No active view")			
		}
        if(view != null && view.getTitle().equals("Welcome")) {
        	view.close();
        }
		
	}
	
	def text(String s){ new TextField(s) }
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
		SWTUtils::captureScreenshot(filename)
	}
	
	def captureScreenshot(String filename, Shell s){
		SWTUtils::captureScreenshot(filename, s.getWidget)
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
}
