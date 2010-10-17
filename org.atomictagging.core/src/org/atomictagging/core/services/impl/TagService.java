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
package org.atomictagging.core.services.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.atomictagging.core.accessors.DB;
import org.atomictagging.core.services.ITagService;
import org.eclipse.core.runtime.Assert;

/**
 * @author Stephan Mann
 */
public class TagService extends AbstractService implements ITagService {

	private static PreparedStatement	allTags;
	private static PreparedStatement	checkTag;
	private static PreparedStatement	insertTag;

	static {
		try {
			allTags = DB.CONN.prepareStatement( "SELECT tag FROM tags" );
			checkTag = DB.CONN.prepareStatement( "SELECT tagid FROM tags WHERE tag = ?" );
			insertTag = DB.CONN.prepareStatement( "INSERT INTO tags (tag) VALUES (?)", Statement.RETURN_GENERATED_KEYS );
		} catch ( final SQLException e ) {
			e.printStackTrace();
		}
	}


	@Override
	public List<String> getAll() {
		final List<String> tags = new ArrayList<String>();

		try {
			final ResultSet tagResult = allTags.executeQuery();

			while ( tagResult.next() ) {
				tags.add( tagResult.getString( "tag" ) );
			}
		} catch ( final SQLException e ) {
			e.printStackTrace();
		}

		return tags;
	}


	@Override
	public long save( final String tag ) {
		Assert.isTrue( tag != null && !tag.isEmpty() );

		long tagId = -1;
		try {
			checkTag.setString( 1, tag );
			checkTag.execute();
			tagId = getIdOfExistingEntity( checkTag, "tagid" );

			if ( tagId == -1 ) {
				insertTag.setString( 1, tag );
				insertTag.execute();
				tagId = getAutoIncrementId( insertTag );
			}
		} catch ( final SQLException e ) {
			e.printStackTrace();
		}

		return tagId;
	}

}
