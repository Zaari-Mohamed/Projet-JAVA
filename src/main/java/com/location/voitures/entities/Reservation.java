package com.location.voitures.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation implements Serializable {

    public enum StatutReservation {
        EN_ATTENTE, CONFIRMEE, ANNULEE, TERMINEE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voiture_id", nullable = false)
    private Voiture voiture;

    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDateTime dateFin;

    @Column(name = "cout_total", nullable = false)
    private Double coutTotal;

    @Enumerated(EnumType.STRING)
    // Enlever le nullable=false si vous utilisez la valeur par défaut dans le code
    // Java
    // Si la colonne est NOT NULL, l'initialisation est obligatoire.
    @Column(name = "statut_reservation", length = 20)
    private StatutReservation statut;

    // Constructeurs
    public Reservation() {
        // CORRECTION : Initialiser le statut par défaut pour éviter l'erreur SQL NOT
        // NULL
        this.statut = StatutReservation.EN_ATTENTE;
    }

    // Ce constructeur permet de créer une réservation avec un statut spécifié
    public Reservation(Admin admin, Voiture voiture, LocalDateTime dateDebut, LocalDateTime dateFin,
            Double coutTotal, StatutReservation statut) {
        this.admin = admin;
        this.voiture = voiture;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.coutTotal = coutTotal;
        this.statut = statut;
    }

    // Constructeur souvent utilisé pour la création initiale
    public Reservation(Admin admin, Voiture voiture, LocalDateTime dateDebut, LocalDateTime dateFin,
            Double coutTotal) {
        this(admin, voiture, dateDebut, dateFin, coutTotal, StatutReservation.EN_ATTENTE);
    }


    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Voiture getVoiture() {
        return voiture;
    }

    public void setVoiture(Voiture voiture) {
        this.voiture = voiture;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public Double getCoutTotal() {
        return coutTotal;
    }

    public void setCoutTotal(Double coutTotal) {
        this.coutTotal = coutTotal;
    }

    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

	
	public void ajouter(){
		new ReservationDAO().save(this);;
	}

	public void supprimer(){
		new ReservationDAO().delete(this);;
	}

}
