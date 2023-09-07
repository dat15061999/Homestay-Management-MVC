package org.example.untils;

import java.util.List;

public interface CRUD <T> {
    void save();
    T create(T t);
    void delete(T t);
    void update(T t);
    List<T> readFile();
    T find(int idT);
}
