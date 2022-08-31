package ie.tus.financialmanager.repositories;

import ie.tus.financialmanager.entity.Bill;
import ie.tus.financialmanager.entity.UserInfo;
import ie.tus.financialmanager.util.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Repository
public interface BillRepository extends JpaRepository<Bill,Integer>, JpaSpecificationExecutor<Bill> {
}
