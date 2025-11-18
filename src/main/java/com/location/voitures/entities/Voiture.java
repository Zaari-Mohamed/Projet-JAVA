package com.location.voitures.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "voitures")
public class Voiture implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marque", nullable = false, length = 50)
    private String marque;

    @Column(name = "modele", nullable = false, length = 50)
    private String modele;

    @Column(name = "annee", nullable = false)
    private Integer annee;

    @Column(name = "immatriculation", nullable = false, unique = true, length = 20)
    private String immatriculation;

    @Column(name = "taux_journalier", nullable = false)
    private Double tauxJournalier; // Crucial pour le calcul du coût dans M2

    @Column(name = "disponible", nullable = false)
    private Boolean disponible = true;

    // Relation inverse OneToMany vers Reservation (M2)
    @OneToMany(mappedBy = "voiture", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reservation> reservations = new HashSet<>();

    // Constructeurs
    public Voiture() {
    }

    public Voiture(String marque, String modele, Integer annee, String immatriculation, Double tauxJournalier, Boolean disponible) {
        this.marque = marque;
        this.modele = modele;
        this.annee = annee;
        this.immatriculation = immatriculation;
        this.tauxJournalier = tauxJournalier;
        this.disponible = disponible;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }
    public Integer getAnnee() { return annee; }
    public void setAnnee(Integer annee) { this.annee = annee; }
    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }
    public Double getTauxJournalier() { return tauxJournalier; }
    public void setTauxJournalier(Double tauxJournalier) { this.tauxJournalier = tauxJournalier; }
    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    public Set<Reservation> getReservations() { return reservations; }
    public void setReservations(Set<Reservation> reservations) { this.reservations = reservations; }
    
    // Méthode utilitaire pour ajouter une réservation
    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setVoiture(this);
    }
}