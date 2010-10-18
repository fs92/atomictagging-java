package org.atomictagging.ui.composites;

import org.atomictagging.core.types.IAtom;
import org.atomictagging.ui.listeners.AtomEvent;
import org.atomictagging.ui.listeners.IAtomListener;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

/**
 * @author strangeoptics
 * 
 */
public class CompositeAtom extends CompositeBase implements IAtomListener {

	private Text	txId;
	private Text	txAtom;
	private List	lsTags;

	private IAtom	atom;


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

		lsTags = new List( parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL );
		final GridData gd = new GridData( SWT.FILL, SWT.TOP, true, false );
		gd.heightHint = 150;
		lsTags.setLayoutData( gd );
	}


	public void setInput( final IAtom atom ) {
		this.atom = atom;
		bind();
	}


	@Override
	public void bindInput( final DataBindingContext context ) {

		context.bindValue( SWTObservables.observeText( txId ), PojoObservables.observeValue( atom, "id" ) );
		context.bindValue( SWTObservables.observeText( txAtom ), PojoObservables.observeValue( atom, "data" ) );
		context.bindList( SWTObservables.observeItems( lsTags ), PojoObservables.observeList( atom, "tags" ) );
	}


	// / IAtomListener ////////////////////////////

	@Override
	public void atomsAvailable( final AtomEvent event ) {

		final IAtom iAtom = event.getFirst();
		setInput( iAtom );
	}
}
