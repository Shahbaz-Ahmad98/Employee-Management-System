package net.javaguides.ems_backened.repository;

import net.javaguides.ems_backened.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
