package org.atomictagging.ui.composites;

import org.atomictagging.ui.test.TestData;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class GroupCommonTagsAtoms extends GroupBase implements KeyListener {

	private Text txTags;
	private Text txAtoms;
	
	private CompositeImportImages compositeImportImages;
	
	public GroupCommonTagsAtoms(Composite parent, int style) {
		super(parent, style);
		setText("common tags and atoms");
		
		createControl(this);
	}
	
	private void createControl(Composite parent) {
		
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		createLabel(parent, "Tags");
		
		txTags = createText(parent);
		txTags.addKeyListener(this);
		
		createLabel(parent, "Atoms");
		
		txAtoms = createText(parent);
		
		new AutoCompleteField(txTags, new TextsContentAdapter(), TestData.tags);
		new AutoCompleteField(txAtoms, new TextsContentAdapter(), TestData.atoms);
	}
	
	public void setCompositeImportImages(CompositeImportImages compositeImportImages) {
		this.compositeImportImages = compositeImportImages;
	}

	@Override
	public void bindInput(DataBindingContext context) {
		// TODO Auto-generated method stub

	}

	/// KeyListener ///////////////////////
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.keyCode == SWT.ARROW_DOWN) {
			String[] split = txTags.getText().split(",");
			for(int i=0; i<split.length; i++) {
				split[i] = split[i].trim();
			}
			System.out.println("CR");
			
			compositeImportImages.addTagToAll(split);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	

}
