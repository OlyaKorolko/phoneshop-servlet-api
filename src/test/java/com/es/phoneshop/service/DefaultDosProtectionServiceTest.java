package com.es.phoneshop.service;

import com.es.phoneshop.service.impl.DefaultDosProtectionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDosProtectionServiceTest {
    private DosProtectionService dosProtectionService;

    @Before
    public void setUp() {
        dosProtectionService = DefaultDosProtectionService.getInstance();
    }

    @Test
    public void testIsAllowed() {
        for (int i = 0; i < 20; i++) {
            assertTrue(dosProtectionService.isAllowed("1", new Date()));
        }
        assertFalse(dosProtectionService.isAllowed("1", new Date()));
    }

    @Test
    public void testIsAllowedMultipleThreads() throws InterruptedException {
        class AnotherRunnable implements Runnable {
            boolean isAllowed = true;
            
            @Override
            public void run() {
                isAllowed = dosProtectionService.isAllowed("2", new Date());
            }
        }
        AnotherRunnable runnable = new AnotherRunnable();
        for (int i = 0; i < 20; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
            thread.join();
            assertTrue(runnable.isAllowed);
        }
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        assertFalse(runnable.isAllowed);
    }
}

