package org.atomictagging.ui.parts;

import javax.inject.Inject;

import org.atomictagging.ui.composites.CompositeMoleculeImage;
import org.atomictagging.ui.composites.CompositeMoleculeSearch;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ImagePart {

	private final CompositeMoleculeSearch	compMoleculeSearch;
	private final CompositeMoleculeImage	compMoleculeImage;


	@Inject
	public ImagePart( final Composite parent ) {

		final GridLayout layout = new GridLayout();
		parent.setLayout( layout );

		compMoleculeSearch = new CompositeMoleculeSearch( parent, SWT.NONE );
		compMoleculeSearch.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

		compMoleculeImage = new CompositeMoleculeImage( parent, SWT.NONE );

		compMoleculeSearch.addAtomListener( compMoleculeImage );

		// compMoleculeImage.setInput(mBuilder.buildWithAtomsAndTags());
	}


	@Focus
	public void onFocus() {
		// TODO Your code here
	}

}