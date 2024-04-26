package org.example.database;

import org.example.model.Type;

public interface CRUD<Person> {
    Person find (Type type, String firstName, String lastName, String mobile);
    void create (Type type,Person person);
    boolean remove (String personId);
    void modify (Type type,Person person);
}
