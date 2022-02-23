import requests

panel_ip = "192.168.39.30"
panel_port = 30500

admin_username = "admin"
admin_password = "navyblue"


employees_data = [
    {
        "firstName": "EmployeeFirstName1",
        "lastName": "EmployeeLastName1",
        "employeeNumber": "332201",
        "password": "pwd"
    },

    {
        "firstName": "EmployeeFirstName2",
        "lastName": "EmployeeLastName2",
        "employeeNumber": "332202",
        "password": "pwd"
    },

    {
        "firstName": "EmployeeFirstName3",
        "lastName": "EmployeeLastName3",
        "employeeNumber": "332203",
        "password": "pwd"
    },
]

professors_data = [
    {
        "firstName": "professorFirstName1",
        "lastName": "professorLastName1",
        "professorNumber": "749601",
        "password": "pwd",
        "department": "computer science",
        "field": "cloud computing",
        "position": "assossiate professor"
    },

    {
        "firstName": "professorFirstName2",
        "lastName": "professorLastName2",
        "professorNumber": "749602",
        "password": "pwd",
        "department": "computer science",
        "field": "cloud computing",
        "position": "assossiate professor"
    },

    {
        "firstName": "professorFirstName3",
        "lastName": "professorLastName3",
        "professorNumber": "749603",
        "password": "pwd",
        "department": "computer science",
        "field": "cloud computing",
        "position": "assossiate professor"
    },
]

students_data = [
    {
        "firstName":"studentFirstName1",
        "lastName":"studentLastName1",
        "studentNumber":"9312430001",
        "password":"pwd",
        "major":"computer science",
        "entranceYear":"2000",
        "educationalState":"normall",
    },

    {
        "firstName":"studentFirstName2",
        "lastName":"studentLastName2",
        "studentNumber":"9312430002",
        "password":"pwd",
        "major":"computer science",
        "entranceYear":"2000",
        "educationalState":"normall",
    },

    {
        "firstName":"studentFirstName3",
        "lastName":"studentLastName3",
        "studentNumber":"9312430003",
        "password":"pwd",
        "major":"computer science",
        "entranceYear":"2000",
        "educationalState":"normall",
    },
    {
        "firstName":"studentFirstName4",
        "lastName":"studentLastName4",
        "studentNumber":"9312430004",
        "password":"pwd",
        "major":"computer science",
        "entranceYear":"2000",
        "educationalState":"normall",
    },

    {
        "firstName":"studentFirstName5",
        "lastName":"studentLastName5",
        "studentNumber":"9312430005",
        "password":"pwd",
        "major":"computer science",
        "entranceYear":"2000",
        "educationalState":"normall",
    },

    {
        "firstName":"studentFirstName6",
        "lastName":"studentLastName6",
        "studentNumber":"9312430006",
        "password":"pwd",
        "major":"computer science",
        "entranceYear":"2000",
        "educationalState":"normall",
    },

    {
        "firstName":"studentFirstName7",
        "lastName":"studentLastName7",
        "studentNumber":"9312430007",
        "password":"pwd",
        "major":"computer science",
        "entranceYear":"2000",
        "educationalState":"normall",
    },

    {
        "firstName":"studentFirstName8",
        "lastName":"studentLastName8",
        "studentNumber":"9312430008",
        "password":"pwd",
        "major":"computer science",
        "entranceYear":"2000",
        "educationalState":"normall",
    },

    {
        "firstName":"studentFirstName9",
        "lastName":"studentLastName9",
        "studentNumber":"9312430009",
        "password":"pwd",
        "major":"computer science",
        "entranceYear":"2000",
        "educationalState":"normall",
    },

    {
        "firstName":"studentFirstName10",
        "lastName":"studentLastName10",
        "studentNumber":"9312430010",
        "password":"pwd",
        "major":"computer science",
        "entranceYear":"2000",
        "educationalState":"normall",
    },
]

