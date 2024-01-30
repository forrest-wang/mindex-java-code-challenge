package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

public class ReportingStructureServiceImplTests {
    private final EmployeeRepository mockEmployeeRepository = mock(EmployeeRepository.class);
    private final ReportingStructureService reportingStructureService =
            new ReportingStructureServiceImpl(mockEmployeeRepository);

    private final Employee testEmployee3 = new Employee(
            UUID.randomUUID().toString(),
            "Edelgard",
            "von Hresvelg",
            "Software Engineer III",
            "Engineering",
            null
    );
    private final Employee testEmployee4 = new Employee(
            UUID.randomUUID().toString(),
            "Claude",
            "von Regan",
            "Software Engineer II",
            "Engineering",
            null
    );
    private final Employee testEmployee5 = new Employee(
            UUID.randomUUID().toString(),
            "Dimitri",
            "Blaiddyd",
            "Software Engineer I",
            "Engineering",
            null
    );

    private final Employee testEmployee2 = new Employee(
            UUID.randomUUID().toString(),
            "Byleth",
            "Eisner",
            "Engineering Manager",
            "Engineering",
            Arrays.asList(testEmployee3, testEmployee4, testEmployee5)
    );
    private final Employee testEmployee1 = new Employee(
            UUID.randomUUID().toString(),
            "Rhea",
            "Seiros",
            "Director of Engineering",
            "Engineering",
            Collections.singletonList(testEmployee2)
    );

    @Before
    public void setup() {
        when(mockEmployeeRepository.findByEmployeeId(testEmployee1.getEmployeeId())).thenReturn(testEmployee1);
        when(mockEmployeeRepository.findByEmployeeId(testEmployee2.getEmployeeId())).thenReturn(testEmployee2);
        when(mockEmployeeRepository.findByEmployeeId(testEmployee3.getEmployeeId())).thenReturn(testEmployee3);
        when(mockEmployeeRepository.findByEmployeeId(testEmployee4.getEmployeeId())).thenReturn(testEmployee4);
        when(mockEmployeeRepository.findByEmployeeId(testEmployee5.getEmployeeId())).thenReturn(testEmployee5);
    }


    @Test
    public void testCalculateReportingStructure() {
        String testEmployee1Id = testEmployee1.getEmployeeId();
        ReportingStructure expectedReportingStructure = new ReportingStructure(testEmployee1Id, 4);

        ReportingStructure actualReportingStructure = reportingStructureService.calculate(testEmployee1Id);

        assertEquals(expectedReportingStructure, actualReportingStructure);
    }
}
