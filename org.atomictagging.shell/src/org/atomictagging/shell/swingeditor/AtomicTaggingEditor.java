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
package org.atomictagging.shell.swingeditor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AtomicTaggingEditor extends JFrame {

	private JTextArea		editArea	= null;

	private final Action	saveAction	= new SaveAction();
	private final Action	exitAction	= new ExitAction();

	private File			file;
	private CountDownLatch	latch;


	public AtomicTaggingEditor( File file, CountDownLatch latch ) {
		this.file = file;
		this.latch = latch;
		init();
	}


	public void init() {
		editArea = new JTextArea( 15, 80 );
		editArea.setBorder( BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
		editArea.setFont( new Font( "monospaced", Font.PLAIN, 14 ) );
		JScrollPane scrollingText = new JScrollPane( editArea );

		JPanel content = new JPanel();
		content.setLayout( new BorderLayout() );
		content.add( scrollingText, BorderLayout.CENTER );

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = menuBar.add( new JMenu( "File" ) );
		fileMenu.setMnemonic( 'F' );
		fileMenu.add( saveAction );
		fileMenu.addSeparator();
		fileMenu.add( exitAction );

		setContentPane( content );
		setJMenuBar( menuBar );

		setTitle( "AT Editor" );
		pack();
		setLocationRelativeTo( null );
		setVisible( true );

		if ( file != null )
			openFile( file );
	}


	public void setFile( File file ) {
		this.file = file;
	}


	public void setCountDownLatch( CountDownLatch latch ) {
		this.latch = latch;
	}


	public void openFile( File file ) {
		try {
			FileReader reader = new FileReader( file );
			editArea.read( reader, "" );
		} catch ( IOException ioex ) {
			System.out.println( ioex );
		}
	}


	private void close() {
		latch.countDown();
		dispose();
	}

	class SaveAction extends AbstractAction {
		SaveAction() {
			super( "Save..." );
			putValue( MNEMONIC_KEY, new Integer( 'S' ) );
		}


		@Override
		public void actionPerformed( ActionEvent e ) {
			try {
				FileWriter writer = new FileWriter( file );
				editArea.write( writer );
				writer.flush();
				writer.close();
			} catch ( IOException ioex ) {
				JOptionPane.showMessageDialog( AtomicTaggingEditor.this, ioex );
				latch.countDown();
				close();
			}
		}
	}

	class ExitAction extends AbstractAction {

		public ExitAction() {
			super( "Exit" );
			putValue( MNEMONIC_KEY, new Integer( 'X' ) );
		}


		@Override
		public void actionPerformed( ActionEvent e ) {
			close();
		}
	}
}