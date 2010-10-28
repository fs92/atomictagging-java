package org.atomictagging.ui.composites;

import java.util.ArrayList;
import java.util.List;

import org.atomictagging.core.services.ATService;
import org.atomictagging.core.services.IAtomService;
import org.atomictagging.core.services.ITagService;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.ui.listeners.AtomEvent;
import org.atomictagging.ui.listeners.IAtomListener;
import org.atomictagging.utils.StringUtils;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @author strangeoptics
 * 
 */
public class CompositeAtomSearch extends CompositeBase implements SelectionListener {

	private Text						txId;
	private Button						btIdSearch;
	private Text						txTags;
	private Button						btTagsSearch;
	private Text						txAtoms;
	private Button						btAtomsSearch;

	private final List<IAtomListener>	listeners;

	IAtomService						atomService	= ATService.getAtomService();
	ITagService							tagService	= ATService.getTagService();


	public CompositeAtomSearch( final Composite parent, final int style ) {
		super( parent, style );
		listeners = new ArrayList<IAtomListener>();

		createControl( this );
	}


	private void createControl( final Composite parent ) {

		final GridLayout layout = new GridLayout( 3, false );
		parent.setLayout( layout );

		createLabel( parent, "ID" );
		txId = createText( parent );

		btIdSearch = new Button( parent, SWT.PUSH );
		btIdSearch.setText( "search" );
		btIdSearch.addSelectionListener( this );

		createLabel( parent, "Tags" );
		txTags = createText( parent );

		btTagsSearch = new Button( parent, SWT.PUSH );
		btTagsSearch.setText( "search" );
		btTagsSearch.addSelectionListener( this );

		createLabel( parent, "Atoms" );
		txAtoms = createText( parent );

		btAtomsSearch = new Button( parent, SWT.PUSH );
		btAtomsSearch.setText( "search" );
		btAtomsSearch.addSelectionListener( this );

		final String[] tags = tagService.getAllAsArray();
		final String[] atoms = atomService.getDomainAsArray();

		new AutoCompleteField( txTags, new TextsContentAdapter(), tags );
		new AutoCompleteField( txAtoms, new TextsContentAdapter(), atoms );
	}


	@Override
	public void bindInput( final DataBindingContext context ) {

	}


	// / SelectionListener ////////////////////////////

	@Override
	public void widgetSelected( final SelectionEvent e ) {
		if ( e.widget == btIdSearch ) {

			final IAtom atom = atomService.find( new Long( txId.getText() ) );
			fireAtomAvailableEvent( new AtomEvent().addAtom( atom ) );
		}
		if ( e.widget == btTagsSearch ) {

			final List<String> list = StringUtils.breakCommaSeparatedString( txTags.getText() );
			final List<IAtom> resultList = atomService.find( list );
			fireAtomAvailableEvent( new AtomEvent().setAtoms( resultList ) );
		}
		if ( e.widget == btAtomsSearch ) {
			final IAtom atom = atomService.findByData( txAtoms.getText() );
			fireAtomAvailableEvent( new AtomEvent().addAtom( atom ) );
		}
	}


	@Override
	public void widgetDefaultSelected( final SelectionEvent e ) {
	}


	// / IAtomListener ///////////////////////////////////

	public void addAtomListener( final IAtomListener listener ) {
		listeners.add( listener );
	}


	public void fireAtomAvailableEvent( final AtomEvent event ) {
		for ( final IAtomListener listener : listeners ) {
			listener.atomsAvailable( event );
		}
	}
}
