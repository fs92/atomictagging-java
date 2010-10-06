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
package org.atomictagging.core.services;

import java.util.List;

import org.atomictagging.core.types.IAtom;

/**
 * @author Stephan Mann
 */
public interface IAtomService {

	public enum Filter {
		INCLUDE, EXCLUDE
	}


	IAtom find( long atomId );


	List<IAtom> find( List<String> tags );


	List<IAtom> find( List<String> tags, Filter filter );


	List<IAtom> findUserAtoms( List<String> tags );


	void save( IAtom atom );


	void delete( IAtom atom );

}
