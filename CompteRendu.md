<div align="center">

# Compte Rendu SAé 302
**Développer des applications communicantes**

</div>

Réalisation d'une application client/serveur utilisant les fonctions communicantes permettant de
concevoir un protocole applicatif au-dessus de la pile de communication TCP/IP.

## Fonctionnalités principales

1. Le serveur permet de gérer un total de 10 utilisateurs.

2. Un utilisateur est identifié par un email et un mot de passe stockés en clair.
   
3. Un message est une chaîne de caractères définit par une action/commande (INVITATION
ou MESSAGE), par une origine, par le destinataire et par un corps de message.

4. Chaque utilisateur est autorisé à avoir jusqu’à 10 amis.
   
5. Le serveur stocke les messages relatifs aux utilisateurs en mémoire RAM.
   
6. Un nombre illimité de messages peuvent être stockés dans la mémoire RAM. 
   
7. Le destinataire du message peut être “TOUS” Dans ce cas, le serveur réplique le message vers tous les amis de l’émetteur. Semblable à un ***Broadcast***
   
8. Pour consulter ses messages, l’utilisateur connecté doit envoyer un message de lecture de ses messages au format *“lecture,login”*. (côté client l’utilisateur lit les messages chaque
10 secondes).

9. Pour inviter un autre utilisateur, l’invitant envoie un message *“demande_ami,login,ami”*.
    
10.  A l’acceptation d’une invitation, l’invitant reçoit un message du serveur lui notifiant cette acceptation.
    

## Organisation du travail

Nous étions déjà tous d'accord pour faire du versionnage avec GitHub, nous avions donc naturellement déjà créé les différents repos avant le début de la SAé.

Lundi nous avons constituer notre équipe de 4 : Enzo MANZINALI, Bastien LAURENT, Anas IDIRI et Romain MARC.
La répartition des rôles fut assez rapide à appliqué étant donner que nous étions tous rapidement d'accord sur comment produire le travail demandé. Nous nous sommes tous réunis pour mettre en place le protocol qui sera la colonne vertébrale du projet (*sprint planning*).
Anas s'est appliqué sur le backend du serveur, pendant qu'Enzo, Romain et Bastien commençaient à travailler sur le client.

Mardi, Anas avança sur le serveur avec Bastien, pendant que Romain et Enzo travaillaient sur le client.

Mercredi, nous avons terminer officielement le serveur avec toutes ses fonctionalités. Enzo commença à créer le client pour PC en utilisant JavaFX sur VSCode, permettant d'implémenter facilement un GUI. Nous avons continué d'améliorer le Client Android, nous demandant plus d'effort.

Jeudi, nous avons compléter le cahier des charges ainsi que commencer à rédiger le compte-rendu ainsi qu'à préparer la présentation.


## Serveur JavaChat

### Architecture

Le backend est structuré selon le pattern d'architecture MVC. La terminologie utilisée (Action class, Router) est celle utilisée par le framework Laraveln qui est un framework MVC PHP que j'apprécie.
<br>
L'application se présente comme suit:

```
├── Actions
│   ├── AcceptFriendRequest.java
│   ├── DeleteAccount.java
│   ├── GetFriend.java
│   ├── GetFriendRequest.java
│   ├── GetMessage.java
│   ├── Login.java
│   ├── SendFriendRequest.java
│   ├── SendMessage.java
│   ├── Signup.java
│   └── SupprimerAmi.java
│
├── Models
│   ├── Friend.java
│   ├── Message.java
│   └── User.java
│
├── Main.java
├── Router.java
└── Server.java
```

#### Server
La classe Server sert avant tout à initier le serveur UDP et écouter sur le port 1337. C'est cette classe qui porte en elle le tableau d'utilisateurs, ainsi que quelques méthodes tel que:
- `get_client_datagram()` Permet de récupérer le datagramme UDP envoyé par le client
- `get_to_send_datagram()` Permet de générér la string (au format CSV) à envoyé au client. Pour ce faire cette classe va faire appel au "*Router*".
- `send_datagram()` Permet d'envoyer un datagramme UDP au client

