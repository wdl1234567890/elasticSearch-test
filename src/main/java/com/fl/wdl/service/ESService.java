package com.fl.wdl.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ESService {
	
	@Autowired
	RestHighLevelClient restHighLevelClient;
	
	public ArrayList<Map<String,Object>> search(String keyword,int pageNo) throws IOException{
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("title", keyword));
		searchSourceBuilder.highlighter(new HighlightBuilder().field("title").preTags("<span style='color:red'>").postTags("</span>"));
		searchSourceBuilder.from((pageNo - 1)*10);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		ArrayList<Map<String,Object>> list = new ArrayList<>();
		for(SearchHit searchHit : searchResponse.getHits().getHits()) {
			Map<String, Object> data = searchHit.getSourceAsMap();
			Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
			if(highlightFields != null) {
				StringBuffer sb = new StringBuffer();
				for(Text text : highlightFields.get("title").fragments()) {
					sb.append(text.string());
				}
				data.put("title", sb.toString());
			}
			list.add(data);
		}
		return list;
	}
}
