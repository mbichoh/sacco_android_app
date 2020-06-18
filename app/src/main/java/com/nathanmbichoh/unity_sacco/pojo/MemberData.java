package com.nathanmbichoh.unity_sacco.pojo;

public class MemberData {

    private int member_id;
    private String member_name, member_national_id, member_phone;

    public MemberData(int member_id, String member_name, String member_national_id, String member_phone) {
        this.member_id = member_id;
        this.member_name = member_name;
        this.member_national_id = member_national_id;
        this.member_phone = member_phone;
    }

    public int getMember_id() {
        return member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public String getMember_national_id() {
        return member_national_id;
    }

    public String getMember_phone() {
        return member_phone;
    }
}
