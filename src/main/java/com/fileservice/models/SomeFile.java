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
@Document(indexName = "some_file")
@NoArgsConstructor
@AllArgsConstructor
public class SomeFile implements Serializable {

    @Id
    private String ID;

    private String name;
    private byte size;
    private Set<Tag> tags = new HashSet<>();

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void addTags(List<Tag> tags){
        this.tags.addAll(tags);
    }
}
