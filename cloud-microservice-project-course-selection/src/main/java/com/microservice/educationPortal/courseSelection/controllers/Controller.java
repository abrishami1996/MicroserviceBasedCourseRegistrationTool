package com.microservice.educationPortal.courseSelection.controllers;

import com.microservice.educationPortal.courseSelection.models.TakenCourse;
import com.microservice.educationPortal.courseSelection.repository.TakenCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {

    @Autowired
    TakenCourseRepository repository;

    @Value("${AuthServiceIp}")
    private String authServiceIp;

    @Value("${CourseServiceIp}")
    private String courseServiceIp;

    @Value("${AuthServicePort}")
    private String authServicePort;

    @Value("${CourseServicePort}")
    private String courseServicePort;

    private String getRole(String jwttoken){
        final String uri = "http://"+ authServiceIp +":"+authServicePort+"/getRole";
        System.out.println(jwttoken);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwttoken);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        String s = restTemplate.exchange(uri, HttpMethod.GET,entity,String.class).getBody();
        return s.substring(1,s.length()-1);
    }

    private boolean studentExits(String jwttoken, long studentNumber) {
        final String uri = "http://"+ authServiceIp +":"+authServicePort+"/studentExists?studentNumber="+studentNumber;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwttoken);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        String s = restTemplate.exchange(uri, HttpMethod.GET,entity,String.class).getBody();
        if(s == null) throw new IllegalStateException();
        if("true".equalsIgnoreCase(s)) return true;
        else if("false".equalsIgnoreCase(s)) return false;
        throw new IllegalStateException();
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

    private boolean courseExists(int courseCode){
        final String uri = "http://"+ courseServiceIp +":"+courseServicePort+"/exists?courseCode="+courseCode;
        RestTemplate restTemplate = new RestTemplate();
        String s = restTemplate.getForObject(uri,String.class);
        System.out.println(s);
        if("ok".equals(s)){
            return true;
        }
        return false;

    }


    /**
     * This API is used by Students for taking courses
     *
     * @param jwttoken The JWT token of the student taking a course
     * @param courseCode The course id that the student wants to take
     * @return "ok" if successful,
     * "courseNotFound" if invalid courseCode is given,
     * "not authenticated" if the given jwt does not belong to a student,
     * "course already selected" if the course is already taken by the student
     */
    @GetMapping(value = "/takecourse")
    public String takeCourse(String jwttoken, int courseCode){
        String role = getRole(jwttoken);
        if(role.equals("ROLE_STUDENT")) {
            if(courseExists(courseCode)) {
                long studentNumber = getUserNumber(jwttoken);
                if(repository.findByCourseCodeAndStudentNumber(courseCode,studentNumber) != null){
                    return "course already selected";
                }

                TakenCourse tk = new TakenCourse(studentNumber, courseCode);
                repository.save(tk);
                return "ok";
            }
            else{
                return "courseNotFound";
            }
        }
        return "not authenticated";
    }

    /**
     * This API is used by Admins or Employees to take courses
     *
     * @param jwttoken The JWT token of Admin of Employee taking a course for a student
     * @param courseCode The course id that the student wants to take
     * @param studentNumber The id of the student that are taking course for
     * @return "ok" if successful,
     * "courseNotFound" if invalid courseCode is given,
     * "not authenticated" if the given jwt does not belong to a student,
     * "course already selected" if the course is already taken by the student
     */
    @GetMapping(value = "/takecourseforstudent")
    public String takeCourseForStudent(String jwttoken, int courseCode,long studentNumber){
        String role = getRole(jwttoken);
        if( role.equals("ROLE_ADMIN") || role.equals("ROLE_EMPLOYEE")) {
            if(courseExists(courseCode)) {
                if(studentExits(jwttoken, studentNumber)) {
                    if (repository.findByCourseCodeAndStudentNumber(courseCode, studentNumber) != null) {
                        return "course already selected";
                    }

                    TakenCourse tk = new TakenCourse(studentNumber, courseCode);
                    repository.save(tk);
                    return "ok";
                }
                else return "student not exists";
            }
            else return "courseNotFound";
        }
        return "not authenticated";
    }


    @GetMapping(value = "/removetakencourse")
    public String removeTakenCourse(String jwttoken, int courseCode){
        String role = getRole(jwttoken);
        if(role.equals("ROLE_STUDENT")) {
            long studentNumber = getUserNumber(jwttoken);
            try {
                TakenCourse tk = repository.findByCourseCodeAndStudentNumber(courseCode,studentNumber);
                repository.deleteById(tk.getId());
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return "ok";
        }
        return "not authenticated";
    }


    @GetMapping(value = "/removetakencourseforstudent")
    public String removeTakenCourseForStudent(String jwttoken, long studentNumber, int courseCode){
        String role = getRole(jwttoken);
        if( role.equals("ROLE_ADMIN") || role.equals("ROLE_EMPLOYEE")) {
//            if(studentExists(studentNumber)){
                if(courseExists(courseCode)) {
                    TakenCourse tk = repository.findByCourseCodeAndStudentNumber(courseCode,studentNumber);
                    repository.deleteById(tk.getId());
                    return "ok";
                }
//            }
            return "invalid input";
        }
        return "not authenticated";
    }

    /**
     * This method returns the list of courses that are taken by a student
     * This API is available for Admins, Employees, and the student it self
     *
     * @param jwttoken This parameter is used for checking if the person accessing this API is allowed to do so
     * @param studentNumber The student ID
     * @return the list of courses taken by the specified student as a String
     */
    @GetMapping(value = "/getstudentcourseslist")
    public String getStudentCoursesList(String jwttoken,long studentNumber){
        String role = getRole(jwttoken);
        if(role.equals("ROLE_STUDENT") || role.equals("ROLE_ADMIN") || role.equals("ROLE_EMPLOYEE")) {
            // TODO check the student is the one given as the parameter
            List<TakenCourse> l = repository.findByStudentNumber(studentNumber);
            ArrayList<Integer> resList = new ArrayList<Integer>();
            for(int i=0;i<l.size();i++){
                resList.add(l.get(i).getCourseCode());
            }
            return resList.toString();
        }
        return "not authenticated";
    }


    /**
     * Get The list of students that have taken a course
     * This API is available for Admins, Employees and Professors
     *
     * @param jwttoken This parameter is used for checking if the person accessing this API is allowed to do so
     * @param courseCode The course ID
     * @return the list of students taking this course as a String
     */
    @GetMapping(value = "/getcoursememberlist")
    public String getCourseMembersList(String jwttoken, int courseCode){
        String role = getRole(jwttoken);
        if(role.equals("ROLE_PROFESSOR") || role.equals("ROLE_ADMIN") || role.equals("ROLE_EMPLOYEE")) {
            List<TakenCourse> l = repository.findByCourseCode(courseCode);
            ArrayList<Long> resList = new ArrayList<>();
            for(int i=0;i<l.size();i++){
                resList.add(l.get(i).getStudentNumber());
            }
            return resList.toString();
        }
        return "not authenticated";
    }


}
