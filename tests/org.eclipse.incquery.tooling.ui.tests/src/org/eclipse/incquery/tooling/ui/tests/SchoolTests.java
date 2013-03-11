package org.eclipse.incquery.tooling.ui.tests;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;

import org.eclipse.swtbot.eclipse.finder.matchers.WithPartName;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.utils.FileUtils;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.ui.IEditorReference;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.osgi.framework.Bundle;

public class SchoolTests extends AbstractSwtBotTester{
	protected static String PROJECTNAME = "school.tests";
	protected static String EIQFILE = "schoolQueries.eiq";

	@Test
	public void test() {
		createIncQueryProject(PROJECTNAME);
		setupProjectDependency(PROJECTNAME);
		createModelFolder();
		copyButeModel();
		createNewEiq();
		setPattern("pattern schools(Sch) = { School(Sch); }");
		openQueryExplorer();
		//bot.sleep(5000);
	}
	
	protected void createNewEiq() {
		bot.menu("File").menu("New").menu("Other...").click();
		SWTBotShell shell = bot.shell("New");
		shell.activate();
		
		bot.tree().expandNode("EMF-IncQuery").select("EMF-IncQuery Query Definition");
		bot.button("Next >").click();
		
		bot.textWithLabel("File name:").setText(EIQFILE);
		bot.button("Next >").click();
		
		bot.textWithLabel("Pattern name:").setText("testPattern");
		
		// TODO Add gombot meg kellene találni valahogy
		//bot.buttonWithLabel("Add").click();
		//bot.tree().select("http://school.ecore");
		//bot.button("OK").click();
		
		bot.button("Finish").click();
		bot.waitUntil(shellCloses(shell), 5000);
	}
	
	protected void setPattern(String pattern) {
		SWTBot projectExplorerBot = bot.viewByTitle("Project Explorer").bot();
		SWTBotTree tree = projectExplorerBot.tree();
		
		tree.expandNode(PROJECTNAME,"src",EIQFILE).doubleClick();
		
		Matcher<IEditorReference> withPartName = WithPartName
				.withPartName(Matchers.containsString(EIQFILE));
		
		bot.waitUntil(Conditions.waitForEditor(withPartName));
		SWTBotEclipseEditor editor = bot.editorByTitle(EIQFILE).toTextEditor();
		editor.setText(getText("package "+PROJECTNAME,"import \"http://school.ecore\"",pattern));
        editor.save();
	}
	protected void createModelFolder() {
		bot.menu("File").menu("New").menu("Folder").click();
		SWTBotShell shell = bot.shell("New Folder");
		shell.activate();
		bot.tree().select(PROJECTNAME);
		bot.textWithLabel("Folder name:").setText("model");
		bot.button("Finish").click();
		bot.waitUntil(shellCloses(shell), 5000);
	}
	protected void copyButeModel() {
		bot.menu("File").menu("New").menu("File").click();
		SWTBotShell shell = bot.shell("New File");
		shell.activate();
		bot.tree().expandNode(PROJECTNAME).expandNode("model");
		bot.textWithLabel("File name:").setText("BUTE.school");
		bot.button("Finish").click();
		bot.waitUntil(shellCloses(shell), 5000);
		
		bot.editorByTitle("BUTE.school").close();
		
		SWTBot projectExplorerBot = bot.viewByTitle("Project Explorer").bot();
		SWTBotTree tree = projectExplorerBot.tree();
		tree.expandNode(PROJECTNAME, "model", "BUTE.school")
				.contextMenu("Open With").menu("Text Editor").click();

		Matcher<IEditorReference> withPartName = WithPartName
				.withPartName(Matchers.containsString("BUTE.school"));		
		bot.waitUntil(Conditions.waitForEditor(withPartName));

		Bundle bundle = Activator.getContext().getBundle();
		String contents = FileUtils.read(bundle.getEntry("test-files/BUTE.school"));

		SWTBotEclipseEditor editor = bot.editor(withPartName).toTextEditor();		
		editor.setText(contents);
		editor.save();
	}
	protected void openQueryExplorer() {
		bot.menu("Window").menu("Show View").menu("Other...").click();
		SWTBotShell shell = bot.shell("Show View");
		shell.activate();
		
		bot.tree().expandNode("EMF-IncQuery").select("Query Explorer");
		bot.button("OK").click();

		bot.waitUntil(shellCloses(shell), 5000);
	}	 
}
