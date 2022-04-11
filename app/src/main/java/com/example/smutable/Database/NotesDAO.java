package com.example.smutable.Database;


import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.smutable.Models.Notes2;

import java.util.List;

@Dao
public interface NotesDAO {
    @Insert(onConflict = REPLACE)
    void insert(Notes2 notes2);

    @Query("SELECT * FROM notes2 ORDER BY id DESC")
    List<Notes2> getAll();

    @Query("UPDATE notes2 SET title = :title, notes = :notes WHERE ID = :id")
    void update(int id, String title, String notes);

    @Delete
    void delete(Notes2 notes2);
}
