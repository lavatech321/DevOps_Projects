FROM python:latest
ADD python_test.py  /tmp
RUN useradd raman
RUN apt-get update
RUN apt-get install git -y
