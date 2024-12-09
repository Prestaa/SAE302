# Aspect protocolaire

## INSCRIPTION

### Demande d'inscription

**client ➡️ serveur**
```
inscription,login,mdp
```

**client ⬅️ serveur**
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

## CONNEXION

### Demande d'authentification

**client ➡️ serveur**
```
connexion,login,mdp
```

**client ⬅️ serveur**
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

## DEMANDE D'AMI

### Demande d'ami

**client ➡️ serveur**
```
demande_ami,demandeur,receveur
```


**client ⬅️ serveur**
<br>
si ok:
```
reponse,demande_ami,demandeur,receveur,ok
```

<br>
si pas ok:
- Demandeur/Receveur n'existe pas / est vide / contient une `,`
```
reponse,demande_ami,login,erreur
```

### Récupération des demandes d'amis
> Donne moi les demandes d'amis qui me concerne
> demandeur = celui qui demande en ami

**client ➡️ serveur**
```
recuperer_demande,login
```

**client ⬅️ serveur**
<br>
si ok:
```
reponse,recuperation_demande,login,demandeur
```

<br>
si pas ok:
- Login n'existe pas / est vide / contient une `,`
- Le receveur n'a pas recu de demande d'ami de ce demandeur
```
reponse,recuperation_demande,login,erreur
```

### Acceptation d'une demande d'ami

**client ➡️ serveur**
```
# Accepte la demande en ami
accepte_demande,receveur,demandeur,oui
# Refuse la demande en ami
accepte_demande,receveur,demandeur,non
```

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
> Le demandeur récupère les demandes d'amis qu'ils avaient envoyées et qui ont été acceptées.

**client ➡️ serveur**
```
# Le receveur a accepté la demande d'ami
reponse,demande_accepte,demandeur,receveur,oui
# Le receveur a refusé la demande d'ami
reponse,demande_accepte,demandeur,receveur,non
```


## GESTION DES MESSAGES

### Envoi d'un message

**client ➡️ serveur**
```
envoie_message,login,receveur,message
```

**client ⬅️ serveur**

<br>
si ok:
```
reponse,envoie_message,login,receveur,message
```
 
<br>
si pas ok:
- Login/Receveur n'existe pas / est vide / contient une `,`
- Pas ami avec le receveur

```
reponse,envoie_message,login,receveur,erreur
```

## Récupérer les messages envoyés/reçus

**client ➡️ serveur**
```
recuperer_message,login,receveur,message
```

**client ⬅️ serveur**

<br>
si ok:
```
reponse,recuperer_message,login,receveur,message
```

<br>
si pas ok:
- Login/Receveur n'existe pas / est vide / contient une `,`
- Pas ami avec receveur
```
reponse,recuperer_message,login,receveur,erreur
```