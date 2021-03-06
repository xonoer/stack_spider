package com.esy.stack.download.impl;

import com.esy.stack.download.BaseAnalysis;
import com.esy.stack.entity.Article;
import com.esy.stack.entity.WebSiteColumn;
import com.esy.stack.util.Constants;
import com.esy.stack.util.StringManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by guanjie on 2017/3/22.
 * 同花顺
 */
@Component
public class THSAnalysis extends BaseAnalysis {

    private static final StringManager stringManager = StringManager.getStringManageByFileName(Constants.HANDLER_PROP_PATH);

    @Override
    protected int getWebSiteId() {
        return stringManager.getIntValue("ths_website_id");
    }

    @Override
    protected List<Article> parseArticles(String content, WebSiteColumn aWebSiteColumn) {
        List<Article> result = new ArrayList<>();
        Document doc = Jsoup.parse(content);
        System.out.println(doc);
        Elements atagList = doc.select(".t_news .txt_t a[href ^= newsDetail]");
        for (Element each : atagList) {
            Article record = new Article();
            record.setTitle(each.text());
            record.setUrl(each.attr("href"));
            record.setCreateTime(new Date());
            record.setColumnId(aWebSiteColumn.getId());
            result.add(record);
        }
        return result;
    }

    @Override
    protected List<String> parseColumnUrl(WebSiteColumn aWebSiteColumn) {
        List<String> result = new ArrayList<>();
        for(int i = 1; i <= aWebSiteColumn.getTotalPages(); i++)
            result.add(MessageFormat.format(aWebSiteColumn.getColumnUrl(), i));
        return result;
    }
}
