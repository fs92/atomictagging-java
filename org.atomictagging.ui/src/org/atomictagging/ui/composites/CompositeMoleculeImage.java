package org.atomictagging.ui.composites;

import java.io.ByteArrayInputStream;

import org.atomictagging.core.types.IMolecule;
import org.atomictagging.ui.dto.ImageMoleculeDto;
import org.atomictagging.ui.lists.ListViewerAtom;
import org.atomictagging.ui.test.TestData;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CompositeMoleculeImage extends CompositeBase implements MouseListener, KeyListener {

	private Image image;
	private Label lbImage;
	private Text txTags;
	private Text txAtoms;
	private ListViewer lsTags;
	private ListViewerAtom lsAtoms;
	
	private IMolecule molecule;
	
	
	public CompositeMoleculeImage(Composite parent, int style) {
		super(parent, style);
		
		createControl(this);
	}
	
	private void createControl(Composite parent) {
		
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);

		image = new Image(getDisplay(), 200, 200);
		
		lbImage = new Label(parent, SWT.BORDER);
		lbImage.setImage(image);
		lbImage.addMouseListener(this);
		GridData gd = new GridData(150, 150);
		gd.horizontalAlignment = SWT.CENTER;
		gd.horizontalSpan = 2;
		lbImage.setLayoutData(gd);
		
		createLabel(parent, "Tags");
		
		txTags = createText(parent);
		txTags.addKeyListener(this);
	
		lsTags = new ListViewer(parent, SWT.BORDER);
		lsTags.getControl().setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		GridData gdTags = new GridData(SWT.FILL, SWT.TOP, true, false);
		gdTags.heightHint = 100;
		gdTags.horizontalSpan = 2;
		lsTags.getControl().setLayoutData(gdTags);
		lsTags.setContentProvider(new ArrayContentProvider());
		lsTags.setSorter(new ViewerSorter());
		
		createLabel(parent, "Atoms");
		
		txAtoms = createText(parent);
		
		lsAtoms = new ListViewerAtom(parent, SWT.BORDER);
		lsAtoms.getControl().setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		GridData gdAtoms = new GridData(SWT.FILL, SWT.TOP, true, false);
		gdAtoms.heightHint = 100;
		gdAtoms.horizontalSpan = 2;
		lsAtoms.getControl().setLayoutData(gdAtoms);
		
		
		new AutoCompleteField(txTags, new TextsContentAdapter(), TestData.tags);
		new AutoCompleteField(txAtoms, new TextsContentAdapter(), TestData.atoms);
	}
	
	public void setInput(IMolecule molecule) {
		this.molecule = molecule;
		bind();
	}
	
	public void setInput(ImageMoleculeDto imd) {
		this.molecule = imd.getMolecule();
		bind();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(imd.getImgBytes());
		ImageData imgData = new ImageData(bais);
		Image img = new Image(getDisplay(), imgData);
		
		lbImage.setImage(img);
	}
	
	public void addTag(String[] tags) {
		for(String tag : tags) {
			molecule.getTags().add(tag);
		}
		lsTags.setInput(molecule.getTags());
	}

	@Override
	public void bindInput(DataBindingContext context) {
		
		context.bindList(SWTObservables.observeItems(lsTags.getControl()), PojoObservables.observeList(molecule, "tags"));
		
		lsAtoms.setInput(molecule.getAtoms());
	}

	/// MouseListener /////////////////////
	
	@Override
	public void mouseDoubleClick(MouseEvent e) {}

	@Override
	public void mouseDown(MouseEvent e) {
		FileDialog dialog = new FileDialog(getShell());
		String fileName = dialog.open();
		
		System.out.println(fileName);
	}

	@Override
	public void mouseUp(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
/// KeyListener ///////////////////////////
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.keyCode == SWT.ARROW_DOWN) {
			String text = txTags.getText();
			txTags.setText("");
			
			addTag(new String[] {text});
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
