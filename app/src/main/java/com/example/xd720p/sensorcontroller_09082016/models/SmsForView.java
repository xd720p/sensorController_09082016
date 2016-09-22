package com.example.xd720p.sensorcontroller_09082016.models;

/**
 * Created by xd720p on 20.09.16.
 */
public class SmsForView {
    private String smsName;

    private String temperature;



    private String sensorName;

    public SmsForView(final String smsName, final String temperature, final String sensorName) {
        this.smsName = smsName;
        this.temperature = temperature;
        this.sensorName = sensorName;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSmsName() {
        return smsName;
    }

    public void setSmsName(final String smsName) {
        this.smsName = smsName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(final String temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "Sms{" +
                "smsName='" + smsName + '\'' +
                ", temperature='" + temperature + '\'' +
                ", sensorName=" + sensorName + '\'' +
                '}';
    }


}
