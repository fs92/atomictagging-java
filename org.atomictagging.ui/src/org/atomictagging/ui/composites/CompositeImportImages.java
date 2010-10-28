package org.atomictagging.ui.composites;

import java.util.ArrayList;
import java.util.List;

import org.atomictagging.ui.model.ImageMolecule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

public class CompositeImportImages extends ScrolledComposite {

	private final Composite						comp;

	CompositeImportImages						me;

	private final List<CompositeMoleculeImage>	compositeMoleculeImages;


	public CompositeImportImages( final Composite parent, final int style ) {
		super( parent, style | SWT.H_SCROLL | SWT.BORDER );
		me = this;
		compositeMoleculeImages = new ArrayList<CompositeMoleculeImage>();

		comp = new Composite( this, SWT.BORDER );

		final RowLayout layout = new RowLayout( SWT.HORIZONTAL );
		layout.wrap = false;
		comp.setLayout( layout );

		setContent( comp );
		setExpandVertical( true );
		setExpandHorizontal( true );
		setMinSize( 400, 400 );

	}


	public void setInput() {
		final CompositeMoleculeImage c1 = new CompositeMoleculeImage( comp, SWT.BORDER );
		comp.layout( true );

		final Rectangle r = me.getClientArea();
		final Rectangle compClientArea = comp.getClientArea();
		final Point sizeContent = comp.computeSize( SWT.DEFAULT, SWT.DEFAULT );

		System.out.println( "scrolledComp: " + r );
		System.out.println( "content" + compClientArea );
		System.out.println( comp.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

		me.setMinSize( sizeContent );
	}


	public void setInput( final List<ImageMolecule> list ) {
		for ( final ImageMolecule imageMolecule : list ) {

			final CompositeMoleculeImage c1 = new CompositeMoleculeImage( comp, SWT.BORDER );
			c1.setInput( imageMolecule );
			compositeMoleculeImages.add( c1 );
		}

		comp.layout();

		final Rectangle r = me.getClientArea();
		final Rectangle compClientArea = comp.getClientArea();
		final Point sizeContent = comp.computeSize( SWT.DEFAULT, SWT.DEFAULT );

		me.setMinSize( sizeContent );
	}


	public List<ImageMolecule> getInput() {
		final List<ImageMolecule> list = new ArrayList<ImageMolecule>();

		for ( final CompositeMoleculeImage cmi : compositeMoleculeImages ) {
			list.add( cmi.getInput() );
		}

		return list;
	}


	public void addTagsToAll( final String[] tags ) {
		for ( final CompositeMoleculeImage cmi : compositeMoleculeImages ) {
			cmi.addTags( tags );
		}
	}


	public void addAtomsToAll( final String[] atoms ) {
		for ( final CompositeMoleculeImage cmi : compositeMoleculeImages ) {
			cmi.addAtoms( atoms );
		}
	}

}
