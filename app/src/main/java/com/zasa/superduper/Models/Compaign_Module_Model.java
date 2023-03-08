package com.zasa.superduper.Models;

public class Compaign_Module_Model {
    int moduledPic;
    private String module_name;
    private String image_url;

    public Compaign_Module_Model(int moduledPic, String module_name) {
        this.moduledPic = moduledPic;
        this.module_name = module_name;
    }

    public int getModuledPic() {
        return moduledPic;
    }

    public void setModuledPic(int moduledPic) {
        this.moduledPic = moduledPic;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
