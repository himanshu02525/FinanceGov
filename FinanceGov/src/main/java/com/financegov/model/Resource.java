
package com.financegov.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.financegov.enums.ResourceStatus;

@Entity
@Table(name = "Resource")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ResourceID")
    private Long resourceId;

    @NotNull(message = "Program ID is required")
    @Column(name = "ProgramID", nullable = false)
    private Long programId;

    @NotBlank(message = "Resource type is required")
    @Column(name = "Type", nullable = false)
    private String type;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private ResourceStatus status;
}
