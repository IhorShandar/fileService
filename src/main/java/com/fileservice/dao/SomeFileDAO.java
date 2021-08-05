package com.fileservice.dao;

import com.fileservice.models.SomeFile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Set;

public interface SomeFileDAO extends ElasticsearchRepository<SomeFile, String> {
        Set<SomeFile> findByNameContains(String name);
        Set<SomeFile> findByTagsContains(String tags);
        Set<SomeFile> findAll();

}
