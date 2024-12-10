from socket import *;

s = socket(AF_INET, SOCK_DGRAM)

s.connect(("127.0.0.1", 1337))

print("CRÉATION DE A")
s.send("inscription,a,a\n".encode())
s.recv(30)

print("CRÉATION DE B")
s.send("inscription,b,b\n".encode())
s.recv(30)

print("CRÉATION DE C")
s.send("inscription,c,c\n".encode())
s.recv(30)


print("ON SE CONNECTE À A")
s.send("connexion,a,a\n".encode())
s.recv(30)

print("ON DEMANDE EN AMI B")
s.send("demande_ami,a,b\n".encode())
s.recv(30)

print("ON SE CONNECTE À C")
s.send("connexion,c,c\n".encode())
s.recv(30)

print("ON DEMANDE EN AMI B")
s.send("demande_ami,c,b\n".encode())
s.recv(30)


print("ON SE CONNECTE À B")
s.send("connexion,b,b\n".encode())
s.recv(30)

print("ON AFFICHE LES DEMANDES RÉÇUES PAR B")
s.send("recuperer_demande,b\n".encode())
print(s.recv(50))
s.send("recuperer_demande,b\n".encode())
print(s.recv(50))

print("ON ACCEPTE LA DEMANDE DE A")
s.send("accepter_demande,a,b,oui\n".encode())
print(s.recv(50))

print("ON REFUSE LA DEMANDE DE C")
s.send("accepter_demande,a,c,non\n".encode())
print(s.recv(50))

print("ON AFFICHE LES AMIS DE A")
s.send("recuperer_amis,a\n".encode())
print(s.recv(250))

print("ON AFFICHE LES AMIS DE B")
s.send("recuperer_amis,b\n".encode())
print(s.recv(250))

print("ON AFFICHE LES AMIS DE C")
s.send("recuperer_amis,c\n".encode())
print(s.recv(250))

print("ON ENVOIE UN MESSAGE DE A VERS B")
s.send("envoi_message,a,b,Test du user A".encode())
print(s.recv(250))

print("ON ENVOIE UN AUTRE MESSAGE DE A VERS B")
s.send("envoi_message,a,b,Second test du user A".encode())
print(s.recv(250))

print("TEMPORISATION")
s.send("".encode())
print(s.recv(20))

print("ON RECUPÈRE LES MESSAGES CÔTÉ B")
s.send("demande_message,b,a,0".encode())
print(s.recv(250))

s.send("demande_message,b,a,1".encode())
print(s.recv(250))

s.send("demande_message,b,a,2".encode())
print(s.recv(250))

