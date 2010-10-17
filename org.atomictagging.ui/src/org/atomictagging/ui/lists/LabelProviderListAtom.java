package org.atomictagging.ui.lists;

import org.atomictagging.core.types.IAtom;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class LabelProviderListAtom implements ILabelProvider {

	@Override
	public Image getImage(Object element) {
		return null;
	}
	
	@Override
	public String getText(Object o) {
		return ((IAtom)o).getData();
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

}
