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
package org.atomictagging.core.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;

import org.apache.commons.configuration.CombinedConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link Configuration}.
 * 
 * @author Stephan Mann
 */
public class ConfigurationTest {

	private CombinedConfiguration	confBackup;

	// Test configuration files
	private final File				test1	= new File( "test/test1.ini" );
	private final File				test2	= new File( "test/test2.ini" );


	/**
	 * Setup for the test. Creates a new empty Configuration.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		Field field = Configuration.class.getDeclaredField( "conf" );
		field.setAccessible( true );
		confBackup = (CombinedConfiguration) field.get( null );
		field.set( null, new CombinedConfiguration() );
	}


	/**
	 * Cleanup after the test. Resets the Configuration to the initial state.
	 * 
	 * @throws Exception
	 */
	@After
	public void cleanUp() throws Exception {
		Field field = Configuration.class.getDeclaredField( "conf" );
		field.setAccessible( true );
		field.set( null, confBackup );
	}


	/**
	 * Test method for {@link org.atomictagging.core.configuration.Configuration#addFile(java.io.File)}. <br>
	 * <br>
	 * This method also tests {@link Configuration#get()}. As of now, that should give us 100% code coverage in
	 * {@link Configuration}.
	 */
	@Test
	public void testAddFileFile() {
		assertFilesReadable();

		try {
			Configuration.addFile( test1 );
			Configuration.addFile( test2 );
		} catch ( Exception e ) {
			fail( "Failed to load configuration. There seems to be something wrong with the test files." );
		}

		assertConfigurationContent();
		assertEquals( "blub", Configuration.get().getString( "section2.bla" ) );
	}


	/**
	 * Test method for {@link org.atomictagging.core.configuration.Configuration#addFile(java.io.File)}. <br>
	 * <br>
	 * This test illustrates a feature of Apache Configuration. Values with the same keys don't get overwritten. Who
	 * ever comes first wins.
	 */
	@Test
	public void testAddFileFileAlternate() {
		assertFilesReadable();

		try {
			Configuration.addFile( test2 );
			Configuration.addFile( test1 );
		} catch ( Exception e ) {
			fail( "Failed to load configuration. There seems to be something wrong with the test files." );
		}

		assertConfigurationContent();
		assertEquals( "blabla", Configuration.get().getString( "section2.bla" ) );
	}


	private void assertFilesReadable() {
		if ( !test1.canRead() || !test2.canRead() ) {
			fail( "Can't read configuration test files." );
		}
	}


	private void assertConfigurationContent() {
		assertEquals( "bar", Configuration.get().getString( "section1.foo" ) );
		assertEquals( 23, Configuration.get().getInt( "section2.fla" ) );
		assertEquals( 42.42, Configuration.get().getDouble( "section2.flo" ), 0 );
		assertNull( Configuration.get().getString( "section3.nonexistent" ) );

		boolean sawEx = false;
		try {
			Configuration.get().getInt( "NoSuchElement" );
		} catch ( NoSuchElementException e ) {
			sawEx = true;
		}
		if ( !sawEx ) {
			fail( "Didn't see expected exception." );
		}
	}
}
