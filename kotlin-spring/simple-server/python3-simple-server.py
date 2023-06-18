from http.server import SimpleHTTPRequestHandler
from socketserver import ThreadingMixIn, TCPServer
import time

# Multi-threaded HTTP Server
class ThreadedHTTPServer(ThreadingMixIn, TCPServer):
    pass

class Handler(SimpleHTTPRequestHandler):
    def do_GET(self):
        # 요청 처리 로직을 여기에 구현합니다.
        print('GET request received')
        time.sleep(3)
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()
        self.wfile.write(b"Hello, World!")
        print('GET response sent')

if __name__ == '__main__':

    server_address = ('', 8000)
    httpd = ThreadedHTTPServer(server_address, Handler)
    print('Server started on port 8000')
    httpd.serve_forever()