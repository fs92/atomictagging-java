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

import org.atomictagging.ui.listeners.AtomEvent;
import org.atomictagging.ui.listeners.IAtomListener;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * @author strangeoptics
 * 
 */
public class AtomsTableViewer extends TableViewer implements IAtomListener {

	/**
	 * @param parent
	 * @param style
	 */
	public AtomsTableViewer( final Composite parent, final int style ) {
		super( parent, style );

		setContentProvider( new ArrayContentProvider() );
		setLabelProvider( new AtomsLabelProvider() );
	}


	// / IAtomListener ///////////////////////

	@Override
	public void atomsAvailable( final AtomEvent event ) {

		setInput( event.getAtoms() );

	}

}
