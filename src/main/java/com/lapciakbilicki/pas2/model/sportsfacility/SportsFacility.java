package com.lapciakbilicki.pas2.model.sportsfacility;

import com.lapciakbilicki.pas2.model.IsIdentified;
import com.lapciakbilicki.pas2.validator.Unique;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Unique
public abstract class SportsFacility implements IsIdentified {

    private String id;

    @NotNull
    @DecimalMin("10")
    private double pricePerHours;

    @NotNull
    private boolean access;

    @NotNull
    @Valid
    private Field field;

    @NotNull
    @Size(min = 3, max = 30)
    private String name;

    private String type = this.getClass().getSimpleName();

    public SportsFacility() {

    }

    public SportsFacility(String id, double pricePerHours, boolean access, Field field, String name) {
        this.id = id;
        this.pricePerHours = pricePerHours;
        this.access = access;
        this.field = field;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPricePerHours() {
        return pricePerHours;
    }

    public void setPricePerHours(double pricePerHours) {
        this.pricePerHours = pricePerHours;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SportsFacility that = (SportsFacility) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void copyAttributionsWithoutId(SportsFacility sportsFacility) {
        this.pricePerHours = sportsFacility.pricePerHours;
        this.access = sportsFacility.access;
        this.field = sportsFacility.field;
        this.name = sportsFacility.name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
