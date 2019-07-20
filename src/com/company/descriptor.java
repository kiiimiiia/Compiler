package com.company;

public class descriptor {

    String DescriptorType ;
    String Type ;
    String Address ;



    String SizeOrAdrMode ;

    public descriptor(String descriptorType, String type, String address , String SizeOradrMode) {
        DescriptorType = descriptorType;
        Type = type;
        Address = address;
        SizeOrAdrMode = SizeOradrMode;
    }

    public String getDescriptorType() {
        return DescriptorType;
    }

    public void setDescriptorType(String descriptorType) {
        DescriptorType = descriptorType;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
    public String getSizeOrAdrMode() {
        return SizeOrAdrMode;
    }

    public void setSizeOrAdrMode(String sizeOrAdrMode) {
        SizeOrAdrMode = sizeOrAdrMode;
    }
}
