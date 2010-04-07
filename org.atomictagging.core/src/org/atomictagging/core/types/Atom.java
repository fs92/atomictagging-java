package org.atomictagging.core.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of an atom
 * 
 * @author Stephan Mann
 */
public class Atom implements IAtom {

	private final long			id;
	private final String		data;
	private final List<String>	tags;


	private Atom( AtomBuilder builder ) {
		this.id = builder.atomId;
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
	public long getId() {
		return id;
	}


	@Override
	public String getData() {
		return data;
	}


	@Override
	public List<String> getTags() {
		return tags;
	}


	@Override
	public String toString() {
		return "Atom: id=" + id + "; data=" + data + "; tags=" + tags;
	}

	/**
	 * Build a consistent atom while regarding all constraints.
	 * 
	 * @author Stephan Mann
	 */
	public static class AtomBuilder {
		private long				atomId;
		private String				atomData;
		private final List<String>	atomTags	= new ArrayList<String>();


		/**
		 * Set the ID for the atom.
		 * 
		 * @param id
		 * @return The builder
		 */
		public AtomBuilder withId( long id ) {
			if (id < 0) {
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
		public AtomBuilder withData( String data ) {
			if (data == null || data.isEmpty()) {
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
		public AtomBuilder withTag( String tag ) {
			if (tag == null || tag.isEmpty()) {
				throw new IllegalArgumentException( "A tag must not be NULL or empty." );
			}

			if (atomTags.contains( tag )) {
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
		public AtomBuilder withTags( List<String> tags ) {
			// Don't add the list at once because we need to
			// enforce the rules specified by withTag(String).
			for ( String tag : tags ) {
				withTag( tag );
			}
			return this;
		}


		/**
		 * Create the atom from this builder instance.
		 * 
		 * @return A consistent atom instance
		 */
		public Atom buildWithDataAndTag() {
			if (atomData == null || atomData.isEmpty()) {
				throw new IllegalArgumentException( "Data in an atom must not be NULL or empty." );
			}

			if (atomTags.size() == 0) {
				throw new IllegalArgumentException( "An atom must have at least one tag." );
			}

			return new Atom( this );
		}
	}

}
