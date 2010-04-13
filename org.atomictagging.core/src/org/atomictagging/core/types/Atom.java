package org.atomictagging.core.types;

import java.util.ArrayList;
import java.util.Collections;
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


	public AtomBuilder modify() {
		// We don't need to do the checks. The atom must have been
		// consistent since there is only one way to create it.
		AtomBuilder builder = new AtomBuilder();
		builder.atomId = getId();
		builder.atomData = getData();
		builder.atomTags = getTags();
		return builder;
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
		return Collections.unmodifiableList( tags );
	}


	@Override
	public String toString() {
		return "Atom: id=" + id + "; data=" + data + "; tags=" + tags;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( data == null ) ? 0 : data.hashCode() );
		result = prime * result + (int) ( id ^ ( id >>> 32 ) );
		result = prime * result + ( ( tags == null ) ? 0 : tags.hashCode() );
		return result;
	}


	@Override
	public boolean equals( Object obj ) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Atom other = (Atom) obj;
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!data.equals( other.data )) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (tags == null && other.tags != null) {
			return false;
		}

		// The order of the tags is not important. See List.equals()
		List<String> me = new ArrayList<String>( getTags() );
		List<String> you = new ArrayList<String>( other.getTags() );
		Collections.sort( me );
		Collections.sort( you );

		if (!me.equals( you )) {
			return false;
		}
		return true;
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
		 * Replace whatever tags have been set so far with the tags provided.
		 * 
		 * @param tags
		 * @return The builder
		 */
		public AtomBuilder replaceTags( List<String> tags ) {
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
