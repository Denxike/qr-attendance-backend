package com.Qr.Qr.service;

import java.util.List;
import java.util.Map;

public interface SuperAdminService {
    Map<String, Object> getDashboardStats();
    List<Map<String, Object>> getAllUsers();
    Map<String, Object> getSystemReports();
    void createAdmin(Map<String, String> request);
    void deleteUser(Long userId);
}

