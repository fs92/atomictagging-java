package org.atomictagging.ui.composites;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class CompositeBase extends Composite {

	private DataBindingContext bindingContext;
	
	public CompositeBase(Composite parent, int style) {
		super(parent, style);
	}
	
	protected void bind() {
		if(bindingContext != null) {
			bindingContext.dispose();
		}
		
		bindingContext = new DataBindingContext();
		
		bindInput(bindingContext);
	}

	public abstract void bindInput(DataBindingContext context);
	
	protected Text createText(Composite parent) {
		Text text = new Text(parent, SWT.BORDER);
		
		GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false);
		text.setLayoutData(gd);
		
		return text;
	}
	
	protected Label createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		
		label.setText(text);
		
		GridData gd = new GridData(SWT.FILL, SWT.TOP, false, false);
		label.setLayoutData(gd);
		
		return label;
	}
}
