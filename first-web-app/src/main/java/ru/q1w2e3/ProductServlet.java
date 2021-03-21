package ru.q1w2e3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.q1w2e3.persist.Product;
import ru.q1w2e3.persist.ProductRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/product/*")
public class ProductServlet extends HttpServlet {

    private static final Pattern pathParam = Pattern.compile("\\/(\\d*)$");

    private ProductRepository productRepository;

    @Override
    public void init() throws ServletException {
        productRepository = (ProductRepository) getServletContext().getAttribute("productRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getPathInfo() == null || req.getPathInfo().equals("") || req.getPathInfo().equals("/")) {
            resp.getWriter().println("<table>");
            resp.getWriter().println("<tr>");
            resp.getWriter().println("<th>Id</th>");
            resp.getWriter().println("<th>Name</th>");
            resp.getWriter().println("<th>Description</th>");
            resp.getWriter().println("<th>Price</th>");
            resp.getWriter().println("</tr>");

            for (Product product : productRepository.findAll()) {
                resp.getWriter().println("<tr>");
                resp.getWriter().println("<td><a href='" + getServletContext().getContextPath() + "/product/" + product.getId() + "'>" + product.getId() + "</a></td>");
                resp.getWriter().println("<td>" + product.getName() + "</td>");
                resp.getWriter().println("<td>" + product.getDescription() + "</td>");
                resp.getWriter().println("<td>" + product.getPrice() + "</td>");
                resp.getWriter().println("</tr>");
            }
            resp.getWriter().println("</table>");
        } else {
            Matcher matcher = pathParam.matcher(req.getPathInfo());
            if (matcher.matches()) {
                long id;
                try {
                    id = Long.parseLong(matcher.group(1));
                } catch (NumberFormatException ex) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                Product product = productRepository.findById(id);
                resp.getWriter().println("<p><b>Product info</b></p>");
                resp.getWriter().println("Name: " + product.getName() + "<br>");
                resp.getWriter().println("Description: " + product.getDescription() + "<br>");
                resp.getWriter().println("Price: " + product.getPrice() + "<br>");
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
