package com.example.xd720p.sensorcontroller_09082016.models;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by xd720p on 12.09.16.
 */

@Table(name = "Temperature")
public class Temperature extends Model {

    @Column(name = "OBSERVATION_POINT")
    private Integer OBSERVATION_POINT;

    @Column(name = "SENSOR")
    private Integer SENSOR;

    @Column(name = "VALUE")
    private Integer VALUE;

    @Column(name = "DATE_TIME")
    private String DATE_TIME;

    public Temperature() {
        super();
    }

    public Temperature(Integer OBSERVATION_POINT, Integer SENSOR, Integer VALUE, String DATE_TIME) {
        super();
        this.OBSERVATION_POINT = OBSERVATION_POINT;
        this.SENSOR = SENSOR;
        this.VALUE = VALUE;
        this.DATE_TIME = DATE_TIME;
    }

    public Integer getOBSERVATION_POINT() {
        return OBSERVATION_POINT;
    }

    public void setOBSERVATION_POINT(Integer OBSERVATION_POINT) {
        this.OBSERVATION_POINT = OBSERVATION_POINT;
    }

    public Integer getSENSOR() {
        return SENSOR;
    }

    public void setSENSOR(Integer SENSOR) {
        this.SENSOR = SENSOR;
    }

    public Integer getVALUE() {
        return VALUE;
    }

    public void setVALUE(Integer VALUE) {
        this.VALUE = VALUE;
    }

    public String getDATE_TIME() {
        return DATE_TIME;
    }

    public void setDATE_TIME(String DATE_TIME) {
        this.DATE_TIME = DATE_TIME;
    }
}
