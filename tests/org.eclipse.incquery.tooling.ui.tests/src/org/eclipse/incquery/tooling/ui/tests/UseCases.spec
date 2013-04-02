package org.eclipse.incquery.tooling.ui.tests

import static extension org.eclipse.incquery.tooling.ui.tests.UseCasesSpecSpec.*

describe "Use cases spec" {
	
	extension static UiTestHelper api = new UiTestHelper()
	static val projectName = "school.tests"
	
	/*
	 * EMF-IncQuery project is the container of all EMF-IncQuery related artifacts.
	 * The wizard can be found under the EMF-IncQuery category.
	 * 
	 * In File > New > Project selecting EMF-IncQuery Project opens up the
	 * "New Project" wizard
	 */
	fact "New EMF-IncQuery project wizard" {
		"File".menu.choose("New", "Project...")
		"New Project".shell.focus
		tree.choose("EMF-IncQuery","EMF-IncQuery Project")
	}
	
	/*
	 * After finishing the new project wizard, the project is created.
	 */
	fact "New EMF-IncQuery project created" {
		val shell = "New Project".shell
		"Next >".button.click
		"Project name:".text.setText(projectName)
		"Finish".button.click
		shell.waitUntilCloses
		"Project Explorer".view.focus
		tree.choose(projectName)
	}
	/*
	 * Query explorer view can be opened from Show View > Other... menu
	 */
	fact "Query Explorer opens" {
		"Window".menu.choose("Show View", "Other...")
		val shell = "Show View".shell.focus
		tree.choose("EMF-IncQuery","Query Explorer")
		"OK".button.click
		shell.waitUntilCloses
		"Query Explorer".view
	}
}
