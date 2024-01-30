package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

public class CompensationServiceImplTests {
    private final CompensationRepository mockCompensationRepository = mock(CompensationRepository.class);
    private final CompensationService compensationService = new CompensationServiceImpl(mockCompensationRepository);

    private final String testEmployeeId = UUID.randomUUID().toString();
    private final Compensation testCompensation = new Compensation(
        testEmployeeId,
        100000,
        Instant.now().truncatedTo(ChronoUnit.MILLIS)
    );

    @Test
    public void testCreateCompensation() {
        when(mockCompensationRepository.insert(testCompensation)).thenReturn(testCompensation);

        Compensation actualCompensation = compensationService.create(testCompensation);

        assertEquals(testCompensation, actualCompensation);
    }

    @Test
    public void testReadCompensation() {
        when(mockCompensationRepository.findByEmployeeId(testEmployeeId)).thenReturn(testCompensation);

        Compensation actualCompensation = compensationService.read(testEmployeeId);

        assertEquals(testCompensation, actualCompensation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadCompensationNotFound() {
        when(mockCompensationRepository.findByEmployeeId("testId")).thenReturn(null);

        compensationService.read("testId");
    }
}
