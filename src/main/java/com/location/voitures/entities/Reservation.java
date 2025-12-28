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
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;
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
    @Column(name = "statut_reservation", length = 20)
    private StatutReservation statut;
    public Reservation() {
        this.statut = StatutReservation.EN_ATTENTE;
    }
    public Reservation(Utilisateur utilisateur, Voiture voiture, LocalDateTime dateDebut, LocalDateTime dateFin,
            Double coutTotal, StatutReservation statut) {
        this.utilisateur = utilisateur;
        this.voiture = voiture;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.coutTotal = coutTotal;
        this.statut = statut;
    }
    public Reservation(Utilisateur utilisateur, Voiture voiture, LocalDateTime dateDebut, LocalDateTime dateFin,
            Double coutTotal) {
        this(utilisateur, voiture, dateDebut, dateFin, coutTotal, StatutReservation.EN_ATTENTE);
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
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
}
