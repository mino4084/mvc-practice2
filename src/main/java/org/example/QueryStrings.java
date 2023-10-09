package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryStrings {
    private List<QueryString> queryStringList = new ArrayList<>();

    public QueryStrings(String queryStringLine) {
        String[] queryStringTokens = queryStringLine.split("&");
        Arrays.stream(queryStringTokens)
                .forEach(q -> {
                    String[] values = q.split("=");
                    if (values.length != 2) {
                        throw new IllegalArgumentException("잘못된 QueryString 포맷의 문자열");
                    }
                    queryStringList.add(new QueryString(values[0], values[1]));
                });
    }

    public String getValue(String key) {
        return this.queryStringList.stream()
                .filter(queryString -> queryString.exists(key))
                .map(QueryString::getValue)
                .findFirst()
                .orElse(null);
    }
}
