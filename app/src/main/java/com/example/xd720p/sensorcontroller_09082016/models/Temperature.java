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

@Table(name = "Temperature")
public class Temperature extends Model {

    @Column(name = "OBSERVATION_POINT")
    private Long OBSERVATION_POINT;

    @Column(name = "SENSOR")
    private Long SENSOR;

    @Column(name = "VALUE")
    private Double VALUE;

    @Column(name = "DATE_TIME")
    private Long DATE_TIME;

    public Temperature() {
        super();
    }

    public Temperature(Long OBSERVATION_POINT, Long SENSOR, Double VALUE, Long DATE_TIME) {
        super();
        this.OBSERVATION_POINT = OBSERVATION_POINT;
        this.SENSOR = SENSOR;
        this.VALUE = VALUE;
        this.DATE_TIME = DATE_TIME;
    }

    public Long getOBSERVATION_POINT() {
        return OBSERVATION_POINT;
    }

    public void setOBSERVATION_POINT(Long OBSERVATION_POINT) {
        this.OBSERVATION_POINT = OBSERVATION_POINT;
    }

    public Long getSENSOR() {
        return SENSOR;
    }

    public void setSENSOR(Long SENSOR) {
        this.SENSOR = SENSOR;
    }

    public Double getVALUE() {
        return VALUE;
    }

    public void setVALUE(Double VALUE) {
        this.VALUE = VALUE;
    }

    public Long getDATE_TIME() {
        return DATE_TIME;
    }

    public void setDATE_TIME(Long DATE_TIME) {
        this.DATE_TIME = DATE_TIME;
    }

    public static Temperature getLastSensorTemp (Long sensID) {
        return new Select().from(Temperature.class).where("SENSOR = ?", sensID)
                .orderBy("DATE_TIME DESC").limit(1).executeSingle();
    }

    public static List<Temperature> getSensorTemps (Long sensID) {
        return new Select().from(Temperature.class).where("SENSOR = ?", sensID)
                .orderBy("DATE_TIME DESC").execute();
    }

    public static List<Temperature> getTempsForObjByDate(Long startDate, Long lastDate, Long objId) {
        return new Select().from(Temperature.class).where("OBSERVATION_POINT = ? AND DATE_TIME >= ? AND DATE_TIME <= ?"
        , objId, startDate-(24*60*60*1000), lastDate).orderBy("DATE_TIME ASC").execute();
    }

    public static void deleteBySensor(Long sensID) {
        new Delete().from(Temperature.class).where("SENSOR = ?", sensID).execute();
    }
    

//    public static List<ObservationPoints> getItems() {
//        return new Select()
//                .from(ObservationPoints.class)
//                .execute();
//    }
//
//    public static List<String> getSensors() {
//
//        List<ObservationPoints> temp = new Select(new String[]{"Id, SENSOR"})
//                .from(ObservationPoints.class)
//                .orderBy("Name ASC")
//                .execute();
//        List<String> res = new LinkedList<String>();
//
//        for (ObservationPoints item : temp) {
//            res.add(item.getNAME());
//        }
//
//        return res;
//    }
//
//    public static void updateObject(Temperature newObj, Long ID) {
//        new Update(ObservationPoints.class)
//                .set("OBSERVATION_POINT = ?, SENSOR = ?, VALUE = ?, DATE_TIME = ?",
//                        newObj.getOBSERVATION_POINT(),
//                        newObj.getSENSOR(),
//                        newObj.getVALUE(),
//                        newObj.getDATE_TIME())
//                        .where("ID = ?", ID).execute();
//    }
//
////    public static ObservationPoints findByName(String name) {
////        return new Select()
////                .from(ObservationPoints.class)
////                .where("NAME = ?", name)
////                .executeSingle();
////    }
//
//    public static void deleteByID(Long ID) {
//        new Delete().from(ObservationPoints.class).where("ID = ?", ID).execute();
//    }

}
