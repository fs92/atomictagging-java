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
import java.util.ArrayList;
import java.util.List;

import org.atomictagging.core.accessors.DB;
import org.atomictagging.core.services.ITagService;

/**
 * @author Stephan Mann
 */
public class TagService implements ITagService {

	@Override
	public List<String> getAll() {
		final List<String> result = new ArrayList<String>();
		final String getAll = "SELECT tag FROM tags";

		try {
			PreparedStatement readTags = DB.CONN.prepareStatement( getAll );
			ResultSet tagResult = readTags.executeQuery();

			while ( tagResult.next() ) {
				result.add( tagResult.getString( "tag" ) );
			}

			return result;
		} catch ( SQLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
