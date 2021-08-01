package com.fl.wdl.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.fl.wdl.pojo.Goods;

@Component
public class ESUtils {
	
	@Autowired
	RestHighLevelClient restHighLevelClient;
	
	public Boolean collectDataToES(String keyword) throws MalformedURLException, IOException {
		String url = "https://search.jd.com/Search?keyword=" + keyword + "&enc=utf-8";
//		System.out.println(url);
		Document document = Jsoup.parse(new URL(url), 30000);
//		document.getElementsByClass("pc-items-item").forEach(i->{
//			System.out.println(i.html());
//		});

		Elements elements = document.getElementsByClass("gl-i-wrap");
		ArrayList<Goods> goodses = new ArrayList<>();
		for(Element e : elements) {
			String imgInfo = e.getElementsByClass("p-img").get(0).getElementsByTag("a").html();
			String cover = imgInfo.substring(imgInfo.lastIndexOf("=")+2, imgInfo.lastIndexOf(">")-1);
			String title = e.getElementsByClass("p-name").get(0).getElementsByTag("em").get(0).text();
			float price = Float.parseFloat(e.getElementsByClass("p-price").get(0).getElementsByTag("i").get(0).text());
			Goods goods = new Goods(cover,title,price);
			goodses.add(goods);
		}
		BulkRequest bulkRequest = new BulkRequest("goods_index");
		for(Goods g : goodses) {
			bulkRequest.add(new IndexRequest().source(JSON.toJSONString(g), XContentType.JSON));
		}
		BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
		return bulkResponse.hasFailures();

	}
}
