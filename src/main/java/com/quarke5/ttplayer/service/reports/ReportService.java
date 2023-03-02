package com.quarke5.ttplayer.service.reports;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

public interface ReportService {

    void getApplicantExcel(HttpServletResponse response) throws IOException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;
}
