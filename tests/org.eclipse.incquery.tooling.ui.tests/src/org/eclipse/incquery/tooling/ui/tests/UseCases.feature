package org.eclipse.incquery.tooling.ui.tests

import org.eclipse.incquery.tooling.ui.tests.swtbot.Editor
import org.eclipse.incquery.tooling.ui.tests.swtbot.Shell
import org.eclipse.incquery.tooling.ui.tests.swtbot.View
import org.eclipse.incquery.tooling.ui.tests.swtbot.Wait
import org.jnario.lib.StepArguments

import static org.eclipse.incquery.tooling.ui.tests.UseCasesFeatureBackground.*

import static extension org.jnario.lib.JnarioIterableExtensions.*
import static extension org.jnario.lib.StringConversions.*
	
Feature:  Use cases
	Background:
		extension static UiTestHelper ui = new UiTestHelper()
		//var String projectName
	
	Scenario: New EMF-IncQuery project wizard
		var Shell shell
		Given "New, Project..." is selected from the "File" menu
			ui.menu(args.second).choose(args.first.split(", "))
		And "New Project" window is open
			shell = ui.shell(args.first).focus 
			ui.captureScreenshot(args.first + "-window.png",shell)
		Then I select "EMF-IncQuery Project" under "EMF-IncQuery"
			ui.tree.choose(args.second, args.first)

	Scenario: New EMF-IncQuery project created
		var Shell shell
		Given "New Project" window is open 
		/*And I want to create a "school.tests" project
			projectName = args.first*/
		When  I click on the "Next >" button
			ui.button(args.first).click 
		And enter "school.tests" in the text field "Project name:"
			ui.text(args.second).setText(args.first)
		And I click on the "Finish" button 
		And wait for the window to close
			shell.waitUntilCloses
		And set up dependancies for project "school.tests"
			ui.setupProjectDependency(args.first, '''
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
		Then I  have "school.tests" in the "Project Explorer" view
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

	Scenario: Load model instance by pressing play button
		var View qeView
		Given I  create a "model" folder in "school.tests"
			ui.createFolderInPath(args.first, args.second) 
		And copied my "test-files/BUTE.school" file to "school.tests, model"
			ui.addFileFromBundleToFolder(
				args.first.split("/").last,
				args.first,
				args.second.split(", ")
			)
		And I open the "school.tests, model, BUTE.school" from the "Project Explorer" view in the default editor
			ui.view(args.second).focus
			ui.tree.doubleClick(args.first.split(", "))
		And wait for the "BUTE.school" editor to open
			Wait::forEditor(args.first)
		And I have a "Query Explorer" view open
			qeView = ui.view(args.first).focus
		And I click on  the "Load model" dropdown button in the Query Explorer widget
			ui.toolbarDropDownbutton(args.first, qeView).click
		Then model "BUTE.school" should be loaded in the Query Explorer view's tree with index "1" from the "school.tests" project
			ui.tree(args.second.toInt,qeView).choose(
				"[platform:/resource/"+args.third+"/model/"+args.first+"][school.presentation.SchoolEditorID]")
		And  I make a screenshot  named "model-loaded.jpg"
			ui.captureScreenshot(args.first)
			
		
	Scenario: New EMF-IncQuery query definition
		var Shell shell
		When "New, Other..." is selected from the "File" menu
		And "New" window is open
		And I select "EMF-IncQuery Query Definition" under "EMF-IncQuery"
		And I click on the "Next >" button
		And enter "schoolQueries.eiq" in the text field "File name:"
		And I click on the "Next >" button again
			// TODO itt nem kéne implementáció....
			ui.button(args.first).click  
		And enter "testPattern" in the text field "Pattern name:"
		// TODO Add gombot meg kellene találni valahogy
		And I click on the "Finish" button 
		And wait for the window to close 
		Then I  have "school.tests, src, schoolQueries.eiq" in the "Project Explorer" view
		
	Scenario: Edit Queries
		var Editor e
		var View qeView 
		When "schoolQueries.eiq" editor is open
			e = ui.editor(args.first)
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
			ui.editor(args.first).save()
		And I have a "Query Explorer" view open
		And I click on  the "Load model" dropdown button in the Query Explorer widget
		Then I have "Runtime, school.tests" in Query Explorer view's tree with index "0"
			ui.tree(args.second.toInt,qeView).choose(args.first.split(", "))	
		And I make a screenshot named "pattern-loaded.jpg"
		
		
		