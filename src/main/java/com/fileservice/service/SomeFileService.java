package com.fileservice.service;

import com.fileservice.dao.SomeFileDAO;
import com.fileservice.models.SomeFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SomeFileService {

    @Autowired
    SomeFileDAO someFileDAO;

    public void save(SomeFile someFile){
        someFileDAO.save(someFile);
    }

    public void deleteAll(){
        someFileDAO.deleteAll();
    }

    public ResponseEntity<?> deleteByID(String ID){
        if (someFileDAO.findById(ID).isPresent()){
            someFileDAO.deleteById(ID);
            return ResponseEntity.ok("{\"success\":true}");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"success\":false,\"error\":\"file not found\"}");
    }

    public SomeFile findById(String id) {
        return someFileDAO.findById(id).orElse(null);
    }

    public List<SomeFile> findByTags(Set<String> tagList){
        List<SomeFile> all = someFileDAO.findAll();
        List<SomeFile> newFiles = new ArrayList<>();
        for (SomeFile file: all) {
            if (file.getTags().containsAll(tagList)){
                newFiles.add(file);
            }
        }
        if (newFiles.isEmpty()){
            return all;
        }
        return newFiles;
    }

    public List<SomeFile> findByName(String name){
        return someFileDAO.findByNameContains(name);
    }

}
