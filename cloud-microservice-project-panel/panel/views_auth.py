import datetime
import requests

from django.http import HttpResponse,HttpResponseBadRequest,HttpResponseRedirect
from django.shortcuts import render
from django.conf import settings
from http import cookies

from .exceptions import NoOrInvalidJwtException
from .forms import *
from .views import check_user_jwt_and_return_roles_and_jwt


def login_page(request):
    if request.method != 'POST':
        return render(request, "authModulePages/LoginPage.html")
    else:
        try:
            username = request.POST["username"]
            password = request.POST["password"]
        except:
            return HttpResponseBadRequest()

        if not username or not password or len(username) <= 0 or len(password) <= 0:
            return HttpResponseBadRequest()

        url = settings.AUTH_SERVICE_URL + '/login'
        jsonDict = {"username": username, "password": password}
        r = requests.get(url, json=jsonDict)
        print(url, r.text)
        if r.status_code == 200:
            jwt = r.json()["accessToken"]
            a = HttpResponseRedirect("/")
            c = cookies.SimpleCookie()
            c['SID'] = jwt
            c['SID']["path"] = '/'
            expires = datetime.datetime.utcnow() + datetime.timedelta(days=30)
            c['SID']['expires'] = expires
            header_value = str(c).replace("Set-Cookie: ", '')
            a["Set-Cookie"] = header_value
            return a
        else:
            return render(request, "authModulePages/LoginPage.html")


def logout(request):
    a = HttpResponseRedirect("login")
    c = cookies.SimpleCookie()
    c['SID'] = ""
    c['SID']["path"] = '/'
    c['SID']['expires'] = datetime.datetime.utcnow() - datetime.timedelta(days=1)
    header_value = str(c).replace("Set-Cookie: ", '')
    a["Set-Cookie"] = header_value
    return a


def register_student(request):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_ADMIN" not in role_list:
        return render(request, "NotLoggedInpage.html")

    if request.method == "POST":
        form = StudentRegisterForm(request.POST)
        if form.is_valid():
            url = settings.AUTH_SERVICE_URL + '/StudentSignUp'
            student_info = form.cleaned_data
            headers = {"Authorization": "Bearer " + jwt}
            r = requests.post(url, json=student_info, headers=headers)
            print(url, r.text)
            if r.status_code == 200:
                form = StudentRegisterForm()
                return render(request, "authModulePages/RegisterPage.html", {"form": form})
            else:
                return HttpResponse("Error signing up the user\n" + str(r.text))
        else:
            return HttpResponse("invalid form inputs : "+str(form.errors))
    else:
        form = StudentRegisterForm()
        return render(request, "authModulePages/RegisterPage.html", {"form": form})


def register_professor(request):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_ADMIN" not in role_list:
        return render(request, "NotLoggedInpage.html")

    if request.method == "POST":
        form = ProfessorRegisterForm(request.POST)
        if form.is_valid():
            url = settings.AUTH_SERVICE_URL + '/ProfessorSignUp'
            prof_info = form.cleaned_data
            headers = {"Authorization": "Bearer " + jwt}
            r = requests.post(url, json=prof_info, headers=headers)
            print(url, r.text)
            if r.status_code == 200:
                form = ProfessorRegisterForm()
                return render(request, "authModulePages/RegisterPage.html", {"form": form})
            else:
                return HttpResponse("Error signing up the user\n" + str(r.text))
        else:
            return HttpResponse("Invalid form data")

    else:
        form = ProfessorRegisterForm()
        return render(request, "authModulePages/RegisterPage.html", {"form": form})


def register_employee(request):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_ADMIN" not in role_list:
        return render(request, "NotLoggedInpage.html")

    if request.method == "POST":
        form = EmployeeRegisterForm(request.POST)
        if form.is_valid():
            url = settings.AUTH_SERVICE_URL +'/EmployeeSignUp'
            employee_info = form.cleaned_data
            headers = {"Authorization": "Bearer " + jwt}
            r = requests.post(url, json=employee_info, headers=headers)
            print(url, r.text)
            if r.status_code == 200:
                form = EmployeeRegisterForm()
                return render(request, "authModulePages/RegisterPage.html", {"form": form})
            else:
                return HttpResponse("Error signing up the user\n"+str(r.text))

    else:
        form = EmployeeRegisterForm()
        return render(request, "authModulePages/RegisterPage.html", {"form": form})


def add_role(request):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_ADMIN" not in role_list:
        return render(request, "NotLoggedInpage.html")

    if request.method == "POST":
        form = AddRoleForm(request.POST)
        if form.is_valid():
            url = settings.AUTH_SERVICE_URL +'/addRole'
            headers = {"Authorization": "Bearer " + jwt }
            params = {"username": form.cleaned_data["username"],
                      "role": form.cleaned_data["role"]}
            r = requests.get(url,  headers=headers, params=params)
            print(url, r.text)
            if r.status_code == 200:
                form = AddRoleForm()
                return render(request, "authModulePages/RegisterPage.html", {"form": form})
            else:
                return HttpResponse("Error adding role:\n"+str(r.text))

    else:
        form = AddRoleForm()
        return render(request, "authModulePages/RegisterPage.html", {"form": form})


def get_student_list(request):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_ADMIN" not in role_list and "ROLE_EMPLOYEE" not in role_list:
        return render(request, "NotLoggedInpage.html")

    url = settings.AUTH_SERVICE_URL +'/getStudents'
    headers = {"Authorization": "Bearer " + jwt}
    r = requests.get(url, headers=headers)
    print(url, r.text)
    sl = r.json()
    return render(request, "authModulePages/StudentListPage.html", context={"userList" : sl})


def get_professor_list(request):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_ADMIN" not in role_list and "ROLE_EMPLOYEE" not in role_list:
        return render(request, "NotLoggedInpage.html")

    url = settings.AUTH_SERVICE_URL +'/getProfessors'
    headers = {"Authorization": "Bearer " + jwt}
    r = requests.get(url, headers=headers)
    print(url, r.text)
    pl = r.json()
    return render(request, "authModulePages/ProfessorListPage.html", context={"userList": pl})


def get_employee_list(request):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_ADMIN" not in role_list:
        return render(request, "NotLoggedInpage.html")
    url = settings.AUTH_SERVICE_URL +'/getEmployees'
    headers = {"Authorization": "Bearer " + jwt}
    r = requests.get(url, headers=headers)
    print(url, r.text)
    el = r.json()
    return render(request, "authModulePages/EmployeeListPage.html", context={"userList": el})


def delete_user(request):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    if "ROLE_ADMIN" not in role_list:
        return render(request, "NotLoggedInpage.html")

    if request.method=='POST':
        form = DeleteUserForm(request.POST)
        if form.is_valid():
            url = settings.AUTH_SERVICE_URL + '/DeleteUser'
            headers = {"Authorization": "Bearer " + jwt}
            params = {"username": form.cleaned_data["username"]}
            r = requests.post(url, headers=headers,params=params)
            print(url, r.text)
            return HttpResponse(r)
        else:
            form = DeleteUserForm()
            return render(request, "authModulePages/DeleteUserPage.html", {"form": form})
    else:
        form = DeleteUserForm()
        return render(request, "authModulePages/DeleteUserPage.html",{"form":form})