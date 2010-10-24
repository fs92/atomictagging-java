package org.atomictagging.ui.lists;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Composite;

public class ListViewerAtom extends ListViewer {

	public ListViewerAtom( final Composite parent, final int style ) {
		super( parent, style );

		setContentProvider( new ArrayContentProvider() );
		setLabelProvider( new LabelProviderListAtom() );
	}

}
