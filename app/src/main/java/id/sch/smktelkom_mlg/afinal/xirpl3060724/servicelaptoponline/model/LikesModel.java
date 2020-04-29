package id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hp on 28/04/2018.
 */

public class LikesModel {
    public String id;
    public String email;
    public String name;

    public LikesModel() {

    }

    public LikesModel(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", this.email);
        result.put("name", this.name);

        return result;
    }

}
