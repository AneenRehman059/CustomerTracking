package com.zasa.superduper.Models;
public class Routes_Model {
    private String assign_area;
    private String route_id;
//    private String assign_shop;
//    private String assign_line;
//    private String last_purchase;


    public Routes_Model(String assign_area) {
        this.assign_area = assign_area;
    }

    public String getAssign_area() {
        return assign_area;
    }

    public void setAssign_area(String assign_area) {
        this.assign_area = assign_area;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }
}
