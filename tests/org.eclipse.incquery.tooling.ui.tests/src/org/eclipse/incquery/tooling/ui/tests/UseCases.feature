package org.eclipse.incquery.tooling.ui.tests

import org.eclipse.incquery.tooling.ui.tests.swtbot.Editor
import org.eclipse.incquery.tooling.ui.tests.swtbot.Shell
import org.jnario.lib.StepArguments

import static org.eclipse.incquery.tooling.ui.tests.UseCasesFeatureBackground.*

import static extension org.jnario.lib.JnarioIterableExtensions.*
	
Feature:  Use cases
	Background:
		extension static UiTestHelper ui = new UiTestHelper()
		var String projectName
	
	Scenario: New EMF-IncQuery project wizard
		var Shell shell
		Given "New, Project..." is selected from the "File" menu
			ui.menu(args.second).choose(args.first.split(", "))
		And "New Project" window is open
			shell = ui.shell(args.first).focus 
			ui.captureScreenshot(args.first + "-window.png")
		Then I select "EMF-IncQuery Project" under "EMF-IncQuery"
			ui.tree.choose(args.second, args.first)

	Scenario: New EMF-IncQuery project created
		var Shell shell
		Given "New Project" window is open 
		/*And I want to create a "school.tests" project
			projectName = args.first*/
		When  I click on the "Next >" button
			ui.button(args.first).click 
		And enter "school.tests" in the text  field "Project name:"
			ui.text(args.second).setText(args.first)
		And I click on the "Finish" button 
		And wait for the window to close 
			shell.waitUntilCloses
		Then I will have "school.tests" in the "Project Explorer" view
			ui.view(args.second).focus
			ui.tree.choose(args.first.split(", "))
			
//Feature: Executing and debugging queries
	Scenario: Open Query Explorer
		When "Show View, Other..." is selected from the "Window" menu
		And "Show View" window is open
		And I select "Query Explorer" under "EMF-IncQuery"
		And I click on the "OK" button
		And wait for the window to close
		Then "Query Explorer" view should be open
			ui.view(args.first) 
		
	Scenario: New EMF-IncQuery query definition
		var Shell shell
		When "New, Other..." is selected from the "File" menu
		And "New" window is open
		And I select "EMF-IncQuery Query Definition" under "EMF-IncQuery"
		And I click on the "Next >" button
		And enter "schoolQueries.eiq" in the text  field "File name:"
		And I click on the "Next >" button again
			// TODO itt nem k�ne implement�ci�....
			ui.button(args.first).click  
		And enter "testPattern" in the text  field "Pattern name:"
		// TODO Add gombot meg kellene tal�lni valahogy
		And I click on the "Finish" button 
		And wait for the window to close 
		Then I will have "school.tests, src, schoolQueries.eiq" in the "Project Explorer" view
		
	Scenario: Edit Queries
		var Editor e
		When "schoolQueries.eiq" editor is open
			e = ui.editor(args.first)
		And I enter a test pattern and save changes
			e.setText('''
						package school.tests
						import "http://school.ecore
						pattern schools(Sch) = { School(Sch); }
						''')
			e.save
		Then something good happens
			println("good") 

		
		
		