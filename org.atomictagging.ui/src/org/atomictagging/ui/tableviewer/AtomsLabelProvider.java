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

import org.atomictagging.core.types.IAtom;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author strangeoptics
 * 
 */
public class AtomsLabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage( final Object element, final int columnIndex ) {
		return null;
	}


	@Override
	public String getColumnText( final Object element, final int columnIndex ) {
		final IAtom atom = (IAtom) element;
		return atom.getData();
	}


	@Override
	public void addListener( final ILabelProviderListener listener ) {

	}


	@Override
	public void dispose() {

	}


	@Override
	public boolean isLabelProperty( final Object element, final String property ) {
		return false;
	}


	@Override
	public void removeListener( final ILabelProviderListener listener ) {

	}

}
