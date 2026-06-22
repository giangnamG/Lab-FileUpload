<%@ Page Language="C#" %>
<%@ Import Namespace="System" %>
<%@ Import Namespace="System.IO" %>

<!doctype html>
<script runat="server">
    protected string Message = "";
    protected const string ExpectedExtension = "aspx";

    protected string ExtensionNote(string fileName)
    {
        string extension = Path.GetExtension(fileName).TrimStart('.').ToLowerInvariant();
        if (extension != ExpectedExtension)
        {
            return " Note: ASPX lab executes ." + ExpectedExtension + " payloads. This file will be served as static content.";
        }

        return " Browse the uploaded link to execute it.";
    }

    protected void Page_Load(object sender, EventArgs e)
    {
        if (Request.HttpMethod == "POST" && Request.Files.Count > 0)
        {
            HttpPostedFile file = Request.Files["file"];
            if (file != null && file.ContentLength > 0)
            {
                string uploadDir = Server.MapPath("~/uploads/");
                Directory.CreateDirectory(uploadDir);

                string fileName = Path.GetFileName(file.FileName);
                string target = Path.Combine(uploadDir, fileName);
                file.SaveAs(target);

                string href = "uploads/" + Server.UrlEncode(fileName);
                Message = "Uploaded: <a href=\"" + href + "\">" + href + "</a>" + ExtensionNote(fileName);
            }
        }
        else if (Request.HttpMethod == "POST")
        {
            Message = "No file received.";
        }
    }
</script>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>ASPX Upload Lab</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 680px; margin: 48px auto; line-height: 1.5; }
        form { display: flex; gap: 12px; align-items: center; margin-top: 24px; }
        button { padding: 8px 14px; cursor: pointer; }
        .message { margin-top: 20px; padding: 12px; background: #f2f4f7; }
    </style>
</head>
<body>
    <h1>ASPX Upload Lab</h1>
    <form method="post" enctype="multipart/form-data">
        <input type="file" name="file" required>
        <button type="submit">Upload</button>
    </form>
    <% if (!String.IsNullOrEmpty(Message)) { %>
        <div class="message"><%= Message %></div>
    <% } %>
</body>
</html>
