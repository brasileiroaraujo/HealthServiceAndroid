package br.ufcg.embedded.health.structures;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.text.NoCopySpan.Concrete;
import br.ufcg.embedded.health.R;

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
    
    /**
     * Method to analyse blood pressure. (Diretriz de hipertensão da sociedade brasileira de cardiologia)
     *http://publicacoes.cardiol.br/consenso/2010/Diretriz_hipertensao_associados.pdf
     * 
     * @param systolic
     * @param diastolic
     * @return classification of blood pressure
     */
    public String analyzePressure(Context frame) {
        ClassificationBloodPressure resultSys;
        ClassificationBloodPressure resultDis;
        ClassificationBloodPressure result;
        //Systolic classification
        if ((systolic < 90)) {
            resultSys = ClassificationBloodPressure.LOW_PRESSURE;
        } else if (systolic >= 130 && systolic <= 139) {
            resultSys = ClassificationBloodPressure.PRE_HYPERTENSION;
        } else if (systolic >= 140 && systolic <= 159) {
            resultSys = ClassificationBloodPressure.HIGH_PRESSURE_1;
        } else if (systolic >= 160) {
            resultSys = ClassificationBloodPressure.HIGH_PRESSURE_2;
        } else {
            resultSys = ClassificationBloodPressure.NORMAL;
        }
        
        //Diastolic classification
        if (diastolic < 60) {
            resultDis = ClassificationBloodPressure.LOW_PRESSURE;
        } else if (diastolic >= 85 && diastolic <= 89) {
            resultDis = ClassificationBloodPressure.PRE_HYPERTENSION;
        } else if (diastolic >= 90 && diastolic <= 99) {
            resultDis = ClassificationBloodPressure.HIGH_PRESSURE_1;
        } else if (diastolic >= 100) {
            resultDis = ClassificationBloodPressure.HIGH_PRESSURE_2;
        } else {
            resultDis = ClassificationBloodPressure.NORMAL;
        }
        
        if(resultDis.getValue() > resultSys.getValue()){
            result = resultDis;
        }else{
            result = resultSys;
        }
        
        if(result.equals(ClassificationBloodPressure.LOW_PRESSURE)){
            return frame.getResources().getString(R.string.pressure_low_blood);
        }else if(result.equals(ClassificationBloodPressure.PRE_HYPERTENSION)){
            return frame.getResources().getString(R.string.pressure_prehypertension);
        }else if(result.equals(ClassificationBloodPressure.HIGH_PRESSURE_1)){
            return frame.getResources().getString(R.string.pressure_high_blood_stage1);
        }else if(result.equals(ClassificationBloodPressure.HIGH_PRESSURE_2)){
            return frame.getResources().getString(R.string.pressure_high_blood_stage2);
        }else{
            return frame.getResources().getString(R.string.pressure_normal);
        }
    }
}
