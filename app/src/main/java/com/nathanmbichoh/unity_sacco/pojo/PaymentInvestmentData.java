package com.nathanmbichoh.unity_sacco.pojo;

public class PaymentInvestmentData {

    private String investment_date, investment_maturity_date, investment_amount, investment_rate, investment_status, investment_refunds, investment_balance, investment_interest, member_bank_name, member_bank_account_no;

    public PaymentInvestmentData(String investment_date, String investment_maturity_date, String investment_amount, String investment_rate, String investment_status, String investment_refunds, String investment_balance, String investment_interest, String member_bank_name, String member_bank_account_no) {
        this.investment_date = investment_date;
        this.investment_maturity_date = investment_maturity_date;
        this.investment_amount = investment_amount;
        this.investment_rate = investment_rate;
        this.investment_status = investment_status;
        this.investment_refunds = investment_refunds;
        this.investment_balance = investment_balance;
        this.investment_interest = investment_interest;
        this.member_bank_name = member_bank_name;
        this.member_bank_account_no = member_bank_account_no;
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

    public String getInvestment_refunds() {
        return investment_refunds;
    }

    public String getInvestment_balance() {
        return investment_balance;
    }

    public String getInvestment_interest() {
        return investment_interest;
    }

    public String getMember_bank_name() {
        return member_bank_name;
    }

    public String getMember_bank_account_no() {
        return member_bank_account_no;
    }
}