courses_data = [
    {
        "name":"courseName1",
        "code":"4201",
        "creditHour":3,
        "department":"computer science",
        "year":"2000",
        "semester":"Fall",
        "classroom":"B35",
        "professorId":749601,
    },

    {
        "name":"courseName2",
        "code":"4202",
        "creditHour":3,
        "department":"computer science",
        "year":"2000",
        "semester":"Fall",
        "classroom":"B36",
        "professorId":749602,
    },

    {
        "name":"courseName3",
        "code":"4203",
        "creditHour":3,
        "department":"computer science",
        "year":"2000",
        "semester":"Fall",
        "classroom":"B37",
        "professorId":749603,
    },

    {
        "name":"courseName4",
        "code":"4204",
        "creditHour":3,
        "department":"computer science",
        "year":"2000",
        "semester":"Fall",
        "classroom":"B38",
        "professorId":749601,
    },

    {
        "name":"courseName5",
        "code":"4205",
        "creditHour":3,
        "department":"computer science",
        "year":"2000",
        "semester":"Fall",
        "classroom":"B39",
        "professorId":749602,
    },

    {
        "name":"courseName6",
        "code":"4206",
        "creditHour":3,
        "department":"computer science",
        "year":"2000",
        "semester":"Fall",
        "classroom":"B40",
        "professorId":749603,
    },
]

def get_csrf_in_page(path, jwt=None):
    url = "http://" + panel_ip + ":" + str(panel_port) + "/"+path
    if jwt:
        r = requests.get(url,cookies={"SID":jwt})
    else:
        r = requests.get(url)
    csrf_cookie = r.cookies["csrftoken"]
    l1 = r.text.find("csrfmiddlewaretoken")
    tag_start = r.text.rfind("<", l1 - 200, l1)
    tag_end = r.text.find(">", l1) + 1
    tag = r.text[tag_start:tag_end]
    vl = tag.find("value")
    endQute = tag.find('"', vl + 20)
    csrf_token = tag[vl + 7:endQute]
    return csrf_cookie,csrf_token

def login_to_panel_as_admin():
    url = "http://" + panel_ip + ":" + str(panel_port) + "/login"
    csrf_cookie,csrf_token = get_csrf_in_page("login")
    cookies = {'csrftoken': csrf_cookie}
    params = {"username": admin_username, "password": admin_password,"csrfmiddlewaretoken" : csrf_token}
    r = requests.request("POST",url, data=params, headers=dict(Referer=url), cookies=cookies, allow_redirects=False)
    return r.cookies["SID"]

admin_jwt = login_to_panel_as_admin()

##### Create Employees #####

def register_employee_on_panel(employee_info, jwt):
    url = "http://" + panel_ip + ":" + str(panel_port) + "/register/employee"
    csrf_cookie, csrf_token = get_csrf_in_page("register/employee",jwt)
    cookies = {'SID': jwt, "csrftoken": csrf_cookie}
    employee_info["csrfmiddlewaretoken"] = csrf_token
    r = requests.request("POST", url, data=employee_info, headers=dict(Referer=url), cookies=cookies)
    print(r.status_code)

    
for index, emp_info in enumerate(employees_data):
    print("registring Employee", (index+1))
    register_employee_on_panel(emp_info, admin_jwt)


##### Create Professors #####
def register_professor_on_panel(info, jwt):
    url = "http://" + panel_ip + ":" + str(panel_port) + "/register/professor"
    csrf_cookie, csrf_token = get_csrf_in_page("register/professor",jwt)
    cookies = {'SID': jwt, "csrftoken": csrf_cookie}
    info["csrfmiddlewaretoken"] = csrf_token
    r = requests.request("POST", url, data=info, headers=dict(Referer=url), cookies=cookies)
    print(r.status_code)

for index, prof_info in enumerate(professors_data):
    print("registring Professor", (index+1))
    register_professor_on_panel(prof_info, admin_jwt)

##### Create Students #####
def register_student_on_panel(student_info, jwt):
    url = "http://" + panel_ip + ":" + str(panel_port) + "/register/student"
    csrf_cookie, csrf_token = get_csrf_in_page("register/student",jwt)
    cookies = {'SID': jwt, "csrftoken": csrf_cookie}
    student_info["csrfmiddlewaretoken"] = csrf_token
    r = requests.request("POST", url, data=student_info, headers=dict(Referer=url), cookies=cookies)
    print(r.status_code)


for index, stud_info in enumerate(students_data):
    print("registring Student", (index+1))
    register_student_on_panel(stud_info, admin_jwt)

##### Create Coruses #####
def register_course(info, jwt):
    url = "http://" + panel_ip + ":" + str(panel_port) + "/courses/available/add"
    csrf_cookie, csrf_token = get_csrf_in_page("courses/available/add",jwt)
    cookies = {'SID': jwt, "csrftoken": csrf_cookie}
    info["csrfmiddlewaretoken"] = csrf_token
    r = requests.request("POST", url, data=info, headers=dict(Referer=url), cookies=cookies)
    print(r.status_code)

for index, course_info in enumerate(courses_data):
    print("registring course", (index+1))
    register_course(course_info, admin_jwt)
