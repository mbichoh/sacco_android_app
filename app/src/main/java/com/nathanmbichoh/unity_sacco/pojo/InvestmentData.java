package com.nathanmbichoh.unity_sacco.pojo;

public class InvestmentData {

    private String investment_id, investment_date, investment_maturity_date, investment_amount, investment_rate, investment_status;

    public InvestmentData(String investment_id, String investment_date, String investment_maturity_date, String investment_amount, String investment_rate, String investment_status) {
        this.investment_id = investment_id;
        this.investment_date = investment_date;
        this.investment_maturity_date = investment_maturity_date;
        this.investment_amount = investment_amount;
        this.investment_rate = investment_rate;
        this.investment_status = investment_status;
    }

    public String getInvestmentId() {
        return investment_id;
    }

    public String getInvestment_date() {
        return investment_date;
    }

    public String getInvestment_maturity_date() {
        return investment_maturity_date;
    }

    public String getInvestment_amount() {
        return investment_amount;
    }

    public String getInvestment_rate() {
        return investment_rate;
    }

    public String getInvestment_status() {
        return investment_status;
    }

}
