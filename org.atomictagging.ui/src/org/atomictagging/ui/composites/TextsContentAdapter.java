package org.atomictagging.ui.composites;

import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.fieldassist.IControlContentAdapter2;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class TextsContentAdapter implements IControlContentAdapter, IControlContentAdapter2 {

	@Override
	public void setControlContents( final Control control, final String contents, final int cursorPosition ) {

		String text = ( (Text) control ).getText();

		final int lastIndexOfComma = text.lastIndexOf( ',' );
		if ( lastIndexOfComma != -1 ) {
			text = text.substring( 0, lastIndexOfComma );
			text += ", " + contents;
		} else {
			text = contents;
		}

		( (Text) control ).setText( text );
		( (Text) control ).setSelection( text.length(), text.length() );
	}


	@Override
	public void insertControlContents( final Control control, final String contents, final int cursorPosition ) {

	}


	@Override
	public String getControlContents( final Control control ) {
		final String content = ( (Text) control ).getText();

		if ( content.lastIndexOf( ',' ) == content.length() - 1 ) {
			return "";
		}

		final String[] words = content.split( "," );

		if ( words.length == 0 ) {
			return null;
		}

		return words[words.length - 1].trim();
	}


	@Override
	public int getCursorPosition( final Control control ) {
		return ( (Text) control ).getCaretPosition();
	}


	@Override
	public Rectangle getInsertionBounds( final Control control ) {
		final Text text = (Text) control;
		final Point caretOrigin = text.getCaretLocation();
		// We fudge the y pixels due to problems with getCaretLocation
		// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=52520
		return new Rectangle( caretOrigin.x + text.getClientArea().x, caretOrigin.y + text.getClientArea().y + 3, 1,
				text.getLineHeight() );
	}


	@Override
	public void setCursorPosition( final Control control, final int position ) {
		( (Text) control ).setSelection( new Point( position, position ) );
	}


	@Override
	public Point getSelection( final Control control ) {
		return ( (Text) control ).getSelection();
	}


	@Override
	public void setSelection( final Control control, final Point range ) {
		( (Text) control ).setSelection( range );
	}

}
