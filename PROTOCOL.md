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
- Le mot de passe est incorrect
- Login avec une `,` dedans

```bash
reponse,connexion,login,erreur
```

<br><br>

## Demande d'ami

### Demande d'ami

> **client ➡️ serveur**
```bash
demande_ami,demandeur,receveur

# exemple: foo demande bar en ami
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

<br><br>

### Récupération des demandes d'amis
Récupération des personnes qui nous demandent en ami
<br>

> **client ➡️ serveur**
```bash
recuperer_demande,login,id

# Étant donné qu'on peut avoir plusieurs demande d'ami en cours, 
# on les index par ID
# exemple:
recuperer_demande,test,0
```


> **client ⬅️ serveur**
<br>

si ok:
```bash
# La suite peut être égal à oui ou à non 
# cela permet de savoir si on a d'autre demande d'ami pas
reponse,recuperation_demande,login,demandeur,suite
```

<br>

si pas ok:
- Login/Demandeur n'existe pas / est vide 
- Login/Demandeur contient une `,`
```bash
reponse,recuperation_demande,login,demandeur,erreur
```

<br>
si on n'a pas de demande d'ami:
```
reponse,recuperation_demande,login,demandeur,pas_de_demande_ami
```

<br><br>

### Supression d'un ami

> **client ➡️ serveur**
```bash
supprimer_ami,login,ami
```


> **client ⬅️ serveur**
<br>

si ok:
```bash
reponse,supprimer_ami,login,ami,ok
```

<br>

si pas ok:
- Login / Ami n'existe pas / est vide 
- Ami n'est pas un ami
```bash
reponse,supprimer_ami,login,ami,erreur
```

<br><br>

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

<br>

**Reponse en cas d'erreur:**
- Receveur/Demandeur n'existe pas / est vide 
- Demandeur n'a pas envoyé de demande d'ami

```bash
reponse,accepter_demande,receveur,demandeur,erreur
```

<br><br>

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

Les valeur de ami<x> seront `null` si on n'a pas d'ami, un retour comme celui-ci peut être observé:

```
reponse,recuperer_amis,username,Michel,John Doe,M.Spies,Hercule,null,null,null,null,null
```

<br>
si pas ok:
- Receveur/Demandeur n'existe pas / est vide 
- Receveur/Demande contient une `,`

```bash
reponse,recuperer_amis,username,ami1,ami2,ami3,ami4,ami5,ami6,ami7,ami8,ami9,ami10,erreur
```

<br><br>

## Gestion des messages

### Envoi d'un message

> **client ➡️ serveur**
```bash
envoi_message,login,receveur,message

# Exemple
# B envoi un message à A
envoi_message,b,a,Salut

# B envoi un message en broadcast (à tous ses amis)
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
- Login/Receveur contient une `,`
- Nous ne sommes pas ami avec le receveur

  
```bash
reponse,envoi_message,login,receveur,erreur
```

  <br><br>

## Récupérer les messages envoyés/reçus

> **client ➡️ serveur**
```bash
demande_message,login,ami,id

# Exemple pour récupèrer les deux mreiers messages envoyés par ami
demande_message,login,ami,0
demande_message,login,ami,1
```

> **client ⬅️ serveur**

<br>

si ok:
```bash
# Suite vaudra oui ou non, il indique si on a d'autre message après celui la
reponse,demande_message,login,ami,envoyeur,message,suite
```

<br>

si pas ok:
- Login/Receveur n'existe pas / est vide 
- Pas ami avec receveur
```bash
reponse,demande_message,login,ami,envoyeur,message,erreur
```

  <br><br>

## Gestion du compte

### Suppression du compte
> **client ➡️ serveur**
```bash
supprimer,login,password
```

> **client ⬅️ serveur**

<br>

si ok:
```bash
reponse,delete,login,ok
```

<br>

si pas ok:
- Login n'existe pas / est vide 
- Password incorrect
  
```bash
reponse,delete,login,erreur
```

