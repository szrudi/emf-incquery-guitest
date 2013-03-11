package org.eclipse.incquery.tooling.ui.tests;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.matchers.WithPartName;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.utils.FileUtils;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.ui.IEditorReference;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.osgi.framework.Bundle;

public class SchoolTests extends AbstractIncQueryTester{
	protected static String PROJECTNAME = "school.tests";
	protected static String EIQFILE = "schoolQueries.eiq";
	protected static String MODELFILE = "BUTE.school";

	@Test
	public void test() {
		createIncQueryProject(PROJECTNAME);
		setupProjectDependency(PROJECTNAME);

		openQueryExplorer();

		createModelFolder();
		copyButeModel();
		
		createNewEiq();
		setPattern("pattern schools(Sch) = { School(Sch); }");

		bot.sleep(5000);
		
		loadModelToExplorerWithPlayButton(MODELFILE);
		assertModelLoaded(MODELFILE);

		loadPatternsToExplorerWithPlayButton(EIQFILE);
		assertPatternLoaded(EIQFILE);

		bot.sleep(5000);
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
		getTreeItemFromProject(PROJECTNAME,"src",EIQFILE).select().doubleClick();
		
		Matcher<IEditorReference> withPartName = WithPartName
				.withPartName(Matchers.containsString(EIQFILE));
		
		bot.waitUntil(Conditions.waitForEditor(withPartName));
		SWTBotEclipseEditor editor = bot.editorByTitle(EIQFILE).toTextEditor();
		editor.setText(getText("package "+PROJECTNAME,"import \"http://school.ecore\"",pattern));
        editor.saveAndClose();
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
		bot.tree().expandNode(PROJECTNAME, "model").select();
		bot.textWithLabel("File name:").setText(MODELFILE);
		bot.button("Finish").click();
		bot.waitUntil(shellCloses(shell), 5000);
		
		bot.editorByTitle(MODELFILE).close();
		
		getTreeItemFromProject(PROJECTNAME, "model", MODELFILE)
				.contextMenu("Open With").menu("Text Editor").click();

		Matcher<IEditorReference> withPartName = WithPartName
				.withPartName(Matchers.containsString(MODELFILE));		
		bot.waitUntil(Conditions.waitForEditor(withPartName));

		Bundle bundle = Activator.getContext().getBundle();
		String contents = FileUtils.read(bundle.getEntry("test-files/BUTE.school"));

		SWTBotEclipseEditor editor = bot.editor(withPartName).toTextEditor();		
		editor.setText(contents);
		editor.saveAndClose();
	}
	protected void openQueryExplorer() {
		bot.menu("Window").menu("Show View").menu("Other...").click();
		SWTBotShell shell = bot.shell("Show View");
		shell.activate();
		
		bot.tree().expandNode("EMF-IncQuery").select("Query Explorer");
		bot.button("OK").click();

		bot.waitUntil(shellCloses(shell), 5000);
	}
	
	protected void loadModelToExplorerWithPlayButton(String modelFileName){
		getTreeItemFromProject(PROJECTNAME, "model", modelFileName)
				.contextMenu("Open With").menu("School Model Editor").click();
		Matcher<IEditorReference> withPartName = WithPartName
				.withPartName(Matchers.containsString(modelFileName));		
		bot.waitUntil(Conditions.waitForEditor(withPartName));
		
		pressPlayButtonInQueryExplorer();
	}
	protected void assertModelLoaded(String modelFileName){
		SWTBotTree middleTree = bot.viewByTitle("Query Explorer").bot().tree(1);
		middleTree.getTreeItem("[platform:/resource/"+PROJECTNAME+"/model/"+modelFileName+"][school.presentation.SchoolEditorID]");	
	}
	
	protected void loadPatternsToExplorerWithPlayButton(String patternFileName){
		getTreeItemFromProject(PROJECTNAME, "src", patternFileName).select().doubleClick();
		
		Matcher<IEditorReference> withPartName = WithPartName
				.withPartName(Matchers.containsString(patternFileName));		
		bot.waitUntil(Conditions.waitForEditor(withPartName),15*1000);
		
		pressPlayButtonInQueryExplorer();	
	}
	protected void assertPatternLoaded(String patternFileName){
		SWTBotTree tree = bot.viewByTitle("Query Explorer").bot().tree(0);
		tree.expandNode("Runtime", PROJECTNAME, "schools");	

		tree = bot.viewByTitle("Query Explorer").bot().tree(1);
		SWTBotTreeItem[] items = tree.expandNode("[platform:/resource/"+PROJECTNAME+"/model/"+MODELFILE+"][school.presentation.SchoolEditorID]").getItems();
		boolean found = false;
		for (SWTBotTreeItem item : items){
			if(item.getText().startsWith(PROJECTNAME+".schools")){
				found = true;
				break;
			}
		}
		assertTrue(found);
	}
	
	protected void pressPlayButtonInQueryExplorer(){
		pressPlayButtonInQueryExplorer("click");
	}
	protected void pressPlayButtonInQueryExplorer(String action){
		SWTBotView queryExplorerBot = bot.viewByTitle("Query Explorer");
		SWTBotToolbarDropDownButton playBtn = queryExplorerBot.toolbarDropDownButton("Load model");
		if (action == "click"){
			playBtn.click();
		} else {
			playBtn.menuItem(action).click();
		}		
	}
}
