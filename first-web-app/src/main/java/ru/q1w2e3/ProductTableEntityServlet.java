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

@WebServlet(urlPatterns = "/product/*")
public class ProductTableEntityServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ProductTableEntityServlet.class);

    private ProductRepository productRepository;

    @Override
    public void init() throws ServletException {
        logger.info("Product Table Entity servlet");

        productRepository = (ProductRepository) getServletContext().getAttribute("productRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Product product = productRepository.findById(findProductId(req));

        resp.getWriter().println("<p><b>Product info</b></p>");
        resp.getWriter().println("Name: " + product.getName() + "<br>");
        resp.getWriter().println("Description: " + product.getDescription() + "<br>");
        resp.getWriter().println("Price: " + product.getPrice() + "<br>");
    }

    private Long findProductId(HttpServletRequest req) {
        Long productId = Long.parseLong(req.getPathInfo().replaceAll("/", ""));
        return productId;
    }
}
