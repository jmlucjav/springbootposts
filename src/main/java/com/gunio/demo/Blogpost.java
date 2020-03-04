package com.gunio.demo;

import lombok.Data;

import java.util.Date;

@Data
public class Blogpost {
    private String id;
    private String author;
    private String title;
    private String content;
    private Date date;
}
