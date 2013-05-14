package org.eclipse.incquery.tooling.ui.tests.swtbot.specific

import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.Tree
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.View

class MiddleTree extends Tree {
	
	new(View qeView){
		super(1,qeView.widget)
	}
}
	