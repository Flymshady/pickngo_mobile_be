package cz.uhk.fim.bs.pickngo_mobile_be.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findCustomerByEmail(String email);
    Optional<Customer> findCustomerByEmailShort(String emailShort);
}
