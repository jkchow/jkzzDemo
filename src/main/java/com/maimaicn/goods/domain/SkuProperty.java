package com.maimaicn.goods.domain;

/**
 * @Description skuPropertyJson实体，
 * @Author 张进
 * @Time 2018/11/30 15:03
 */
public class SkuProperty {
    private Integer pId;
    private String pn;
    private Integer vId;
    private String vn;
    private String r;//备注
    private String img;//sku图片

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public Integer getvId() {
        return vId;
    }

    public void setvId(Integer vId) {
        this.vId = vId;
    }

    public String getVn() {
        return vn;
    }

    public void setVn(String vn) {
        this.vn = vn;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
