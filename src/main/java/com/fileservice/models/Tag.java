package com.fileservice.models;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;

@Data
@Document(indexName = "tags")
@NoArgsConstructor
public class Tag implements Serializable {

    private String tag;

    public Tag(String tag) {
        this.tag = tag;
    }

}
