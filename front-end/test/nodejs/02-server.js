// 引入http模块
const http = require('http');
// 搭建服务器
http.createServer(function(request, reponse) {
    // 发送HTTP头部
    // HTTP 状态值：200：OK
    // 内容类型：text/html
    reponse.writeHead(200, {'Content-Type': 'text/html'});
    // 发送响应数据
    reponse.end('<h1>Hello Node.js Server</h1>');
}).listen(8888); // 在那个端口
// 终端打印信息
console.log('Server running at http://127.0.0.1:8888/');
