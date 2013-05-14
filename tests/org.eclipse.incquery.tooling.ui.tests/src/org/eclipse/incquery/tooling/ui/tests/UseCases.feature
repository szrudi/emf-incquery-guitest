package org.eclipse.incquery.tooling.ui.tests

import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Editor
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Window
import org.eclipse.incquery.tooling.ui.tests.swtbot.helper.UiTestBot
import org.eclipse.incquery.tooling.ui.tests.swtbot.specific.Wizard
import org.jnario.lib.StepArguments

import static org.eclipse.incquery.tooling.ui.tests.UseCasesFeatureBackground.*

import static extension org.jnario.lib.JnarioIterableExtensions.*
import static extension org.jnario.lib.Should.*

Feature:  Use cases
	Background:
		extension static UiTestBot b = new UiTestBot()
		//var String projectName

	Scenario: New EMF-IncQuery project wizard
		var Wizard w
		Given "EMF-IncQuery, EMF-IncQuery Project" wizard is open
			w = b.wizard(args.first.split(", ")).focus
			//b.help.captureScreenshot(args.first + "-window.png",shell)
		And the "Finish" button is inactive
			b.find.button(args.first).isInactive => true
		And enter "school.tests" in the text field "Project name:"
			b.find.text(args.second).setText(args.first)
		And I click on the "Finish" button
			b.find.button(args.first).click
		And wait for the wizard  to close
			w.waitUntilCloses
		And set up dependancies for project "school.tests"
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
		Then I  have "school.tests" in the Project Explorer
			b.projectExplorer.hasItem(args.first.split(", ")) => true
		
	Scenario: New EMF-IncQuery query definition
		Given "EMF-IncQuery, EMF-IncQuery Query Definition" wizard is open
		And enter "schoolQueries.eiq" in the text field "File name:"
		And I click on the "Next >" button
		And enter "testPattern" in the text field "Pattern name:"
		And I click on the "Finish" button 
		And wait for the window to close 
		Then I  have "school.tests, src, schoolQueries.eiq" in the Project Explorer
			
	Scenario: Open Query Explorer
		var Window w
		When "Show View, Other..." is selected from the "Window" menu
			b.find.menu(args.second).choose(args.first.split(", "))
		And "Show View" window is open
			w = b.find.window(args.first).focus 
		And I select "Query Explorer" under "EMF-IncQuery"
			b.find.tree.choose(args.second, args.first)
		And I click on the "OK" button
		And wait for the window to close
			w.waitUntilCloses
		Then "Query Explorer" view should be open
			b.find.view(args.first)

	Scenario: Edit Queries
		var Editor e
		When "schoolQueries.eiq" editor is open
			e = b.find.editor(args.first)
		And I enter the  test patterns
			e.setText('''
					package school.tests
					import "http://school.ecore"
					pattern students(S) = { 
						Student(S);
					}
					pattern teachersOfSchool(T:Teacher,Sch:School) = {
					 	School.teachers(Sch,T);
					}
					pattern coursesOfTeacher(T:Teacher, C:Course) = {
					 	Teacher.courses(T,C);
					} 
					pattern classesOfTeacher(T, SC) = {
					 	find coursesOfTeacher(T,C);
					 	Course.schoolClass(C,SC);
					}
					''')
		And save changes in the "schoolQueries.eiq" editor 
			e.save()
		And I click on  the Green Button of Query Explorer
		Then I have "Runtime, school.tests" in the Pattern Registry
			b.queryExplorer.patternRegistry.hasItem(args.first.split(", ")) => true	
		And I make a screenshot named "pattern-loaded.jpg"

	Scenario: Load model instance by pressing play button
		Given I  create a "model" folder in "school.tests"
			b.help.createFolderInPath(args.first, args.second) 
		And copied my "test-files/BUTE.school" file to "school.tests, model"
			b.help.addFileFromBundleToFolder(
				args.first.split("/").last,
				args.first,
				args.second.split(", ")
			)
		When I open the "school.tests, model, BUTE.school" from the Project Explorer view in the default editor
			b.projectExplorer.activate(args.first.split(", "))
		And wait for the "BUTE.school" editor to open 
			b.help.waitFor.editor(args.first)
		And I click on  the Green Button of Query Explorer
			b.queryExplorer.greenButton.click
		Then model "BUTE.school" should be loaded in the middle tree of Query Explorer from the "school.tests" project
			b.queryExplorer.middleTree.hasItem(
				"[platform:/resource/"+args.second+"/model/"+args.first+"][school.presentation.SchoolEditorID]") => true
		And  I make a screenshot named "model-loaded.jpg"
			b.help.captureScreenshot(args.first)


	Scenario: Query results are shown
		Given I have a model and a query open in the editor
		And they are loaded
		And I have results in the middle tree
		When I modify the querys in the query editor
		Then the query results should be the new results
