package com.gjzg.bean;


public class CountryBean {
    String provinceID;
    String cityID;
    String districtID;
    String locCity;
    String locProvince;
    String locDistrict;
    String province;//省
    String city;//市
    String district;//区

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocCity() {
        return locCity;
    }

    public void setLocCity(String locCity) {
        this.locCity = locCity;
    }

    public String getLocProvince() {
        return locProvince;
    }

    public void setLocProvince(String locProvince) {
        this.locProvince = locProvince;
    }

    public String getLocDistrict() {
        return locDistrict;
    }

    public void setLocDistrict(String locDistrict) {
        this.locDistrict = locDistrict;
    }

    public String getAddress() {
        return  province +" " + city + " " + district;
    }
}
