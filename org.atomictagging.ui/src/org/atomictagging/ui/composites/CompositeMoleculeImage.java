package org.atomictagging.ui.composites;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.atomictagging.core.services.ATService;
import org.atomictagging.ui.lists.ListViewerAtom;
import org.atomictagging.ui.model.ImageMolecule;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CompositeMoleculeImage extends CompositeBase implements MouseListener, KeyListener {

	private Image			image;
	private Label			lbImage;
	private Text			txTags;
	private Text			txAtoms;
	private ListViewer		lsTags;
	private ListViewerAtom	lsAtoms;

	private ImageMolecule	molecule;


	public CompositeMoleculeImage( final Composite parent, final int style ) {
		super( parent, style );

		createControl( this );
	}


	private void createControl( final Composite parent ) {

		final GridLayout layout = new GridLayout( 2, false );
		parent.setLayout( layout );

		image = new Image( getDisplay(), 200, 200 );

		lbImage = new Label( parent, SWT.BORDER );
		lbImage.setImage( image );
		lbImage.addMouseListener( this );
		final GridData gd = new GridData( 200, 200 );
		gd.horizontalAlignment = SWT.CENTER;
		gd.horizontalSpan = 2;
		lbImage.setLayoutData( gd );

		createLabel( parent, "Tags" );

		txTags = createText( parent );
		txTags.addKeyListener( this );

		lsTags = new ListViewer( parent, SWT.BORDER );
		lsTags.getControl().setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
		final GridData gdTags = new GridData( SWT.FILL, SWT.TOP, true, false );
		gdTags.heightHint = 100;
		gdTags.horizontalSpan = 2;
		lsTags.getControl().setLayoutData( gdTags );
		lsTags.setContentProvider( new ArrayContentProvider() );
		lsTags.setSorter( new ViewerSorter() );

		createLabel( parent, "Atoms" );

		txAtoms = createText( parent );

		lsAtoms = new ListViewerAtom( parent, SWT.BORDER );
		lsAtoms.getControl().setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
		final GridData gdAtoms = new GridData( SWT.FILL, SWT.TOP, true, false );
		gdAtoms.heightHint = 100;
		gdAtoms.horizontalSpan = 2;
		lsAtoms.getControl().setLayoutData( gdAtoms );

		final String[] tags = ATService.getTagService().getAllAsArray();
		final String[] atoms = ATService.getAtomService().findUserAtomsAsArray();

		new AutoCompleteField( txTags, new TextsContentAdapter(), tags );
		new AutoCompleteField( txAtoms, new TextsContentAdapter(), atoms );
	}


	public void setInput( final ImageMolecule imd ) {
		this.molecule = imd;
		bind();

		final ByteArrayInputStream bais = new ByteArrayInputStream( imd.getByteThumb() );
		final ImageData imgData = new ImageData( bais );
		final Image img = new Image( getDisplay(), imgData );

		lbImage.setImage( img );
	}


	public ImageMolecule getInput() {
		return molecule;
	}


	/**
	 * Adds all tags that are not already contained in the molecule.
	 * 
	 * @param tags
	 */
	public void addTags( final String[] tags ) {
		final List<String> moleculeTags = molecule.getTags();

		for ( final String tag : tags ) {
			if ( !tag.equals( "" ) && !moleculeTags.contains( tag ) ) {
				moleculeTags.add( tag );
			}
		}

		lsTags.setInput( moleculeTags );
	}


	@Override
	public void bindInput( final DataBindingContext context ) {

		context.bindList( SWTObservables.observeItems( lsTags.getControl() ),
				PojoObservables.observeList( molecule, "tags" ) );

		lsAtoms.setInput( molecule.getAtoms() );
	}


	// / MouseListener /////////////////////

	@Override
	public void mouseDoubleClick( final MouseEvent e ) {
	}


	@Override
	public void mouseDown( final MouseEvent e ) {
		final FileDialog dialog = new FileDialog( getShell() );
		final String fileName = dialog.open();

		System.out.println( fileName );
	}


	@Override
	public void mouseUp( final MouseEvent e ) {
		// TODO Auto-generated method stub

	}


	// / KeyListener ///////////////////////////

	@Override
	public void keyPressed( final KeyEvent e ) {
		if ( e.keyCode == SWT.ARROW_DOWN ) {
			final String text = txTags.getText();
			txTags.setText( "" );

			addTags( new String[] { text } );
		}

	}


	@Override
	public void keyReleased( final KeyEvent e ) {
		// TODO Auto-generated method stub

	}
}
