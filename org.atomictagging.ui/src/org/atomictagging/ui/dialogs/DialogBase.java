/**
 * This file is part of Atomic Tagging.
 * 
 * Atomic Tagging is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Atomic Tagging is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Atomic Tagging. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.atomictagging.ui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author strange
 * 
 */
public abstract class DialogBase extends Dialog {

	private Shell	dialogShell;


	/**
	 * @param parent
	 * @param style
	 */
	public DialogBase( Shell parent, int style ) {
		super( parent, style );
	}


	public void open() {
		dialogShell = new Shell( getParent(), SWT.DIALOG_TRIM );

		final GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;

		dialogShell.setLayout( gridLayout );

		createContents( dialogShell );

		dialogShell.layout();
		dialogShell.pack();

		dialogShell.open();

		Display display = dialogShell.getDisplay();
		while ( !dialogShell.isDisposed() ) {
			if ( !display.readAndDispatch() )
				display.sleep();
		}

	}


	public abstract void createContents( Shell shell );


	public void close() {
		dialogShell.close();
	}
}
