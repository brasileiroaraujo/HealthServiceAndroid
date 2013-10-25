package com.signove.health.structures;

import java.util.Date;

public class HealthData implements Comparable<HealthData>{
    private String device;
    private Double heartbeat;
    private Date date;
    private Integer id;
    
    public HealthData(String device, Double heartbeat, Date date, Integer id) {
        this.device = device;
        this.heartbeat = heartbeat;
        this.date = date;
        this.id = id;
    }
    
    public HealthData(String device, Double heartbeat, Date date) {
        super();
        this.device = device;
        this.heartbeat = heartbeat;
        this.date = date;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Double getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(Double heartbeat) {
        this.heartbeat = heartbeat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public int compareTo(HealthData another) {
        if (this.getDate().before(another.getDate())) {
            return -1;
        }
        if (this.getDate().after(another.getDate())) {
            return 1;
        }
        return 0;
    }
}
