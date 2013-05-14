package org.eclipse.incquery.tooling.ui.tests

import org.eclipse.incquery.tooling.ui.tests.swtbot.helper.UiTestBot

import static org.eclipse.incquery.tooling.ui.tests.UseCasesSpecSpec.*

describe "Use cases spec" {
	
	extension static UiTestBot api = new UiTestBot()
	static val projectName = "school.tests"
	
	/*
	 * EMF-IncQuery project is the container of all EMF-IncQuery related artifacts.
	 * The wizard can be found under the EMF-IncQuery category.
	 * 
	 * In File > New > Project selecting EMF-IncQuery Project opens up the
	 * "New Project" wizard
	 */
	fact "New EMF-IncQuery project wizard" {
		find.menu("File").choose("New", "Project...")
		find.window("New Project").focus
		find.tree.choose("EMF-IncQuery","EMF-IncQuery Project")
	}
	
	/*
	 * After finishing the new project wizard, the project is created.
	 */
	fact "New EMF-IncQuery project created" {
		find.menu("File").choose("New", "Project...")
		val shell = find.window("New Project").focus
		find.tree.choose("EMF-IncQuery","EMF-IncQuery Project")
		find.button("Next >").click
		find.text("Project name:").setText(projectName)
		find.button("Finish").click
		shell.waitUntilCloses
		find.view("Project Explorer").focus
		find.tree.choose(projectName)
	}
	
	/*
	 * Query explorer view can be opened from Show View > Other... menu
	 */
	fact "Query Explorer opens" {
		find.menu("Window").choose("Show View", "Other...")
		val shell = find.window("Show View").focus
		find.tree.choose("EMF-IncQuery","Query Explorer")
		find.button("OK").click
		shell.waitUntilCloses
		find.view("Query Explorer")
	}
	
	fact "Open my.eiq file and set test pattern" {
		projectExplorer.open("test_project", "src", "my.eiq")
		help.waitFor.editor("my.eiq")
		find.editor("my.eiq")
			.setText("pattern schools(Sch) = { School(Sch); }")
		find.editor("my.eiq").save
		queryExplorer.greenButton.click
	}		
}
