package com.fl.wdl.repository;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.search.suggest.Suggest.Suggestion.Entry.Option;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest.Suggestion;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.repository.Repository;

import com.fl.wdl.pojo.News;

public interface NewsRepository extends Repository<News, Long>{
	
	
	default List<String> findTagsByPrefix(String prefix) throws IOException {
		RestHighLevelClient restHighLevelClient = RestClients.create(ClientConfiguration.localhost()).rest();
		SearchRequest searchRequest = new SearchRequest("news");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		SuggestionBuilder  completionSuggestionBuilder  = SuggestBuilders.completionSuggestion("tags").prefix(prefix).size(10).skipDuplicates(true);
		SuggestBuilder suggestBuilder = new SuggestBuilder();
		suggestBuilder.addSuggestion("tags_suggest", completionSuggestionBuilder);
		sourceBuilder.suggest(suggestBuilder);
		sourceBuilder.fetchSource(new String[] {"title","content","url"}, null);
		searchRequest.source(sourceBuilder);
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		List<String> tags = new LinkedList<>();
		List<Option> options = (List<Option>) searchResponse.getSuggest().getSuggestion("tags_suggest").getEntries().get(0).getOptions();
		for(Option option : options) {
			tags.add(option.getText().string());
		}
		restHighLevelClient.close();
		return tags;
	}
	
	@Highlight(fields = {
	        @HighlightField(name = "title"),
	        @HighlightField(name = "content")
	}, parameters = @HighlightParameters(preTags = "<span style='color:red'>", postTags = "</span>"))
	SearchPage<News> findByTitleOrContent(String title, String content, Pageable pageable);
}
