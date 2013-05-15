package org.eclipse.incquery.tooling.ui.tests

import static org.eclipse.incquery.tooling.ui.tests.QueryEditorFeatureBackground.*
import org.eclipse.incquery.tooling.ui.tests.swtbot.helper.UiTestBot

Feature: Query editor
	
	Background:
		extension static UiTestBot b = new UiTestBot()
		Given you have Eclipse set-up and EMF-IncQuery installed   
			b.ready => true
	
	