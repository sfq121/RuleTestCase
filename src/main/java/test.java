/**
 * @author xiaojingxian
 * @version 1.0
 * @date 2024/4/10 9:12 AM
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileServerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取请求的文件路径
        String filePath = request.getPathInfo();

        // 创建文件对象
        File file = new File(filePath);

        if (file.exists() && file.isFile()) {
            // 设置响应头
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());

            // 设置响应状态码
            response.setStatus(HttpServletResponse.SC_OK);

            // 获取输出流
            OutputStream outputStream = response.getOutputStream();

            // 读取文件并写入输出流
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            // 关闭流
            outputStream.close();
        } else {
            // 文件不存在或不是文件，返回404错误
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("文件未找到");
        }
    }
}

