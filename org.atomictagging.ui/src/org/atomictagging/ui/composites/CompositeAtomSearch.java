package org.atomictagging.ui.composites;

import org.atomictagging.core.services.ATService;
import org.atomictagging.core.services.IAtomService;
import org.atomictagging.core.services.ITagService;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CompositeAtomSearch extends CompositeBase implements SelectionListener {

	private Text		txId;
	private ComboViewer	cvData;
	private Text		txTag;

	private Button		btIdSearch;

	IAtomService		atomService	= ATService.getAtomService();
	ITagService			tagService	= ATService.getTagService();


	public CompositeAtomSearch( final Composite parent, final int style ) {
		super( parent, style );

		createControl( this );
	}


	private void createControl( final Composite parent ) {

		final GridLayout layout = new GridLayout( 3, false );
		parent.setLayout( layout );

		final Label lbId = new Label( parent, SWT.NONE );
		lbId.setText( "ID" );

		txId = createText( parent );
		// txId.setText(test.getText());

		btIdSearch = new Button( parent, SWT.PUSH );
		btIdSearch.setText( "search" );
		btIdSearch.addSelectionListener( this );

		final Label lbAtom = new Label( parent, SWT.NONE );
		lbAtom.setText( "Data" );

		cvData = new ComboViewer( parent, SWT.BORDER );
		final GridData gdData = new GridData( SWT.FILL, SWT.TOP, true, false );
		gdData.horizontalSpan = 2;
		cvData.getControl().setLayoutData( gdData );

		createLabel( parent, "Tags" );

		txTag = createText( parent );

		final String[] tags = tagService.getAllAsArray(); // new String[] { "title", "author", "genre", "year", "award",
															// "artist", "album", "cover", "thumb", "filename",
															// "person", "place", "country", "date", "city", "animal" };
		new AutoCompleteField( txTag, new TextsContentAdapter(), tags );
	}


	@Override
	public void bindInput( final DataBindingContext context ) {

	}


	// / SelectionListener ////////////////////////////

	@Override
	public void widgetSelected( final SelectionEvent e ) {
		if ( e.widget == btIdSearch ) {

		}
	}


	@Override
	public void widgetDefaultSelected( final SelectionEvent e ) {
	}

}
