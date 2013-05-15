package org.eclipse.incquery.tooling.ui.tests;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.incquery.tooling.ui.tests.swtbot.helper.AbstractIncQueryTester;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.matchers.WithPartName;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.eclipse.ui.IEditorReference;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Order;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

public class SchoolTests extends AbstractIncQueryTester{
	protected static String PROJECTNAME = "school.tests";
	protected static String EIQFILE = "schoolQueries.eiq";
	protected static String MODELFILE = "BUTE.school";
	
	@Test
	@Ignore
	public void exampleSWTBotTestCase1() {	
		//setTestPattern
		SWTWorkbenchBot bot = new SWTWorkbenchBot();
		bot.viewByTitle("Project Explorer").bot().tree()
			.expandNode("test_project","src","my.eiq")
			.select().doubleClick();
		Matcher<IEditorReference> withPartName = WithPartName
			.withPartName(Matchers.containsString("my.eiq"));
		bot.waitUntil(Conditions.waitForEditor(withPartName));
		SWTBotEclipseEditor editor = bot.editorByTitle("my.eiq")
			.toTextEditor();
		editor.setText("pattern schools(Sch) = { School(Sch); }");
		editor.save();
		SWTBotView qEBot = bot.viewByTitle("Query Explorer");
		qEBot.toolbarDropDownButton("Load model").click();

	}
	
	@Test
	@Ignore
	public void exampleSWTBotTestCase2() {
		//createIncQueryProject
		SWTWorkbenchBot bot = new SWTWorkbenchBot();
		bot.menu("File").menu("New").menu("Project...").click();
		SWTBotShell shell = bot.shell("New Project");
		shell.activate();
		bot.tree().expandNode("EMF-IncQuery","EMF-IncQuery Project")
			.select();
		bot.button("Next >").click();
		bot.textWithLabel("Project name:").setText("test_project");
		bot.button("Finish").click();
		bot.waitUntil(shellCloses(shell), 20000);
		bot.viewByTitle("Project Explorer").bot().tree()
			.expandNode("projectName");
	}
   
	@Test
	@Order(0)
	public void createIncQueryProject() {
		boolean isCreated = false;
		bot.menu("File").menu("New").menu("Project...").click();
		SWTBotShell shell = bot.shell("New Project");
		shell.activate();
		bot.tree().expandNode("EMF-IncQuery","EMF-IncQuery Project").select();
		bot.button("Next >").click();
		bot.textWithLabel("Project name:").setText(PROJECTNAME);
		bot.button("Finish").click();
		bot.waitUntil(shellCloses(shell), 20000);
		setupProjectDependency();
		try {
			bot.viewByTitle("Project Explorer").bot().tree()
				.expandNode(PROJECTNAME);
			isCreated = true;
		} catch (WidgetNotFoundException e) {
			isCreated = false;
		}
		
		assertTrue("Project with name \""+PROJECTNAME+"\" is not created", isCreated);

	}
	
	@Test
	@Order(2)
	public void createNewEiq() {
		// Arrange
		bot.menu("File").menu("New").menu("Other...").click();
		SWTBotShell shell = bot.shell("New");
		shell.activate();
		bot.tree().expandNode("EMF-IncQuery")
			.select("EMF-IncQuery Query Definition");
		bot.button("Next >").click();
		
		// Act
		bot.textWithLabel("File name:").setText(EIQFILE);
		bot.button("Next >").click();
		bot.textWithLabel("Pattern name:").setText("testPattern");
		bot.button("Finish").click();
		bot.waitUntil(shellCloses(shell), 15000);
		
		// Assert
		SWTBotTreeItem item; 
		try {
			item = bot.viewByTitle("Project Explorer").bot()
				.tree().expandNode("school.tests", "src", EIQFILE);
		} catch (WidgetNotFoundException e){
			item = null;
		}
		assertNotNull(EIQFILE + " was not created!", item);
	}
	
	// TODO Add gombot meg kellene találni valahogy
	//bot.buttonWithLabel("Add").click();
	//bot.tree().select("http://school.ecore");
	//bot.button("OK").click();
	
