package org.eclipse.incquery.tooling.ui.tests.swtbot.helper

import java.util.ArrayList
import org.eclipse.incquery.tooling.ui.tests.Activator
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Window
import org.eclipse.swtbot.swt.finder.utils.FileUtils
import org.eclipse.swtbot.swt.finder.utils.SWTUtils
import org.osgi.framework.Bundle

import static extension org.eclipse.incquery.tooling.ui.tests.swtbot.helper.TestHelper.*

class TestHelper {
	static var screenshotCounter = 0
	private val Finder find
	private val WaitFor waitFor
	
	new(Finder f){
		find = f
		waitFor = new WaitFor
	}
	
	def waitFor(){ waitFor }
		
	def captureScreenshot(String filename){
		screenshotCounter = screenshotCounter + 1
		SWTUtils::captureScreenshot(screenshotCounter + "-" + filename)
	}
	
	def captureScreenshot(String filename, Window s){
		screenshotCounter = screenshotCounter + 1
		SWTUtils::captureScreenshot(screenshotCounter + "-" + filename, s.widget.widget)
	}
	def createFolderInPath(String folderName, String... path){
		find.menu("File").choose("New","Folder")
		val s = find.window("New Folder").focus
		find.tree.choose(path)
		find.text("Folder name:").setText(folderName)
		find.button("Finish").click
		s.waitUntilCloses(40000)
	}
	
	def addNewFileToFolder(String fileName, String... folderPath){
		find.menu("File").choose("New","File")
		val s = find.window("New File").focus
		find.tree.choose(folderPath)
		find.text("File name:").setText(fileName)
		find.button("Finish").click
		s.waitUntilCloses		
	}
	
	def addFileFromBundleToFolder(String fileName, String bundleFilePath, String... path){
		val Bundle bundle = Activator::getContext().getBundle()
		val String contents = FileUtils::read(bundle.getEntry(bundleFilePath));
		addNewFileToFolder(fileName+".txt",path)//.subList(0, path.size-1).toArray)
		find.editor(fileName+".txt").setText(contents).saveAndClose
		find.view("Project Explorer").focus
		
		var s = new ArrayList<String>();
		s.addAll(path)
		s.add(fileName+".txt")
		find.tree.choose(s.join(",").split(",")) // ehh...

		find.menu("File").choose("Rename...")
		find.text("New name:").setText(fileName)
		find.button("OK").click
	}
	def setupProjectDependency(String projectName, String manifest) {
		find.view("Project Explorer").focus
		val String[] pl = newArrayList(projectName,"META-INF","MANIFEST.MF")
		val String[] al = newArrayList("Open With","Text Editor")
		find.tree.chooseActionFromContextMenu(pl,al);	
		waitFor.editor("MANIFEST.MF");
		find.editor("MANIFEST.MF").setText(manifest).saveAndClose	
	}
}
