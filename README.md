<div align="center">

# JavaChat
Simple social network written in Java

</div>



## 🚀 Getting Started
> **You'll need to install ant first.**

```bash
# Compile and run the server
make build

# Run the client
make client
```

## 📝 Technical details

- Le serveur est dans la dir `server/` et a pour package com.server. 
- Le client est dans la dir `client/` et a pour package com.assembleurnational.javachatclient.
- Le serveur écoute sur le port 1337.

## 🧪 Test

```bash
# Lancer le serveur
make

# Interagir avec lui
make client
# Ou via le script prévu à cet effet
python3 script.py
# Ou en plus bas level
nc -vu 127.0.0.1 1337
```