from socket import *;

s = socket(AF_INET, SOCK_DGRAM)

s.connect(("127.0.0.1", 1337))

s.send("inscription,a,a\n".encode())
s.recv(30)
s.send("inscription,b,b\n".encode())
s.recv(30)
s.send("inscription,c,c\n".encode())
s.recv(30)
s.send("connexion,a,a\n".encode())
s.recv(30)

s.send("delete,c,c\n".encode())
print(s.recv(30))
