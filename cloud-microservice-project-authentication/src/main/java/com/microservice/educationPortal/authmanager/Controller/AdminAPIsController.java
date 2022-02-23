package com.microservice.educationPortal.authmanager.Controller;

import com.microservice.educationPortal.authmanager.Model.*;
import com.microservice.educationPortal.authmanager.SignUpRequestClasses.EmployeeRequest;
import com.microservice.educationPortal.authmanager.SignUpRequestClasses.ProfessorRequest;
import com.microservice.educationPortal.authmanager.SignUpRequestClasses.StudentRequest;
import com.microservice.educationPortal.authmanager.repository.RoleRepository;
import com.microservice.educationPortal.authmanager.repository.FacultyEmployeeRepository;
import com.microservice.educationPortal.authmanager.repository.ProfessorRepository;
import com.microservice.educationPortal.authmanager.repository.StudentRepository;
import com.microservice.educationPortal.authmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;


@RestController
public class AdminAPIsController {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    FacultyEmployeeRepository facultyEmployeeRepository;

    @Autowired
    RoleRepository roleRepository;

    private AuthenticationManager authenticationManager;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/StudentSignUp", method = RequestMethod.POST)
    public String StudentSignUp(@RequestBody StudentRequest req) {
        if (userRepository.existsByUsername(Long.toString(req.getStudentNumber()))) {
            return ("Fail");
        } else {
            User user = new User();
            user.setTypeOfUser("Student");
            Student student = new Student(req.getStudentNumber(), req.getFirstName(), req.getLastName(), req.getMajor(), req.getEntranceYear(), req.getEducationalState()
            );
            Role userRole = roleRepository.findByName("ROLE_STUDENT");
            user.setRoles(Collections.singleton(userRole));
            user.setUsername(Long.toString(student.getStudentNumber()));
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            studentRepository.save(student);
            user.setFK(student.getId());
            userRepository.save(user);
            return ("success");
        }


    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/ProfessorSignUp", method = RequestMethod.POST)
    public String ProfessorSignUp(@RequestBody ProfessorRequest req) {
        if (userRepository.existsByUsername(Long.toString(req.getProfessorNumber()))) {
            return ("Fail");
        } else {
            User user = new User();
            user.setTypeOfUser("Professor");
            Professor professor = new Professor(req.getProfessorNumber(), req.getFirstName(), req.getLastName(), req.getDepartment(), req.getPosition(), req.getField());
            Role userRole = roleRepository.findByName("ROLE_PROFESSOR");
            user.setRoles(Collections.singleton(userRole));
            user.setUsername(Long.toString(professor.getProfessorNumber()));
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            professorRepository.save(professor);
            user.setFK(professor.getId());
            userRepository.save(user);
            return ("success");
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/EmployeeSignUp", method = RequestMethod.POST)
    public String EmployeeSignUp(@RequestBody EmployeeRequest req) {
        if (userRepository.existsByUsername(Long.toString(req.getEmployeeNumber()))) {
            return ("Fail");
        } else {
            User user = new User();
            user.setTypeOfUser("Employee");
            FacultyEmployee employee = new FacultyEmployee(req.getEmployeeNumber(), req.getFirstName(), req.getLastName());
            Role userRole = roleRepository.findByName("ROLE_EMPLOYEE");
            user.setRoles(Collections.singleton(userRole));
            user.setUsername(Long.toString(employee.getEmployeeNumber()));
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            facultyEmployeeRepository.save(employee);
            user.setFK(employee.getId());
            userRepository.save(user);
            return ("success");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/DeleteUser", method = RequestMethod.POST)
    public String DeleteUser(@RequestParam String username) {

        if (userRepository.existsByUsername(username)) {
            User u = userRepository.findByUsername(username);
            int key=u.getFK();
            if(u.getTypeOfUser().equals("Student")){
                studentRepository.deleteById(key);
            }
            else if(u.getTypeOfUser().equals("Professor")){
                professorRepository.deleteById(key);
            }
            else if(u.getTypeOfUser().equals("Employee")){
                facultyEmployeeRepository.deleteById(key);
            }

            userRepository.deleteByUsername(username);

            return "ok";

        } else {
            return "کاربری با این شماره وجود ندارد";
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/addRole")
    public String AddRole(@RequestParam String username, @RequestParam String role){

        if (userRepository.existsByUsername(username)) {
            User user=userRepository.findByUsername(username);
            System.out.println(user.getUsername());
            Role theNewRole = roleRepository.findByName(role);
            if(theNewRole == null){
                return "role not found";
            }
            System.out.println(theNewRole);
            if (! user.getRoles().contains(theNewRole)){
                user.getRoles().add(theNewRole);
                userRepository.save(user);
                return "ok";
            }
            else{
                return "کاربر نقش را داشته است";
            }
        }
        return "user not found";

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/getProfessors" , method = RequestMethod.GET)
    public List<Professor> getProfessors()
    {
        return professorRepository.findAll();
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/getEmployees" , method = RequestMethod.GET)
    public List<FacultyEmployee> getEmployees()
    {
        return facultyEmployeeRepository.findAll();
    }
}