package com.financegov.model;
 
import java.util.List;
 
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
 
@Entity
@Table(name = "citizen_business_NEW")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitizenBusiness {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entityId;
 
    @NotBlank(message = "Name is required")
    @Size(max = 15, message = "Name must not exceed 15 characters")
    private String name;
 
    @NotBlank(message = "Type is required")
    @Pattern(regexp = "^(Citizen|Business)$",
            message = "Type must be either Citizen or Business")
    private String type;
 
    private String address;
 
    @NotBlank(message = "Contact number is required")
    @Size(min = 10, max = 10, message = "Contact must be 10 digits")
    @Pattern(regexp = "^[0-9]+$", message = "Contact must contain only digits")
    private String contactInfo;
 
    private String status;
 
    @OneToMany(mappedBy = "citizenBusiness")
    private List<EntityDocument> documents;
}
 