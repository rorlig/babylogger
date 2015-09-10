package com.rorlig.babyapp.parse_dao;

/**
 * @author gaurav gupta
 */

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONObject;

@ParseClassName("WeightToAge")
public class WeightToAge extends ParseObject {

//    private double AgeToMonth;
//    private float P10;
//    private float P25;
//
//    private float P50;
//
//    private float P75;
//
//    private float P90;
//
//    private float P95;
//
//    private



    public WeightToAge(){

    }

    public WeightToAge(double ageToMonth,
                       double p10,
                       double p25,
                       double p50,
                       double p75,
                       double p95,
                       int sex){
        setAgeToMonth(ageToMonth);
        setP10(p10);
        setP25(p25);
        setP50(p50);
        setP75(p75);
        setP95(p95);
        setSex(sex);
    }

    public double getAgeToMonth() {
        return getDouble("AgeMonths");
    }

    public void setAgeToMonth(double ageToMonth) {
            put("AgeMonths", ageToMonth);
    }

    public double getP10() {
        return getDouble("P10");
    }

    public void setP10(double p10) {
        put("P10", p10);
    }

    public double getP25() {
        return getDouble("P25");
    }

    public void setP25(double p25) {
        put("P25", p25);
    }

    public double getP50() {
        return getDouble("P50");
    }

    public void setP50(double p50) {
        put("P50", p50);
    }

    public double getP75() {
        return getDouble("P75");
    }

    public void setP75(double p75) {
        put("P75", p75);

    }

    public double getP90() {
        return getDouble("P90");

    }

    public void setP90(double p90) {
        put("P90", p90);
    }

    public double getP95() {
        return getDouble("P95");
    }

    public void setP95(double p95) {
        put("P95", p95);
    }

    public void setSex(int sex) {
        put("Sex", sex);
    }

    public int getSex() {
       return getInt("Sex");
    }

    @Override
    public String toString() {
        return "WeightToAge{" +
                "AgeToMonth=" + getAgeToMonth() +
                ", P10=" + getP10() +
                ", P25=" + getP25() +
                ", P50=" + getP50() +
                ", P75=" + getP75() +
                ", P90=" + getP90() +
                ", P95=" + getP95() +
                ", Sex=" + getSex() +
                "} " + super.toString();
    }
}
