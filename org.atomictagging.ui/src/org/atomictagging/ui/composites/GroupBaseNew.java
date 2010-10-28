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
package org.atomictagging.ui.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author strangeoptics
 * 
 */
public class GroupBaseNew extends Group {

	/**
	 * @param parent
	 * @param style
	 */
	public GroupBaseNew( final Composite parent, final int style ) {
		super( parent, style );
		// TODO Auto-generated constructor stub
	}


	protected Text createText( final Composite parent ) {
		final Text text = new Text( parent, SWT.BORDER );

		final GridData gd = new GridData( SWT.FILL, SWT.TOP, true, false );
		text.setLayoutData( gd );

		return text;
	}


	protected Label createLabel( final Composite parent, final String text ) {
		final Label label = new Label( parent, SWT.NONE );

		label.setText( text );

		final GridData gd = new GridData( SWT.FILL, SWT.TOP, false, false );
		label.setLayoutData( gd );

		return label;
	}


	@Override
	protected void checkSubclass() {
	}

}
