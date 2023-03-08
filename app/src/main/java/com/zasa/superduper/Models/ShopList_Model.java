package com.zasa.superduper.Models;

public class ShopList_Model {
    private String shop_name;
    private String shop_description;

    public ShopList_Model(String shop_name, String shop_description) {
        this.shop_name = shop_name;
        this.shop_description = shop_description;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_description() {
        return shop_description;
    }

    public void setShop_description(String shop_description) {
        this.shop_description = shop_description;
    }
}
