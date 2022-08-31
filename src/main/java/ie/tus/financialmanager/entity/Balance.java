package ie.tus.financialmanager.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


public class Balance {

    private Double balance;

    private String startDate;

    private String endDate;

    private UserInfo userInfo;

}
