package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    @Autowired
    public ReportingStructureServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public ReportingStructure calculate(String employeeId) {
        LOG.debug("Calculating reporting structure for employee id [{}]", employeeId);
        ReportingStructure reportingStructure = new ReportingStructure();

        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        List<Employee> currentReports = employee.getDirectReports();
        int numReports = determineNumReports(currentReports);

        reportingStructure.setEmployeeId(employeeId);
        reportingStructure.setNumberOfReports(numReports);

        return reportingStructure;
    }

    private int determineNumReports(List<Employee> directReports) {
        if(directReports == null) {
            return 0;
        }

        AtomicInteger numReports = new AtomicInteger(directReports.size());

        directReports.forEach(employee -> {
            Employee fullRecord = employeeRepository.findByEmployeeId(employee.getEmployeeId());
            if(fullRecord.getDirectReports() != null) {
                numReports.addAndGet(determineNumReports(fullRecord.getDirectReports()));
            }
        });

        return numReports.get();
    }

}
