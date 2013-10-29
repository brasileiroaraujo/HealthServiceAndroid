package br.ufcg.embedded.health.structures;

import java.util.Date;

public class HealthData implements Comparable<HealthData> {
    private String device;
    private Double systolic;
    private Double diastolic;
    private Double MAP;
    private Date date;
    private Integer id;

    public HealthData(String device, Double systolic, Double diastolic,
            Double map, Date date, Integer id) {
        super();
        this.device = device;
        this.systolic = systolic;
        this.diastolic = diastolic;
        MAP = map;
        this.date = date;
        this.id = id;
    }

    public HealthData(String device, Double systolic, Double diastolic,
            Double map, Date date) {
        super();
        this.device = device;
        this.systolic = systolic;
        this.diastolic = diastolic;
        MAP = map;
        this.date = date;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
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
            return 1;
        }
        if (this.getDate().after(another.getDate())) {
            return -1;
        }
        return 0;
    }

    public Double getSystolic() {
        return systolic;
    }

    public void setSystolic(Double systolic) {
        this.systolic = systolic;
    }

    public Double getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(Double diastolic) {
        this.diastolic = diastolic;
    }

    public Double getMAP() {
        return MAP;
    }

    public void setMAP(Double mAP) {
        MAP = mAP;
    }
}
