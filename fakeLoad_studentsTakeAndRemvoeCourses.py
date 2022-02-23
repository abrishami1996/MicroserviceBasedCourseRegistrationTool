from time import sleep
import requests
import _thread

panel_ip = "192.168.39.30"
panel_port = 30500

students_credentials = [
    { "username": "9312430001", "password": "pwd"},
    { "username": "9312430002", "password": "pwd"},
    { "username": "9312430003", "password": "pwd"},
    { "username": "9312430004", "password": "pwd"},
    { "username": "9312430005", "password": "pwd"},
    { "username": "9312430006", "password": "pwd"},
    { "username": "9312430007", "password": "pwd"},
    { "username": "9312430008", "password": "pwd"},
    { "username": "9312430009", "password": "pwd"},
    { "username": "9312430010", "password": "pwd"},
]

courses_codes = [
    4201,
    4202,
    4203,
    4204,
    4205,
    4206
]

students_jwts = []

def get_csrf_from_page(path,jwt=None):
    url = "http://" + panel_ip + ":" + str(panel_port) + "/"+path
    if jwt:
        r = requests.get(url,cookies={"SID":jwt})
    else:
        r = requests.get(url)
    # print(url)
    csrf_cookie = r.cookies["csrftoken"]
    l1 = r.text.find("csrfmiddlewaretoken")
    tag_start = r.text.rfind("<", l1 - 200, l1)
    tag_end = r.text.find(">", l1) + 1
    tag = r.text[tag_start:tag_end]
    vl = tag.find("value")
    endQute = tag.find('"', vl + 20)
    csrf_token = tag[vl + 7:endQute]
    return csrf_cookie,csrf_token


def login_to_panel(UserName , PassWord):
    url = "http://" + panel_ip + ":" + str(panel_port) + "/login"
    csrf_cookie,csrf_token = get_csrf_from_page("login")
    cookies = {'csrftoken': csrf_cookie}
    params = {"username": UserName, "password": PassWord,"csrfmiddlewaretoken" : csrf_token}
    r = requests.request("POST",url, data=params, headers=dict(Referer=url), cookies=cookies, allow_redirects=False)
    return r.cookies["SID"]


def take_course(course_id, jwt):
    url = "http://" + panel_ip + ":" + str(panel_port) + "/courses/taking/" + str(course_id) + "/add"
    cookies = {'SID': jwt}
    r = requests.request("GET", url, cookies=cookies, allow_redirects=False)
    print(r.status_code)
    print(r.text)


def drop_course(course_id, jwt):
    url = "http://" + panel_ip + ":" + str(panel_port) + "/courses/taking/" + str(course_id) + "/remove"
    cookies = {'SID': jwt}
    r = requests.request("GET", url, cookies=cookies, allow_redirects=False)
    print(r.status_code)



for index, student_cred in enumerate(students_credentials):
    student_username = student_cred["username"]
    student_password = student_cred["password"]
    students_jwts.append(login_to_panel(student_username, student_password))
    print("student", index+1, "logged in.")
    


def student_takes_and_drops_courses(student_index):
    student_username = students_credentials[student_index]["username"]
    for course in courses_codes:
        print("student " + student_username + " taking course " + str(course))
        try:
            take_course(course, students_jwts[student_index])
        except:
            sleep(500)

    for course in courses_codes:
        print("student " + student_username + " dropping course " + str(course))
        try:
            drop_course(course, students_jwts[student_index])
        except:
            sleep(500)
            
def student_takes_and_drops_courses_forever(student_index):
    while 1:
        student_takes_and_drops_courses(student_index)
        
_thread.start_new_thread(student_takes_and_drops_courses_forever, (0,))
_thread.start_new_thread(student_takes_and_drops_courses_forever, (1,))
_thread.start_new_thread(student_takes_and_drops_courses_forever, (2,))
_thread.start_new_thread(student_takes_and_drops_courses_forever, (3,))
_thread.start_new_thread(student_takes_and_drops_courses_forever, (4,))
_thread.start_new_thread(student_takes_and_drops_courses_forever, (5,))
_thread.start_new_thread(student_takes_and_drops_courses_forever, (6,))
_thread.start_new_thread(student_takes_and_drops_courses_forever, (7,))
_thread.start_new_thread(student_takes_and_drops_courses_forever, (8,))
_thread.start_new_thread(student_takes_and_drops_courses_forever, (9,))

while 1:
    pass




