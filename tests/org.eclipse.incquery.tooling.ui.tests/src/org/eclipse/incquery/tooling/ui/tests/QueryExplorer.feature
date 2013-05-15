package org.eclipse.incquery.tooling.ui.tests

import org.eclipse.incquery.tooling.ui.tests.swtbot.helper.UiTestBot

import static org.eclipse.incquery.tooling.ui.tests.QueryExplorerFeatureBackground.*

import static extension org.jnario.lib.Should.*

Feature: Query Explorer

	Background:
	extension static UiTestBot b = new UiTestBot()
	Given you have Eclipse set-up and EMF-IncQuery installed   
		b.ready => true

	