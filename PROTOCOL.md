# Aspect protocolaire

## Inscription

### Demande d'inscription

> **client ➡️ serveur**
```
inscription,login,mdp
```

> **client ⬅️ serveur**
<br>

si ok:
```
reponse,inscription,login,ok
```

<br>

si pas ok:
- Nombre d'utilisateur max atteint
- Login existe déjà / est vide / contient une `,` 
- Mot de passe contient une `,`
```
reponse,inscription,login,erreur
```

<br><br>

## Connexion

### Demande d'authentification

> **client ➡️ serveur**
```
connexion,login,mdp
```

> **client ⬅️ serveur**
<br>

si ok:
```
reponse,connexion,login,ok
```

<br>

si pas ok:
- Login n'existe pas / est vide / contient une `,`
- Le mot de passe ne correspond pas au login
- Login avec une virgule dedans
```
reponse,connexion,login,erreur
```

## Demande d'ami

### Demande d'ami

> **client ➡️ serveur**
```
demande_ami,demandeur,receveur
```


> **client ⬅️ serveur**
<br>

si ok:
```
reponse,demande_ami,demandeur,receveur,ok
```

<br>

si pas ok:
- Demandeur/Receveur n'existe pas / est vide / contient une `,`
- Le receveur est déjà ami avec le demandeur
- L'utilisateur a atteint le nombre maximal d'ami
- L'utilisateur essaye de s'ajouter en ami
```
reponse,demande_ami,login,erreur
```

### Récupération des demandes d'amis
> Donne moi les demandes d'amis qui me concerne
> demandeur = celui qui demande en ami

> **client ➡️ serveur**
```
# id -> id de la demande d'ami vu qu'on peut en avoir plusieurs (commence à 0)
recuperer_demande,login,id
```

> **client ⬅️ serveur**
<br>

si ok:
```
# suite = est ce qu'on a d'autre demande d'ami après celle la ?
reponse,recuperation_demande,login,demandeur,suite(=oui|non)
```

<br>

si pas ok:
- Login n'existe pas / est vide / contient une `,`
- Le receveur n'a pas recu de demande d'ami de ce demandeur
```
reponse,recuperation_demande,login,demandeur,erreur

# Si on n'a pas de demande d'ami
reponse,recuperation_demande,login,demandeur,pas_de_demande_ami
```


### Acceptation d'une demande d'ami

> **client ➡️ serveur**
```
# Accepte la demande en ami
accepter_demande,receveur,demandeur,oui
# Refuse la demande en ami
accepter_demande,receveur,demandeur,non
```

> **client ⬅️ serveur**
<br>

si ok:
```
reponse,accepter_demande,receveur,demandeur,ok
```

<br>

si pas ok: 
**Reponse en cas d'erreur:**
- Demandeur n'a pas envoyé de demande d'ami
- Receveur/Demandeur n'existe pas / est vide / contient une `,`

```
reponse,accepter_demande,receveur,demandeur,erreur
```


### Affichage de nos amis

> **client ➡️ serveur**
```
recuperer_amis,login
```

> **client ⬅️ serveur**

<br>

si ok:
```
reponse,recuperer_amis,username,ami1,ami2,ami3,ami4,ami5,ami6,ami7,ami8,ami9,ami10
```

<br>
si pas ok:
- Si on n'a aucune demande d'ami acceptée
- Receveur/Demandeur n'existe pas / est vide / contient une `,`

```
reponse,recuperer_amis,username,ami1,ami2,ami3,ami4,ami5,ami6,ami7,ami8,ami9,ami10,erreur
```


## Gestion des messages

### Envoi d'un message

> **client ➡️ serveur**
```
envoi_message,login,receveur,message
```

> **client ⬅️ serveur**

<br>

si ok:
```
reponse,envoi_message,login,receveur,ok
```
 
<br>

si pas ok:
- Login/Receveur n'existe pas / est vide / contient une `,`
- Pas ami avec le receveur

```
reponse,envoi_message,login,receveur,erreur
```


## Récupérer les messages envoyés/reçus

> **client ➡️ serveur**
```
demande_message,login,ami,id
```

> **client ⬅️ serveur**

<br>

si ok:
```
reponse,demande_message,login,ami,envoyeur,message,suite(=oui ou non)
```

<br>

si pas ok:
- Login/Receveur n'existe pas / est vide / contient une `,`
- Pas ami avec receveur
```
reponse,demande_message,login,ami,envoyeur,message,erreur
```

