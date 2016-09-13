package com.example.xd720p.sensorcontroller_09082016.models;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by xd720p on 12.09.16.
 */

@Table(name = "ElectricCounte")
public class ElectricCounter extends Model {

    @Column(name = "OBSERVATION_POINT")
    private Integer OBSERVATION_POINT;

    @Column(name = "ACTIVE")
    private Integer ACTIVE;

    @Column(name = "CREATED_AT")
    private String CREATED_AT;

    @Column(name = "MODIFIED_AT")
    private String MODIFIED_AT;

    public ElectricCounter() {
        super();
    }

    public ElectricCounter(Integer OBSERVATION_POINT, Integer ACTIVE, String CREATED_AT, String MODIFIED_AT) {
        super();
        this.OBSERVATION_POINT = OBSERVATION_POINT;
        this.ACTIVE = ACTIVE;
        this.CREATED_AT = CREATED_AT;
        this.MODIFIED_AT = MODIFIED_AT;
    }

    public Integer getOBSERVATION_POINT() {
        return OBSERVATION_POINT;
    }

    public void setOBSERVATION_POINT(Integer OBSERVATION_POINT) {
        this.OBSERVATION_POINT = OBSERVATION_POINT;
    }

    public Integer getACTIVE() {
        return ACTIVE;
    }

    public void setACTIVE(Integer ACTIVE) {
        this.ACTIVE = ACTIVE;
    }

    public String getCREATED_AT() {
        return CREATED_AT;
    }

    public void setCREATED_AT(String CREATED_AT) {
        this.CREATED_AT = CREATED_AT;
    }

    public String getMODIFIED_AT() {
        return MODIFIED_AT;
    }

    public void setMODIFIED_AT(String MODIFIED_AT) {
        this.MODIFIED_AT = MODIFIED_AT;
    }
}
