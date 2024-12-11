# Aspect protocolaire

## Inscription

### Demande d'inscription

> **client ➡️ serveur**
```bash
inscription,login,mot_de_passe

# exemple
inscription,test,password123
```

> **client ⬅️ serveur**
<br>

si ok:
```bash
reponse,inscription,login,ok
```

<br>

si pas ok:
- Nombre d'utilisateur max atteint
- Login existe déjà / est vide  
- Mot de passe contient une `,`
```bash
reponse,inscription,login,erreur
```

<br><br>

## Connexion

### Demande d'authentification

> **client ➡️ serveur**
```bash
connexion,login,mdp

# exemple
connexion,test,password123
```

> **client ⬅️ serveur**
<br>

si ok:
```bash
reponse,connexion,login,ok
```

<br>

si pas ok:
- Login n'existe pas / est vide 
- Le mot de passe ne correspond pas au login
- Login avec une virgule dedans
```bash
reponse,connexion,login,erreur
```

## Demande d'ami

### Demande d'ami

> **client ➡️ serveur**
```bash
demande_ami,demandeur,receveur

# exemple -> foo demande bar en ami
demande_ami,foo,bar
```


> **client ⬅️ serveur**
<br>

si ok:
```bash
reponse,demande_ami,demandeur,receveur,ok
```

<br>

si pas ok:
- Demandeur/Receveur n'existe pas / est vide 
- Le receveur est déjà ami avec le demandeur
- L'utilisateur a atteint le nombre maximal d'ami
- L'utilisateur essaye de s'ajouter en ami
```bash
reponse,demande_ami,login,erreur
```

### Récupération des demandes d'amis
> Donne moi les demandes d'amis qui me concerne
> demandeur = celui qui demande en ami

> **client ➡️ serveur**
```bash
recuperer_demande,login,id

# Étant donné qu'on peut avoir plusieurs demande d'ami on les indexe par ID
# on déduit si on a d'autre ami en fonction de la réponse du serveur, dernier paramètre

recuperer_demande,test,0
```

> **client ⬅️ serveur**
<br>

si ok:
```bash
# La suite peut être égal à oui ou à non 
# cela signifie qu'on a d'autre demande ou pas
reponse,recuperation_demande,login,demandeur,suite
```

<br>

si pas ok:
- Login n'existe pas / est vide 
```bash
reponse,recuperation_demande,login,demandeur,erreur
```

<br>
si on n'a pas d'ami de demande d'ami:
```
reponse,recuperation_demande,login,demandeur,pas_de_demande_ami
```


### Acceptation d'une demande d'ami

> **client ➡️ serveur**
```bash
# Accepte la demande en ami
accepter_demande,receveur,demandeur,oui

# Refuse la demande en ami
accepter_demande,receveur,demandeur,non
```

> **client ⬅️ serveur**
<br>

si ok:
```bash
reponse,accepter_demande,receveur,demandeur,ok
```

<br>

si pas ok: 
**Reponse en cas d'erreur:**
- Demandeur n'a pas envoyé de demande d'ami
- Receveur/Demandeur n'existe pas / est vide 

```bash
reponse,accepter_demande,receveur,demandeur,erreur
```


### Affichage de nos amis

> **client ➡️ serveur**
```bash
recuperer_amis,login

# exemple
recuperer_amis,test
```

> **client ⬅️ serveur**

<br>

si ok:
```bash
reponse,recuperer_amis,username,ami1,ami2,ami3,ami4,ami5,ami6,ami7,ami8,ami9,ami10
```

<br>

Les valeur de ami<x> seront `null` si on n'a pas d'ami. C'est à dire qu'un retour comme celui-ci sera envoyé:

```
reponse,recuperer_amis,username,test1,user_x,test3,null,null,null,null,null,null,null
```

<br>
si pas ok:
- Si on n'a aucune demande d'ami acceptée
- Receveur/Demandeur n'existe pas / est vide 

```bash
reponse,recuperer_amis,username,ami1,ami2,ami3,ami4,ami5,ami6,ami7,ami8,ami9,ami10,erreur
```


## Gestion des messages

### Envoi d'un message

> **client ➡️ serveur**
```bash
envoi_message,login,receveur,message

# Exemple
# A envoi un message à B
envoi_message,b,a,Salut

# A envoi un message en broadcast
envoi_message,b,tous,Broadcast test
```

> **client ⬅️ serveur**

<br>

si ok:
```bash
reponse,envoi_message,login,receveur,ok
```
 
<br>

si pas ok:
- Login/Receveur n'existe pas / est vide 
- Pas ami avec le receveur

```bash
reponse,envoi_message,login,receveur,erreur
```


## Récupérer les messages envoyés/reçus

> **client ➡️ serveur**
```bash
demande_message,login,ami,id

# Exemple pour récupèrer les deux dernières demandes d'ami
demande_message,login,ami,0
demande_message,login,ami,1
```

> **client ⬅️ serveur**

<br>

si ok:
```bash
# Suite vaudra oui ou non, il indique si on a une autre demande d'ami après celle la ou pas
reponse,demande_message,login,ami,envoyeur,message,suite
```

<br>

si pas ok:
- Login/Receveur n'existe pas / est vide 
- Pas ami avec receveur
```bash
reponse,demande_message,login,ami,envoyeur,message,erreur
```


## Supprimer son compte

> **client ➡️ serveur**
```bash
supprimer,login,password
```

> **client ⬅️ serveur**

<br>

si ok:
```bash
response,delete,login,ok
```

<br>

si pas ok:
- Login/Receveur n'existe pas / est vide 
- Pas ami avec receveur
```bash
response,delete,login,erreur
```

