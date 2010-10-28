/**
 * This file is part of Atomic Tagging.
 * 
 * Atomic Tagging is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Atomic Tagging is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Atomic Tagging. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.atomictagging.ui.composites;

import java.util.ArrayList;
import java.util.List;

import org.atomictagging.core.services.ATService;
import org.atomictagging.core.services.IAtomService;
import org.atomictagging.core.services.IMoleculeService;
import org.atomictagging.core.services.ITagService;
import org.atomictagging.core.types.IMolecule;
import org.atomictagging.ui.listeners.IAtomListener;
import org.atomictagging.ui.listeners.MoleculeEvent;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author strangeoptics
 * 
 */
public class CompositeMoleculeSearch extends GroupBaseNew implements SelectionListener {

	private Text						txId;
	private Button						btIdSearch;
	private Text						txTags;
	private Button						btTagsSearch;
	private Text						txAtoms;
	private Button						btAtomsSearch;

	private final List<IAtomListener>	listeners;

	private final IAtomService			atomService		= ATService.getAtomService();
	private final ITagService			tagService		= ATService.getTagService();
	private final IMoleculeService		moleculeService	= ATService.getMoleculeService();


	/**
	 * @param parent
	 * @param style
	 */
	public CompositeMoleculeSearch( final Composite parent, final int style ) {
		super( parent, style );

		listeners = new ArrayList<IAtomListener>();

		createControl( this );
	}


	private void createControl( final Composite parent ) {

		final GridLayout layout = new GridLayout( 3, false );
		parent.setLayout( layout );

		final Label lbId = createLabel( parent, "ID" );
		txId = createText( parent );

		btIdSearch = new Button( parent, SWT.PUSH );
		btIdSearch.setText( "search" );
		btIdSearch.addSelectionListener( this );

		final Label lbTags = createLabel( parent, "Tags" );
		txTags = createText( parent );
		txTags.setEnabled( false );

		btTagsSearch = new Button( parent, SWT.PUSH );
		btTagsSearch.setText( "search" );
		btTagsSearch.addSelectionListener( this );

		final Label lbAtoms = createLabel( parent, "Atoms" );
		txAtoms = createText( parent );
		txAtoms.setEnabled( false );

		btAtomsSearch = new Button( parent, SWT.PUSH );
		btAtomsSearch.setText( "search" );
		btAtomsSearch.addSelectionListener( this );

		final String[] tags = tagService.getAllAsArray();
		final String[] atoms = atomService.getDomainAsArray();

		new AutoCompleteField( txTags, new TextsContentAdapter(), tags );
		new AutoCompleteField( txAtoms, new TextsContentAdapter(), atoms );
	}


	@Override
	public void widgetSelected( final SelectionEvent e ) {
		if ( e.widget == btIdSearch ) {

			final long id = Long.parseLong( txId.getText() );
			final IMolecule molecule = moleculeService.find( id );

			fireAtomAvailableEvent( new MoleculeEvent().addMolecule( molecule ) );

		}
		if ( e.widget == btTagsSearch ) {

		}
		if ( e.widget == btAtomsSearch ) {

		}
	}


	@Override
	public void widgetDefaultSelected( final SelectionEvent e ) {

	}


	// / IAtomListener ///////////////////////////////////

	public void addAtomListener( final IAtomListener listener ) {
		listeners.add( listener );
	}


	public void fireAtomAvailableEvent( final MoleculeEvent event ) {
		for ( final IAtomListener listener : listeners ) {
			listener.moleculesAvailable( event );
		}
	}

}
