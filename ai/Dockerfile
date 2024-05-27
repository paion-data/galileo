# Copyright 2024 Paion Data. All rights reserved.
FROM ubuntu:latest

LABEL maintainer="Jiaqi Liu"
LABEL maintainer-email="jiaqiliu@paion-data.dev"

WORKDIR /app

RUN apt-get update
RUN apt-get upgrade -y
RUN apt update
RUN apt upgrade -y
RUN apt install build-essential -y
RUN apt install software-properties-common -y

RUN add-apt-repository ppa:deadsnakes/ppa
RUN apt update
RUN apt install python3.10 python3.10-venv python3.10-dev -y
RUN apt -y install python3-pip
RUN PATH=$PATH:/home/ubuntu/.local/bin
RUN ls -la /usr/bin/python3
RUN rm /usr/bin/python3
RUN ln -s python3.10 /usr/bin/python3

COPY . /app

RUN pip3 install flask
RUN pip3 install requests
RUN pip3 install flask-cors
RUN pip3 install -U setuptools
RUN pip3 install vosk
RUN pip3 install flasgger==0.9.7b2

ENTRYPOINT ["flask", "run", "--host=0.0.0.0"]
