import json

import requests

from django.http import HttpResponse, HttpResponseRedirect
from django.shortcuts import render
from django.conf import settings
from django.urls import reverse

from .exceptions import NoOrInvalidJwtException
from .views import check_user_jwt_and_return_roles_and_jwt, get_user_detail
from .forms import *


def get_all_available_courses(request):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    url = settings.COURSE_MANAGE_SERVICE_URL + '/getListOfCourses'
    r = requests.get(url)
    print(url, r.text)
    courses = r.json()
    return render(request, "courseManagePages/AvailableCoursesPage.html", {"courses": courses, "roleList": role_list})


def add_available_courses(request):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    # check user has needed authorization
    if "ROLE_ADMIN" not in role_list and "ROLE_EMPLOYEE" not in role_list:
        return render(request, "NotLoggedInpage.html")

    if request.method == 'POST':
        form = AddCourseForm(request.POST)
        if form.is_valid():
            url = settings.COURSE_MANAGE_SERVICE_URL + '/add'
            params = {"jwttoken": jwt}
            r = requests.post(url, params=params, json=form.cleaned_data)
            print(url, r.text)
            if r.status_code != 200:
                return HttpResponse("Error creating the course : " + str(r.text))
            form = AddCourseForm()
            return render(request, "courseManagePages/AddCourseToAvailableList.html",
                          {"form": form, "roleList": role_list})
        else:
            return HttpResponse("invalid form input")
    else:
        form = AddCourseForm()
        return render(request, "courseManagePages/AddCourseToAvailableList.html",
                      {"form": form, "roleList": role_list})


def edit_available_course(request, courseId):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    # check user has needed authorization
    if "ROLE_ADMIN" not in role_list and "ROLE_EMPLOYEE" not in role_list:
        return render(request, "NotLoggedInpage.html")

    if request.method == 'POST':
        form = AddCourseForm(request.POST)
        if form.is_valid():
            url = settings.COURSE_MANAGE_SERVICE_URL + '/edit'
            params = {"jwttoken": jwt}
            r = requests.post(url, params=params, json=form.cleaned_data)
            print(url, r.text)
            if r.status_code != 200:
                return HttpResponse("Error creating the course : " + str(r.text))
            return HttpResponseRedirect(reverse("availableCourses"))
        else:
            return HttpResponse("invalid form input")
    else:

        url = settings.COURSE_MANAGE_SERVICE_URL + "/getCourseInfo"
        params = {"courseCode": courseId}
        r = requests.get(url, params=params)
        print(r.text)
        course_info = json.loads(r.text)

        form = AddCourseForm()
        form.fields["name"].initial = course_info["name"]
        form.fields["code"].initial = course_info["code"]
        form.fields["creditHour"].initial = course_info["creditHour"]
        form.fields["department"].initial = course_info["department"]
        form.fields["year"].initial = course_info["year"]
        form.fields["semester"].initial = course_info["semester"]
        form.fields["classroom"].initial = course_info["classroom"]
        form.fields["professorId"].initial = course_info["professorId"]
        return render(request, "courseManagePages/AddCourseToAvailableList.html", {"form": form})


def remove_available_courses(request):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_ADMIN" not in role_list and "ROLE_EMPLOYEE" not in role_list:
        return render(request, "NotLoggedInpage.html")

    if request.method == 'POST':
        form = DeleteCourseForm(request.POST)
        if form.is_valid():
            url = settings.COURSE_MANAGE_SERVICE_URL + '/remove'
            r = requests.get(url, params={"jwttoken": jwt, "courseCode": form.cleaned_data["id"]})
            print(url, r.text)
            if r.status_code != 200:
                return HttpResponse("Error creating the course : " + str(r.text))
            form = DeleteCourseForm()
            return render(request, "courseManagePages/DeleteCoursePage.html", {"form": form})
        else:
            return HttpResponse("invalid form input")
    else:
        form = DeleteCourseForm()
        return render(request, "courseManagePages/DeleteCoursePage.html", {"form": form})


def get_thought_courses(request):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_PROFESSOR" not in role_list:
        return render(request, "NotLoggedInpage.html")

    prof_details = get_user_detail(jwt)
    professor_id = prof_details["professorNumber"]
    url = settings.COURSE_MANAGE_SERVICE_URL + "/getTaughtCourses"
    params = {"professorId": professor_id, 'jwttoken': jwt}
    r = requests.get(url, params=params)
    return HttpResponse(str(r.text))


def get_course_taught_by_professor(request, professor_id):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_ADMIN" not in role_list and "ROLE_EMPLOYEE" not in role_list:
        return render(request, "NotLoggedInpage.html")

    url = settings.COURSE_MANAGE_SERVICE_URL + "/getTaughtCoursesBy"
    params = {"professorId": professor_id, 'jwttoken': jwt}
    r = requests.get(url, params=params)
    print(url, r.text)
    return HttpResponse(r.text)
