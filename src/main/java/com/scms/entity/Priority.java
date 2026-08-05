package com.scms.entity;

import com.scms.enums.PriorityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "priorities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "complaints")
public class Priority extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 20)
    private PriorityType name;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer level;

    @Size(max = 255)
    @Column(length = 255)
    private String description;

    @Column(name = "response_sla_hours")
    private Integer responseSlaHours;

    @Builder.Default
    @OneToMany(mappedBy = "priority", cascade = CascadeType.ALL)
    private List<Complaint> complaints = new ArrayList<>();
}