package com.example.xd720p.sensorcontroller_09082016.models;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xd720p on 12.09.16.
 */

@Table(name = "Sensors")
public class Sensors extends Model {

    @Column(name = "OBSERVATION_POINT")
    private Long OBSERVATION_POINT;

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

    public Sensors(Long OBSERVATION_POINT, Integer POSITION, Integer ACTIVE, String NAME, String CODE_NAME, String SMS_NAME, String CREATED_AT, String MODIFIED_AT) {
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

    public Long getOBSERVATION_POINT() {
        return OBSERVATION_POINT;
    }

    public void setOBSERVATION_POINT(Long OBSERVATION_POINT) {
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

    public static List<Sensors> getItems() {
        return new Select()
                .from(Sensors.class)
                .execute();
    }

    public static List<String> getObjectSMSNames(Long companyID) {

        List<Sensors> temp = new Select(new String[]{"Id, SMS_NAME"})
                .from(Sensors.class)
                .where("OBSERVATION_POINT =?", companyID)
                .orderBy("Name ASC")
                .execute();
        List<String> res = new ArrayList<String>();

        for (Sensors item : temp) {
            res.add(item.getSMS_NAME());
        }

        return res;
    }

    public static List<Sensors> getSensorsForObject(Long companyID) {

        return new Select(new String[]{"Id, SMS_NAME"})
                .from(Sensors.class)
                .where("OBSERVATION_POINT = ?", companyID)
                .orderBy("SMS_NAME ASC")
                .execute();

    }

    public static void updateObject(Sensors newObj, String smsName, Long company) {
        new Update(Sensors.class)
                .set("POSITION = ?, NAME = ?, CODE_NAME = ?, SMS_NAME = ?, MODIFIED_AT = ?",
                        newObj.getPOSITION(),
                        newObj.getNAME(),
                        newObj.getCODE_NAME(),
                        newObj.getSMS_NAME(),
                        newObj.getMODIFIED_AT()).where("SMS_NAME = ? AND OBSERVATION_POINT = ?", smsName, company).execute();
    }

    public static Sensors findBySmsNameAndPoint(String smsName, Long company) {
        return new Select()
                .from(Sensors.class)
                .where("SMS_NAME = ? AND OBSERVATION_POINT = ?", smsName, company)
                .executeSingle();
    }

    public static void delete(String smsName, Long compID) {
        new Delete().from(Sensors.class).where("SMS_NAME = ? AND OBSERVATION_POINT = ?", smsName, compID).execute();
    }

    public static List<Sensors> getAllActiveTSensors() {
        List<ObservationPoints> ops = ObservationPoints.getAllActiveTPoints();
        List<Sensors> sens = new LinkedList<Sensors>();

        for (ObservationPoints item : ops) {
           List<Sensors> temp = new Select()
                   .from(Sensors.class)
                   .where("OBSERVATION_POINT = ?", item.getNAME())
                   .execute();

            sens.addAll(temp);
        }

        return sens;

    }

    public static List<Sensors> getAllForPoint (ObservationPoints inputPoint) {
        return new Select()
                .from(Sensors.class)
                .where("OBSERVATION_POINT = ?", inputPoint.getNAME())
                .execute();
    }

    public static void deleteAll() {
        new Delete().from(Sensors.class).execute();
    }
}
