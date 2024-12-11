# Compte Rendu SAé 302 : Développer des applications communicantes

Réalisation d'une application client/serveur utilisant les fonctions communicantes permettant de
concevoir un protocole applicatif au-dessus de la pile de communication TCP/IP.

## Les fonctionalités du serveur seront les suivantes

1. Le serveur permet de gérer un total de 10 utilisateurs.

2. Un utilisateur est identifié par un email et un mot de passe stockés en clair.
   
3. Un message est une chaîne de caractères définit par une action/commande (INVITATION
ou MESSAGE), par une origine, par le destinataire et par un corps de message.

4. Chaque utilisateur est autorisé à avoir jusqu’à __x__ amis.
   
5. Le serveur stocke les messages relatifs aux utilisateurs en mémoire RAM.
   
6. Un nombre presque illimité de messages peuvent être stocker dans la mémoire RAM. _(Les précédents sont automatiquement supprimés.)_
   
7. Le destinataire du message peut être “TOUS” Dans ce cas, le serveur réplique le message vers tous les amis de l’émetteur. Semblable à un ***Broadcast***
   
8. Pour consulter ses messages, l’utilisateur connecté doit envoyer un message de lecture de
ses messages au format *“lecture,login”*. (côté client l’utilisateur lit les messages chaque
10 secondes).

9. Pour inviter un autre utilisateur, l’invitant envoie un message *“demande_ami,login,ami”*.
    
10.  A l’acceptation d’une invitation, l’invitant reçoit un message du serveur lui notifiant cette acceptation.