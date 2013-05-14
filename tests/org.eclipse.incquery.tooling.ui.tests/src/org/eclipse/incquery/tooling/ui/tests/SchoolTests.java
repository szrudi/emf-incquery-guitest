package org.eclipse.incquery.tooling.ui.tests;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;
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
import org.eclipse.swtbot.swt.finder.utils.FileUtils;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;
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
import org.osgi.framework.Bundle;

@RunWith(FeatureRunner.class)
public class SchoolTests extends AbstractIncQueryTester{
	protected static String PROJECTNAME = "school.tests";
	protected static String EIQFILE = "schoolQueries.eiq";
	protected static String MODELFILE = "BUTE.school";
   
	@Test
	@Order(0)
	public void createIncQueryProject() {
		bot.menu("File").menu("New").menu("Project...").click();
		SWTBotShell shell = bot.shell("New Project");
		shell.activate();
		bot.tree().expandNode("EMF-IncQuery","EMF-IncQuery Project").select();
		bot.button("Next >").click();
		bot.textWithLabel("Project name:").setText(PROJECTNAME);
		bot.button("Finish").click();
		bot.waitUntil(shellCloses(shell), 20000);
	}
	
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
	@Order(1)
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
	
	@Test
	@Order(2)
	public void openQueryExplorer() {
		bot.menu("Window").menu("Show View").menu("Other...").click();
		SWTBotShell shell = bot.shell("Show View");
		shell.activate();
		
		bot.tree().expandNode("EMF-IncQuery").select("Query Explorer");
		bot.button("OK").click();

		bot.waitUntil(shellCloses(shell), 5000);
	}

	@Test
	@Order(3)
	public void createModelFolder() {
		bot.menu("File").menu("New").menu("Folder").click();
		SWTBotShell shell = bot.shell("New Folder");
		shell.activate();
		bot.tree().select(PROJECTNAME);
		bot.textWithLabel("Folder name:").setText("model");
		bot.button("Finish").click();
		bot.waitUntil(shellCloses(shell), 5000);
	}
	
	@Test
	@Order(4)
	public void copyButeModel() {
		bot.menu("File").menu("New").menu("File").click();
		SWTBotShell shell = bot.shell("New File");
		shell.activate();
		bot.tree().expandNode(PROJECTNAME, "model").select();
		bot.textWithLabel("File name:").setText(MODELFILE);
		bot.button("Finish").click();
		bot.waitUntil(shellCloses(shell), 30000);
		
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
	
	@Test
	@Order(5)
	public void createNewEiq() {
		bot.menu("File").menu("New").menu("Other...").click();
		SWTBotShell shell = bot.shell("New");
		shell.activate();
		bot.tree().expandNode("EMF-IncQuery")
			.select("EMF-IncQuery Query Definition");
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
	
	@Test
	@Order(6)
	public void setTestPattern() {
		String pattern = "pattern schools(Sch) = { School(Sch); }";
		getTreeItemFromProject(PROJECTNAME,"src",EIQFILE).select().doubleClick();
		
		Matcher<IEditorReference> withPartName = WithPartName
				.withPartName(Matchers.containsString(EIQFILE));
		
		bot.waitUntil(Conditions.waitForEditor(withPartName));
		SWTBotEclipseEditor editor = bot.editorByTitle(EIQFILE).toTextEditor();
		editor.setText(getText("package "+PROJECTNAME,"import \"http://school.ecore\"",pattern));
        editor.saveAndClose();
	}
	
	@Test
	@Order(7)
	public void loadModelToExplorerWithPlayButton(){
		getTreeItemFromProject(PROJECTNAME, "model", MODELFILE)
				.contextMenu("Open With").menu("School Model Editor").click();
		Matcher<IEditorReference> withPartName = WithPartName
				.withPartName(Matchers.containsString(MODELFILE));		
		bot.waitUntil(Conditions.waitForEditor(withPartName));
		
		pressPlayButtonInQueryExplorer();
		
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
	@Order(8)
	public void loadPatternsToExplorerWithPlayButton(){
		getTreeItemFromProject(PROJECTNAME, "src", EIQFILE).select().doubleClick();
		
		Matcher<IEditorReference> withPartName = WithPartName
				.withPartName(Matchers.containsString(EIQFILE));		
		bot.waitUntil(Conditions.waitForEditor(withPartName),15*1000);
		
		pressPlayButtonInQueryExplorer();	

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
	
	private void pressPlayButtonInQueryExplorer(){
		pressPlayButtonInQueryExplorer("click");
	}
	private void pressPlayButtonInQueryExplorer(String action){
		SWTBotView queryExplorerBot = bot.viewByTitle("Query Explorer");
		SWTBotToolbarDropDownButton playBtn = queryExplorerBot.toolbarDropDownButton("Load model");
		if (action == "click"){
			playBtn.click();
		} else {
			playBtn.menuItem(action).click();
		}		
	}
}
