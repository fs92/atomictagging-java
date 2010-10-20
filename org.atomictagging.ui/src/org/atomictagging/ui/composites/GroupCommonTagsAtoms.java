package org.atomictagging.ui.composites;

import org.atomictagging.core.services.ATService;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class GroupCommonTagsAtoms extends GroupBase implements KeyListener {

	private Text					txTags;
	private Text					txAtoms;

	private CompositeImportImages	compositeImportImages;


	public GroupCommonTagsAtoms( final Composite parent, final int style ) {
		super( parent, style );
		setText( "common tags and atoms" );

		createControl( this );
	}


	private void createControl( final Composite parent ) {

		final GridLayout layout = new GridLayout( 2, false );
		parent.setLayout( layout );
		createLabel( parent, "Tags" );

		txTags = createText( parent );
		txTags.addKeyListener( this );

		createLabel( parent, "Atoms" );

		txAtoms = createText( parent );
		txAtoms.addKeyListener( this );

		final String[] tags = ATService.getTagService().getAllAsArray();
		final String[] atoms = ATService.getAtomService().findUserAtomsAsArray();

		new AutoCompleteField( txTags, new TextsContentAdapter(), tags );
		new AutoCompleteField( txAtoms, new TextsContentAdapter(), atoms );
	}


	public void setCompositeImportImages( final CompositeImportImages compositeImportImages ) {
		this.compositeImportImages = compositeImportImages;
	}


	@Override
	public void bindInput( final DataBindingContext context ) {
		// TODO Auto-generated method stub

	}


	// / KeyListener ///////////////////////

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

					compositeImportImages.addTagsToAll( split );

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

					compositeImportImages.addAtomsToAll( split );

					txAtoms.setText( "" );
				}
			}
		}
	}


	@Override
	public void keyReleased( final KeyEvent e ) {
		// TODO Auto-generated method stub

	}

}
