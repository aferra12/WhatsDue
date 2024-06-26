//THIS CODE WAS TAKEN FROM WORK WE DID IN LECTURE
package persist;

import exceptions.CrudException;

import java.sql.SQLException;

/**
 * An interface to implement basic CRUD operations.
 *
 * @param <T> base type.
 */
public interface CRUDPersister<T> {

    /**
     * Add <pre>t</pre> to database and store the
     * "generated key" by the database to the
     * <pre>id</pre> field of <pre>t</pre>.
     *
     * @param t must have an <pre>id</pre> field.
     * @throws CrudException if operation fails.
     */
    void create(T t) throws CrudException, SQLException;

    /**
     * Search the database to find a record with the given <pre>id</pre>.
     *
     * @param name the primary key to search for a record.
     * @return an object that encapsulates the retrieved record.
     * @throws CrudException if operation fails.
     */
    T read(String name) throws CrudException;

    /**
     * Update the database record of <pre>t</pre> with its current state.
     *
     * @param t must have an <pre>id</pre> field.
     * @throws CrudException if operation fails.
     */
    void update(T t) throws CrudException;

    /**
     * Remove the record with <pre>id</pre> primary key.
     *
     * @param name corresponds to the primary key of a record in a table.
     * @throws CrudException if operation fails.
     */
    void delete(String name) throws CrudException;

}
