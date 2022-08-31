package ie.tus.financialmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer ID;
    private String privilegeNumber;
    private String privilegeName;
    private String privilegeTipflag;
    private String privilegeTypeflag;
    private String privilegeUrl;
    private String icon;

    @ManyToMany(mappedBy = "privileges")
    @JsonIgnore
    private Set<Role> roles;

}
