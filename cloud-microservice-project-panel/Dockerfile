FROM python:3
#LABEL maintainer="abrishami.mahdi1996@gmail.com"
EXPOSE 8000
RUN mkdir /code
WORKDIR /code
COPY ./requirements.txt /code/requirements.txt
RUN pip install -r requirements.txt
COPY . /code/
CMD ["python3","manage.py","runserver","0.0.0.0:8000"]
