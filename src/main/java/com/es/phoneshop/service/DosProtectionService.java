package com.es.phoneshop.service;

import java.util.Date;

public interface DosProtectionService {
    boolean isAllowed(String ip, Date timeOfRequest);
}
