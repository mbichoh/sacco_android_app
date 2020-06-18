package com.nathanmbichoh.unity_sacco.pojo;

public class PaymentData {

    private String payment_id, payment_name, payment_amount, payment_date;

    public PaymentData(String payment_id, String payment_name, String payment_amount, String payment_date) {
        this.payment_id = payment_id;
        this.payment_name = payment_name;
        this.payment_amount = payment_amount;
        this.payment_date = payment_date;
    }

    public String getPaymentId() {
        return payment_id;
    }

    public String getPayment_name() {
        return payment_name;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public String getPayment_date() {
        return payment_date;
    }
}
