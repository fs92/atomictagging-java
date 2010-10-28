package org.atomictagging.ui.parts;

import javax.inject.Inject;

import org.atomictagging.core.services.ATService;
import org.atomictagging.core.services.IAtomService;
import org.atomictagging.core.types.Atom;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.ui.composites.CompositeAtom;
import org.atomictagging.ui.composites.CompositeAtomSearch;
import org.atomictagging.ui.tableviewer.AtomsTableViewer;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author work
 * 
 */
public class AtomPart implements SelectionListener, ISelectionChangedListener {

	private final CompositeAtomSearch	compAtomSearch;
	private final AtomsTableViewer		tvAtoms;
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

		tvAtoms = new AtomsTableViewer( parent, SWT.BORDER | SWT.FULL_SELECTION );
		tvAtoms.addSelectionChangedListener( this );
		final GridData gdAtoms = new GridData( SWT.FILL, SWT.TOP, true, false );
		gdAtoms.heightHint = 200;
		tvAtoms.getTable().setLayoutData( gdAtoms );

		compAtom = new CompositeAtom( parent, SWT.BORDER );
		compAtom.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

		// compAtomSearch.addAtomListener( compAtom );
		compAtomSearch.addAtomListener( tvAtoms );

		btNew = new Button( parent, SWT.PUSH );
		btNew.setText( "new" );
		btNew.addSelectionListener( this );

		btSave = new Button( parent, SWT.PUSH );
		btSave.setText( "save" );
		btSave.addSelectionListener( this );
	}


	@Focus
	public void onFocus() {
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

			final IAtom atom = atomService.find( id );

			compAtom.setInput( atom );
		}
	}


	@Override
	public void widgetDefaultSelected( final SelectionEvent e ) {

	}


	// / ISelectionChangedListener ///////////////////////

	@Override
	public void selectionChanged( final SelectionChangedEvent event ) {
		final IStructuredSelection selection = (IStructuredSelection) event.getSelection();

		if ( !selection.isEmpty() ) {
			compAtom.setInput( (IAtom) selection.getFirstElement() );
		}
	}

}