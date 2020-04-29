package id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hp on 11/04/2018.
 */

public class CenterModel {
    public String id;
    public String serviceName;
    public String deskripsi;
    public String address;
    public String email;
    public long numphone;
    public int like;
    public String urlFile;
    public double latitude;
    public double longtitude;
    public String jambuka;

    public CenterModel() {

    }

    public CenterModel(String id, String serviceName, String address, String deskripsi, String email, long numphone, int like, String urlFile, double latitude, double longtitude, String jambuka) {
        this.id = id;
        this.serviceName = serviceName;
        this.address = address;
        this.deskripsi = deskripsi;
        this.email = email;
        this.numphone = numphone;
        this.like = like;
        this.urlFile = urlFile;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.jambuka = jambuka;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getNumber() {
        return numphone;
    }

    public void setNumber(int number) {
        this.numphone = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public String getBukajam() {
        return jambuka;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("serviceName", this.serviceName);
        result.put("address", this.address);
        result.put("deskripsi", this.deskripsi);
        result.put("email", this.email);
        result.put("numphone", this.numphone);
        result.put("like", this.like);
        result.put("urlFile", this.urlFile);
        result.put("latitude", this.latitude);
        result.put("longtitude", this.longtitude);
        result.put("jambuka", this.jambuka);

        return result;
    }

}
