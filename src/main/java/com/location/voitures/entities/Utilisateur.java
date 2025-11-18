package com.location.voitures.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "utilisateur")
// Configuration pour l'héritage de Admin.java (M2)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_utilisateur", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("UTILISATEUR")
public class Utilisateur implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;
    
    // Le mot de passe stocké DOIT être le hash pour la sécurité (M2/AuthService)
    @Column(name = "mot_de_passe_hashed", nullable = false, length = 60) 
    private String motDePasseHashed; 
    
    @Column(name = "telephone", length = 20)
    private String telephone;

    // Relation inverse OneToMany vers Reservation (M2)
    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reservation> reservations = new HashSet<>();
    
    // Constructeurs
    public Utilisateur() {
    }

    public Utilisateur(String nom, String prenom, String email, String motDePasseHashed, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasseHashed = motDePasseHashed;
        this.telephone = telephone;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotDePasseHashed() { return motDePasseHashed; }
    public void setMotDePasseHashed(String motDePasseHashed) { this.motDePasseHashed = motDePasseHashed; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public Set<Reservation> getReservations() { return reservations; }
    public void setReservations(Set<Reservation> reservations) { this.reservations = reservations; }

    // Méthode utilitaire pour ajouter une réservation
    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setUtilisateur(this);
    }
}