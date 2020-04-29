package id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hp on 17/04/2018.
 */

public class ArticleModel {
    public String id;
    public String title;
    public int like;
    public String catalog;
    public Date date;
    public String source;
    public String sebab;
    public String solusi;

    public ArticleModel() {

    }

    public ArticleModel(String id, String title, int like, String catalog, Date date, String source, String sebab, String solusi) {
        this.id = id;
        this.title = title;
        this.like = like;
        this.catalog = catalog;
        this.date = date;
        this.source = source;
        this.sebab = sebab;
        this.solusi = solusi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getCatalog() {
        return catalog;
    }

    public int getLike() {
        return like;
    }

    public String getSource() {
        return source;
    }

    public String getSebab() {
        return sebab;
    }

    public String getSolusi() {
        return solusi;
    }

    public Date getDate() {
        return date;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", this.id);
        result.put("title", this.title);
        result.put("catalog", this.catalog);
        result.put("like", this.like);
        result.put("sebab", this.sebab);
        result.put("solusi", this.solusi);
        result.put("date", this.date);

        return result;
    }

}
