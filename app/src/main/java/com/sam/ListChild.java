package com.sam;

/**
 * Created by MOHD IMTIAZ on 20-Mar-17.
 */

public class ListChild {
    private String date;
    private String p1,p2,p3,p4,p5,p6,p7;
    private String total;

    public ListChild() {
    }

    public ListChild(String date, String p1, String p2, String p3, String p4, String p5, String p6, String p7, String total) {
        this.date = date;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.p5 = p5;
        this.p6 = p6;
        this.p7 = p7;
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public String getP1() {
        return p1;
    }

    public String getP2() {
        return p2;
    }

    public String getP3() {
        return p3;
    }

    public String getP4() {
        return p4;
    }

    public String getP5() {
        return p5;
    }

    public String getP6() {
        return p6;
    }

    public String getP7() {
        return p7;
    }

    public String getTotal() {
        return total;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    public void setP3(String p3) {
        this.p3 = p3;
    }

    public void setP4(String p4) {
        this.p4 = p4;
    }

    public void setP5(String p5) {
        this.p5 = p5;
    }

    public void setP6(String p6) {
        this.p6 = p6;
    }

    public void setP7(String p7) {
        this.p7 = p7;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