#### Router
La classe "router" permet d'effectuer le mapping entre la commande envoyé par le client, et l'action à appeler.

#### Actions
Les actions sont l'équivalents des controllers du modèle MVC. Cependant dans notre cas ils n'effectuent qu'une seule tâche. Dans le framework Laravel on nomme cela des "Action Class". C'est pour cela que j'ai choisi de conserver la terminologie en les nommant Actions. On voit qu'on a basiquement une Action par commande, ce qui permet de rendre notre code extrêmement modulable. Si on souhaite ajouter une nouvelle commande, il suffit de créer l'Action qui lui est associé, et de créer la "Route" qui map la commande souhaitée à l'action.

#### Models
Les models sont basiquement des classes qui permettent d'encapsuler de la données. Dans notre cas nous avons trois modèles.

- User.java
Contient le login, le password, ainsi qu'un tableau d'amis.

- Friend.java
Contient le `User ami`, un booleen qui nous permet de savoir si il est ami ou si c'est une demande d'ami ainsi qu'un tableau de message.

- Message.java
Contient un `User envoyeur`, un `User receveur` ainsi que le contenu du message.

### Compilation

Pour lancer le serveur faire, aller à la racine du projet et faire

```bash
make
```

Pour lancer les tests avec le script python on peut faire
```bash
# Ou via le script prévu à cet effet
python3 script.py
```

## Client Android JavaChat

Le client peut être lancer dans l'émulateur *Android Studio* ou en téléchargeant l'APK sur son smartphone.


<img src="UserCreate.jpg" alt="Création d'un utilisateur" width="310"/>

L'utilisateur peut créer son compte en cliquant sur *Sign Up*.

<img src="AjoutAmi.jpg" alt="Ajout d'un ami" width="310"/>

L'utilisateur peut se connecter sur la page d'accueil puis ajouter des amis en indiquant son ID et en cliquant sur *Ajouter*.
Le boutton pour *Supprimer* n'est pas encore fonctionnel côté client.

<img src="chatandroid.jpg" alt="Chat sur client Android" width="310"/>





Le Client Android met à jour les messages reçus seulement lorsque le boutton afficher messages est cliqué. Au total, 8 messages peuvent être stockés sur le client Android.

## Client PC JavaChat

Le client PC fut créé avec JavaFX sur VSCode. Il contient les mêmes fonctionalités que le client Android, mais est dynamique.

Le client est structuré de la manière suivante :


```
src/
└── main/
    ├── java/
    │   └── com/assembleurnational/javachatclient/
    │       ├── AddFriendController.java
    │       ├── App.java
    │       ├── ChatListController.java
    │       ├── DeleteFriendController.java
    │       ├── FriendRequestsController.java
    │       ├── PrimaryController.java
    │       └── SettingsController.java
    └── resources/
        ├── addFriend.fxml
        ├── chatList.fxml
        ├── deleteFriend.fxml
        ├── friendRequests.fxml
        ├── primary.fxml
        └── settings.fxml

```

D'une part, les fichiers `.java` contiennent le code de chaque page du client. D'autre part, les fichiers `.fxml` contiennent la disposition des éléments sur les pages, ce qui peut s'apparenter au fonctionnement d'Android Studio.


![alt text](image.png)
Configuration des paramètres de connexion au serveur : Adresse IP et Port

<img src="ClientPClogin.png" alt="Login sur le client PC" width="390"/>

Client PC se connecte sur son compte


![alt text](Screenshot_20241213_085708.png)

Page de dialogue de demande d'ami : On peut soit accepter soit refuser la demande.


![Chat entre 2 amis](ChatPC.png)

Chat lancé entre 2 amis sur le client PC.
Le Client met à jour les messages reçus toutes les 10 secondes. Une infinité de messages sont stockés sur le client PC (du moins jusqu'au remplissage de la RAM de la JVM).

### Compilation

Pour lancer le client, aller à la racine du projet et faire

```bash
make client
```

Note: Apache Ant est nécessaire au lancement du client.
