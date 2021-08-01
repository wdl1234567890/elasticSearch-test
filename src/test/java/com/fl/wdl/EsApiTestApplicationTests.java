package com.fl.wdl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchPage;

import com.fl.wdl.pojo.News;

@SpringBootTest
public class EsApiTestApplicationTests{
	
	
	@Autowired
	com.fl.wdl.repository.NewsRepository newsRepository;

	@Test
	public void testTags() throws IOException {
		List<String> tags = newsRepository.findTagsByPrefix("张");
		System.out.println(tags);
	}
	
	@Test
	public void testSearch() throws IOException {
		SearchPage<News> newsPage = newsRepository.findByTitleOrContent("张伯伦", "张伯伦", PageRequest.of(0, 10));
		//SearchPage
		List<SearchHit<News>> searchHits = newsPage.getSearchHits().getSearchHits();
		for(SearchHit<News> searchHit : searchHits) {
			News news = searchHit.getContent();
			List<String>  highlightTitle = searchHit.getHighlightField("title");
			List<String>  highlightContent = searchHit.getHighlightField("content");
			if(highlightTitle != null && highlightTitle.size() > 0) {
				StringBuffer sb = new StringBuffer();
				highlightTitle.forEach(t -> sb.append(t));
				news.setTitle(sb.toString());
			}
			if(highlightContent != null && highlightContent.size() > 0) {
				StringBuffer sb = new StringBuffer();
				highlightContent.forEach(c -> sb.append(c));
				news.setContent(sb.toString());
			}
			
			if(news.getTitle().length() > 40) {
				news.setTitle(news.getTitle().substring(0, 40).concat("..."));
			}
			
			if(news.getContent().length() > 150) {
				news.setContent(news.getContent().substring(0, 150).concat("..."));
			}
			
			System.out.println(news);
			
		}
	}

}