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
import java.util.List;

/**
 * Implementation of an atom
 * 
 * @author Stephan Mann
 */
public class Atom extends Entity implements IAtom {

	private String			data;
	private List<String>	tags;


	public Atom() {
		tags = new ArrayList<String>();
	}


	private Atom( final AtomBuilder builder ) {
		if ( builder.atomId > 0 ) {
			this.id = builder.atomId;
		}
		this.data = builder.atomData;
		this.tags = builder.atomTags;
	}


	/**
	 * Start building an atom.
	 * 
	 * @return A builder instance
	 */
	public static AtomBuilder build() {
		return new AtomBuilder();
	}


	@Override
	public AtomBuilder modify() {
		// We don't need to do the checks. The atom must have been
		// consistent since there is only one way to create it.
		final AtomBuilder builder = new AtomBuilder();
		builder.atomId = getId();
		builder.atomData = getData();
		builder.atomTags = getTags();
		return builder;
	}


	@Override
	public String getData() {
		return data;
	}


	@Override
	public void setData( final String data ) {
		this.data = data;
	}


	@Override
	public List<String> getTags() {
		// TODO Check whether this is required to work within a Eclipse 4 application.
		// return Collections.unmodifiableList( tags );
		return tags;
	}


	@Override
	public void setTags( final List<String> tags ) {
		this.tags = tags;
	}


	@Override
	public String toString() {
		return "Atom: id=" + getId() + "; data=" + getData() + "; tags=" + tags;
	}

	/**
	 * Build a consistent atom while regarding all constraints.
	 * 
	 * @author Stephan Mann
	 */
	public static class AtomBuilder {
		private long			atomId;
		private String			atomData;
		private List<String>	atomTags	= new ArrayList<String>();


		/**
		 * Set the ID for the atom.
		 * 
		 * @param id
		 * @return The builder
		 */
		public AtomBuilder withId( final long id ) {
			if ( id < 0 ) {
				throw new IllegalArgumentException( "ID of an atom must be a number greater 0." );
			}

			atomId = id;
			return this;
		}


		/**
		 * Set the data for the atom.
		 * 
		 * @param data
		 * @return The builder
		 */
		public AtomBuilder withData( final String data ) {
			if ( data == null || data.isEmpty() ) {
				throw new IllegalArgumentException( "Data in an atom must not be NULL or empty." );
			}

			atomData = data;
			return this;
		}


		/**
		 * Set a tag for the atom. Multiple calls will result in multiple tags.
		 * 
		 * @param tag
		 * @return The builder
		 */
		public AtomBuilder withTag( final String tag ) {
			if ( tag == null || tag.isEmpty() ) {
				throw new IllegalArgumentException( "A tag must not be NULL or empty." );
			}

			if ( atomTags.contains( tag ) ) {
				throw new IllegalArgumentException( "Duplicate tag <" + tag + "> for atom." );
			}

			atomTags.add( tag );
			return this;
		}


		/**
		 * Set a list of tags for the atom. Will not overwrite previously added tags.
		 * 
		 * @param tags
		 * @return The builder
		 */
		public AtomBuilder withTags( final List<String> tags ) {
			// Don't add the list at once because we need to
			// enforce the rules specified by withTag(String).
			for ( final String tag : tags ) {
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
		public AtomBuilder replaceTags( final List<String> tags ) {
			this.atomTags = new ArrayList<String>();
			withTags( tags );
			return this;
		}


		/**
		 * Create the atom from this builder instance.
		 * 
		 * @return A consistent atom instance
		 */
		public Atom buildWithDataAndTag() {
			if ( atomData == null || atomData.isEmpty() ) {
				throw new IllegalArgumentException( "Data in an atom must not be NULL or empty." );
			}

			if ( atomTags.size() == 0 ) {
				throw new IllegalArgumentException( "An atom must have at least one tag." );
			}

			return new Atom( this );
		}
	}

}
