package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingServiceImplTest {

    private String reportingStructureUrl;

    @Autowired
    private ReportingStructureService reportingStructureService;
    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{id}";
    }

    @Test
    public void testReportingStructure() {
        Employee director = new Employee();
        director.setEmployeeId(UUID.randomUUID().toString());
        director.setPosition("Director of Operations");
        director.setDepartment("Operations");

        List<Employee> directorReports = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Employee manager = generateEmployeeWithThreeReports();
            directorReports.add(manager);
        }

        director.setDirectReports(directorReports);
        employeeService.create(director);


        ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, director.getEmployeeId()).getBody();
        assertNotNull(reportingStructure);
        // 1 director has 3 managers directly reporting
        // each manager has 3 support engineers directly reporting
        // 9 support engineers, 3 managers = 12 direct reports for this director
        assertEquals(12, reportingStructure.getNumberOfReports());
    }

    private Employee generateEmployeeWithThreeReports() {
        List<Employee> directReports = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Employee employee = new Employee();
            employee.setEmployeeId(UUID.randomUUID().toString());
            employee.setPosition("Support Engineer II");
            employee.setDepartment("Operations");
            employeeService.create(employee);
            directReports.add(employee);
        }

        Employee manager = new Employee();
        manager.setEmployeeId(UUID.randomUUID().toString());
        manager.setDepartment("Operations");
        manager.setPosition("Support Manager I");
        manager.setDirectReports(directReports);
        employeeService.create(manager);
        return manager;
    }
}
