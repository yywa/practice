import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.glass.ui.Size;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import org.apache.http.HttpHost;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.reflection.MetaObject;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import sun.rmi.runtime.Log;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ElasticSearchOperation {
    private static String host = "47.106.193.40";
    private static Integer port = 9200;
    private static final Integer MAX = 10000;

    private static ThreadLocal<Map<String, String>> scrollThreadLoacl = new ThreadLocal<Map<String, String>>() {
        @Override
        protected Map<String, String> initialValue() {
            return new HashMap();
        }
    };

    public static RestHighLevelClient client;

    public static void createConnection() {
        try {
            HttpHost[] httpHosts = new HttpHost[1];
            httpHosts[0] = new HttpHost(host, port);
            client = new RestHighLevelClient(RestClient.builder(httpHosts));
            System.out.println("成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("失败");
        }
    }

    public static void validateClient() {
        if (client == null) {
            createConnection();
        }
    }

    public String[] getIndices(String condition) {
        validateClient();
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest();
            getIndexRequest.indices(condition + "*");
            GetIndexResponse response = client.indices().get(getIndexRequest, RequestOptions.DEFAULT);
            return response.getIndices();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean createIndex(String indexName) {
        validateClient();
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean indexExist(String indexName) {
        boolean flag = false;
        validateClient();
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest();
            getIndexRequest.indices(indexName);
            flag = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean deleteIndex(String indexName) {
        boolean flag = false;
        validateClient();
        if (!indexExist(indexName)) {
            return true;
        }
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            client.indices().delete(request, RequestOptions.DEFAULT);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public static long countIndex(String indexName) {
        long totalHits = 0L;
        validateClient();
        if (!indexExist(indexName)) {
            return totalHits;
        }
        try {
            SearchRequest searchRequest = new SearchRequest(indexName);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            totalHits = hits.totalHits;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return totalHits;
    }

    public static boolean deleteRecordByCondition(String indexName, String type, String key, String value) {
        validateClient();
        try {
            BulkRequest bulkRequest = new BulkRequest();
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.matchQuery(key, value));
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.types(type);
            searchRequest.source(sourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            List<String> ids = Lists.newArrayList();
            for (SearchHit hit : hits) {
                ids.add(hit.getId());
            }
            for (String id : ids) {
                DeleteRequest deleteRequest = new DeleteRequest(indexName, type, id);
                bulkRequest.add(deleteRequest);

            }
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static Map<String, Object> findDateById(String indexName, String id) {
        Map<String, Object> map = Maps.newHashMap();
        validateClient();
        try {
            GetRequest getRequest = new GetRequest(indexName, indexName, id);
            GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
            map = response.getSourceAsMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static List<Map<String, Object>> findAllData(String indexName) {
        validateClient();
        List<Map<String, Object>> list = Lists.newArrayList();
        List<Map<String, Object>> data = findAllData(indexName, MAX);
        while (data != null && data.size() > 0) {
            list.addAll(data);
            data = findAllData(indexName, MAX);
        }
        return list;
    }

    public static List<Map<String, Object>> findData(String indexName, String key, Object value, int batchSize) {
        validateClient();
        return getBatchSize(indexName, new MatchQueryBuilder(key, value), batchSize);
    }

    public static Long countData(String indexName, String key, Object value) {
        validateClient();
        long count = 0L;
        try {
            SearchRequest searchRequest = new SearchRequest(indexName);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery(key, value));
            searchRequest.source(searchSourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            count = response.getHits().totalHits;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static List<Map<String, Object>> findDataByRange(String indexName, String key, Object highRange, Object lowRange, int batchSize) {
        validateClient();
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder(key).lte(highRange);
        rangeQueryBuilder.gte(lowRange);
        return getBatchSize(indexName, rangeQueryBuilder, batchSize);
    }

    public static long countDataByRange(String indexName, String typeName, String key, Object highRange, Object lowRange) {
        validateClient();
        SearchHits rangeHits = getRangeHits(indexName, typeName, key, highRange, lowRange);
        return rangeHits.totalHits;
    }

    public static List<Map<String, Object>> findByCondition(String indexName, String typeName, Map<String, Object> condition) {
        validateClient();
        List<Map<String, Object>> list = Lists.newArrayList();
        try {
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            for (Map.Entry<String, Object> entry : condition.entrySet()) {
                builder.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
            }
            searchSourceBuilder.query(builder);
            searchSourceBuilder.size(MAX);
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.types(typeName);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] hits1 = hits.getHits();
            for (SearchHit hit : hits1) {
                list.add(hit.getSourceAsMap());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static boolean insertOneSync(String indexName, Map<String, Object> data) {
        return insertOneSync(indexName, data, 10000);
    }

    public static boolean insertOneSync(String indexName, Map<String, Object> data, long overTime) {
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                insertOneForResponse(indexName, data);
                return true;
            }
        };
        return getTimeOut(callable, overTime);
    }

    public void insertOneASync(String indexName, Map<String, Object> data) {
        validateClient();
        ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {

            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("异步单条插入失败");
            }
        };
        IndexRequest request = new IndexRequest(indexName, indexName);
        request.source(data);
        client.indexAsync(request, RequestOptions.DEFAULT, listener);
    }

    public static boolean insertBulkSync(String indexName, List<Map<String, Object>> data) {
        getBulkInsertSync(indexName, data);
        return true;
    }

    public static boolean insertBulkSync(String indexName, List<Map<String, Object>> data, long overTime) {
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                getBulkInsertSync(indexName, data);
                return true;
            }
        };
        return getTimeOut(callable, overTime);
    }

    public static boolean insertBulkASync(String indexName, List<Map<String, Object>> data) {
        validateClient();
        ActionListener<BulkResponse> listener = new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkItemResponses) {

            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("异步批量插入失败");
            }
        };
        BulkRequest bulkRequest = new BulkRequest();
        for (Map<String, Object> map : data) {
            IndexRequest request = new IndexRequest(indexName, indexName);
            request.source(map);
            bulkRequest.add(request);
        }
        client.bulkAsync(bulkRequest, RequestOptions.DEFAULT, listener);
        return true;
    }

    public static boolean upsertOneSyncById(String indexName, Map<String, Object> data, String id) {
        return upsertOneSyncById(indexName, data, id, 10000);
    }

    public static boolean upsertOneSyncById(String indexName, Map<String, Object> data, String id, long overTime) {
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                upsertOneForResponse(indexName, data, id);
                return null;
            }
        };
        return getTimeOut(callable, overTime);
    }

    public static void upsertOneASyncById(String indexName, Map<String, Object> data, String id) {
        upsertOneForASyncResponse(indexName, data, id);
    }

    public static boolean upsertOneForASyncResponse(String indexName, Map<String, Object> data, String id) {
        validateClient();
        ActionListener<UpdateResponse> listener = new ActionListener<UpdateResponse>() {
            @Override
            public void onResponse(UpdateResponse updateResponse) {

            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("异步修改执行失败");
            }
        };
        UpdateRequest request = new UpdateRequest(indexName, indexName, id);
        request.doc(data);
        request.upsert(data);
        client.updateAsync(request, RequestOptions.DEFAULT, listener);
        return true;
    }


    public static UpdateResponse upsertOneForResponse(String indexName, Map<String, Object> data, String id) {
        validateClient();
        UpdateResponse response = null;
        try {
            UpdateRequest request = new UpdateRequest(indexName, indexName, id);
            request.doc(data);
            request.upsert(data);
            response = client.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static boolean upsertBulkSyncById(String indexName, List<Map<String, Object>> data) {
        return ussertBulkSyncById(indexName, data, 10000);
    }

    public static boolean ussertBulkSyncById(String indexName, List<Map<String, Object>> data, long overTime) {
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                upsertBulkForResponse(indexName, data);
                return true;
            }
        };
        return getTimeOut(callable, overTime);
    }

    public static void upsertBulkASyncById(String indexName, List<Map<String, Object>> data) {
        upsertBulkASyncForResponse(indexName, data);
    }

    public static boolean upsertBulkASyncForResponse(String indexName, List<Map<String, Object>> data) {
        validateClient();
        ActionListener<BulkResponse> listener = new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkItemResponses) {

            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("批量执行");
            }
        };
        BulkRequest request = new BulkRequest();
        for (Map<String, Object> map : data) {
            String id = map.get("id").toString();
            if (!Strings.isNullOrEmpty(id)) {
                map.remove("id");
                UpdateRequest updateRequest = new UpdateRequest(indexName, indexName, id);
                updateRequest.doc(map);
                updateRequest.upsert(map);
                request.add(updateRequest);
            }
        }
        client.bulkAsync(request, RequestOptions.DEFAULT, listener);
        return true;
    }

    public static BulkResponse upsertBulkForResponse(String indexName, List<Map<String, Object>> data) {
        validateClient();
        BulkResponse response = null;
        BulkRequest request = new BulkRequest();
        for (Map<String, Object> map : data) {
            String id = map.get("id").toString();
            if (!Strings.isNullOrEmpty(id)) {
                map.remove("id");
                UpdateRequest updateRequest = new UpdateRequest(indexName, indexName, id);
                updateRequest.doc(map);
                updateRequest.upsert(map);
                request.add(updateRequest);
            }
        }
        try {
            response = client.bulk(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void getBulkInsertSync(String indexName, List<Map<String, Object>> data) {
        validateClient();
        try {
            BulkRequest bulkRequest = new BulkRequest();
            for (Map<String, Object> map : data) {
                IndexRequest request = new IndexRequest(indexName, indexName);
                request.source(map);
                bulkRequest.add(request);
            }
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean getTimeOut(Callable<?> task, long timeOut) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        FutureTask<?> futureTask = (FutureTask<?>) executorService.submit(task);
        executorService.execute(futureTask);
        try {
            futureTask.get(timeOut, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static IndexResponse insertOneForResponse(String indexName, Map<String, Object> data) {
        validateClient();
        IndexResponse indexResponse = null;
        try {
            IndexRequest indexRequest = new IndexRequest(indexName, indexName);
            indexRequest.source(data);
            indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexResponse;
    }

    public static SearchHits getRangeHits(String indexName, String typeName, String key, Object highRange, Object lowRange) {
        SearchResponse searchResponse = null;
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder(key).lte(highRange);
            rangeQueryBuilder.gte(lowRange);
            searchSourceBuilder.query(rangeQueryBuilder);
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.types(typeName);
            searchRequest.source(searchSourceBuilder);
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResponse.getHits();
    }


    public static List<Map<String, Object>> findAllData(String indexName, int batchSize) {
        validateClient();
        return getBatchSize(indexName, QueryBuilders.matchAllQuery(), batchSize);
    }


    private static List<Map<String, Object>> getBatchSize(String indexName, QueryBuilder queryBuilder, int batchSize) {
        List<Map<String, Object>> list = Lists.newArrayList();
        if (scrollThreadLoacl.get() == null || !scrollThreadLoacl.get().containsKey(indexName)) {
            list = getFirstScrollData(indexName, queryBuilder, batchSize);
        } else {
            String scroll = scrollThreadLoacl.get().get(indexName);
            list = getScrollData(scroll, indexName);
        }
        return list;
    }


    private static List<Map<String, Object>> getFirstScrollData(String indexName, QueryBuilder queryBuilder, int batchSize) {
        List<Map<String, Object>> list = Lists.newArrayList();
        try {
            Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1));
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.scroll(scroll);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(queryBuilder);
            searchSourceBuilder.size(batchSize);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            String scrollId = response.getScrollId();
            Map<String, String> map = Maps.newHashMap();
            map.put(indexName, scrollId);
            scrollThreadLoacl.set(map);
            SearchHits hit = response.getHits();
            SearchHit[] hits1 = hit.getHits();
            for (SearchHit searchHit : hits1) {
                list.add(searchHit.getSourceAsMap());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Map<String, Object>> getScrollData(String scrollId, String indexName) {
        List<Map<String, Object>> list = Lists.newArrayList();
        try {
            Scroll scroll = new Scroll(TimeValue.timeValueMinutes(2));
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(scroll);
            SearchResponse response = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            SearchHit[] hits1 = hits.getHits();
            for (SearchHit hit : hits1) {
                list.add(hit.getSourceAsMap());
            }
            if (hits == null || hits1.length == 0) {
                ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
                clearScrollRequest.addScrollId(scrollId);
                client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
            } else {
                scrollThreadLoacl.get().put(indexName, response.getScrollId());
            }
        } catch (IOException e) {
            if (scrollThreadLoacl.get().containsKey(indexName)) {
                scrollThreadLoacl.get().remove(indexName);
            }
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        createConnection();
        System.out.println(indexExist("yyw"));
    }
}
