package org.atomictagging.ui.parts;

import java.util.Arrays;

import javax.inject.Inject;

import org.atomictagging.core.services.ATService;
import org.atomictagging.core.services.IAtomService.Filter;
import org.atomictagging.core.services.ITagService;
import org.atomictagging.ui.composites.CompositeAtom;
import org.atomictagging.ui.composites.CompositeAtomSearch;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class AtomPart {

	private final CompositeAtomSearch	compAtomSearch;
	private final CompositeAtom			compAtom;


	@Inject
	public AtomPart( final Composite parent ) {
		final GridLayout layout = new GridLayout();
		parent.setLayout( layout );

		compAtomSearch = new CompositeAtomSearch( parent, SWT.BORDER );
		compAtomSearch.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

		compAtom = new CompositeAtom( parent, SWT.BORDER );
		compAtom.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

		compAtomSearch.addAtomListener( compAtom );

		// final AtomBuilder atb = new AtomBuilder();
		// atb.withData( "William Gibson" ).withTag( "Author" );
		// final Atom atom = atb.buildWithDataAndTag();

		// compAtom.setInput( atom );
	}


	@Focus
	public void onFocus() {
		final ITagService tagService = ATService.getTagService();
		System.out.println( tagService.getAll() );

		System.out.println( ATService.getAtomService().find( Arrays.asList( "x-fileref" ), Filter.EXCLUDE ) );
		System.out.println( "---------------------------------------------------------------------------------" );
		System.out.println( ATService.getAtomService().findUserAtoms() );
	}

}