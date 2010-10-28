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
package org.atomictagging.ui.listeners;

import java.util.ArrayList;
import java.util.List;

import org.atomictagging.core.types.IMolecule;

/**
 * @author strangeoptics
 * 
 */
public class MoleculeEvent {

	private List<IMolecule>	molecules;


	public MoleculeEvent() {
		molecules = new ArrayList<IMolecule>();
	}


	/**
	 * @return the molecules
	 */
	public List<IMolecule> getMolecules() {
		return molecules;
	}


	/**
	 * @param molecules
	 *            the molecules to set
	 */
	public void setMolecules( final List<IMolecule> molecules ) {
		this.molecules = molecules;
	}


	public MoleculeEvent addMolecule( final IMolecule molecule ) {
		molecules.add( molecule );
		return this;
	}


	public IMolecule getFirst() {
		return molecules.get( 0 );
	}
}
