 
package org.atomictagging.ui.parts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.atomictagging.core.types.Atom;
import org.atomictagging.core.types.Atom.AtomBuilder;
import org.atomictagging.core.types.Molecule.MoleculeBuilder;
import org.atomictagging.ui.composites.CompositeImportImages;
import org.atomictagging.ui.composites.GroupCommonTagsAtoms;
import org.atomictagging.ui.dto.ImageMoleculeDto;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

public class ImageImportPart implements SelectionListener {
	
	private Composite parent;
	private Button btLoad;
	private GroupCommonTagsAtoms groupCommon;
	private CompositeImportImages compImportImages;
	
	@Inject
	public ImageImportPart(Composite parent) {
		this.parent = parent;
		
		GridLayout layout = new GridLayout();
		parent.setLayout(layout);
		
		btLoad = new Button(parent, SWT.PUSH);
		btLoad.setText("Load Images");
		btLoad.addSelectionListener(this);
		
		groupCommon = new GroupCommonTagsAtoms(parent, SWT.NONE);
		groupCommon.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		
		compImportImages = new CompositeImportImages(parent, SWT.NONE);
		compImportImages.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		groupCommon.setCompositeImportImages(compImportImages);
		
	}
	
	
	boolean first = false;
	
	@Focus
	public void onFocus() {
//		System.out.println("ImageImportPart.onFocus()");
//		if(first == false) {
//			first = true;
//			compImportImages.setInput();
//			compImportImages.setInput();
//			compImportImages.setInput();
//			compImportImages.setInput();
//			compImportImages.setInput();
//			compImportImages.setInput();
//			compImportImages.setInput();
//		}
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if(e.widget == btLoad) {
			FileDialog dialog = new FileDialog(parent.getShell(), SWT.MULTI);
			dialog.open();
			String[] fileNames = dialog.getFileNames();
			String filePath = dialog.getFilterPath();
			System.out.println(fileNames);
			
			List<ImageMoleculeDto> list = new ArrayList<ImageMoleculeDto>();
			for(String fileName : fileNames) {
				MoleculeBuilder mb = new MoleculeBuilder();
				mb.withTag("image");
				AtomBuilder ab = new AtomBuilder();
				String fullFileName = filePath + "\\" + fileName;
				ab.withData(fullFileName);
				ab.withTag("image");
				mb.withAtom(ab.buildWithDataAndTag());
				ImageMoleculeDto iad = new ImageMoleculeDto(new File(fullFileName), mb.buildWithAtomsAndTags());
				list.add(iad);
			}
			
			compImportImages.setInput(list);
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}
	
	
}