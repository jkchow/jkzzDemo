package com.maimaicn.goods.domain;

public class UpdateSkuPojo {
    private Integer skuId;
    private Integer stock;
    private Double b2cp;//b2c价格
    private Double b2bp;//b2b价格
    private Double rd;//平台红包抵扣
    private Double srd;//商家红包抵扣
    private Double sr;//分享返佣

    public UpdateSkuPojo(Integer skuId, Integer stock, Double b2cp, Double b2bp, Double rd, Double srd, Double sr) {
        this.skuId = skuId;
        this.stock = stock;
        this.b2cp = b2cp;
        this.b2bp = b2bp;
        this.rd = rd;
        this.srd = srd;
        this.sr = sr;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getB2cp() {
        return b2cp;
    }

    public void setB2cp(Double b2cp) {
        this.b2cp = b2cp;
    }

    public Double getB2bp() {
        return b2bp;
    }

    public void setB2bp(Double b2bp) {
        this.b2bp = b2bp;
    }

    public Double getRd() {
        return rd;
    }

    public void setRd(Double rd) {
        this.rd = rd;
    }

    public Double getSrd() {
        return srd;
    }

    public void setSrd(Double srd) {
        this.srd = srd;
    }

    public Double getSr() {
        return sr;
    }

    public void setSr(Double sr) {
        this.sr = sr;
    }
}
