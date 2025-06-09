package br.com.visitsafe.model.release;

import br.com.visitsafe.model.enums.ReleaseStatusEnum;
import br.com.visitsafe.model.unit.Unit;
import br.com.visitsafe.model.user.EmployeeUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;
@Entity
@Table(name = "releases")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
public abstract class Release {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private Unit unit;

    @Column(nullable = false)
    private OffsetDateTime validFrom;

    @Column(nullable = false)
    private OffsetDateTime validUntil;

    @Column(nullable = false)
    private OffsetDateTime dailyStart;

    @Column(nullable = false)
    private OffsetDateTime dailyEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReleaseStatusEnum status;

    @Column
    private OffsetDateTime checkinAt;

    @Column
    private OffsetDateTime checkoutAt;

    @ManyToOne
    private EmployeeUser checkinBy;

    @ManyToOne
    private EmployeeUser checkoutBy;


    @Column(updatable = false)
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = this.updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
