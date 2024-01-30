package com.mindex.challenge.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplIntegrationTest {
    private String reportingStructureUrl;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    private static final String DATASTORE_LOCATION = "/test_employee_database.json";

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        InputStream inputStream = this.getClass().getResourceAsStream(DATASTORE_LOCATION);

        Employee[] employees = null;

        try {
            employees = objectMapper.readValue(inputStream, Employee[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Employee employee : employees) {
            employeeRepository.insert(employee);
        }

        reportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{employeeId}";
    }

    @Test
    public void testCalculateReportingStructure() {
        String expectedEmployeeId = "478df7de-3931-48dc-a077-7702ce518f7e";
        ReportingStructure expectedReportingStructure = new ReportingStructure(expectedEmployeeId, 8);

        ReportingStructure actualReportingStructure = restTemplate.getForObject(
                reportingStructureUrl,
                ReportingStructure.class,
                expectedEmployeeId
            );

        assertEquals(expectedEmployeeId, actualReportingStructure.getEmployeeId());
        assertEquals(8, actualReportingStructure.getNumberOfReports());
        assertEquals(expectedReportingStructure, actualReportingStructure);
    }
}
