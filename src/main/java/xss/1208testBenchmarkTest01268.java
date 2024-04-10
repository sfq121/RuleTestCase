package xss;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

@WebServlet(value = "/xss-02/BenchmarkTest01268")
public class BenchmarkTest01268 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String param = request.getParameter("BenchmarkTest01268");
        if (param == null) param = "";

        String bar = new Test().doSomething(request, param);

        response.setHeader("X-XSS-Protection", "0");
        int length = 1;
        if (bar != null) {
            bar = StringEscapeUtils.escapeHtml4(bar);
            length = bar.length();
            response.getWriter().write(bar, 0, length);
        }
    } 

    private class Test {

        public String doSomething(HttpServletRequest request, String param)
                throws ServletException, IOException {

            // Chain a bunch of propagators in sequence
            String a92384 = param; // assign
            StringBuilder b92384 = new StringBuilder(a92384); // stick in stringbuilder
            b92384.append(" SafeStuff"); // append some safe content
            b92384.replace(
                    b92384.length() - "Chars".length(),
                    b92384.length(),
                    "Chars"); // replace some of the end content
            java.util.HashMap<String, Object> map92384 = new java.util.HashMap<String, Object>();
            map92384.put("key92384", b92384.toString()); // put in a collection
            String c92384 = (String) map92384.get("key92384"); // get it back out
            String d92384 = c92384.substring(0, c92384.length() - 1); // extract most of it
            String e92384 =
                    new String(
                            org.apache.commons.codec.binary.Base64.decodeBase64(
                                    org.apache.commons.codec.binary.Base64.encodeBase64(
                                            d92384.getBytes()))); // B64 encode and decode it
            String f92384 = e92384.split(" ")[0]; // split it on a space
            org.owasp.benchmark.helpers.ThingInterface thing =
                    org.owasp.benchmark.helpers.ThingFactory.createThing();
            String bar = thing.doSomething(f92384); // reflection

            return bar;
        }
    } 
}