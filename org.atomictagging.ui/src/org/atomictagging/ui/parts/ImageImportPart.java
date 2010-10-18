package org.atomictagging.ui.parts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.atomictagging.ui.composites.CompositeImportImages;
import org.atomictagging.ui.composites.GroupCommonTagsAtoms;
import org.atomictagging.ui.model.ImageMolecule;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

public class ImageImportPart implements SelectionListener {

	private final Composite				parent;
	private final Button				btLoad;
	private final GroupCommonTagsAtoms	groupCommon;
	private final CompositeImportImages	compImportImages;


	@Inject
	public ImageImportPart( final Composite parent ) {
		this.parent = parent;

		final GridLayout layout = new GridLayout();
		parent.setLayout( layout );

		btLoad = new Button( parent, SWT.PUSH );
		btLoad.setText( "Load Images" );
		btLoad.addSelectionListener( this );

		groupCommon = new GroupCommonTagsAtoms( parent, SWT.NONE );
		groupCommon.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

		compImportImages = new CompositeImportImages( parent, SWT.NONE );
		compImportImages.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

		groupCommon.setCompositeImportImages( compImportImages );

	}

	boolean	first	= false;


	@Focus
	public void onFocus() {
		// System.out.println("ImageImportPart.onFocus()");
		// if(first == false) {
		// first = true;
		// compImportImages.setInput();
		// compImportImages.setInput();
		// compImportImages.setInput();
		// compImportImages.setInput();
		// compImportImages.setInput();
		// compImportImages.setInput();
		// compImportImages.setInput();
		// }
	}


	@Override
	public void widgetSelected( final SelectionEvent e ) {
		if ( e.widget == btLoad ) {
			final FileDialog dialog = new FileDialog( parent.getShell(), SWT.MULTI );
			dialog.open();
			final String[] fileNames = dialog.getFileNames();
			final String filePath = dialog.getFilterPath();
			System.out.println( fileNames );

			final List<ImageMolecule> list = new ArrayList<ImageMolecule>();
			for ( final String fileName : fileNames ) {
				final String fullFileName = filePath + "\\" + fileName;
				final ImageMolecule imageMolecule = new ImageMolecule( new File( fullFileName ) );
				list.add( imageMolecule );
			}
			compImportImages.setInput( list );

			// List<ImageMoleculeDto> list = new ArrayList<ImageMoleculeDto>();
			// for(String fileName : fileNames) {
			// MoleculeBuilder mb = new MoleculeBuilder();
			// mb.withTag("image");
			// AtomBuilder ab = new AtomBuilder();
			// String fullFileName = filePath + "\\" + fileName;
			// ab.withData(fullFileName);
			// ab.withTag("image");
			// mb.withAtom(ab.buildWithDataAndTag());
			// ImageMoleculeDto iad = new ImageMoleculeDto(new File(fullFileName), mb.buildWithAtomsAndTags());
			// list.add(iad);
			// }
			//
			// compImportImages.setInput(list);
		}
	}


	@Override
	public void widgetDefaultSelected( final SelectionEvent e ) {
	}

}