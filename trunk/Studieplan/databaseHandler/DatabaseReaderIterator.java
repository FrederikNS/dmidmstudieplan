/**
 * 
 */
package databaseHandler;

import java.util.Iterator;
import dataClass.Course;
import databaseHandler.DatabaseReader.Iter;

/**
 * @author Niels Thykier
 * Allows Iteration over the Database for courses.
 * Please note that this Iterator does not support the remove method and (as per java standards)
 * this method will throw the UnsupportedOperationException if called.
 * 
 * Other than that, this is a wrapper around the DatabaseReader.Iter class that (unlike the 
 * class it wraps around) implements the Iterator interface (and follows its standards).
 */
public class DatabaseReaderIterator implements Iterator<Course> {

	private Iter iter;
	
	public DatabaseReaderIterator(DatabaseReader db) {
		this.iter = db.new Iter();
	}
	
	public DatabaseReaderIterator(Iter iter)  {
		this.iter = iter;
	}

	
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return iter.hasNext();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public Course next() {
		return iter.getNextCourse();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		//Not supported... Follow the standard
		throw new UnsupportedOperationException();
	}

}
