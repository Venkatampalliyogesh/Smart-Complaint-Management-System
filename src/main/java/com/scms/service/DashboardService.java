package com.scms.service;

import com.scms.dto.DashboardDTO;

public interface DashboardService {

    DashboardDTO getUserDashboard();

    DashboardDTO getAdminDashboard();

}