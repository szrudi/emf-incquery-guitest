package org.eclipse.incquery.tooling.ui.tests.swtbot.helper;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;

import java.util.Arrays;

import org.eclipse.incquery.tooling.ui.tests.Activator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.matchers.WithPartName;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.FileUtils;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jnario.runner.FeatureRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;

/*
 * From: http://cataquavice.svn.sourceforge.net/viewvc/cataquavice/trunk/tests/de.abg.jreichert.serviceqos.ui.test/src/de/abg/jreichert/serviceqos/ui/swtbottest/AbstractSwtBotTester.java?revision=439&view=markup
 */

//@RunWith(SWTBotJunit4ClassRunner.class)
@RunWith(FeatureRunner.class)
public abstract class AbstractIncQueryTester {

    protected static SWTWorkbenchBot bot;
    
    @BeforeClass
    public static void beforeClass() throws Exception {
        bot = new SWTWorkbenchBot();
        SWTBotView view = bot.activeView();
        if(view != null && view.getTitle().equals("Welcome")) {
        	view.close();
        }
    }

    @AfterClass
    public static void destroy() {
        UIThreadRunnable.syncExec(new VoidResult() {
            public void run() {
                resetWorkbench();
            }
        });
    }

    private static void resetWorkbench() {
        try {
            IWorkbench workbench = PlatformUI.getWorkbench();
            IWorkbenchWindow workbenchWindow =
                    workbench.getActiveWorkbenchWindow();
            IWorkbenchPage page = workbenchWindow.getActivePage();
            Shell activeShell = Display.getCurrent().getActiveShell();
            if (activeShell != workbenchWindow.getShell()) {
                activeShell.close();
            }
            
            page.closeAllEditors(false);
            String defaultPerspectiveId =
                    workbench.getPerspectiveRegistry().getDefaultPerspective();
            workbench.showPerspective(defaultPerspectiveId, workbenchWindow);
            //page.resetPerspective();
        } catch (WorkbenchException e) {
            throw new RuntimeException(e);
        }
    }
	
	protected String getManifestText(String projectName) {
		return getText(
				"Manifest-Version: 1.0",
				"Bundle-ManifestVersion: 2",
				"Bundle-Name: "+projectName,
				"Bundle-SymbolicName: "+projectName+";singleton:=true",		
				"Bundle-Version: 0.0.1.qualifier",
				"Require-Bundle: org.eclipse.emf.ecore,",
				" org.eclipse.emf.transaction,",
				" org.eclipse.incquery.runtime,",
				" org.eclipse.xtext.xbase.lib,",
				" school;bundle-version=\"0.7.0\",",
				" school.edit;bundle-version=\"0.7.0\",",
				" school.editor;bundle-version=\"0.7.0\"",
				"Bundle-RequiredExecutionEnvironment: JavaSE-1.6");
	}

	protected String getText(String... lines) {
		StringBuilder sb = new StringBuilder();
		for (String line : lines) {
			sb.append(line).append("\n");
		}
		return sb.toString();
	}
	protected SWTBotTreeItem getTreeItemFromProject(String... strings) {
		return bot.viewByTitle("Project Explorer").bot().tree().expandNode(strings);
	}
	protected void createFolderInProject(String folder, String project) {
		bot.menu("File").menu("New").menu("Folder").click();
		SWTBotShell shell = bot.shell("New Folder");
		shell.activate();
		bot.tree().select(project);
		bot.textWithLabel("Folder name:").setText(folder);
		bot.button("Finish").click();
		bot.waitUntil(shellCloses(shell), 5000);
	}
	public void addFileFromBundleToFolder(String file, String from, String to) {
		bot.menu("File").menu("New").menu("File").click();
		SWTBotShell shell = bot.shell("New File");
		shell.activate();
		bot.tree().expandNode(to.split("/")).select();
		bot.textWithLabel("File name:").setText(file);
		bot.button("Finish").click();
		bot.waitUntil(shellCloses(shell), 30000);
		
		bot.editorByTitle(file).close();
		
		getTreeItemFromProject(append(to.split("/"),file))
				.contextMenu("Open With").menu("Text Editor").click();

		Matcher<IEditorReference> withPartName = WithPartName
				.withPartName(Matchers.containsString(file));		
		bot.waitUntil(Conditions.waitForEditor(withPartName));

		Bundle bundle = Activator.getContext().getBundle();
		String contents = FileUtils.read(bundle.getEntry(from));

		SWTBotEclipseEditor editor = bot.editor(withPartName).toTextEditor();		
		editor.setText(contents);
		editor.saveAndClose();
	}
	
	protected static <T> T[] append(T[] arr, T element) {
	    final int N = arr.length;
	    arr = Arrays.copyOf(arr, N + 1);
	    arr[N] = element;
	    return arr;
	}
	
	/*
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
	}*/

}