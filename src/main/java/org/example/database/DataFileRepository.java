package org.example.database;

import org.example.HashFormatter;
import org.example.model.Person;
import org.example.model.Type;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.NoSuchFileException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataFileRepository {

    private Map<String, Field> fieldsMap;
    private String className;
    private String baseStoragePath;
    private File folder;

    private List<Type> types;


    public DataFileRepository(String path, List<Type> types, Class<?> clazz) {

        if (path == null || path.isEmpty()) {
            throw new NullPointerException("Path must not be null !");
        }

        if (clazz == null) {
            throw new NullPointerException("Class must not be null!");
        }

        fieldsMap = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() != String.class) {
                throw new IllegalArgumentException("Fields of the class " + clazz.getName() + " have to be String type");
            }
            fieldsMap.put(field.getName(), field);
        }
        this.types = types;
        this.baseStoragePath = path;
        this.className = clazz.getSimpleName();
        this.folder = new File(path);
    }

    protected List<Person> getAllByPrarms(Type type, Map<String, String> params) {
        List<Person> queryResullts = new ArrayList<>();
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            XMLParser xmlParser = new XMLParser();
            for (File file : files) {

                Map<String, String> fieldsFromFile = xmlParser.parseElement(file, fieldsMap);
                boolean isHere = true;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (!fieldsFromFile.get(key).equals(value)) {
                        isHere = false;
                        break;
                    }
                }

                if (isHere) {
                    queryResullts.add(generateObject(fieldsFromFile));
                }

            }
        } else {
            System.out.println("Podana ścieżka nie jest folderem lub nie istnieje.");
        }
        return queryResullts;
    }
    protected Person getOneByPrarms(Type type, Map<String, String> params) {
        String nameFolderToSearch = baseStoragePath +"/"+className+"/"+type.getName();
        File folderToSearch = new File(nameFolderToSearch);
        if (folderToSearch.exists() && folderToSearch.isDirectory()) {
            File[] files = folderToSearch.listFiles();
            XMLParser xmlParser = new XMLParser();
            for (File file : files) {

                Map<String, String> fieldsFromFile = xmlParser.parseElement(file, fieldsMap);
                boolean isHere = true;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (!fieldsFromFile.get( entry.getKey() ).equals( entry.getValue()) ) {
                        isHere = false;
                        break;
                    }
                }

                if (isHere) {
                   return  generateObject(fieldsFromFile);
                }

            }
        }
        return null;

    }

    protected boolean update(Type type,Person person){
       if(delete(person.getPersonId())) {
           return  save(type,person.getPersonId(), person);
       }else{
           return false;
       }

    }

    public boolean save(Type type, String fileName, Person person) {
        Map<String, String> fields =  map(person);
        try {
            File file = new File(baseStoragePath +"/"+className+"/"+type.getName()+"/" + fileName+ ".xml");
            if ( file.createNewFile() == false) {
                throw new NoSuchFileException("Error during creating file");
            }

            FileWriter writer = new FileWriter(file);

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<root>\n");
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                writer.write("    <" + key + ">" + value + "</" + key + ">\n");
            }
            writer.write("</root>");


            writer.close();
            return true;

        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas tworzenia pliku XML");
            e.printStackTrace();
            return false;
        }

    }

    protected boolean delete(String personId) {
        Map<String , String> params = new HashMap<>();
        params.put("personId",personId);
        for (Type type: types){

            String nameFolderToSearch = baseStoragePath +"/"+className +"/"+type.getName();
            File folderToSearch = new File(nameFolderToSearch);
            if (folderToSearch.exists() && folderToSearch.isDirectory()) {
                File[] files = folderToSearch.listFiles();
                XMLParser xmlParser = new XMLParser();
                for (File file : files) {

                    Map<String, String> fieldsFromFile = xmlParser.parseElement(file, fieldsMap);
                    boolean isHere = true;
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        if (!fieldsFromFile.get( entry.getKey() ).equals( entry.getValue()) ) {
                            isHere = false;
                            break;
                        }
                    }

                    if (isHere) {
                       return file.delete();
                    }

                }
            }
        }
        return false;

    }

    protected Map<String, String> map(Object entity) {
        Map<String, String> map = new HashMap<>();
        Class<?> clazz = entity.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                map.put(fieldName, String.valueOf(value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    protected Person generateObject(Map<String , String > fields){

        if(fields == null){
            throw new NullPointerException("Fields map cannot be null !");
        }

        try {

            Class<Person> personClass = Person.class;

            Person object = personClass.newInstance();

            for (Map.Entry<String, String> entry : fields.entrySet()) {
                if(entry.getKey() == null || entry.getKey().isEmpty() ){
                    throw new ClassCastException("Field names of class you want create cannot be null or empty !");
                }
                String key = entry.getKey();
                String value = entry.getValue();
                Field field = personClass.getDeclaredField(key);
                field.setAccessible(true);
                field.set(object, value);
            }

            return object;
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException  e) {
            throw new RuntimeException("Error while generating object", e);
        }
    }


}
