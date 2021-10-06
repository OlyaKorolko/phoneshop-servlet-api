package com.es.phoneshop.service.impl;

import com.es.phoneshop.service.DosProtectionService;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long NUMBER_THRESHOLD = 20;
    private static final long TIME_THRESHOLD_IN_MILLI = 60000;
    private static volatile DosProtectionService instance;
    private final Map<String, ConcurrentLinkedDeque<Date>> timestampMap = new ConcurrentHashMap<>();

    private DefaultDosProtectionService() {
    }

    public static synchronized DosProtectionService getInstance() {
        if (instance == null) {
            synchronized (DosProtectionService.class) {
                if (instance == null) {
                    instance = new DefaultDosProtectionService();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean isAllowed(String ip, Date timeOfRequest) {
        ConcurrentLinkedDeque<Date> timeStampsDeque = timestampMap.get(ip);
        if (timeStampsDeque == null) {
            timeStampsDeque = new ConcurrentLinkedDeque<>();
            timestampMap.put(ip, timeStampsDeque);
        } else if (timeStampsDeque.size() == NUMBER_THRESHOLD) {
            if (timeOfRequest.getTime() - timeStampsDeque.getFirst().getTime() < TIME_THRESHOLD_IN_MILLI) {
                return false;
            } else {
                timeStampsDeque.removeFirst();
            }
        }
        timeStampsDeque.addLast(timeOfRequest);
        return true;
    }
}
