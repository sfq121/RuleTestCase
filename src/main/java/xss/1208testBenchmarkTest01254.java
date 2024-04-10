package xss;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

@WebServlet(value = "/xss-02/BenchmarkTest01254")
public class BenchmarkTest01254 extends HttpServlet {

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

        String param = request.getParameter("BenchmarkTest01254");
        if (param == null) param = "";

        String bar = new Test().doSomething(request, param);

        response.setHeader("X-XSS-Protection", "0");
        // Use Apache Commons Text to escape HTML special characters
        String safeOutput = StringEscapeUtils.escapeHtml4(bar);
        response.getWriter().print(safeOutput.toCharArray());
    } // end doPost

    private class Test {

        public String doSomething(HttpServletRequest request, String param)
                throws ServletException, IOException {

            // Chain a bunch of propagators in sequence
            String a23874 = param; // assign
            StringBuilder b23874 = new StringBuilder(a23874); // stick in stringbuilder
            b23874.append(" SafeStuff"); // append some safe content
            b23874.replace(
                    b23874.length() - "Chars".length(),
                    b23874.length(),
                    "Chars"); // replace some of the end content
            java.util.HashMap<String, Object> map23874 = new java.util.HashMap<String, Object>();
            map23874.put("key23874", b23874.toString()); // put in a collection
            String c23874 = (String) map23874.get("key23874"); // get it back out
            String d23874 = c23874.substring(0, c23874.length() - 1); // extract most of it
            String e23874 =
                    new String(
                            org.apache.commons.codec.binary.Base64.decodeBase64(
                                    org.apache.commons.codec.binary.Base64.encodeBase64(
                                            d23874.getBytes()))); // B64 encode and decode it
            String f23874 = e23874.split(" ")[0]; // split it on a space
            org.owasp.benchmark.helpers.ThingInterface thing =
                    org.owasp.benchmark.helpers.ThingFactory.createThing();
            String bar = thing.doSomething(f23874); // reflection

            return bar;
        }
    } // end innerclass Test
} // end DataflowThruInnerClass