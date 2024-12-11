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

s.send("demande_ami,a,b\n".encode())
s.recv(30)
s.send("demande_ami,c,b\n".encode())
s.recv(30)


s.send("accepter_demande,a,b,oui\n".encode())
print(s.recv(50))
s.send("accepter_demande,c,b,oui\n".encode())
print(s.recv(50))

print("ON ENVOIE UN MESSAGE DE A VERS B")
s.send("envoi_message,a,b,Test du user A".encode())
print(s.recv(250))

print("ON ENVOIE UN AUTRE MESSAGE DE A VERS B")
s.send("envoi_message,a,b,Second test du user A".encode())
print(s.recv(250))

print("ON RECUPÈRE LES MESSAGES CÔTÉ B")
s.send("demande_message,b,a,0".encode())
print(s.recv(250))

s.send("demande_message,b,a,1".encode())
print(s.recv(250))

print("B ENVOI UN MESSAGE À A")
s.send("envoi_message,b,a,Salam".encode())
s.recv(50)

print("A RECUPÈRE")
s.send("demande_message,a,b,0".encode())
print(s.recv(50))


print("B ENVOI A TLM")
s.send("envoi_message,b,tous,Salam zodjn".encode())
print(s.recv(50))
s.send("demande_message,c,b,0".encode())
print(s.recv(50))
