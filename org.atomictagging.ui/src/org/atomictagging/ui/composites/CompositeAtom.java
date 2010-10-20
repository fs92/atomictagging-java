package org.atomictagging.ui.composites;

import org.atomictagging.core.services.ATService;
import org.atomictagging.core.services.ITagService;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.ui.listeners.AtomEvent;
import org.atomictagging.ui.listeners.IAtomListener;
import org.atomictagging.ui.tableviewer.TagsTableViewer;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

/**
 * @author strangeoptics
 * 
 */
public class CompositeAtom extends CompositeBase implements IAtomListener, KeyListener {

	private Text				txId;
	private Text				txAtom;
	private Text				txTags;
	private List				lsTags;
	private TagsTableViewer		tvTags;
	private Table				tbTags;

	private IAtom				atom;

	private final ITagService	tagService	= ATService.getTagService();


	/**
	 * @param parent
	 * @param style
	 */
	public CompositeAtom( final Composite parent, final int style ) {
		super( parent, style );

		createControl( this );
	}


	private void createControl( final Composite parent ) {

		final GridLayout layout = new GridLayout( 2, false );
		parent.setLayout( layout );

		final Label lbId = new Label( parent, SWT.NONE );
		lbId.setText( "ID" );

		txId = createText( parent );

		final Label lbAtom = new Label( parent, SWT.NONE );
		lbAtom.setText( "Atom" );

		txAtom = createText( parent );

		final Label lbTags = new Label( parent, SWT.NONE );
		lbTags.setText( "Tags" );

		txTags = new Text( parent, SWT.BORDER );
		txTags.addKeyListener( this );
		txTags.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

		final Label lbEmpty = new Label( parent, SWT.NONE );

		tvTags = new TagsTableViewer( parent, SWT.BORDER | SWT.FULL_SELECTION );
		tbTags = tvTags.getTable();
		final GridData gd = new GridData( SWT.FILL, SWT.TOP, true, false );
		gd.heightHint = 150;
		tbTags.setLayoutData( gd );

		final String[] tags = tagService.getAllAsArray();

		new AutoCompleteField( txTags, new TextsContentAdapter(), tags );
	}


	public void setInput( final IAtom atom ) {
		this.atom = atom;
		bind();
	}


	public IAtom getInput() {
		return atom;
	}


	@Override
	public void bindInput( final DataBindingContext context ) {

		context.bindValue( SWTObservables.observeText( txId, SWT.Modify ), PojoObservables.observeValue( atom, "id" ) );
		context.bindValue( SWTObservables.observeText( txAtom, SWT.Modify ),
				PojoObservables.observeValue( atom, "data" ) );
		tvTags.setInput( atom.getTags() );
	}


	// / IAtomListener ////////////////////////////

	@Override
	public void atomsAvailable( final AtomEvent event ) {

		final IAtom iAtom = event.getFirst();
		setInput( iAtom );
	}


	// / KeyListener //////////////////////////////

	@Override
	public void keyPressed( final KeyEvent e ) {
		if ( e.keyCode == SWT.ARROW_DOWN ) {
			if ( e.widget == txTags ) {
				final String tags = txTags.getText();
				if ( tags != null && !tags.equals( "" ) ) {
					final String[] split = tags.split( "," );
					for ( int i = 0; i < split.length; i++ ) {
						split[i] = split[i].trim();

						atom.getTags().add( split[i] );
					}
					tvTags.refresh();

					txTags.setText( "" );
				}
			}
		}
	}


	@Override
	public void keyReleased( final KeyEvent e ) {

	}

}
