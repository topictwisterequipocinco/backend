package com.quarke5.ttplayer.service.reports.impl;

import com.quarke5.ttplayer.service.reports.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

@Service
public class ReportServiceImpl implements ReportService {

    private ApplicantReportImpl applicantReportImpl;

    @Autowired
    public ReportServiceImpl(ApplicantReportImpl applicantReportImpl) {
        this.applicantReportImpl = applicantReportImpl;
    }

    @Override
    public void getApplicantExcel(HttpServletResponse response) throws IOException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headervalue = "attachment; filename=Applicant_info.xlsx";

        response.setHeader(headerKey, headervalue);
        applicantReportImpl.export(response);
    }

}
