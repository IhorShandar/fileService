package com.fileservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fileservice.models.SomeFile;
import com.fileservice.models.Tag;
import com.fileservice.service.SomeFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class MainController {

    @Autowired
    SomeFileService fileService;

    @PostMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFile(@RequestBody SomeFile someFile){
        if (someFile.getSize() > 0){
            String[] splitName = someFile.getName().split("\\.");
            if (splitName.length > 0){
                String extension = splitName[splitName.length-1];
                Set<Tag> addTag = new HashSet<>();
                switch (extension){
                    case "mp3": addTag.add(new Tag("audio")); someFile.setTags(addTag); break;
                    case "doc":
                    case "docx":
                    case "txt": addTag.add(new Tag("document")); someFile.setTags(addTag); break;
                    case "jpg":
                    case "png": addTag.add(new Tag("image")); someFile.setTags(addTag); break;
                    case "mp4":
                    case "avi":
                    case "wmv": addTag.add(new Tag("video")); someFile.setTags(addTag); break;
                    case "zip":
                    case "rar":
                    case "7z": addTag.add(new Tag("archive")); someFile.setTags(addTag); break;
                }
            }
            fileService.save(someFile);
            return ResponseEntity.ok("{\"ID\":\"" + someFile.getID() + "\"}");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\":false,\"error\":\"error description\"}");
        }

    }

    @GetMapping(value = "/file/{ID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getByID(@PathVariable String ID) throws JsonProcessingException {
        String result;
        ObjectMapper objectMapper = new ObjectMapper();
        SomeFile someFile = fileService.findById(ID);
        if (someFile == null){
            result = "{\"success\":false,\"error\":\"file not found\"}";
        } else {
            result = objectMapper.writeValueAsString(someFile);
        }
       return result;
    }

    @PostMapping(value = "/file/{ID}/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> assignTags(@PathVariable String ID, @RequestBody List<Tag> tags) {
        SomeFile someFile = fileService.findById(ID);
        if (someFile == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"success\":false,\"error\":\"file not found\"}");
        } else {
            someFile.addTags(tags);
            fileService.save(someFile);
        }
        return ResponseEntity.ok("{\"success\":true}");
    }

    @DeleteMapping(value = "/file/{ID}/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteTags(@PathVariable String ID, @RequestBody List<Tag> tags) {
        SomeFile someFile = fileService.findById(ID);
        if (someFile == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"success\":false,\"error\":\"file not found\"}");
        } else {
            for (Tag tag : tags) {
                if (!someFile.getTags().remove(tag)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\":false,\"error\":\"tag not found in file\"}");
                }
            }
            fileService.save(someFile);
        }
        return ResponseEntity.ok("{\"success\":true}");
    }

    @DeleteMapping(value = "/file/{ID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable String ID){
        return fileService.deleteByID(ID);
    }

    @DeleteMapping(value = "/DeleteAllFiles")
    public String deleteAll(){
        fileService.deleteAll();
        return "Deleted";
    }

    @GetMapping("/file")
    public Map<String, Object> get(@RequestParam String tags, @RequestParam int page, @RequestParam int size){
        List<String> taggs = Arrays.asList(tags.split(","));
        Set<Tag> tagList = new HashSet<>();
        taggs.forEach(tagg -> tagList.add(new Tag(tagg)));
        List<SomeFile> someFiles = fileService.findByTags(tagList);

        Map<String, Object> result = new HashMap<>();
        result.put("total", someFiles.size());
        result.put("page", someFiles);
        return result;
    }

    @GetMapping("/files")
    public List<SomeFile> getPartName(@RequestParam String q){
        return fileService.findByName(q);
    }

}
