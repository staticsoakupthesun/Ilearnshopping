<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/12/10
  Time: 19:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>图片上传</title>
</head>
<body>
<%--在页面点击图片上传会执行action 如果action为空 图片会上传到当前浏览器页面的路径--%>
<form action="" method="post" enctype="multipart/form-data">

    <input type="file" name="upload_file">
    <input type="submit" value="图片上传">

</form>

</body>
</html>
