package org.eclipse.incquery.tooling.ui.tests.swtbot.specific

import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.ToolbarDropDownButton
import org.eclipse.incquery.tooling.ui.tests.swtbot.basic.View

class GreenButton extends ToolbarDropDownButton {

	new(View qeView){
		super("Load model",qeView.widget)
	}
}
