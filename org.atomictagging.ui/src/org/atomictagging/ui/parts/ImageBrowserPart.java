package org.atomictagging.ui.parts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.inject.Inject;

import org.atomictagging.core.configuration.Configuration;
import org.atomictagging.core.services.ATService;
import org.atomictagging.core.services.IMoleculeService;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.IMolecule;
import org.atomictagging.ui.composites.TextsContentAdapter;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @author strangeoptics
 * 
 */
public class ImageBrowserPart implements SelectionListener {

	private final Composite			parent;
	private final Text				txTags;
	private final Button			btSearchTags;
	private final Text				txAtoms;
	private final Button			btSearchAtoms;
	private final Gallery			gallery;

	private final IMoleculeService	moleculeService	= ATService.getMoleculeService();


	/**
	 * @param parent
	 */
	@Inject
	public ImageBrowserPart( final Composite parent ) {
		this.parent = parent;
		final GridLayout layout = new GridLayout( 2, false );
		parent.setLayout( layout );

		txTags = new Text( parent, SWT.BORDER );
		txTags.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

		btSearchTags = new Button( parent, SWT.PUSH );
		btSearchTags.setText( "search" );
		btSearchTags.addSelectionListener( this );

		txAtoms = new Text( parent, SWT.BORDER );
		txAtoms.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

		btSearchAtoms = new Button( parent, SWT.PUSH );
		btSearchAtoms.setText( "search" );
		btSearchAtoms.addSelectionListener( this );

		gallery = new Gallery( parent, SWT.V_SCROLL | SWT.MULTI );
		gallery.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

		// Renderers
		final DefaultGalleryGroupRenderer gr = new DefaultGalleryGroupRenderer();
		gr.setMinMargin( 2 );
		gr.setItemHeight( 150 );
		gr.setItemWidth( 150 );
		gr.setAutoMargin( true );
		gallery.setGroupRenderer( gr );

		final DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
		gallery.setItemRenderer( ir );

		final String[] tags = ATService.getTagService().getAllAsArray();
		final String[] atomDomain = ATService.getAtomService().getDomainAsArray();

		new AutoCompleteField( txTags, new TextsContentAdapter(), tags );
		new AutoCompleteField( txAtoms, new TextsContentAdapter(), atomDomain );
	}


	@Focus
	public void onFocus() {
	}


	private void showQuery( final List<IMolecule> molecules, final String name ) {
		final GalleryItem group = new GalleryItem( gallery, SWT.NONE );
		group.setText( "Group " + name ); //$NON-NLS-1$
		group.setExpanded( true );

		final String targetDirName = Configuration.get().getString( "base.dir" );

		for ( final IMolecule molecule : molecules ) {
			final List<IAtom> thumbs = molecule.findAtomsWithTag( "thumb" );

			if ( thumbs.size() > 0 ) {

				final IAtom thumbAtom = thumbs.get( 0 );
				try {
					final ImageData imgData = new ImageData( new FileInputStream( new File( targetDirName + "/"
							+ thumbAtom.getData() ) ) );
					final Image itemImage = new Image( parent.getDisplay(), imgData );

					final GalleryItem item = new GalleryItem( group, SWT.NONE );
					item.setImage( itemImage );
				} catch ( final FileNotFoundException e1 ) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}
	}


	// / SelectionListener /////////////////////////////////

	@Override
	public void widgetSelected( final SelectionEvent e ) {
		if ( e.widget == btSearchTags ) {

			final List<IMolecule> molecules = moleculeService.find( org.atomictagging.utils.StringUtils
					.breakCommaSeparatedString( txTags.getText() ) );
			showQuery( molecules, txTags.getText() );
		}
		if ( e.widget == btSearchAtoms ) {

			final List<IMolecule> molecules = moleculeService.findByAtomData( txAtoms.getText() );
			showQuery( molecules, txAtoms.getText() );
		}
	}


	@Override
	public void widgetDefaultSelected( final SelectionEvent e ) {

	}

}