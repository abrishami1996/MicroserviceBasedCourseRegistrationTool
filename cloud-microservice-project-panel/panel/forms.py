from django import forms


class StudentRegisterForm(forms.Form):
    studentNumber = forms.IntegerField()
    firstName = forms.CharField()
    lastName = forms.CharField()
    major = forms.CharField()
    entranceYear = forms.IntegerField()
    eStates = (
        ("normall", "normall"),
        ("guest", "guest"),
    )
    educationalState = forms.ChoiceField(choices=eStates)
    password = forms.CharField()


class EmployeeRegisterForm(forms.Form):
    employeeNumber = forms.IntegerField()
    firstName = forms.CharField()
    lastName = forms.CharField()
    password = forms.CharField()


class ProfessorRegisterForm(forms.Form):
    professorNumber = forms.IntegerField()
    firstName = forms.CharField()
    lastName = forms.CharField()
    department = forms.CharField()
    field = forms.CharField()
    position = forms.CharField()
    password = forms.CharField()


class AddRoleForm(forms.Form):
    username = forms.CharField()
    role = forms.CharField()


class AddCourseForm(forms.Form):
    code = forms.IntegerField()
    name = forms.CharField()
    creditHour = forms.IntegerField()
    department = forms.CharField()
    year = forms.IntegerField()
    semesterChoices = (
        ("Fall", "Fall"),
        ("Spring", "Spring"),
    )
    semester = forms.ChoiceField(choices=semesterChoices)
    classroom = forms.CharField()
    professorId = forms.IntegerField()


class DeleteUserForm(forms.Form):
    username = forms.CharField()


class DeleteCourseForm(forms.Form):
    id = forms.IntegerField()


class TakeOrDropCourseForStudnetForm(forms.Form):
    course_code = forms.IntegerField()
    student_number = forms.IntegerField()