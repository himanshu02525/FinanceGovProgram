package com.financegov.model;

import java.time.LocalDateTime;

import com.financegov.enums.*;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "disclosures")
@Data
@NoArgsConstructor
public class Disclosure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long disclosureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id", nullable = false)
    private CitizenBusiness citizenBusiness;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private DisclosureType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private DisclosureStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime submissionDate = LocalDateTime.now();
}