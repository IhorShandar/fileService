package com.fileservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Document(indexName = "some_file2")
@NoArgsConstructor
@AllArgsConstructor
public class SomeFile implements Serializable {

    @Id
    private String ID;

    private String name;
    private byte size;
    private Set<String> tags = new HashSet<>();

    public void addTags(List<String> tags){
        this.tags.addAll(tags);
    }
}
