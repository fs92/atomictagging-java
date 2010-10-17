 
package org.atomictagging.ui.parts;

import javax.inject.Inject;

import org.atomictagging.core.types.Atom;
import org.atomictagging.core.types.CoreTags;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.Molecule;
import org.atomictagging.core.types.Molecule.MoleculeBuilder;
import org.atomictagging.ui.composites.CompositeMoleculeImage;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ImagePart {
	@Inject
	public ImagePart(Composite parent) {
		CompositeMoleculeImage compMoleculeImage = new CompositeMoleculeImage(parent, SWT.NONE);
		
		IAtom name = Atom.build().withData( "Lomo Saltado" ).withTag( "Dish" )
		.buildWithDataAndTag();
		
		MoleculeBuilder mBuilder = Molecule.build().withAtom( name )
		.withTag( "picture" ).withTag("jpg");
		
		compMoleculeImage.setInput(mBuilder.buildWithAtomsAndTags());
	}
	
	
	
	@Focus
	public void onFocus() {
		//TODO Your code here
	}
	
	
}