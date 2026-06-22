package lab;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet(urlPatterns = {"/", "/upload"})
@MultipartConfig
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String EXPECTED_EXTENSION = "jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        render(resp, "");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Part filePart = req.getPart("file");
        String fileName = submittedFileName(filePart);

        File uploadDir = new File(getServletContext().getRealPath("/uploads"));
        uploadDir.mkdirs();

        File target = new File(uploadDir, fileName);
        filePart.write(target.getAbsolutePath());

        String href = req.getContextPath() + "/uploads/" + urlEncode(fileName);
        render(resp, "Uploaded: <a href=\"" + href + "\">" + href + "</a>" + extensionNote(fileName));
    }

    private void render(HttpServletResponse resp, String message) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            out.println("<!doctype html>");
            out.println("<html lang=\"en\"><head>");
            out.println("<meta charset=\"utf-8\">");
            out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
            out.println("<title>JSP Upload Lab</title>");
            out.println("<style>body{font-family:Arial,sans-serif;max-width:680px;margin:48px auto;line-height:1.5}form{display:flex;gap:12px;align-items:center;margin-top:24px}button{padding:8px 14px;cursor:pointer}.message{margin-top:20px;padding:12px;background:#f2f4f7}</style>");
            out.println("</head><body>");
            out.println("<h1>JSP Upload Lab</h1>");
            out.println("<form method=\"post\" action=\"upload\" enctype=\"multipart/form-data\">");
            out.println("<input type=\"file\" name=\"file\" required>");
            out.println("<button type=\"submit\">Upload</button>");
            out.println("</form>");

            if (message != null && !message.isEmpty()) {
                out.println("<div class=\"message\">" + message + "</div>");
            }

            out.println("</body></html>");
        }
    }

    private String submittedFileName(Part part) {
        String header = part.getHeader("content-disposition");
        for (String token : header.split(";")) {
            String trimmed = token.trim();
            if (trimmed.startsWith("filename")) {
                return new File(trimmed.substring(trimmed.indexOf('=') + 1).trim().replace("\"", "")).getName();
            }
        }

        return "upload.bin";
    }

    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String extensionNote(String fileName) {
        String extension = "";
        int dot = fileName.lastIndexOf('.');
        if (dot >= 0 && dot < fileName.length() - 1) {
            extension = fileName.substring(dot + 1).toLowerCase();
        }

        if (!EXPECTED_EXTENSION.equals(extension)) {
            return " Note: JSP lab executes ." + EXPECTED_EXTENSION + " payloads. This file will be served as static content.";
        }

        return " Browse the uploaded link to execute it.";
    }
}
