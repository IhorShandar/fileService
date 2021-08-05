package com.fileservice.service;

import com.fileservice.dao.SomeFileDAO;
import com.fileservice.models.SomeFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SomeFileService {

    @Autowired
    SomeFileDAO someFileDAO;

    public void save(SomeFile someFile) {
        someFileDAO.save(someFile);
    }

    public void deleteAll() {
        someFileDAO.deleteAll();
    }

    public ResponseEntity<?> deleteByID(String ID) {
        if (someFileDAO.findById(ID).isPresent()) {
            someFileDAO.deleteById(ID);
            return ResponseEntity.ok("{\"success\":true}");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"success\":false,\"error\":\"file not found\"}");
    }

    public SomeFile findById(String id) {
        return someFileDAO.findById(id).orElse(null);
    }

    public Set<SomeFile> findByTags(List<String> tagList) {
        if (tagList.isEmpty()) {
            return someFileDAO.findAll();
        } else if (tagList.size() == 1) {
            return someFileDAO.findByTagsContains(tagList.get(0));
        }
        Set<SomeFile> filesFirstTag = someFileDAO.findByTagsContains(tagList.get(0));
        Set<SomeFile> newFiles = new HashSet<>();
        tagList.remove(0);
        for (SomeFile file : filesFirstTag) {
            if (file.getTags().containsAll(tagList)) {
                newFiles.add(file);
            }
        }
        if (newFiles.isEmpty()) {
            return someFileDAO.findAll();
        }
        return newFiles;
    }

    //find files by tags using recursion
    public Set<SomeFile> findByTags1(List<String> tagList, int index, Set<SomeFile> files) {
        if (tagList.isEmpty()) {
            return someFileDAO.findAll();
        }
        Set<SomeFile> newFiles = new HashSet<>();
        if (index == 0) {
            newFiles = someFileDAO.findByTagsContains(tagList.get(index));
        } else {
            for (SomeFile file : files) {
                if (file.getTags().contains(tagList.get(index))) {
                    newFiles.add(file);
                }
            }
        }
        if (!newFiles.isEmpty() && index == tagList.size() - 1) {
            return newFiles;
        } else if (!newFiles.isEmpty() && index != tagList.size() - 1) {
            return findByTags1(tagList, index + 1, newFiles);
        } else {
            return someFileDAO.findAll();
        }
    }

    //find files by tag1, from {files by tag1} by tag2, from {{files by tag1} by tag2} by tag3, etc.
    public Set<SomeFile> findByTags2(List<String> tagList) {
        if (tagList.isEmpty()) {
            return someFileDAO.findAll();
        } else if (tagList.size() == 1) {
            return someFileDAO.findByTagsContains(tagList.get(0));
        }
        Set<SomeFile> files = someFileDAO.findByTagsContains(tagList.get(0));
        for (int index = 1; index < tagList.size(); index++) {
            Set<SomeFile> newFiles = new HashSet<>();
            for (SomeFile file : files) {
                if (file.getTags().contains(tagList.get(index))) {
                    newFiles.add(file);
                }
            }
            if (newFiles.isEmpty()) {
                return someFileDAO.findAll();
            }else {
                files = newFiles;
            }
        }
        return files;
    }

    public Set<SomeFile> findByName(String name) {
        return someFileDAO.findByNameContains(name);
    }

}
