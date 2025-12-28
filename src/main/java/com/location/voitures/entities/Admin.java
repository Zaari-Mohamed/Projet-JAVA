package com.location.voitures.entities;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Utilisateur {
    @Column(name = "niveau_acces")
    private Integer niveauAcces;
    public Admin() {}
    public Integer getNiveauAcces() { return niveauAcces; }
    public void setNiveauAcces(Integer niveauAcces) { this.niveauAcces = niveauAcces; }
}
