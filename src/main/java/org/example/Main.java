package org.example;

import org.example.model.Person;
import org.example.model.Type;
import org.example.repository.PersonRepository;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        // w tym prowizorzycznym wykonaniu zakładam ze struktura bazy to
        // storage/Person/
        // ---------------External
        // ---------------Internal

        List<Type> types = new ArrayList<>();
        types.add(new Type("External") );
        types.add(new Type("Internal") );

        PersonRepository personRepository = new PersonRepository("storage",types);


        //tworzymy 10 przykladowych rekordow w naszej bazie plikow
        for (int i = 0; i < 10 ; i++){
            Person person =new Person();
            person.setFirstName("Jan"+i);
            person.setLastName("Kowalski"+i);
            person.setEmail("kowalski@mail.com"+i);
            person.setMobile("+4732432342342"+i);
            person.setPesel("435345345345"+i);
            personRepository.create(types.get(0),person);
        }

        // Szukamy osobe według parametrów
        Person person = personRepository.find(types.get(0) , "Jan3", "Kowalski3" ,"+47324323423423");


        if(person!=null){
            System.out.println("znaleziono pracownika: nazwisko"+person.getLastName()+", email:"+person.getEmail());

            // Robimy updejt tej persony
            person.setFirstName("Paweł");
            personRepository.modify(types.get(0),person);


            //Usuwamy persone
            personRepository.remove(person.getPersonId());
        }



    }


}