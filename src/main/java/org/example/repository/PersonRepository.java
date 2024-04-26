package org.example.repository;

import org.example.HashFormatter;
import org.example.database.CRUD;
import org.example.database.DataFileRepository;
import org.example.model.Person;
import org.example.model.Type;

import java.io.File;
import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonRepository extends DataFileRepository implements CRUD<Person> {


    public PersonRepository(String path,List<Type> types) {
        super(path,types,Person.class);
    }

    @Override
    public Person find(Type type, String firstName, String lastName, String mobile) {
        Map<String , String> params = new HashMap<>();
        params.put("firstName" , firstName );
        params.put("lastName" , lastName );
        params.put("mobile" , mobile );
        return getOneByPrarms(type,params);
    }

    @Override
    public void create(Type type, Person person) {
        String id = genereteNewId();
        person.setPersonId(id);
        save(type, id,person);
    }

    @Override
    public boolean remove(String personId) {
        return delete(personId);
    }

    @Override
    public void modify(Type type,Person person) {
        update(type,person);
    }

    public String genereteNewId() {
        String genId = HashFormatter.generateMD5(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        return genId ;
    }
}
