package xss;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.encoder.Encode;

@WebServlet(value = "/xss-05/BenchmarkTest02587")
public class BenchmarkTest02587 extends HttpServlet {

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

        String queryString = request.getQueryString();
        String paramval = "BenchmarkTest02587" + "=";
        int paramLoc = -1;
        if (queryString != null) paramLoc = queryString.indexOf(paramval);
        if (paramLoc == -1) {
            response.getWriter()
                    .println(
                            "getQueryString() couldn't find expected parameter '"
                                    + "BenchmarkTest02587"
                                    + "' in query string.");
            return;
        }

        String param =
                queryString.substring(
                        paramLoc
                                + paramval
                                        .length()); 
        int ampersandLoc = queryString.indexOf("&", paramLoc);
        if (ampersandLoc != -1) {
            param = queryString.substring(paramLoc + paramval.length(), ampersandLoc);
        }
        param = java.net.URLDecoder.decode(param, "UTF-8");

        String bar = doSomething(request, param);

        response.setHeader("X-XSS-Protection", "0");
        Object[] obj = {"a", Encode.forHtml(bar)};
        response.getWriter().printf(java.util.Locale.US, "Formatted like: %1$s and %2$s.", obj);
    } 

    private static String doSomething(HttpServletRequest request, String param)
            throws ServletException, IOException {

        String a93081 = param; 
        StringBuilder b93081 = new StringBuilder(a93081); 
        b93081.append(" SafeStuff"); 
        b93081.replace(
                b93081.length() - "Chars".length(),
                b93081.length(),
                "Chars"); 
        java.util.HashMap<String, Object> map93081 = new java.util.HashMap<String, Object>();
        map93081.put("key93081", b93081.toString()); 
        String c93081 = (String) map93081.get("key93081"); 
        String d93081 = c93081.substring(0, c93081.length() - 1); 
        String e93081 =
                new String(
                        org.apache.commons.codec.binary.Base64.decodeBase64(
                                org.apache.commons.codec.binary.Base64.encodeBase64(
                                        d93081.getBytes()))); 
        String f93081 = e93081.split(" ")[0]; 
        org.owasp.benchmark.helpers.ThingInterface thing =
                org.owasp.benchmark.helpers.ThingFactory.createThing();
        String bar = thing.doSomething(f93081); 

        return bar;
    }
}