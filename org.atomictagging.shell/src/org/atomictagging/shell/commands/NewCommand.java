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
package org.atomictagging.shell.commands;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.atomictagging.core.accessors.DbWriter;
import org.atomictagging.core.types.Atom;
import org.atomictagging.core.types.IAtom;
import org.atomictagging.core.types.Molecule;
import org.atomictagging.core.types.Atom.AtomBuilder;
import org.atomictagging.core.types.Molecule.MoleculeBuilder;
import org.atomictagging.shell.IShell;

/**
 * @author tokei
 * 
 */
public class NewCommand extends AbstractModifyCommand {

	private final List<IAtom>	atoms	= new ArrayList<IAtom>();


	/**
	 * @param shell
	 */
	public NewCommand( IShell shell ) {
		super( shell );
	}


	@Override
	public String getCommandString() {
		return "new";
	}


	@Override
	public String getHelpMessage() {
		return "new <a/m>\t- Create new atoms and join them into a new molecule";
	}


	@Override
	public int handleInput( String input, PrintStream stdout ) {
		String[] parts = input.trim().split( " ", 1 );
		if ( parts.length != 1 ) {
			stdout.println( "Please specify type (atom, molecule)." );
			return 1;
		}

		String type = parts[0];
		if ( !type.equals( "atom" ) && !type.equals( "molecule" ) ) {
			stdout.println( "Please specify valid type: atom or molecule." );
			return 1;
		}

		if ( type.equals( "atom" ) ) {
			return handleAtom( 0, stdout );
		}
		return handleMolecule( 0, stdout );
	}


	@Override
	protected int handleAtom( long id, PrintStream stdout ) {
		File temp = EditCommand.writeAtomTempFile( Arrays.asList( "orphan" ), "" );
		EditCommand.openEditorAndWait( temp );
		AtomBuilder builder = EditCommand.getAtomFromFile( Atom.build(), temp );
		temp.delete();

		atoms.add( builder.buildWithDataAndTag() );
		stdout.println( atoms.size()
				+ " new molecule(s) cached. Use \"new molecule\" to create a molecule containing them." );
		return 0;
	}


	@Override
	protected int handleMolecule( long id, PrintStream stdout ) {
		if ( atoms.size() == 0 ) {
			stdout.println( "Create atoms first via \"new atom\"." );
			return 1;
		}

		MoleculeBuilder builder = Molecule.build().withAtoms( atoms ).withTag( "created-via-shell" );
		DbWriter.write( builder.buildWithAtomsAndTags() );
		atoms.clear();
		return 0;
	}

}
