package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import services.ServiceFactory;
import services.TaskService;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * this class setups common test environment and utilities for TaskServlet.
 */
abstract class TaskServletTestBase {

    protected TestableTaskServlet servlet;
    @Mock
    protected TaskService taskServiceMock;
    @Mock
    protected HttpServletRequest requestMock;
    @Mock
    protected HttpServletResponse responseMock;
    protected StringWriter responseWriter;

    /**
     * inner class that extends TaskServlet to expose and track protected methods
     */
    protected static class TestableTaskServlet extends TaskServlet {
        private boolean doPatchCalled = false;

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
            super.doGet(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
            super.doPost(req, resp);
        }

        @Override
        protected void doPatch(HttpServletRequest req, HttpServletResponse resp) {
            super.doPatch(req, resp);
            doPatchCalled = true;
        }

        public boolean isDoPatchCalled() {
            return doPatchCalled;
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        servlet = new TestableTaskServlet();
        taskServiceMock = mock(TaskService.class);
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();

        // set service factory to use the mock service
        ServiceFactory.setTaskServiceInstance(taskServiceMock);

        when(responseMock.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }
}