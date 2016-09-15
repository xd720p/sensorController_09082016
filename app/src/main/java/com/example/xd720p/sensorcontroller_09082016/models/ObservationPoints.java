package com.example.xd720p.sensorcontroller_09082016.models;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xd720p on 12.09.16.
 */

@Table(name = "ObservationPoints")
public class ObservationPoints extends Model {

    @Column(name = "POSITION")
    private Integer POSITION;

    @Column(name = "NAME")
    private String NAME;

    @Column(name = "ACTIVE")
    private Integer ACTIVE;

    @Column(name = "PHONE_T")
    private String PHONE_T;

    @Column(name = "PASSWORD_T")
    private Integer PASSWORD_T;

    @Column(name = "ACTIVE_T")
    private Integer ACTIVE_T;

    @Column(name = "PHONE_E")
    private String PHONE_E;

    @Column(name = "PASSWORD_E")
    private Integer PASSWORD_E;

    @Column(name = "ACTIVE_E")
    private Integer ACTIVE_E;

    @Column(name = "LAST_QUERY")
    private String LAST_QUERY;

    @Column(name = "CREATED_AT")
    private String CREATED_AT;

    @Column(name = "MODIFIED_AT")
    private String MODIFIED_AT;

    public ObservationPoints() {
        super();
    }

    public ObservationPoints(String NAME, Integer POSITION, Integer ACTIVE, String PHONE_T, Integer PASSWORD_T, Integer ACTIVE_T, String PHONE_E, Integer PASSWORD_E, Integer ACTIVE_E, String LAST_QUERY, String CREATED_AT, String MODIFIED_AT) {
        super();
        this.NAME = NAME;
        this.POSITION = POSITION;
        this.ACTIVE = ACTIVE;
        this.PHONE_T = PHONE_T;
        this.PASSWORD_T = PASSWORD_T;
        this.ACTIVE_T = ACTIVE_T;
        this.PHONE_E = PHONE_E;
        this.PASSWORD_E = PASSWORD_E;
        this.ACTIVE_E = ACTIVE_E;
        this.LAST_QUERY = LAST_QUERY;
        this.CREATED_AT = CREATED_AT;
        this.MODIFIED_AT = MODIFIED_AT;
    }



    public Integer getACTIVE_T() {
        return ACTIVE_T;
    }

    public void setACTIVE_T(Integer ACTIVE_T) {
        this.ACTIVE_T = ACTIVE_T;
    }

    public Integer getACTIVE_E() {
        return ACTIVE_E;
    }

    public void setACTIVE_E(Integer ACTIVE_E) {
        this.ACTIVE_E = ACTIVE_E;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Integer getPOSITION() {
        return POSITION;
    }

    public void setPOSITION(Integer POISTION) {
        this.POSITION = POISTION;
    }

    public Integer getACTIVE() {
        return ACTIVE;
    }

    public void setACTIVE(Integer ACTIVE) {
        this.ACTIVE = ACTIVE;
    }

    public String getPHONE_T() {
        return PHONE_T;
    }

    public void setPHONE_T(String PHONE_T) {
        this.PHONE_T = PHONE_T;
    }

    public String getPHONE_E() {
        return PHONE_E;
    }

    public void setPHONE_E(String PHONE_E) {
        this.PHONE_E = PHONE_E;
    }

    public String getLAST_QUERY() {
        return LAST_QUERY;
    }

    public void setLAST_QUERY(String LAST_QUERY) {
        this.LAST_QUERY = LAST_QUERY;
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

    public Integer getPASSWORD_T() {
        return PASSWORD_T;
    }

    public void setPASSWORD_T(Integer PASSWORD_T) {
        this.PASSWORD_T = PASSWORD_T;
    }

    public Integer getPASSWORD_E() {
        return PASSWORD_E;
    }

    public void setPASSWORD_E(Integer PASSWORD_E) {
        this.PASSWORD_E = PASSWORD_E;
    }

    public static List<ObservationPoints> getItems() {
        return new Select()
                .from(ObservationPoints.class)
                .execute();
    }

    public static List<String> getObjectNames() {

        List<ObservationPoints> temp = new Select(new String[]{"Id, NAME"})
                .from(ObservationPoints.class)
                .orderBy("Name ASC")
                .execute();
        List<String> res = new LinkedList<String>();

        for (ObservationPoints item : temp) {
            res.add(item.getNAME());
        }

        return res;
    }

    public static void updateObject(ObservationPoints newObj, String oldName) {
        new Update(ObservationPoints.class)
                .set("NAME = ?, PHONE_T = ?, PASSWORD_T = ?, ACTIVE_T = ?, PHONE_E = ?, PASSWORD_E = ?, ACTIVE_E = ?",
                        newObj.getNAME(),
                        newObj.getPHONE_T(),
                        newObj.getPASSWORD_T(),
                        newObj.getACTIVE_T(),
                        newObj.getPHONE_E(),
                        newObj.getPASSWORD_E(),
                        newObj.getACTIVE_E()).where("NAME = ?", oldName).execute();
    }

    public static ObservationPoints findByName(String name) {
        return new Select()
                .from(ObservationPoints.class)
                .where("NAME = ?", name)
                .executeSingle();
    }

    public static void deleteByName(String name) {
        new Delete().from(ObservationPoints.class).where("NAME = ?", name).execute();
    }

    public static List<ObservationPoints> getAllActiveTPoints(){
        return new Select().
                from(ObservationPoints.class)
                .where("ACTIVE_T = ?", 1)
                .execute();
    }

    public static void deleteAll() {

    }
}
