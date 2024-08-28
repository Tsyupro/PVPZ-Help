package com.example.ukrposhtaboxing;

public class BoxingInfo {
    private String trackingNumber;
    private String locality;
    private String name;
    private double postpaid;
    private double delivery;
    private double commission;
    private boolean isSelected = false;
    private String additionalInfo = ""; // Додатковий опис

    public BoxingInfo(String trackingNumber, String locality, String name, double postpaid, double delivery, double commission) {
        this.trackingNumber = trackingNumber;
        this.locality = locality;
        this.name = name;
        this.postpaid = postpaid;
        this.delivery = delivery;
        this.commission = commission;
    }

    // Геттери і сеттери для всіх полів

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPostpaid() {
        return postpaid;
    }

    public void setPostpaid(double postpaid) {
        this.postpaid = postpaid;
    }

    public double getDelivery() {
        return delivery;
    }

    public void setDelivery(double delivery) {
        this.delivery = delivery;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    @Override
    public String toString() {
        return "BoxingInfo{" +
                "trackingNumber='" + trackingNumber + '\'' +
                ", locality='" + locality + '\'' +
                ", name='" + name + '\'' +
                ", postpaid=" + postpaid +
                ", delivery=" + delivery +
                ", commission=" + commission +
                ", isSelected=" + isSelected +
                ", additionalInfo='" + additionalInfo + '\'' +
                '}';
    }
}