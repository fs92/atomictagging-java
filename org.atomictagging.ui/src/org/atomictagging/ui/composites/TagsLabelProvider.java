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

import java.util.List;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

/**
 * @author work
 * 
 */
public class TagsLabelProvider implements ITableLabelProvider, IColorProvider {

	private final Device	device;
	private final Color		cWhite;
	private final Color		cGreen;

	private List<String>	tagsOriginal;


	/**
	 * @param device
	 */
	public TagsLabelProvider( final Device device ) {
		this.device = device;
		cWhite = new Color( device, 255, 255, 255 );
		cGreen = new Color( device, 0, 255, 0 );
	}


	@Override
	public Image getColumnImage( final Object element, final int columnIndex ) {
		return null;
	}


	@Override
	public String getColumnText( final Object element, final int columnIndex ) {

		return (String) element;
	}


	@Override
	public void addListener( final ILabelProviderListener listener ) {
		// TODO Auto-generated method stub

	}


	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}


	@Override
	public boolean isLabelProperty( final Object element, final String property ) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void removeListener( final ILabelProviderListener listener ) {
		// TODO Auto-generated method stub

	}


	// / IColorProvider ///////////////////////////////

	@Override
	public Color getForeground( final Object element ) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Color getBackground( final Object element ) {
		if ( tagsOriginal != null && tagsOriginal.contains( element ) ) {
			return cWhite;
		}

		return cGreen;
	}


	/**
	 * @param tagsOriginal
	 *            the tagsOriginal to set
	 */
	public void setTagsOriginal( final List<String> tagsOriginal ) {
		this.tagsOriginal = tagsOriginal;
	}


	/**
	 * @return the tagsOriginal
	 */
	public List<String> getTagsOriginal() {
		return tagsOriginal;
	}

}
