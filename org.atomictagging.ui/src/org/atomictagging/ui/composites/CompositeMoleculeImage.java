package org.atomictagging.ui.composites;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

import org.atomictagging.core.configuration.Configuration;
import org.atomictagging.core.services.ATService;
import org.atomictagging.core.services.IAtomService;
import org.atomictagging.core.types.Atom;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.IMolecule;
import org.atomictagging.ui.dialogs.DialogAtom;
import org.atomictagging.ui.listeners.AtomEvent;
import org.atomictagging.ui.listeners.IAtomListener;
import org.atomictagging.ui.listeners.MoleculeEvent;
import org.atomictagging.ui.model.ImageMolecule;
import org.atomictagging.ui.tableviewer.AtomsTableViewer;
import org.atomictagging.ui.tableviewer.TagsTableViewer;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.fieldassist.AutoCompleteField;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

/**
 * @author strangeoptics
 * 
 */
public class CompositeMoleculeImage extends CompositeBase implements MouseListener, KeyListener, IAtomListener {

	private Image				image;
	private Label				lbImage;
	private Text				txTags;
	private Text				txAtoms;
	private TagsTableViewer		tvTags;
	private Table				tbTags;
	private AtomsTableViewer	tvAtoms;
	private Table				tbAtoms;

	// private ListViewerAtom lsAtoms;

	private ImageMolecule		imageMolecule;

	private final IAtomService	atomService	= ATService.getAtomService();


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

		tvTags = new TagsTableViewer( parent, SWT.BORDER );
		tbTags = tvTags.getTable();
		final GridData gdTags = new GridData( SWT.FILL, SWT.TOP, true, false );
		gdTags.heightHint = 100;
		gdTags.horizontalSpan = 2;
		tbTags.setLayoutData( gdTags );
		tbTags.addKeyListener( this );

		createLabel( parent, "Atoms" );

		txAtoms = createText( parent );
		txAtoms.addKeyListener( this );

		tvAtoms = new AtomsTableViewer( parent, SWT.BORDER );
		tbAtoms = tvAtoms.getTable();
		final GridData gdAtoms = new GridData( SWT.FILL, SWT.TOP, true, false );
		gdAtoms.heightHint = 100;
		gdAtoms.horizontalSpan = 2;
		tbAtoms.setLayoutData( gdAtoms );

		final String[] tags = ATService.getTagService().getAllAsArray();
		final String[] atoms = ATService.getAtomService().getDomainAsArray();

		new AutoCompleteField( txTags, new TextsContentAdapter(), tags );
		new AutoCompleteField( txAtoms, new TextsContentAdapter(), atoms );
	}


	public void setInput( final IMolecule iMolecule ) {
		ImageData imgData = null;
		Image img = null;

		if ( !( iMolecule instanceof ImageMolecule ) ) {
			imageMolecule = new ImageMolecule( iMolecule );

			final List<IAtom> thumbAtoms = iMolecule.findAtomsWithTag( "thumb" );
			if ( thumbAtoms.size() > 0 ) {
				final IAtom thumbAtom = thumbAtoms.get( 0 );

				final String targetDirName = Configuration.get().getString( "base.dir" );
				imageMolecule.setFileThumb( new File( targetDirName + "\\" + thumbAtom.getData() ) );
			}

		} else {
			this.imageMolecule = (ImageMolecule) iMolecule;
		}

		bind();

		final ByteArrayInputStream bais = new ByteArrayInputStream( imageMolecule.getByteThumb() );
		imgData = new ImageData( bais );
		img = new Image( getDisplay(), imgData );

		if ( img != null ) {
			lbImage.setImage( img );
		}
	}


	public ImageMolecule getInput() {
		return imageMolecule;
	}


	/**
	 * Adds all tags that are not already contained in the molecule.
	 * 
	 * @param tags
	 */
	public void addTags( final String[] tags ) {

		tvTags.addTags( tags );
	}


	/**
	 * @param atoms
	 */
	public void addAtoms( final String[] atoms ) {
		for ( final String data : atoms ) {
			IAtom atom = atomService.findByData( data );

			if ( atom == null ) {
				atom = new Atom();
				atom.setData( data );
				final DialogAtom dialog = new DialogAtom( getShell(), SWT.NONE );
				dialog.setInput( atom );
				dialog.open();

				atom = dialog.getInput();

				if ( atom != null ) {
					imageMolecule.getAtoms().add( atom );
					tvAtoms.refresh();
				}
			}

			if ( atom != null && !imageMolecule.getAtoms().contains( atom ) ) {
				imageMolecule.getAtoms().add( atom );
				tvAtoms.refresh();
			}
		}
	}


	@Override
	public void bindInput( final DataBindingContext context ) {

		tvTags.setInput( imageMolecule.getTags() );

		tvAtoms.setInput( imageMolecule.getAtoms() );
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

	}


	// / KeyListener ///////////////////////////

	@Override
	public void keyPressed( final KeyEvent e ) {
		if ( e.keyCode == SWT.ARROW_DOWN ) {
			if ( e.widget == txTags ) {
				final String tags = txTags.getText();
				if ( tags != null && !tags.equals( "" ) ) {
					final String[] split = tags.split( "," );
					for ( int i = 0; i < split.length; i++ ) {
						split[i] = split[i].trim();
					}

					tvTags.addTags( split );

					txTags.setText( "" );
				}
			}
			if ( e.widget == txAtoms ) {
				final String atoms = txAtoms.getText();
				if ( atoms != null && !atoms.equals( "" ) ) {
					final String[] split = atoms.split( "," );
					for ( int i = 0; i < split.length; i++ ) {
						split[i] = split[i].trim();
					}

					addAtoms( split );

					txAtoms.setText( "" );
				}
			}
		}
	}


	@Override
	public void keyReleased( final KeyEvent e ) {

	}


	// IAtomListener //////////////////////////////

	@Override
	public void atomsAvailable( final AtomEvent event ) {

	}


	@Override
	public void moleculesAvailable( final MoleculeEvent event ) {
		final IMolecule moleculex = event.getFirst();
		setInput( moleculex );
	}
}
