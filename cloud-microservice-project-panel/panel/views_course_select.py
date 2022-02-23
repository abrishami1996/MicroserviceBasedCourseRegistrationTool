import ast
import requests

from django.http import HttpResponse, HttpResponseRedirect
from django.shortcuts import render
from django.conf import settings
from django.urls import reverse

from panel.exceptions import NoOrInvalidJwtException
from panel.views import check_user_jwt_and_return_roles_and_jwt
from panel.forms import TakeOrDropCourseForStudnetForm


def allTakenCourses(request, studentId):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    user_is_admin_or_employee = False
    user_is_student = False
    if "ROLE_ADMIN" in role_list or "ROLE_EMPLOYEE" in role_list:
        user_is_admin_or_employee = True
    elif "ROLE_STUDENT" in role_list:
        user_is_student = True

    url = settings.COURSE_SELECTION_SERVICE_URL +'/getstudentcourseslist'
    params = {"jwttoken": jwt, "studentNumber": studentId}
    r = requests.get(url, params=params)
    print(url, r.text)
    context = {"list": ast.literal_eval(str(r.json())),
               "user_is_admin_or_employee": user_is_admin_or_employee,
               "user_is_student": user_is_student,
               "studentId": studentId}
    return render(request, "courseSelectionPages/takenCoursesByStudent.html", context=context)


def take_course_as_student(request, courseId ):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_STUDENT" not in role_list:
        return render(request, "NotLoggedInpage.html")

    url = settings.AUTH_SERVICE_URL + '/getUserNumber'
    headers = {"Authorization": "Bearer " + jwt}
    r2 = requests.get(url, headers=headers)
    print(url, r2.text)
    if r2.status_code != 200:
        return HttpResponse("student not recognized")

    studentId = int(r2.text)
    url = settings.COURSE_SELECTION_SERVICE_URL +'/takecourse'
    params = {"jwttoken": jwt, "courseCode": courseId}
    r1 = requests.get(url, params=params)
    print(url, r1.text)
    if r1.status_code == 200 and r1.text == "ok":
        return HttpResponseRedirect("/courses/taking/"+str(studentId)+"/all")
    else:
        return HttpResponse("Error tacking course : " + str(r1.text))


def drop_course_as_student(request, courseId):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_STUDENT" not in role_list:
        return render(request, "NotLoggedInpage.html")

    url = settings.AUTH_SERVICE_URL + '/getUserNumber'
    headers = {"Authorization": "Bearer " + jwt}
    r2 = requests.get(url, headers=headers)
    print(url, r2.text)
    if r2.status_code != 200:
        return HttpResponse("student not recognized")
    studentId = int(r2.text)

    url = settings.COURSE_SELECTION_SERVICE_URL +'/removetakencourse'
    params = {"jwttoken": jwt, "courseCode": courseId}
    r1 = requests.get(url, params=params)
    print(url, r1.text)
    if r1.status_code == 200 and r1.text == "ok":
        return HttpResponseRedirect("/courses/taking/" + str(studentId) + "/all")
    else:
        return HttpResponse("Error tacking course : " + str(r1.text))


def classList(request, courseId):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_ADMIN" not in role_list and "ROLE_EMPLOYEE" not in role_list and "ROLE_PROFESSOR" not in role_list:
        return render(request, "NotLoggedInpage.html")

    url = settings.COURSE_SELECTION_SERVICE_URL + '/getcoursememberlist'
    params = {"jwttoken": jwt, "courseCode": courseId}
    r = requests.get(url, params=params)
    print(url, r.text)
    r = str(r.json())
    list = ast.literal_eval(r)
    context = {"list": list}
    return render(request, "courseSelectionPages/takenCoursesByStudent.html", context=context)


def take_course_for_student(request, student_id, course_id):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_ADMIN" not in role_list and "ROLE_EMPLOYEE" not in role_list:
        return render(request, "NotLoggedInpage.html")
    url = settings.COURSE_SELECTION_SERVICE_URL + '/takecourseforstudent'
    params = {"jwttoken": jwt,
              "courseCode": course_id,
              "studentNumber": student_id}
    r2 = requests.get(url, params=params)
    print(url, r2.text)
    if r2.status_code == 200 and r2.text == "ok":
        return HttpResponseRedirect("/courses/taking/"+str(student_id)+"/all")
    else:
        return HttpResponse("Error tacking course : " + str(r2.text))


def drop_course_for_student(request, student_id, course_id):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_ADMIN" not in role_list and "ROLE_EMPLOYEE" not in role_list:
        return render(request, "NotLoggedInpage.html")

    url = settings.COURSE_SELECTION_SERVICE_URL + '/removetakencourseforstudent'
    params = {"jwttoken": jwt,
              "courseCode": course_id,
              "studentNumber": student_id}
    r2 = requests.get(url, params=params)
    print(url, r2.text)
    if r2.status_code == 200 and r2.text == "ok":
        return HttpResponseRedirect("/courses/taking/" + str(student_id) + "/all")
    else:
        return HttpResponse("Error tacking course : " + str(r2.text))


def take_course_for_student_form_page(request):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_ADMIN" not in role_list and "ROLE_EMPLOYEE" not in role_list:
        return render(request, "NotLoggedInpage.html")

    if request.method == "POST":
        form = TakeOrDropCourseForStudnetForm(request.POST)
        if form.is_valid():
            student_number = form.cleaned_data['student_number']
            course_code = form.cleaned_data['course_code']
            url = reverse('takeCourseForStudent', kwargs={'student_id': student_number, 'course_id': course_code})
            return HttpResponseRedirect(url)
        else:
            form = TakeOrDropCourseForStudnetForm()
            context = {"form": form, "buttonText": "take course"}
            return render(request, 'courseSelectionPages/takeOrDropCourseForStudentFormPage.html', context)
    else:
        form = TakeOrDropCourseForStudnetForm()
        context = {"form": form, "buttonText": "take course"}
        return render(request, 'courseSelectionPages/takeOrDropCourseForStudentFormPage.html', context)


def drop_course_for_student_form_page(request):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_ADMIN" not in role_list and "ROLE_EMPLOYEE" not in role_list:
        return render(request, "NotLoggedInpage.html")

    if request.method == "POST":
        form = TakeOrDropCourseForStudnetForm(request.POST)
        if form.is_valid():
            student_number = form.cleaned_data['student_number']
            course_code = form.cleaned_data['course_code']
            url = reverse('dropCourseForStudent', kwargs={'student_id': student_number, 'course_id': course_code})
            return HttpResponseRedirect(url)
        else:
            form = TakeOrDropCourseForStudnetForm()
            context = {"form": form, "buttonText": "drop course"}
            return render(request, 'courseSelectionPages/takeOrDropCourseForStudentFormPage.html', context)
    else:
        form = TakeOrDropCourseForStudnetForm()
        context = {"form": form, "buttonText": "drop course"}
        return render(request, 'courseSelectionPages/takeOrDropCourseForStudentFormPage.html', context)