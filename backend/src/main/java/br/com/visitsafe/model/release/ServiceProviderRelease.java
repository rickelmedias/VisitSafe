package br.com.visitsafe.model.release;

import br.com.visitsafe.model.enums.ReleaseApprovalStatusEnum;
import br.com.visitsafe.model.visitor.ServiceProviderVisitor;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "service_provider_releases")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServiceProviderRelease extends Release {

    @ManyToOne(optional = false)
    private ServiceProviderVisitor visitor;

    private Boolean isExceptional;

    @Enumerated(EnumType.STRING)
    private ReleaseApprovalStatusEnum approvalStatus;

    private String rejectionJustification;
}