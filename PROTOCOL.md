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
recuperer_demande,login
```

> **client ⬅️ serveur**
<br>

si ok:
```
reponse,recuperation_demande,login,demandeur,ok
```

<br>

si pas ok:
- Login n'existe pas / est vide / contient une `,`
- Le receveur n'a pas recu de demande d'ami de ce demandeur
```
# On met deux fois erreur pour faciliter le parsing côté client
reponse,recuperation_demande,login,erreur,erreur
# Si on n'a pas de demande d'ami
reponse,recuperation_demande,login,erreur,pas_de_demande_ami
```


### Acceptation d'une demande d'ami

> **client ➡️ serveur**
```
# Accepte la demande en ami
accepte_demande,receveur,demandeur,oui
# Refuse la demande en ami
accepte_demande,receveur,demandeur,non
```

> **client ⬅️ serveur**
<br>

si ok:
```
reponse,accepte_demande,receveur,demandeur,ok
```

<br>

si pas ok: 
**Reponse en cas d'erreur:**
- Demandeur n'a pas envoyé de demande d'ami
- Receveur/Demandeur n'existe pas / est vide / contient une `,`

```
reponse,accepte_demande,receveur,login_demandeur,erreur
```


### Récupération des demandes accepté
> La personnee récupère les demandes d'amis qu'ils avaient envoyées et qui ont été acceptées.

> **client ➡️ serveur**
```
recuperer_demande_accepte,login
```

> **client ⬅️ serveur**

<br>

si ok:
```
reponse,recuperer_demande_accepte,login,receveur
```

<br>
si pas ok:
- Si on n'a aucune demande d'ami acceptée
- Receveur/Demandeur n'existe pas / est vide / contient une `,`

```
reponse,demande_accepte,login,erreur
```

## Gestion des messages

### Envoi d'un message

> **client ➡️ serveur**
```
envoi_message,login,receveur,titre,message
```

> **client ⬅️ serveur**

<br>

si ok:
```
reponse,envoi_message,login,receveur,titre,message,ok
```
 
<br>

si pas ok:
- Login/Receveur n'existe pas / est vide / contient une `,`
- Pas ami avec le receveur

```
reponse,envoi_message,login,receveur,null,null,erreur
```


## Récupérer les messages envoyés/reçus

> **client ➡️ serveur**
```
recuperer_message,login,receveur
```

> **client ⬅️ serveur**

<br>

si ok:
```
reponse,recuperer_message,login,receveur,titre,message
```

<br>

si pas ok:
- Login/Receveur n'existe pas / est vide / contient une `,`
- Pas ami avec receveur
```
reponse,recuperer_message,login,receveur,erreur
```

