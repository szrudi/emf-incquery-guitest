package org.eclipse.incquery.tooling.ui.tests.swtbot.specific

import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Tree
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.View

class PatternRegistry extends Tree {
	new(View qeView){
		super(0,qeView.widget)
	}
}
	