package xss;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.util.HtmlUtils;

@WebServlet(value = "/xss-02/BenchmarkTest01438")
public class BenchmarkTest01438 extends HttpServlet {

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

        String param = "";
        boolean flag = true;
        java.util.Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements() && flag) {
            String name = (String) names.nextElement();
            String[] values = request.getParameterValues(name);
            if (values != null) {
                for (int i = 0; i < values.length && flag; i++) {
                    String value = values[i];
                    if (value.equals("BenchmarkTest01438")) {
                        param = name;
                        flag = false;
                    }
                }
            }
        }

        String bar = new Test().doSomething(request, param);

        response.setHeader("X-XSS-Protection", "0");
        String safeBar = HtmlUtils.htmlEscape(bar);
        response.getWriter().write("Parameter value: " + safeBar);
    } // end doPost

    private class Test {

        public String doSomething(HttpServletRequest request, String param)
                throws ServletException, IOException {

            // Chain a bunch of propagators in sequence
            String a32762 = param; // assign
            StringBuilder b32762 = new StringBuilder(a32762); // stick in stringbuilder
            b32762.append(" SafeStuff"); // append some safe content
            b32762.replace(
                    b32762.length() - "Chars".length(),
                    b32762.length(),
                    "Chars"); // replace some of the end content
            java.util.HashMap<String, Object> map32762 = new java.util.HashMap<String, Object>();
            map32762.put("key32762", b32762.toString()); // put in a collection
            String c32762 = (String) map32762.get("key32762"); // get it back out
            String d32762 = c32762.substring(0, c32762.length() - 1); // extract most of it
            String e32762 =
                    new String(
                            org.apache.commons.codec.binary.Base64.decodeBase64(
                                    org.apache.commons.codec.binary.Base64.encodeBase64(
                                            d32762.getBytes()))); // B64 encode and decode it
            String f32762 = e32762.split(" ")[0]; // split it on a space
            org.owasp.benchmark.helpers.ThingInterface thing =
                    org.owasp.benchmark.helpers.ThingFactory.createThing();
            String bar = thing.doSomething(f32762); // reflection

            return bar;
        }
    } // end innerclass Test
} // end DataflowThruInnerClass