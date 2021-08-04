package com.fileservice.dao;

import com.fileservice.models.SomeFile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SomeFileDAO extends ElasticsearchRepository<SomeFile, String> {
        List<SomeFile> findByNameContains(String name);
        List<SomeFile> findAll();

}
