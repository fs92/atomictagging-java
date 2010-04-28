/**
 * 
 */
package org.atomictagging.core.moleculehandler;

import org.atomictagging.core.types.IMolecule;

/**
 * @author Stephan Mann
 * 
 */
public interface IMoleculeExporter extends IMoleculeHandler {

	public boolean canHandle( IMolecule molecule );
}
