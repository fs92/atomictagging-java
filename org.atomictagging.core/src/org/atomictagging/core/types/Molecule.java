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
package org.atomictagging.core.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of a molecule.
 * 
 * @author Stephan Mann
 */
public class Molecule implements IMolecule {

	private final long			id;
	private final List<IAtom>	atoms;
	private final List<String>	tags;


	private Molecule( MoleculeBuilder builder ) {
		this.id = builder.molId;
		this.atoms = Collections.unmodifiableList( builder.molAtoms );
		this.tags = Collections.unmodifiableList( builder.molTags );
	}


	/**
	 * Start building a molecule.
	 * 
	 * @return A builder instance
	 */
	public static MoleculeBuilder build() {
		return new MoleculeBuilder();
	}


	public MoleculeBuilder modify() {
		// We don't need to do the checks. The molecule must have been
		// consistent since there is only one way to create it.
		MoleculeBuilder builder = new MoleculeBuilder();
		builder.molId = getId();
		builder.molTags = getTags();
		builder.molAtoms = getAtoms();
		return builder;
	}


	@Override
	public long getId() {
		return id;
	}


	@Override
	public List<IAtom> getAtoms() {
		return new ArrayList<IAtom>( atoms );
	}


	@Override
	public List<String> getTags() {
		return new ArrayList<String>( tags );
	}


	@Override
	public List<String> getAtomTags() {
		ArrayList<String> result = new ArrayList<String>();

		for ( IAtom atom : atoms ) {
			result.addAll( atom.getTags() );
		}

		return result;
	}


	@Override
	public String toString() {
		return "Molecule: id=" + id + "; tags=" + tags + "; atom#=" + atoms.size();
	}

	/**
	 * Build a consistent molecule while regarding all constraints.
	 * 
	 * @author Stephan Mann
	 */
	public static class MoleculeBuilder {
		private long			molId;
		private List<IAtom>		molAtoms	= new ArrayList<IAtom>();
		private List<String>	molTags		= new ArrayList<String>();


		/**
		 * Set the ID for the molecule.
		 * 
		 * @param id
		 * @return The builder
		 */
		public MoleculeBuilder withId( long id ) {
			if ( id < 0 ) {
				throw new IllegalArgumentException( "ID of a molecule must be a number greater 0." );
			}

			molId = id;
			return this;
		}


		/**
		 * Add a atom to this molecule. Can be called multiple times to add many atoms.
		 * 
		 * @param atom
		 * @return The builder
		 */
		public MoleculeBuilder withAtom( IAtom atom ) {
			if ( atom == null ) {
				throw new IllegalArgumentException( "Atom must not be NULL." );
			}
			if ( molAtoms.contains( atom ) ) {
				throw new IllegalArgumentException( "Duplicate atom <" + atom + "> in molecule." );
			}

			molAtoms.add( atom );
			return this;
		}


		/**
		 * Add a list of atoms to this molecule. Will not overwrite previously added atoms.
		 * 
		 * @param atoms
		 * @return The builder
		 */
		public MoleculeBuilder withAtoms( List<IAtom> atoms ) {
			if ( atoms == null || atoms.contains( null ) ) {
				throw new IllegalArgumentException( "List of atoms must not be NULL or contain NULL elements." );
			}

			// Don't add the list at once because we need to
			// enforce the rules specified by withAtom(IAtom).
			for ( IAtom atom : atoms ) {
				withAtom( atom );
			}
			return this;
		}


		/**
		 * Remove whatever atoms have been set so far.
		 * 
		 * @return The builder
		 */
		public MoleculeBuilder deleteAtoms() {
			molAtoms = new ArrayList<IAtom>();
			return this;
		}


		/**
		 * Add a tag to the molecule. Multiple calls will result in multiple tags.
		 * 
		 * @param tag
		 * @return The builder
		 */
		public MoleculeBuilder withTag( String tag ) {
			if ( tag == null || tag.isEmpty() ) {
				throw new IllegalArgumentException( "A tag must not be NULL or empty." );
			}

			if ( molTags.contains( tag ) ) {
				throw new IllegalArgumentException( "Duplicate tag <" + tag + "> for molecule." );
			}

			molTags.add( tag );
			return this;
		}


		/**
		 * Add a list of tags to the molecule. Will not overwrite previously added tags.
		 * 
		 * @param tags
		 * @return The builder
		 */
		public MoleculeBuilder withTags( List<String> tags ) {
			// Don't add the list at once because we need to
			// enforce the rules specified by withTag(String).
			for ( String tag : tags ) {
				withTag( tag );
			}
			return this;
		}


		/**
		 * Replace whatever tags have been set so far with the tags provided.
		 * 
		 * @param tags
		 * @return The builder
		 */
		public MoleculeBuilder replaceTags( List<String> tags ) {
			this.molTags = new ArrayList<String>();
			withTags( tags );
			return this;
		}


		/**
		 * Create the molecule from this builder instance.
		 * 
		 * @return A consistent molecule instance
		 */
		public Molecule buildWithAtomsAndTags() {
			if ( molAtoms.size() == 0 ) {
				throw new IllegalArgumentException( "Molecules must have at least one atom." );
			}
			if ( molTags.size() == 0 ) {
				throw new IllegalArgumentException( "A molecule must have at least one tag." );
			}

			return new Molecule( this );
		}

	}

}
