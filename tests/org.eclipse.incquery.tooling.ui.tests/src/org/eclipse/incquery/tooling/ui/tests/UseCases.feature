package org.eclipse.incquery.tooling.ui.tests

import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Editor
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Window
import org.eclipse.incquery.tooling.ui.tests.swtbot.helper.UiTestBot
import org.eclipse.incquery.tooling.ui.tests.swtbot.specific.Wizard
import org.jnario.lib.StepArguments

import static extension org.eclipse.incquery.tooling.ui.tests.UseCasesFeatureBackground.*
import static extension org.jnario.lib.JnarioIterableExtensions.*
import static extension org.jnario.lib.Should.*
	
Feature:  Use cases
	EMF-IncQuery provides an intuitive user interface to create, edit and debug queries.
	It assists users with various wizards, supports the editing of query definitions (patterns) in an
	Xtext-based editor and also provides query debugging features with the Query Explorer view.
	
	Background:
	extension static UiTestBot b = new UiTestBot()
	Given you have Eclipse set-up and EMF-IncQuery installed   
		b.ready => true
		
	Scenario: 3.4.1 New EMF-IncQuery project wizard
		var Wizard w
		Given "EMF-IncQuery, EMF-IncQuery Project" wizard is open 
			w = b.wizard(args.first.split(", ")).focus
			//b.help.captureScreenshot(args.first + "-window.png",shell)
		And the "Finish" button is inactive
			b.find.button(args.first).isInactive => true
		When entering "school.tests" in the "Project name:" text field
			b.find.text(args.second).setText(args.first)
		And the "Finish" button is clicked 
			b.find.button(args.first).click
		And after waiting for the wizard to close
			w.waitUntilCloses
		And the dependancies are set up for project "school.tests" as
		''' Manifest-Version: 1.0
			Bundle-ManifestVersion: 2
			Bundle-Name: school.tests
			Bundle-SymbolicName: school.tests;singleton:=true
			Bundle-Version: 0.0.1.qualifier
			Require-Bundle: org.eclipse.emf.ecore,
			 org.eclipse.emf.transaction,
			 org.eclipse.incquery.runtime,
			 org.eclipse.xtext.xbase.lib,
			 school;bundle-version="0.7.0",
			 school.edit;bundle-version="0.7.0",
			 school.editor;bundle-version="0.7.0"
			Bundle-RequiredExecutionEnvironment: JavaSE-1.6
		''' 
			b.help.setupProjectDependency(args.first, args.second)
		Then in the ProjectExplorer there is a "school.tests" item
			assert b.projectExplorer.hasItem(args.first.split(", ")) => true
		
	Scenario: 3.4.2 New EMF-IncQuery query definition
		Given "EMF-IncQuery, EMF-IncQuery Query Definition" wizard is open
		When entering "schoolQueries.eiq" in the "File name:" text field
		And the "Next >" button is clicked
		And entering "testPattern" in the "Pattern name:" text field
		And the "Finish" button is clicked 
		And after waiting for the wizard to close
		Then in the ProjectExplorer there is a "school.tests, src, schoolQueries.eiq" item
	
	Scenario: 3.4.3 New Generator model wizard
		Given the "test-files/GenModSampleProject" is imported to the workspace
		And "EMF-IncQuery, EMF-IncQuery Generator model" wizard is open
		When the "GenModSampleProject" is selected from the table
		And the "school, model, school.genmodel" is selected from the tree 
		And the "Finish" button is clicked 
		And after waiting for the wizard to close
		Then in the ProjectExplorer there is a "school.tests, generator.eiqgen" item
		
	Scenario: 3.4.4 Open Query Explorer
		var Window w
		When "Show View, Other..." is selected from the "Window" menu
			b.find.menu(args.second).choose(args.first.split(", "))
		And "Show View" window is open
			w = b.find.window(args.first).focus 
		And the "EMF-IncQuery, Query Explorer" is selected from the tree 
			b.find.tree.choose(args.first.split(", "))
		And the "OK" button is clicked
		And after waiting for the window to close
			w.waitUntilCloses
		Then "Query Explorer" view is open
			b.find.view(args.first)

	Scenario: 3.4.5 Edit Queries
		var Editor e
		Given the Query Explorer is open
			b.queryExplorer
		And "schoolQueries.eiq" editor is open
			e = b.find.editor(args.first)
		When these test patterns are entered: 
		'''
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
		'''
			e.setText(args.first)
		And the changes in the "schoolQueries.eiq" editor are saved 
			e.save()
		And the Green Button of Query Explorer is clicked
		Then in the Pattern Registry there is a "Runtime, school.tests" item 
			b.queryExplorer.patternRegistry.hasItem(args.first.split(", ")) => true	
		And I make a screenshot named "pattern-loaded.jpg"

	Scenario: 3.4.6 Load model instance by pressing play button
		Given "model" folder is created in the "school.tests" project
			b.help.createFolderInPath(args.first, args.second) 
		And the "test-files/BUTE.school" file is copied to "school.tests, model"
			b.help.addFileFromBundleToFolder(
				args.first.split("/").last,
				args.first,
				args.second.split(", ")
			)
		And the Query Explorer is open
		When the "school.tests, model, BUTE.school" is opened from the Project Explorer
			b.projectExplorer.activate(args.first.split(", "))
		And after waiting for the "BUTE.school" editor to open 
			b.help.waitFor.editor(args.first)
		And the Green Button of Query Explorer is clicked
			b.queryExplorer.greenButton.click
		Then the "BUTE.school" model is loaded in the middle tree of Query Explorer from the "school.tests" project
			b.queryExplorer.middleTree.hasItem(
				"[platform:/resource/"+args.second+"/model/"+args.first+"][school.presentation.SchoolEditorID]") => true
		And I make a screenshot named "model-loaded.jpg"
			b.help.captureScreenshot(args.first)
	
	Scenario: 3.4.7 Query results are shown 
		Given the "BUTE.school" model is loaded
		And the "schoolQueries.eiq" queries are loaded
		And I have results in the middle tree
		When I modify the querys in the query editor
		Then the query results should be the new results
	
	Scenario: 3.4.8 Results follow Pattern Registry checkmars
		Given the "BUTE.school" model is loaded
		And the "schoolQueries.eiq" queries are loaded
		And I have results in the middle tree
		When I modify the checkmark of a query in the Pattern Registy
		Then the query results should resamble this change
	
	Scenario: 3.4.9 Details/Filters show result details
		Given the "BUTE.school" model is loaded
		And the "schoolQueries.eiq" queries are loaded
		And I have results in the middle tree
		When I modify select a result in the middle tree
		Then the Details/Filters table should show the details of that result
