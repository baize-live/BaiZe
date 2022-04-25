package top.byze.controller;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "YOU", value = "/byzeyou")
public class ServletYOU extends HttpServlet {

    private void doWork(HttpServletRequest req, HttpServletResponse res) {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        doWork(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        doWork(req, res);
    }

}
