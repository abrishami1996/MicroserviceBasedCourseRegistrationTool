"""cloud_microservice_project_userpanel URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""

from django.contrib import admin
from django.urls import path

from panel import views, views_auth, views_course_manage, views_course_select

urlpatterns = [
    path('admin', admin.site.urls),


    path('login', views_auth.login_page, name="login"),
    path('logout', views_auth.logout, name="logout"),

    # Auth pages
    path('register/student', views_auth.register_student, name="registerStudent"),
    path('register/professor', views_auth.register_professor, name="registerProfessor"),
    path('register/employee', views_auth.register_employee, name="registerEmployee"),

    path('roles/add', views_auth.add_role, name="addRole"),

    path('student/all', views_auth.get_student_list, name="allStudents"),
    path('professor/all', views_auth.get_professor_list, name="allProfessors"),
    path('employee/all', views_auth.get_employee_list, name="allEmployees"),
    path('deleteUser', views_auth.delete_user, name="deleteUser"),


    # course manage pages
    path('courses/available/all', views_course_manage.get_all_available_courses, name="availableCourses"),
    path('courses/available/add', views_course_manage.add_available_courses, name="addAvailableCourse"),
    path('courses/available/remove', views_course_manage.remove_available_courses, name="removeAvailableCourse"),
    path('courses/available/<int:courseId>/edit', views_course_manage.edit_available_course, name="editAvailableCourse"),

    # course selection page
    path('courses/taking/<int:studentId>/all', views_course_select.allTakenCourses, name="allStudentTakenCourses"),
    path('courses/taking/<int:courseId>/add', views_course_select.take_course_as_student, name="addTakenCourse"),
    path('courses/taking/<int:courseId>/remove', views_course_select.drop_course_as_student, name="removeTakenCourse"),

    path('courses/taking/<int:courseId>/classList', views_course_select.classList, name="courseClassList"),
    path('courses/taught', views_course_manage.get_thought_courses, name="taughtCourses"),
    path('courses/taughtBy/<int:professor_id>', views_course_manage.get_course_taught_by_professor, name="taughtCoursesByProf"),

    path('courses/takeCourseForStudentForm', views_course_select.take_course_for_student_form_page , name="takeCourseForStudentForm"),
    path('courses/dropCourseForStudentForm', views_course_select.drop_course_for_student_form_page , name="dropCourseForStudentForm"),

    path('courses/forStudent/<int:student_id>/addCourse/<int:course_id>',
         views_course_select.take_course_for_student, name="takeCourseForStudent"),

    path('courses/forStudent/<int:student_id>/dropCourse/<int:course_id>',
         views_course_select.drop_course_for_student, name="dropCourseForStudent"),


    # home page
    path('', views.homePage, name="home")
]
