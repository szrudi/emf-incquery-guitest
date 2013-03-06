package org.eclipse.incquery.tooling.ui.tests;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/*
 * From: http://cataquavice.svn.sourceforge.net/viewvc/cataquavice/trunk/tests/de.abg.jreichert.serviceqos.ui.test/src/de/abg/jreichert/serviceqos/ui/swtbottest/AbstractSwtBotTester.java?revision=439&view=markup
 */

@RunWith(SWTBotJunit4ClassRunner.class)
public abstract class AbstractSwtBotTester {

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
    
	protected void createIncQueryProject(String projectName) {
		bot.menu("File").menu("New").menu("Project...").click();
		bot.shell("New Project").activate();
		bot.tree().expandNode("EMF-IncQuery").select("EMF-IncQuery Project");
		bot.button("Next >").click();
		bot.textWithLabel("Project name:").setText(projectName);
		bot.button("Finish").click();
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
            page.resetPerspective();
        } catch (WorkbenchException e) {
            throw new RuntimeException(e);
        }
    }    
}