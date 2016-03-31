package org.bk.producer.event;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class ElasticSearchEvent {

	static String URL_SUBMIT_GOODS = "http://localhost:9200/jd/goods";
	
	
//	public static void main(String[] a){
//		ElasticSearchSnatchService es = new ElasticSearchSnatchService();
//		try {
//			String goods = "{\"goodsId\":\"1594325634-三星(SAMSUNG ) UA55JS9800 55英寸 4..\",\"goodsName\":\"三星(SAMSUNG ) UA55JS9800 55英寸 4..\",\"goodsCode\":\"1594325634\",\"goodsIndex\":\"1594325634-三星(SAMSUNG ) UA55JS9800 55英寸 4..\",\"model\":\"三星(SAMSUNG ) UA55JS9800 55英寸 4..\",\"price\":\"16499.00\",\"merchantName\":\"三星电视专卖店\",\"goodsUrl\":\"http://item.jd.com/1594325634.html\",\"brand\":{\"name\":\"家用电器\",\"code\":\"145335\",\"createTime\":\"20151213143355\",\"lastModifyTime\":\"20151213143355\",\"snatchTime\":\"20151213143355\"},\"merchant\":{\"name\":\"三星电视专卖店\",\"code\":\"145335\",\"catCodes\":[],\"createTime\":\"20151213143355\",\"lastModifyTime\":\"20151213143355\",\"snatchTime\":\"20151213143355\"},\"snatchTime\":\"20151213143355\"}";
//			es.init();
////			es.createIndex(goods);
//			es.search();
//			
////			Document doc = Jsoup.connect(URL_SUBMIT_GOODS).data("goods",goods)
////					.ignoreContentType(true).post();
////			System.out.println("save elasticSearch :"+doc.text());
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//	}
	
	public static void post(String goods){
		
	}
	
	public static ElasticSearchEvent getInstance(){
		if(null==instance){
			instance = new ElasticSearchEvent();
		}
		return instance;
	}
	
	private static ElasticSearchEvent instance;
	
	public static final EventBus eventBus = new EventBus();
	
	static {
		eventBus.register(new Object() {

		    @Subscribe
		    public void lister(Integer integer) {
		        System.out.printf("%s from int%n", integer);
		    }

		    @Subscribe
		    public void lister(Number integer) {
		        System.out.printf("%s from Number%n", integer);
		    }

		    @Subscribe
		    public void lister(Long integer) {
		        System.out.printf("%s from long%n", integer);
		    }
		});
	}
	
	private Client client;

	public void init() {
		try {
			client = TransportClient.builder().build()
			        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public Client getClient(){
		if(null==client){
			init();
		}
		return client;
	}
	
	public void close(){
		client.close();
	}
	
	public void createIndex(String goods) {
		client.prepareIndex("jd", "goods").setSource(goods).execute().actionGet();
	}
	
	public void sendGoods(String goods){
		IndexResponse resp = null;
		try {
			resp = getClient().prepareIndex("jd", "goods").setSource(goods).execute().actionGet();
			
			System.out.println("save elasticSearch :" + resp.getId());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int times = 0;
		while(resp==null || null==resp.getIndex() || "".equals(resp.getId())){
			times++;
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			resp = getClient().prepareIndex("jd", "goods").setSource(goods).execute().actionGet();
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("save:"+goods);
	}
	

	public void search() {
		          QueryBuilder qb = QueryBuilders
		                  .boolQuery()
		                  .must(QueryBuilders.termQuery("goodsCode", "1594325634"))
//		                  .should(QueryBuilders.termQuery("id", "0"))
//		 75                 //.mustNot(QueryBuilders.termQuery("content", "test2"))
//		 76                 //.should(QueryBuilders.termQuery("content", "test3"))
		                  ;
		          
		          SearchResponse response = client.prepareSearch("jd")
		                  .setTypes("goods")
		                  .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//		                  .setQuery(qb) // Query
		                  //.setFilter(FilterBuilders.rangeFilter("age").from(0).to(100)) // Filter
		                  .setFrom(0).setSize(100).setExplain(true)
		                  .execute().actionGet();
		          SearchHits hits = response.getHits();
		          System.out.println(hits.getTotalHits());
		          for (int i = 0; i < hits.getHits().length; i++) {
		              System.out.println(hits.getHits()[i].getSourceAsString());
		          }
		      }    
	
	
}
