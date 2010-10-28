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
import java.util.Arrays;
import java.util.List;

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
public class TagsTableViewer extends TableViewer implements KeyListener {

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
		getTable().addKeyListener( this );
	}


	public void setInput( final List<String> tags ) {
		tagsOrig = new ArrayList<String>( tags );
		tagsInput = tags;
		labelProvider.setTagsOriginal( tagsOrig );
		super.setInput( tags );
	}


	public void addTags( final String[] tags ) {
		addTags( Arrays.asList( tags ) );
	}


	public void addTags( final List<String> tags ) {
		for ( final String tag : tags ) {
			tagsInput.add( tag );
			add( tag );
		}
	}


	public void removeSelectedTag() {
		final IStructuredSelection selection = (IStructuredSelection) getSelection();
		if ( !selection.isEmpty() ) {
			final String tag = (String) selection.getFirstElement();
			removeTag( tag );
		}
	}


	public boolean removeTag( final String tag ) {
		final boolean remove = tagsInput.remove( tag );
		remove( tag );
		return remove;
	}


	// KeyListener /////////////////////////

	@Override
	public void keyPressed( final KeyEvent e ) {
		if ( e.keyCode == SWT.DEL ) {
			removeSelectedTag();
		}
	}


	@Override
	public void keyReleased( final KeyEvent e ) {

	}

}
