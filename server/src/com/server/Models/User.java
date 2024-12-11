package com.server.Models;

import java.util.ArrayList;

import com.server.Server;

public class User {
    private String username, password;
    public int friend_number = 0;
    public Friend[] friends;
    
    public boolean connected = false;

    public User(String username, String password) {
        this.friends = new Friend[Server.MAX_FRIENDS];

        this.username = username;
        this.password = password;
    }

    public String get_username() {
        return this.username;
    }

    public boolean login(String username, String password) {
        return this.username.equals(this.username) && this.password.equals(password); 
    }

    
    /**
     * Permet d'envoyer une friend_request à un utilisateur
     * 
     * @param friend_user
     * @return
     */
    public boolean add_friend_request(User friend_user) {
        // On a atteint le quota max d'ami
        if(this.friend_number >= Server.MAX_FRIENDS) return false;
        
        // L'utilisateur essaye de s'ajouter en ami
        if(friend_user.get_username().equals(this.get_username())) return false;

        // On va chercher si on a déjà cet ami ou si on l'a déja demandé
        for(int i = 0; i <= this.friend_number; i++)
            // Si on a déjà ce user en ami on retourne false
            if(this.friends[i] != null) {
                Friend to_test_friend = this.friends[i];
                User to_test_user = to_test_friend.user;

                if(to_test_user.get_username().equals(friend_user.get_username())) return false;
            }
        
        Friend friend = new Friend(friend_user);
        friends[friend_number++] = friend;
        return true;
    }


    /**
     * Permet de supprimer (refuser) une friend_request reçue
     *  
     * @param to_delete_username
     * @return
     */
    public boolean delete_friend_request(String to_delete_username) {
        int id_to_delete = -1;

        for(int i = 0; i < this.friend_number; i++) {
            Friend friend = friends[i];
            User friend_user = friends[i].user;
        
            // !friend.is_friend() => c'est une friend request
            // friend_user....     => c'est le username de la personne qu'on ne souhaite pas accepter
            if(!friend.is_friend() && friend_user.get_username().equals(to_delete_username)) {
                id_to_delete = i;
            }
        }

        if(id_to_delete == -1) return false;

        // On décale tous les éléments après la friend request
        // de 1 à gauche
        for (int i = id_to_delete; i < this.friend_number - 1; i++) {
            this.friends[i] = this.friends[i + 1];
        }

        return true;

    }


    /**
     * Si on est sender de la friend request, 
     *      -> il faut ajouter l'ami dans la liste d'ami, si on est receiver
     *      -> sinon il faut juste toggle le state is_friend à true.
     * 
     * @param new_friend_user
     * @param sender        Définit si on est l'initiateur de la friend_request ou pas
     */
    public boolean add_friend(User new_friend_user, boolean sender) {
        int id_to_add = -1;

        // "Early-return" si on est sender
        if(sender) {
            // On est l'envoyeur, on doit ajouter l'ami dans notre liste d'ami
            Friend new_friend = new Friend(new_friend_user);
            new_friend.friendify();
            friends[friend_number] = new_friend;
            friend_number++;

            // On a ajouté l'ami on quitte donc la fonction
            return true;
        }

        // Sous entendu ici, si on est receiver grâce au early return

        for(int i = 0; i < this.friend_number; i++) {
            Friend friend = friends[i];
            User friend_user = friends[i].user;
        
            // !friend.is_friend() => c'est une friend request
            // friend_user....     => c'est le username de la personne qu'on ne souhaite pas accepter
            if(!friend.is_friend() && friend_user.get_username().equals(new_friend_user.get_username())) {
                id_to_add = i;
            }
        }

        // On essaye d'ajouter un ami qui nous a pas demandé en ami
        if(id_to_add == -1) return false;

        this.friends[id_to_add].friendify();
        return true;
    }


    /**
     * Permet d'envoyer un message à un ami. Sur un message on a deux user un sender
     * et nun receiver. On doit donc spécifier à l'appel de la méthode si l"utilisateur courant
     * est sender ou receiver.
     * 
     * @param message
     * @param is_sender
     * @return
     */
    public boolean send_message(Message message, boolean is_sender) {
        User to_search;

        // Si on est sender, on recupère le sender du message sinon on recupère le receiver
        to_search = (is_sender) ? message.receiver : message.sender;

        System.out.println(username + ", " + is_sender);

        for(int i = 0; i < this.friend_number; i++) {
            if(friends[i].user.get_username().equals(to_search.get_username())) {
                //System.out.println("Je suis " + get_username() + " j'add dans le tableau de " + friends[i].user.get_username());
                friends[i].message.add(message);
                return true;
            }
        }

        return false;
    }


    /**
     * Permet de récupérer tous les messages recus par un utilisateur, en vérifiant au passage
     * si la position demandé du message (0,1,2...) est dans le tableau ET en vérifiant qu'on est
     * bien ami avec l'utilisateur demandé.
     * 
     * @param friend_user
     * @param position
     * @return
     */
    public ArrayList<Message> get_messages(User friend_user, int position) {
        Friend friend = null;

        // On cherche si cet utilisateur est parmi nos amis
        for(int i = 0; i < this.friend_number; i++) {
            if(friends[i].user.get_username().equals(friend_user.get_username())) {
                friend = friends[i];
            }
        }

        if(friend == null || position >= friend.message.size()) {
            return null;
        }

        return friend.message;
    }


    public void leak_db() {
        //System.out.println("USERNAME: " + username + " PASSWORD: " + password);
        //System.out.println("-------------------------------------");
        for (int i = 0; i < friend_number; i++) {
            Friend tmp_friend = friends[i];
            
            if(!tmp_friend.is_friend())
                continue;
            
            //System.out.println("EST AMI: " + tmp_friend.user.username);
            //System.out.println("\nMESSAGE --");
            for (Message message : tmp_friend.message) {
                //System.out.println(message.sender.get_username() + " a dit " + message.body);
            }
        }
    }
} 
