package org.eclipse.incquery.tooling.ui.tests

import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Editor
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Window
import org.eclipse.incquery.tooling.ui.tests.swtbot.helper.UiTestBot
import org.jnario.lib.StepArguments
import org.eclipse.incquery.tooling.ui.tests.swtbot.specific.Wizard

import static extension org.jnario.lib.JnarioIterableExtensions.*
import static extension org.jnario.lib.Should.*
	
Feature:  I want some examples for my thesis
	
	Background:
		extension static UiTestBot b = new UiTestBot()
	
	Scenario: New EMF-IncQuery project wizard
		var Window w
		Given "New, Project..." is selected from the "File" menu
			b.find.menu(args.second).choose(args.first.split(", "))
		And "New Project" window is open
			w = b.find.window(args.first).focus
			b.help.captureScreenshot(args.first + "-window.png",w)
		Then I select "EMF-IncQuery Project" under "EMF-IncQuery" 
			b.find.tree.choose(args.second, args.first)

	Scenario: New EMF-IncQuery project created
		var Window w
		Given "New Project" window is open 
		When  I click on the "Next >" button
			b.find.button(args.first).click
		And the "Finish" button is inactive
			b.find.button(args.first).isInactive => true
		And enter "test_project" in the text field "Project name:"
			b.find.text(args.second).setText(args.first)
		And I click on the "Finish" button
		And wait for the window to close
			w.waitUntilCloses
		And set up dependancies for project "test_project"
			b.help.setupProjectDependency(args.first, '''
			Manifest-Version: 1.0
			Bundle-ManifestVersion: 2
			Bundle-Name: «args.first»
			Bundle-SymbolicName: «args.first»;singleton:=true
			Bundle-Version: 0.0.1.qualifier
			Require-Bundle: org.eclipse.emf.ecore,
			 org.eclipse.emf.transaction,
			 org.eclipse.incquery.runtime,
			 org.eclipse.xtext.xbase.lib,
			 school;bundle-version="0.7.0",
			 school.edit;bundle-version="0.7.0",
			 school.editor;bundle-version="0.7.0"
			Bundle-RequiredExecutionEnvironment: JavaSE-1.6
			''')
		Then I  have "test_project" in the "Project Explorer" view
			b.find.view(args.second).focus
			b.find.tree.choose(args.first.split(", "))


		
	Scenario: 3.4.2 New EMF-IncQuery query definition
		var Wizard w
		Given "EMF-IncQuery, Query Definition" wizard is open
			w = b.wizard(args.first.split(", ")).focus
		When entering "schoolQueries.eiq" in the "File name:" text field
			b.find.text(args.second).setText(args.first)
		And the "Next >" button is clicked
			b.find.button(args.first).click
		And entering "testPattern" in the "Pattern name:" text field
		And the "Finish" button is clicked 
		And after waiting for the wizard to close
			b.help.waitFor.element(w)
		Then in the ProjectExplorer there is a "[..]schoolQueries.eiq" item
			b.projectExplorer.hasItem(
				args.first.split(", ")) => true

	Scenario: Open my.eiq file and set test pattern
		var Editor e
		Given I open the "test_project, src, my.eiq" file from the "Project Explorer" view
			b.find.view(args.second).focus
			b.find.tree.activate(args.first.split(", "))
		And wait for the "my.eiq" editor to open 
			b.help.waitFor.editor(args.first)
			e = b.find.editor(args.first)
		And I enter the  test pattern 
			e.setText("pattern schools(Sch) = { School(Sch); }")
		And save changes in the "my.eiq" editor and close it 
			b.find.editor(args.first).saveAndClose		
		 
		
Scenario: Add two numbers
  val calc = new Calculator()
  Given I have entered "50" into the calculator
	calc.enter(args.first)
  And I have entered "70" into the calculator
     When I press "add"
	calc.press(args.first)
  Then the result should be "120" 
  	calc.result => args.first
						