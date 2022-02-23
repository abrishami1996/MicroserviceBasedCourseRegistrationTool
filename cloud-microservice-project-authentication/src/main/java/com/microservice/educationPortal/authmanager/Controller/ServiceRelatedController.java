package com.microservice.educationPortal.authmanager.Controller;
import com.microservice.educationPortal.authmanager.Model.FacultyEmployee;
import com.microservice.educationPortal.authmanager.Model.Professor;
import com.microservice.educationPortal.authmanager.Model.Student;
import com.microservice.educationPortal.authmanager.Model.User;
import com.microservice.educationPortal.authmanager.exeption.ResourceNotFoundException;
import com.microservice.educationPortal.authmanager.repository.FacultyEmployeeRepository;
import com.microservice.educationPortal.authmanager.repository.ProfessorRepository;
import com.microservice.educationPortal.authmanager.repository.StudentRepository;
import com.microservice.educationPortal.authmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
public class ServiceRelatedController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    ProfessorRepository professorRepository;
    @Autowired
    FacultyEmployeeRepository facultyEmployeeRepository;

    @GetMapping("/getRole")
    public String GetRole(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        } else if (request.isUserInRole("ROLE_STUDENT")) {
            return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        } else if (request.isUserInRole("ROLE_PROFESSOR")) {
            return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        } else if (request.isUserInRole("ROLE_EMPLOYEE")) {
            return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        }

        return HttpStatus.FORBIDDEN.toString();
    }

    @GetMapping("/getUserDetails")
    public Object GetUserDetails(HttpServletRequest request) {
        String username=request.getUserPrincipal().getName();
        User user=userRepository.findByUsername(username);
        int fk=user.getFK();
        if(request.isUserInRole("ROLE_STUDENT"))
        {
            Student student = studentRepository.findById(fk);
            return student;
        }
        else if(request.isUserInRole("ROLE_EMPLOYEE"))
        {
            FacultyEmployee facultyEmployee = facultyEmployeeRepository.findById(fk);
            return facultyEmployee;
//            return (facultyEmployee.getFirstName()+" "+facultyEmployee.getLastName());
        }
        else if(request.isUserInRole("ROLE_PROFESSOR"))
        {
            Professor professor = professorRepository.findById(fk);
            return professor;
//            return (professor.getFirstName()+" "+professor.getLastName());
        }
        else return user;
    }


    @GetMapping("/getUserId")
    public String getuserID(HttpServletRequest request) {
        String username=request.getUserPrincipal().getName();
        User user=userRepository.findByUsername(username);
        return user.getId().toString();
    }


    @GetMapping("/getUserFK")
    public String getuserFK(HttpServletRequest request) {
        String username=request.getUserPrincipal().getName();
        User user=userRepository.findByUsername(username);
        return user.getFK()+"";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_EMPLOYEE')")
    @RequestMapping(value = "/getStudents" , method = RequestMethod.GET)
    public List<Student> getStudents()
    {
        return studentRepository.findAll();
    }

    @GetMapping("/getUserNumber")
    public String getUserNumber(HttpServletRequest request){
        String username=request.getUserPrincipal().getName();
        User user=userRepository.findByUsername(username);
        switch (user.getTypeOfUser()) {
            case "Student":
                Student stud = studentRepository.findById(user.getFK());
                return Long.toString(stud.getStudentNumber());
            case "Professor":
                Professor prof = professorRepository.findById(user.getFK());
                return Long.toString(prof.getProfessorNumber());
            case "Employee":
                FacultyEmployee employee = facultyEmployeeRepository.findById(user.getFK());
                return Long.toString(employee.getEmployeeNumber());
        }
        throw new RuntimeException("User not found");
//        throw new ResourceNotFoundException("Student","StudentNumber",user);

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_EMPLOYEE')")
    @GetMapping("/studentExists")
    public boolean studentExists(long studentNumber){
        return studentRepository.existsByStudentNumber(studentNumber);
    }



}
