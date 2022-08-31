package ie.tus.financialmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@Entity
//@Data
//@Table
public class House {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
    private Integer id;

    private Integer ownerid;   // 家主编号
    private String housename;  // 家庭名称
    private String address;    // 家庭地址

}
