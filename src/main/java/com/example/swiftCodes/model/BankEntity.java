package com.example.swiftCodes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "banks")
public class BankEntity {
    @Id
    private String id;

    private String bankName;
    private String swiftCode;
    private String address;
    private String countryISO2;
    private String countryName;
    private boolean isHeadquarter;
    @Field("branches")
    private List<Branch> branches = new ArrayList<>();

    public BankEntity() {
    }

    public BankEntity(String bankName, String swiftCode, String address, String countryISO2, String countryName, boolean isHeadquarter) {
        this.bankName = bankName;
        this.swiftCode = swiftCode;
        this.address = address;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.isHeadquarter = isHeadquarter;
    }

    public String getId() {
        return this.id;
    }

    public String getBankName() {
        return this.bankName;
    }

    public String getSwiftCode() {
        return this.swiftCode;
    }

    public String getAddress() {
        return this.address;
    }

    public String getCountryISO2() {
        return this.countryISO2;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public boolean isHeadquarter() {
        return this.isHeadquarter;
    }

    public List<Branch> getBranches() {
        return this.branches;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCountryISO2(String countryISO2) {
        this.countryISO2 = countryISO2;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setHeadquarter(boolean isHeadquarter) {
        this.isHeadquarter = isHeadquarter;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof BankEntity)) return false;
        final BankEntity other = (BankEntity) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$bankName = this.getBankName();
        final Object other$bankName = other.getBankName();
        if (this$bankName == null ? other$bankName != null : !this$bankName.equals(other$bankName)) return false;
        final Object this$swiftCode = this.getSwiftCode();
        final Object other$swiftCode = other.getSwiftCode();
        if (this$swiftCode == null ? other$swiftCode != null : !this$swiftCode.equals(other$swiftCode)) return false;
        final Object this$address = this.getAddress();
        final Object other$address = other.getAddress();
        if (this$address == null ? other$address != null : !this$address.equals(other$address)) return false;
        final Object this$countryISO2 = this.getCountryISO2();
        final Object other$countryISO2 = other.getCountryISO2();
        if (this$countryISO2 == null ? other$countryISO2 != null : !this$countryISO2.equals(other$countryISO2))
            return false;
        final Object this$countryName = this.getCountryName();
        final Object other$countryName = other.getCountryName();
        if (this$countryName == null ? other$countryName != null : !this$countryName.equals(other$countryName))
            return false;
        if (this.isHeadquarter() != other.isHeadquarter()) return false;
        final Object this$branches = this.getBranches();
        final Object other$branches = other.getBranches();
        if (this$branches == null ? other$branches != null : !this$branches.equals(other$branches)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BankEntity;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $bankName = this.getBankName();
        result = result * PRIME + ($bankName == null ? 43 : $bankName.hashCode());
        final Object $swiftCode = this.getSwiftCode();
        result = result * PRIME + ($swiftCode == null ? 43 : $swiftCode.hashCode());
        final Object $address = this.getAddress();
        result = result * PRIME + ($address == null ? 43 : $address.hashCode());
        final Object $countryISO2 = this.getCountryISO2();
        result = result * PRIME + ($countryISO2 == null ? 43 : $countryISO2.hashCode());
        final Object $countryName = this.getCountryName();
        result = result * PRIME + ($countryName == null ? 43 : $countryName.hashCode());
        result = result * PRIME + (this.isHeadquarter() ? 79 : 97);
        final Object $branches = this.getBranches();
        result = result * PRIME + ($branches == null ? 43 : $branches.hashCode());
        return result;
    }

    public String toString() {
        return "BankEntity(id=" + this.getId() + ", bankName=" + this.getBankName() + ", swiftCode=" + this.getSwiftCode() + ", address=" + this.getAddress() + ", countryISO2=" + this.getCountryISO2() + ", countryName=" + this.getCountryName() + ", isHeadquarter=" + this.isHeadquarter() + ", branches=" + this.getBranches() + ")";
    }
}
