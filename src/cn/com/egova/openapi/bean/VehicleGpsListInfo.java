package cn.com.egova.openapi.bean;

import java.util.List;

public class VehicleGpsListInfo {

    private String c;
    private String sim;
    private List<VehicleGpsObj> dlist;

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public List<VehicleGpsObj> getDlist() {
        return dlist;
    }

    public void setDlist(List<VehicleGpsObj> dlist) {
        this.dlist = dlist;
    }


}
