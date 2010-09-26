 
package org.atomictagging.ui.parts;

import javax.inject.Inject;

import org.atomictagging.core.types.Atom;
import org.atomictagging.core.types.Atom.AtomBuilder;
import org.atomictagging.ui.composites.CompositeAtom;
import org.atomictagging.ui.composites.CompositeAtomSearch;
import org.atomictagging.ui.test.ITest;
import org.atomictagging.ui.test.Test;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class AtomPart {
	
	private CompositeAtomSearch compAtomSearch;
	private CompositeAtom compAtom;
	
	@Inject
	private Test test;
	
	
	@Inject
	public AtomPart(Composite parent) {
		GridLayout layout = new GridLayout();
		parent.setLayout(layout);
		
		compAtomSearch = new CompositeAtomSearch(parent, SWT.BORDER);
		compAtomSearch.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		
		compAtom = new CompositeAtom(parent, SWT.BORDER);
		compAtom.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		
		AtomBuilder atb = new AtomBuilder();
		atb.withData("William Gibson").withTag("Author");
		Atom atom = atb.buildWithDataAndTag();
		
		compAtom.setInput(atom);
	}
	
	
	
	@Focus
	public void onFocus() {
		System.out.println(test.getText());
	}
	
	
}