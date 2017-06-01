package jsonjavabean;

import java.util.List;

/**
 * Created by 啊丁 on 2017/4/7.
 */

public class SearchAdvice {

    /**
     * query : 小苹果
     * suggestion_list : ["筷子兄弟 小苹果（新年Remix版）","温拿乐队 小苹果","微蓝海 小苹果","ＭＣ娜娜 小苹果","Beyond Entertainment Ltd. 小苹果","陆俊杰 小苹果","录音师蓦然 小苹果","DJ Lee3 小苹果","T榜 小苹果灭火器","乌拉喵 小苹果"]
     */

    private String query;
    private List<String> suggestion_list;

    public String getQuery() {
        return query;
    }



    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getSuggestion_list() {
        return suggestion_list;
    }

    public void setSuggestion_list(List<String> suggestion_list) {
        this.suggestion_list = suggestion_list;
    }
}
