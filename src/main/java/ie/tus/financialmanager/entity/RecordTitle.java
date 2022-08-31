package ie.tus.financialmanager.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table
public class RecordTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    private String title;
    private String type;//1 收入的title 2 支出的title

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="user_id")
    private UserInfo userInfo;

}
