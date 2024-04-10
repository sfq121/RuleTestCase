package xss;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/xss-05/BenchmarkTest02588")
public class BenchmarkTest02588 extends HttpServlet {

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
        String paramval = "BenchmarkTest02588" + "=";
        int paramLoc = -1;
        if (queryString != null) paramLoc = queryString.indexOf(paramval);
        if (paramLoc == -1) {
            response.getWriter()
                    .println(
                            "getQueryString() couldn't find expected parameter '"
                                    + "BenchmarkTest02588"
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
        Object[] obj = {"a", "b"};
        response.getWriter().printf(java.util.Locale.US, org.owasp.esapi.ESAPI.encoder().encodeForHTML(bar), obj);
    }

    private static String doSomething(HttpServletRequest request, String param)
            throws ServletException, IOException {

        String a1227 = param;
        StringBuilder b1227 = new StringBuilder(a1227);
        b1227.append(" SafeStuff");
        b1227.replace(
                b1227.length() - "Chars".length(),
                b1227.length(),
                "Chars");
        java.util.HashMap<String, Object> map1227 = new java.util.HashMap<String, Object>();
        map1227.put("key1227", b1227.toString());
        String c1227 = (String) map1227.get("key1227");
        String d1227 = c1227.substring(0, c1227.length() - 1);
        String e1227 =
                new String(
                        org.apache.commons.codec.binary.Base64.decodeBase64(
                                org.apache.commons.codec.binary.Base64.encodeBase64(
                                        d1227.getBytes())));
        String f1227 = e1227.split(" ")[0];
        org.owasp.benchmark.helpers.ThingInterface thing =
                org.owasp.benchmark.helpers.ThingFactory.createThing();
        String bar = thing.doSomething(f1227);

        return bar;
    }
}