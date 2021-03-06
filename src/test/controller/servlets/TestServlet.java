package test.controller.servlets;

import test.controller.processors.Processor;

import test.controller.processors.ProcessorLogging;
import test.controller.processors.ProcessorMainPage;

import test.model.ProcessorResult;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestServlet extends javax.servlet.http.HttpServlet {

    private List<Processor> processorList = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        super.init();
        processorList.add(new ProcessorLogging());
        processorList.add(new ProcessorMainPage());

    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        process(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        process(request, response);
    }


    private void process(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");

        System.out.println(action);
        for (Processor tempProcessor: processorList) {
            if (tempProcessor.canProcess(action)){
                ProcessorResult processorResult = tempProcessor.getResult(request);
                request.getSession().setAttribute("includedPage", processorResult.getIncludedPage());
                if (processorResult.isForward()) {
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher(processorResult.getUrl());
                    requestDispatcher.forward(request, response);
                } else {
                    response.sendRedirect(processorResult.getUrl());
                }
            }
        }
    }
}
