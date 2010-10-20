package org.atomictagging.ui.parts;

import java.util.Arrays;

import javax.inject.Inject;

import org.atomictagging.core.services.ATService;
import org.atomictagging.core.services.IAtomService;
import org.atomictagging.core.services.IAtomService.Filter;
import org.atomictagging.core.services.ITagService;
import org.atomictagging.core.types.Atom;
import org.atomictagging.ui.composites.CompositeAtom;
import org.atomictagging.ui.composites.CompositeAtomSearch;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class AtomPart implements SelectionListener {

	private final CompositeAtomSearch	compAtomSearch;
	private final CompositeAtom			compAtom;
	private final Button				btNew;
	private final Button				btSave;

	private final IAtomService			atomService	= ATService.getAtomService();


	@Inject
	public AtomPart( final Composite parent ) {
		final GridLayout layout = new GridLayout();
		parent.setLayout( layout );

		compAtomSearch = new CompositeAtomSearch( parent, SWT.BORDER );
		compAtomSearch.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

		compAtom = new CompositeAtom( parent, SWT.BORDER );
		compAtom.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

		compAtomSearch.addAtomListener( compAtom );

		btNew = new Button( parent, SWT.PUSH );
		btNew.setText( "new" );
		btNew.addSelectionListener( this );

		btSave = new Button( parent, SWT.PUSH );
		btSave.setText( "save" );
		btSave.addSelectionListener( this );
	}


	@Focus
	public void onFocus() {
		final ITagService tagService = ATService.getTagService();
		System.out.println( tagService.getAll() );

		System.out.println( ATService.getAtomService().find( Arrays.asList( "x-fileref" ), Filter.EXCLUDE ) );
		System.out.println( "---------------------------------------------------------------------------------" );
		System.out.println( ATService.getAtomService().findUserAtoms() );
	}


	// / SelectionListener ////////////////////////

	@Override
	public void widgetSelected( final SelectionEvent e ) {
		if ( e.widget == btNew ) {
			final Atom atom = new Atom();
			// atom.setData( "hallo" );
			compAtom.setInput( atom );
		}
		if ( e.widget == btSave ) {
			final long id = atomService.save( compAtom.getInput() );
			System.out.println( "saved = " + id );
		}
	}


	@Override
	public void widgetDefaultSelected( final SelectionEvent e ) {
		// TODO Auto-generated method stub

	}

}