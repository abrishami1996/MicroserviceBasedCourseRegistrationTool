import json
import requests

from django.shortcuts import render
from django.conf import settings

from panel.exceptions import NoOrInvalidJwtException


def __get_user_roles(jwt):
    url = settings.AUTH_SERVICE_URL + '/getRole'
    headers = {"Authorization": "Bearer " + jwt}
    r = requests.get(url, headers=headers)
    if r.status_code == 200:
        raw = r.text
        if raw == "403 FORBIDDEN":
            raise Exception
        rs = raw[1:len(raw) - 1]
        roles = rs.split(",")
        return roles
    else:
        raise Exception


def get_user_detail(jwt):
    url = settings.AUTH_SERVICE_URL + '/getUserDetails'
    headers = {"Authorization": "Bearer " + jwt}
    r = requests.get(url, headers=headers)
    user_info_json = json.loads(r.text)
    return user_info_json
    # return info


def check_user_jwt_and_return_roles_and_jwt(request):
    try:
        jwt = request.COOKIES["SID"]
    except :
        raise NoOrInvalidJwtException()
    try:
        role_list = __get_user_roles(jwt)
        if len(role_list) == 0:
            raise NoOrInvalidJwtException()
    except:
        raise NoOrInvalidJwtException()

    return role_list, jwt


def homePage(request):
    try:
        role_list, jwt = check_user_jwt_and_return_roles_and_jwt(request)
    except NoOrInvalidJwtException:
        return render(request, "NotLoggedInpage.html")

    context = {}
    if 'ROLE_ADMIN' in role_list:
        context["isAdmin"] = True
    else:
        context["isAdmin"] = False

    if 'ROLE_STUDENT' in role_list:
        context["isStudent"] = True
    else:
        context["isStudent"] = False

    if 'ROLE_PROFESSOR' in role_list:
        context["isProfessor"] = True
    else:
        context["isProfessor"] = False

    if 'ROLE_EMPLOYEE' in role_list:
        context["isEmployee"] = True
    else:
        context["isEmployee"] = False

    user_detail = get_user_detail(jwt)
    print(user_detail)
    context["userDetail"] = user_detail
    return render(request, "Home.html", context)
