package com.example.xd720p.sensorcontroller_09082016.models;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by xd720p on 12.09.16.
 */

@Table(name = "Sensors")
public class Sensors extends Model {

    @Column(name = "OBSERVATION_POINT")
    private Integer OBSERVATION_POINT;

    @Column(name = "POSITION")
    private Integer POSITION;

    @Column(name = "ACTIVE")
    private Integer ACTIVE;

    @Column(name = "NAME")
    private String NAME;

    @Column(name = "CODE_NAME")
    private String CODE_NAME;

    @Column(name = "SMS_NAME")
    private String SMS_NAME;

    @Column(name = "CREATED_AT")
    private String CREATED_AT;

    @Column(name = "MODIFIED_AT")
    private String MODIFIED_AT;

    public Sensors() {
        super();
    }

    public Sensors(Integer OBSERVATION_POINT, Integer POSITION, Integer ACTIVE, String NAME, String CODE_NAME, String SMS_NAME, String CREATED_AT, String MODIFIED_AT) {
        super();
        this.OBSERVATION_POINT = OBSERVATION_POINT;
        this.POSITION = POSITION;
        this.ACTIVE = ACTIVE;
        this.NAME = NAME;
        this.CODE_NAME = CODE_NAME;
        this.SMS_NAME = SMS_NAME;
        this.CREATED_AT = CREATED_AT;
        this.MODIFIED_AT = MODIFIED_AT;
    }

    public Integer getOBSERVATION_POINT() {
        return OBSERVATION_POINT;
    }

    public void setOBSERVATION_POINT(Integer OBSERVATION_POINT) {
        this.OBSERVATION_POINT = OBSERVATION_POINT;
    }

    public Integer getPOSITION() {
        return POSITION;
    }

    public void setPOSITION(Integer POSITION) {
        this.POSITION = POSITION;
    }

    public Integer getACTIVE() {
        return ACTIVE;
    }

    public void setACTIVE(Integer ACTIVE) {
        this.ACTIVE = ACTIVE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getCODE_NAME() {
        return CODE_NAME;
    }

    public void setCODE_NAME(String CODE_NAME) {
        this.CODE_NAME = CODE_NAME;
    }

    public String getSMS_NAME() {
        return SMS_NAME;
    }

    public void setSMS_NAME(String SMS_NAME) {
        this.SMS_NAME = SMS_NAME;
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
