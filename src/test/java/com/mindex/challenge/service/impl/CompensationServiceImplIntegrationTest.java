package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplIntegrationTest {
    private String compensationCreateUrl;

    private String findCompensationUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationCreateUrl = "http://localhost:" + port + "/compensation";
        findCompensationUrl = "http://localhost:" + port + "/compensation/{employeeId}";
    }

    @Test
    public void testCreateAndRead() {
        String testEmployeeId = UUID.randomUUID().toString();
        Compensation testCompensation =
                new Compensation(testEmployeeId, 110000, Instant.now().truncatedTo(ChronoUnit.MILLIS));

        //test create
        Compensation createdCompensationRecord = restTemplate.postForObject(
           compensationCreateUrl, testCompensation, Compensation.class
        );

        assertNotNull(createdCompensationRecord.getEmployeeId());
        assertEquals(testCompensation, createdCompensationRecord);

        //test read
        Compensation readCompensationRecord = restTemplate.getForObject(
           findCompensationUrl, Compensation.class, testEmployeeId
        );

        assertEquals(testCompensation, readCompensationRecord);
    }
}
