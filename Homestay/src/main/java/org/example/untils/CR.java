package org.example.untils;

import java.util.List;

public interface CR <T>{
    void save();
    void update(T t);
    List<T> readFile();
    T find(int idT);
}
