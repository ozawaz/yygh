package cn.distantstar.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author: distantstar
 */
@Data
@Document
public class User {
    @Id
    private String id;
    private String name;
    private Integer age;
    private String email;
    private String createDate;

}
