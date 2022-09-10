package com.mindex.challenge.data;

import java.time.Instant;

public class Compensation {

    private Employee employee;
    private Instant effectiveTime;
    private int salary; // us dollars, annual

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Instant getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Instant effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}
