package my.example.jerseyspring.repository;


import my.example.jerseyspring.repository.exceptions.DuplicateIdException;
import my.example.jerseyspring.repository.exceptions.EmployeeNameNotFoundException;
import my.example.jerseyspring.repository.exceptions.EmployeeNotFoundException;
import my.example.jerseyspring.repository.models.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepository {
    private static EmployeeRepository instance;

    private List<Employee> employeeList;

    public EmployeeRepository() {
        employeeList = new ArrayList<>();
        employeeList.add(new Employee(3, "Cupcake"));
        employeeList.add(new Employee(4, "Donuts"));
        employeeList.add(new Employee(5, "Eclair"));
        employeeList.add(new Employee(8, "Froyo"));
        employeeList.add(new Employee(9, "Gingerbread"));

        instance = this;
    }

    static public EmployeeRepository getInstance() {
        return instance;
    }

    public List<Employee> selectAll() {
        return employeeList;
    }

    public Employee select(int id) {
        for (Employee employee : employeeList) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        throw new EmployeeNotFoundException();
    }

    public synchronized void insert(int id, String firstName) {
        try {
            select(id);
        } catch (EmployeeNotFoundException e) {
            // いなければ追加できる
            employeeList.add(new Employee(id, firstName));
            return;
        }
        // 同じIDが存在したら追加できない
        throw new DuplicateIdException();
    }

    public synchronized void update(int id, String firstName) {
        Employee employee = select(id);
        employee.setFirstName(firstName);
    }

    public synchronized void delete(int id) {
        Employee employee = select(id);
        employeeList.remove(employee);
    }

    public List<Employee> search(String name) {
        List<Employee> list = new ArrayList<>();
        for (Employee employee : employeeList) {
            if (employee.getFirstName().contains(name)) {
                list.add(employee);
            }
        }
        if (list.size() > 0) return list;
        throw new EmployeeNameNotFoundException(name);
    }
}
