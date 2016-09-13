package com.example.xd720p.sensorcontroller_09082016.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by xd720p on 12.09.16.
 */

@Table(name = "Electricity")
public class Electricity extends Model {
    @Column(name = "OBSERVATION_POINT")
    private Integer OBSERVATION_POINT;

    @Column(name = "CONSUMED_1")
    private Integer CONSUMED_1;

    @Column(name = "CONSUMED_2")
    private Integer CONSUMED_2;

    @Column(name = "CONSUMED_3")
    private Integer CONSUMED_3;

    @Column(name = "VOLTAGE_1")
    private Integer VOLTAGE_1;

    @Column(name = "VOLTAGE_2")
    private Integer VOLTAGE_2;

    @Column(name = "VOLTAGE_3")
    private Integer VOLTAGE_3;

    @Column(name = "DATE_TIME")
    private String DATE_TIME;

    public  Electricity() {
        super();
    }

    public Electricity(Integer OBSERVATION_POINT, Integer CONSUMED_1, Integer CONSUMED_2, Integer CONSUMED_3, Integer VOLTAGE_1, Integer VOLTAGE_2, Integer VOLTAGE_3, String DATE_TIME) {
        super();
        this.OBSERVATION_POINT = OBSERVATION_POINT;
        this.CONSUMED_1 = CONSUMED_1;
        this.CONSUMED_2 = CONSUMED_2;
        this.CONSUMED_3 = CONSUMED_3;
        this.VOLTAGE_1 = VOLTAGE_1;
        this.VOLTAGE_2 = VOLTAGE_2;
        this.VOLTAGE_3 = VOLTAGE_3;
        this.DATE_TIME = DATE_TIME;
    }

    public Integer getOBSERVATION_POINT() {
        return OBSERVATION_POINT;
    }

    public void setOBSERVATION_POINT(Integer OBSERVATION_POINT) {
        this.OBSERVATION_POINT = OBSERVATION_POINT;
    }

    public Integer getCONSUMED_1() {
        return CONSUMED_1;
    }

    public void setCONSUMED_1(Integer CONSUMED_1) {
        this.CONSUMED_1 = CONSUMED_1;
    }

    public Integer getCONSUMED_2() {
        return CONSUMED_2;
    }

    public void setCONSUMED_2(Integer CONSUMED_2) {
        this.CONSUMED_2 = CONSUMED_2;
    }

    public Integer getCONSUMED_3() {
        return CONSUMED_3;
    }

    public void setCONSUMED_3(Integer CONSUMED_3) {
        this.CONSUMED_3 = CONSUMED_3;
    }

    public Integer getVOLTAGE_1() {
        return VOLTAGE_1;
    }

    public void setVOLTAGE_1(Integer VOLTAGE_1) {
        this.VOLTAGE_1 = VOLTAGE_1;
    }

    public Integer getVOLTAGE_2() {
        return VOLTAGE_2;
    }

    public void setVOLTAGE_2(Integer VOLTAGE_2) {
        this.VOLTAGE_2 = VOLTAGE_2;
    }

    public Integer getVOLTAGE_3() {
        return VOLTAGE_3;
    }

    public void setVOLTAGE_3(Integer VOLTAGE_3) {
        this.VOLTAGE_3 = VOLTAGE_3;
    }

    public String getDATE_TIME() {
        return DATE_TIME;
    }

    public void setDATE_TIME(String DATE_TIME) {
        this.DATE_TIME = DATE_TIME;
    }
}
