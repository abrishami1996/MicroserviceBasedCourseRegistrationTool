package com.microservice.educationPortal.courseManager.controller;

import com.microservice.educationPortal.courseManager.repository.CourseRepository;
import com.microservice.educationPortal.courseManager.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
public class CourseController {


    @Autowired
    CourseRepository repository;

    @Value("${AuthServiceIp}")
    private String authServiceIp;

    @Value("${AuthServicePort}")
    private String authServicePort;

    private String getRole(String jwttoken){
        final String uri = "http://"+authServiceIp+":"+authServicePort+"/getRole";
        System.out.println(jwttoken);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwttoken);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        String s = restTemplate.exchange(uri, HttpMethod.GET,entity,String.class).getBody().toString();
        return s.substring(1,s.length() -1);
    }

    private Long getUserNumber(String jwttoken){
        final String uri = "http://"+ authServiceIp +":"+authServicePort+"/getUserNumber";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwttoken);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        try {
            String res = restTemplate.exchange(uri, HttpMethod.GET,entity,String.class).getBody();
            return Long.parseLong(res);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * This method is used add a new Course
     * This method is only allowed to be invoked by Admins or Employees
     *
     * @param jwttoken The jwt token of the person who wants to use this API
     * @param course The information of the course we want to add
     * @return return "not authenticated" if the given jwt is invalid or does not belong to authorized role
     * "ok" if a course was successfully added,
     * "invalid course" if the course data is not valid
     * "course exists" if the course already exists.
     *
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String addCourse(String jwttoken, @RequestBody Course course) {
        if(getRole(jwttoken).equals("ROLE_ADMIN") || getRole(jwttoken).equals("ROLE_EMPLOYEE")) {
            if(course.getCode() <=0 || course.getName()==null){
                return "invalid course";
            }
            if(repository.findByCode(course.getCode())!=null){
                return "course exists";
            }
            repository.save(course);
            return "ok";
        }
        return "not authenticated";

    }


    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public String editCourse(String jwttoken, @RequestBody Course course){
        if (getRole(jwttoken).equals("ROLE_ADMIN") || getRole(jwttoken).equals("ROLE_EMPLOYEE")) {
            Course c =repository.findByCode(course.getCode());
            if ( c!= null) {
                course.setId(c.getId());
                repository.save(course);
                return "ok";
            }
            return "Not Found";
        }
        return "not authenticated";

    }

    @RequestMapping(value = "/remove")
    public String removeCourse(String jwttoken, int courseCode){
        if (getRole(jwttoken).equals("ROLE_ADMIN") || getRole(jwttoken).equals("ROLE_EMPLOYEE")){
            if(repository.findByCode(courseCode) != null){
                repository.deleteByCode(courseCode);
                return "removed";
            }
            return "Not Found";
        }
        return "not authenticated";

    }

    @RequestMapping(value = "/exists")
    public String courseExists(Integer courseCode){
        if(repository.findByCode(courseCode) != null){
            return "ok";
        }
        else{
            return "not found";
        }
    }

    /**
     * This method is used to get list of courses in the system
     * This method can be invoked by anybody
     * @return The List of courses
     */
    @RequestMapping(value = "/getListOfCourses")
    public List<Course> getListOfCourses(){
        return repository.findAll();
    }

    @RequestMapping(value="/getCourseInfo")
    public Course getCourseInfo(int courseCode){
        return repository.findByCode(courseCode);
    }

    @GetMapping(value = "/getTaughtCourses")
    public List<Course> getTaughtCourses(String jwttoken){
        if (getRole(jwttoken).equals("ROLE_PROFESSOR")) {
            long professorId = getUserNumber(jwttoken);
            return repository.findAllByProfessorId((int) professorId);
        }
        throw new RuntimeException("Not authenticated");
    }

    @GetMapping(value = "/getTaughtCoursesBy")
    public List<Course> getTaughtCoursesBy(String jwttoken, int professorId){
        if (getRole(jwttoken).equals("ROLE_ADMIN") || getRole(jwttoken).equals("ROLE_EMPLOYEE")) {
            return repository.findAllByProfessorId(professorId);
        }
        throw new RuntimeException("Not authenticated");
    }
}
