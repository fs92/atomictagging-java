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

import java.util.ArrayList;
import java.util.List;

import org.atomictagging.ui.composites.TagsLabelProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * @author work
 * 
 */
public class TagsTableViewer extends TableViewer {

	private final TagsLabelProvider	labelProvider;

	private List<String>			tagsOrig;
	private List<String>			tagsInput;


	/**
	 * @param parent
	 * @param style
	 */
	public TagsTableViewer( final Composite parent, final int style ) {
		super( parent, style );

		setContentProvider( new ArrayContentProvider() );

		labelProvider = new TagsLabelProvider( parent.getDisplay() );
		setLabelProvider( labelProvider );
	}


	public void setInput( final List<String> tags ) {
		tagsOrig = new ArrayList<String>( tags );
		tagsInput = tags;
		labelProvider.setTagsOriginal( tagsOrig );
		super.setInput( tags );
	}

}
