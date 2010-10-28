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
package org.atomictagging.ui.tableviewer;

import java.util.List;

import org.atomictagging.core.types.IAtom;
import org.atomictagging.ui.listeners.AtomEvent;
import org.atomictagging.ui.listeners.IAtomListener;
import org.atomictagging.ui.listeners.MoleculeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;

/**
 * @author strangeoptics
 * 
 */
public class AtomsTableViewer extends TableViewer implements IAtomListener, KeyListener {

	private List<IAtom>	atomsInput;


	/**
	 * @param parent
	 * @param style
	 */
	public AtomsTableViewer( final Composite parent, final int style ) {
		super( parent, style );

		setContentProvider( new ArrayContentProvider() );
		setLabelProvider( new AtomsLabelProvider() );
		getTable().addKeyListener( this );
	}


	public void setInput( final List<IAtom> atoms ) {
		this.atomsInput = atoms;
		super.setInput( atoms );
	}


	public void removeSelectedAtom() {
		final IStructuredSelection selection = (IStructuredSelection) getSelection();
		if ( !selection.isEmpty() ) {
			final IAtom atom = (IAtom) selection.getFirstElement();
			removeAtom( atom );
		}
	}


	public boolean removeAtom( final IAtom atom ) {
		final boolean remove = atomsInput.remove( atom );
		remove( atom );
		return remove;
	}


	// / IAtomListener ///////////////////////

	@Override
	public void atomsAvailable( final AtomEvent event ) {

		setInput( event.getAtoms() );

	}


	@Override
	public void moleculesAvailable( final MoleculeEvent event ) {

	}


	// KeyListener /////////////////////////

	@Override
	public void keyPressed( final KeyEvent e ) {
		if ( e.keyCode == SWT.DEL ) {
			removeSelectedAtom();
		}
	}


	@Override
	public void keyReleased( final KeyEvent e ) {

	}

}
