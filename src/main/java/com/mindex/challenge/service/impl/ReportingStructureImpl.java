package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Fetching reporting structure for employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(countReports(employee));
        return reportingStructure;
    }


    private int countReports(Employee employee) {
        LOG.debug("Counting reports for {}", employee.getPosition());
        int numberOfReports = 0;

        if (employee.getDirectReports() != null) {
            for (Employee directReport : employee.getDirectReports()) {
                LOG.debug("{} is a direct report of {}", directReport.getPosition(), employee.getPosition());
                numberOfReports += 1 + countReports(directReport);

            }
        }
        LOG.debug("Found {} reports for {}", numberOfReports, employee.getPosition());
        return numberOfReports;
    }
}
