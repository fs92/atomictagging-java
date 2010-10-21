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

import org.atomictagging.core.services.ATService;
import org.atomictagging.core.services.IAtomService;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.ui.composites.CompositeAtom;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * @author strange
 * 
 */
public class DialogAtom extends DialogBase implements SelectionListener {

	private CompositeAtom		compAtom;
	private Composite			compButtons;
	private Button				btOk;
	private Button				btCancel;

	private IAtom				atom;

	private final IAtomService	atomService	= ATService.getAtomService();


	/**
	 * @param parent
	 * @param style
	 */
	public DialogAtom( final Shell parent, final int style ) {
		super( parent, style );

		setText( "Atom anlegen" );
	}


	@Override
	public void createContents( final Shell shell ) {

		compAtom = new CompositeAtom( shell, SWT.NONE );

		final GridData gd = new GridData( SWT.BEGINNING, SWT.FILL, false, false );
		gd.widthHint = 400;
		compAtom.setLayoutData( gd );

		compButtons = new Composite( shell, SWT.NONE );
		compButtons.setLayout( new GridLayout( 2, false ) );

		final GridData gdButtons = new GridData( SWT.FILL, SWT.TOP, true, false );
		compButtons.setLayoutData( gdButtons );

		btOk = new Button( compButtons, SWT.PUSH );
		btOk.setText( "Ok" );
		btOk.addSelectionListener( this );

		btCancel = new Button( compButtons, SWT.PUSH );
		btCancel.setText( "Cancel" );
		btCancel.addSelectionListener( this );

		compAtom.setInput( atom );
	}


	public void setInput( final IAtom atom ) {
		this.atom = atom;
	}


	public IAtom getInput() {
		return atom;
	}


	@Override
	public void widgetSelected( final SelectionEvent e ) {
		if ( e.widget == btOk ) {

			final long atomId = atomService.save( atom );
			atom = atomService.find( atomId );

		}
		if ( e.widget == btCancel ) {
			atom = null;
		}
		close();
	}


	@Override
	public void widgetDefaultSelected( final SelectionEvent e ) {
	}
}
