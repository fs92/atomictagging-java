package org.atomictagging.ui.composites;

import java.util.ArrayList;
import java.util.List;

import org.atomictagging.ui.dto.ImageMoleculeDto;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

public class CompositeImportImages extends ScrolledComposite {

	private Composite comp;
	
	CompositeImportImages me;
	
	private List<CompositeMoleculeImage> compositeMoleculeImages;
	
	public CompositeImportImages(Composite parent, int style) {
		super(parent, style | SWT.H_SCROLL | SWT.BORDER);
		me = this;
		compositeMoleculeImages = new ArrayList<CompositeMoleculeImage>();
		
	    comp = new Composite(this, SWT.BORDER);

	    RowLayout layout = new RowLayout(SWT.HORIZONTAL);
	    layout.wrap = false;
	    comp.setLayout(layout);
	    
	    setContent(comp);
	    setExpandVertical(true);
	    setExpandHorizontal(true);
	    setMinSize(400, 400);
	    
	}

	public void setInput() {
		CompositeMoleculeImage c1 = new CompositeMoleculeImage(comp, SWT.BORDER);
		comp.layout(true);
		
		Rectangle r = me.getClientArea();
		Rectangle compClientArea = comp.getClientArea();
		Point sizeContent = comp.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		
		System.out.println("scrolledComp: " + r);
		System.out.println("content" + compClientArea);
		System.out.println(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		me.setMinSize(sizeContent);
	}
	
	public void setInput(List<ImageMoleculeDto> list) {
		for(ImageMoleculeDto imageAtom : list) {
			
			CompositeMoleculeImage c1 = new CompositeMoleculeImage(comp, SWT.BORDER);
			c1.setInput(imageAtom);
			compositeMoleculeImages.add(c1);
		}
		
		comp.layout();
		
		Rectangle r = me.getClientArea();
		Rectangle compClientArea = comp.getClientArea();
		Point sizeContent = comp.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		
		me.setMinSize(sizeContent);
	}
	
	public void addTagToAll(String[] tags) {
		for(CompositeMoleculeImage cmi : compositeMoleculeImages) {
			cmi.addTag(tags);
		}
	}

}
