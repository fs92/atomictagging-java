/**
 * 
 */
package org.atomictagging.core.types;

import java.util.List;

/**
 * Representation of an atom
 * 
 * @author Stephan Mann
 */
public interface IAtom {

	/**
	 * Get the ID of this atom.
	 * 
	 * @return The ID of this atom, will be 0 if atom was not loaded from the database
	 */
	public long getId();


	/**
	 * Get the data contained in this atom.
	 * 
	 * @return The atoms data
	 */
	public String getData();


	/**
	 * Get all tags of this atom.
	 * 
	 * @return The atoms tags
	 */
	public List<String> getTags();
}
