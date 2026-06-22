<?php
$uploadDir = __DIR__ . '/uploads/';
$expectedExtension = 'php';
$message = '';

function extension_note(string $fileName, string $expectedExtension, string $runtime): string
{
    $extension = strtolower(pathinfo($fileName, PATHINFO_EXTENSION));
    if ($extension !== $expectedExtension) {
        return " Note: {$runtime} executes .{$expectedExtension} payloads. This file will be served as static content.";
    }

    return ' Browse the uploaded link to execute it.';
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_FILES['file'])) {
    $fileName = basename($_FILES['file']['name']);
    $target = $uploadDir . $fileName;

    if (move_uploaded_file($_FILES['file']['tmp_name'], $target)) {
        $url = '/uploads/' . rawurlencode($fileName);
        $message = "Uploaded: <a href=\"{$url}\">{$url}</a>" . extension_note($fileName, $expectedExtension, 'PHP');
    } else {
        $message = 'Upload failed.';
    }
}
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>PHP Upload Lab</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 680px; margin: 48px auto; line-height: 1.5; }
        form { display: flex; gap: 12px; align-items: center; margin-top: 24px; }
        button { padding: 8px 14px; cursor: pointer; }
        .message { margin-top: 20px; padding: 12px; background: #f2f4f7; }
    </style>
</head>
<body>
    <h1>PHP Upload Lab</h1>
    <form method="post" enctype="multipart/form-data">
        <input type="file" name="file" required>
        <button type="submit">Upload</button>
    </form>
    <?php if ($message !== ''): ?>
        <div class="message"><?= $message ?></div>
    <?php endif; ?>
</body>
</html>