	@Test
	@Order(3)
	public void openQueryExplorer() {
		bot.menu("Window").menu("Show View").menu("Other...").click();
		SWTBotShell shell = bot.shell("Show View");
		shell.activate();
		
		bot.tree().expandNode("EMF-IncQuery").select("Query Explorer");
		bot.button("OK").click();

		bot.waitUntil(shellCloses(shell), 5000);
		boolean queryExplorerIsOpen;
		try {
			bot.viewByTitle("Query Explorer");
			queryExplorerIsOpen = true;
		} catch (WidgetNotFoundException e){
			queryExplorerIsOpen = false;
		}
		
		assertTrue("Could not open Query Explorer", queryExplorerIsOpen);
	}
		
	@Test
	@Order(4)
	public void loadModelToExplorerWithPlayButton(){
		createFolderInProject("model", PROJECTNAME);

		addFileFromBundleToFolder(MODELFILE,"test-files/BUTE.school",PROJECTNAME+"/model");
		getTreeItemFromProject(PROJECTNAME, "model", MODELFILE)
				.contextMenu("Open With").menu("School Model Editor").click();
		Matcher<IEditorReference> withPartName = WithPartName
				.withPartName(Matchers.containsString(MODELFILE));		
		bot.waitUntil(Conditions.waitForEditor(withPartName));
		
		//pressPlayButtonInQueryExplorer();
		SWTBotView queryExplorerBot = bot.viewByTitle("Query Explorer");
		queryExplorerBot.toolbarDropDownButton("Load model").click();
		
		// assert
		SWTBotTree middleTree = bot.viewByTitle("Query Explorer").bot().tree(1);
		try {
			middleTree.getTreeItem("[platform:/resource/"+
				PROJECTNAME+"/model/"+
				MODELFILE+"][school.presentation.SchoolEditorID]");
		} catch (WidgetNotFoundException e){
			fail("Model is not loaded to Query Explorer: "+MODELFILE);
		}
	}
	
	@Test
	@Order(5)
	public void setAndLoadTestPattern() {
		String pattern = "pattern schools(Sch) = { School(Sch); }";
		
		getTreeItemFromProject(PROJECTNAME,"src",EIQFILE).select().doubleClick();
		
		Matcher<IEditorReference> withPartName = WithPartName
				.withPartName(Matchers.containsString(EIQFILE));
		
		bot.waitUntil(Conditions.waitForEditor(withPartName));
		SWTBotEclipseEditor editor = bot.editorByTitle(EIQFILE).toTextEditor();
		editor.setText(getText("package "+PROJECTNAME,"import \"http://school.ecore\"",pattern));
		editor.save();
		
		SWTBotView queryExplorerBot = bot.viewByTitle("Query Explorer");
		queryExplorerBot.toolbarDropDownButton("Load model").click();

		// assert
		SWTBotTree tree = bot.viewByTitle("Query Explorer").bot().tree(0);
		tree.expandNode("Runtime", PROJECTNAME, "schools");	

		tree = bot.viewByTitle("Query Explorer").bot().tree(1);
		SWTBotTreeItem[] items = tree.expandNode("[platform:/resource/"+
				PROJECTNAME+"/model/"+
				MODELFILE+"][school.presentation.SchoolEditorID]").getItems();
		boolean found = false;
		for (SWTBotTreeItem item : items){
			if(item.getText().startsWith(PROJECTNAME+".schools")){
				found = true;
				break;
			}
		}
		assertTrue(found);
	}
	
	public void setupProjectDependency() {
		SWTBot projectExplorerBot = bot.viewByTitle("Project Explorer").bot();
		SWTBotTree tree = projectExplorerBot.tree();
		
		tree.expandNode(PROJECTNAME,"META-INF","MANIFEST.MF")
			.contextMenu("Open With").menu("Text Editor").click();

		Matcher<IEditorReference> withPartName = WithPartName
				.withPartName(Matchers.containsString("MANIFEST.MF"));
		try {
			bot.waitUntil(Conditions.waitForEditor(withPartName));
		} catch (TimeoutException toe) {
			//node.doubleClick();
			//bot.waitUntil(Conditions.waitForEditor(withPartName));
			// FIXME
		}

		SWTBotEclipseEditor textEditor = bot.editorByTitle("MANIFEST.MF").toTextEditor();
		textEditor.setFocus();
		bot.menu("Edit").menu("Select All").click();
		textEditor.setText(getManifestText(PROJECTNAME));
		textEditor.saveAndClose();

		bot.viewByTitle("Project Explorer").show();
	}

}
