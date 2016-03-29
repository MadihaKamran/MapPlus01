import socket
import urlparse 
def num(s):
    try:
        return float(s)
    except ValueError:
        return int(s)

HOST, PORT = '', 8888
listen_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
listen_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
listen_socket.bind((HOST, PORT))
listen_socket.listen(1)
print 'Serving HTTP on port %s ...' % PORT
while True:
    client_connection, client_address = listen_socket.accept()
    request = client_connection.recv(1024)
    parsed = urlparse.urlparse(request)
    s1 = urlparse.parse_qs(parsed.query)['slat'][0]
    s2 = urlparse.parse_qs(parsed.query)['slng'][0]
    e1 = urlparse.parse_qs(parsed.query)['elat'][0]
    e2 = urlparse.parse_qs(parsed.query)['elng'][0].partition('\\')[0]
    tmp = e2.partition(' ')[0]
    ret = ''.join(i for i in tmp if i.isdigit() or i == '.')

    print "starting point Latitude " + s1
    print "starting point Longitude " + s2
    print "starting point Latitude " + e1
    print "starting point Longitude " + e2

    lat = num(s1)/2 + num(e1)/2
    lng = num(s2)/2 + num(ret)/2
    http_response = "HTTP/1.1 200 OK\n"+"Content-Type: text/html\n" + "\n" + str(lat) + " " + str(lng)
    client_connection.sendall(http_response)
    print http_response
    client_connection.close()