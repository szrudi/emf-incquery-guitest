package org.eclipse.incquery.tooling.ui.tests

import org.eclipse.incquery.tooling.ui.tests.swtbot.helper.UiTestBot
import org.eclipse.incquery.tooling.ui.tests.swtbot.specific.Wizard
import org.jnario.lib.StepArguments

import static org.eclipse.incquery.tooling.ui.tests.UseCasesFeatureBackground.*

import static extension org.jnario.lib.JnarioIterableExtensions.*
	
Feature:  Wizards
	
	Background:
		extension static UiTestBot b = new UiTestBot()
		Given you have Eclipse set-up and EMF-IncQuery installed   
			b.ready => true
	
	Scenario: New EMF-IncQuery Project wizard
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
		
	Scenario: New EMF-IncQuery Query Definition wizard
		Given "EMF-IncQuery, EMF-IncQuery Query Definition" wizard is open
		When entering "schoolQueries.eiq" in the "File name:" text field
		And the "Next >" button is clicked
		And entering "testPattern" in the "Pattern name:" text field
		And the "Finish" button is clicked 
		And after waiting for the wizard to close
		Then in the ProjectExplorer there is a "school.tests, src, schoolQueries.eiq" item
	
	Scenario: New EMF-IncQuery Generator model wizard
		Given the "test-files/GenModSampleProject" is imported to the workspace
		And "EMF-IncQuery, EMF-IncQuery Generator model" wizard is open
		When the "GenModSampleProject" is selected from the table
		And the "school, model, school.genmodel" is selected from the tree 
		And the "Finish" button is clicked 
		And after waiting for the wizard to close
		Then in the ProjectExplorer there is a "school.tests, generator.eiqgen" item
		 
		
		