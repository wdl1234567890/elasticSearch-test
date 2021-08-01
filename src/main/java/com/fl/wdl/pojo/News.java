package com.fl.wdl.pojo;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName="news")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
	@Id
	private Long id;
	
	@Field(type = FieldType.Text)
	private String title;
	
	@Field(type = FieldType.Text)
	private String content;
	
	@Field(type = FieldType.Keyword)
	private String url;
	
	@Field(name="update_time", type = FieldType.Date, format = DateFormat.date_optional_time)
	private Date updateTime;
	
	private List<String> tags;
}
